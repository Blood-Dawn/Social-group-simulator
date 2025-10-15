package org.campusboard.sgs.Persistence;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Background task that keeps the local cache in sync with the remote backend.
 * <p>
 * It periodically invokes {@link RemotePostRepository#refreshFromRemote()} and
 * relies on the repository to dispatch change notifications. The scheduler uses
 * a daemon thread so it will not block JVM shutdown.
 */
public class RemotePostSyncClient implements AutoCloseable {

    private static final Duration DEFAULT_POLL_INTERVAL = Duration.ofSeconds(5);

    private final RemotePostRepository repository;
    private final Duration pollInterval;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public RemotePostSyncClient(RemotePostRepository repository) {
        this(repository, DEFAULT_POLL_INTERVAL);
    }

    public RemotePostSyncClient(RemotePostRepository repository, Duration pollInterval) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.pollInterval = pollInterval == null || pollInterval.isNegative() || pollInterval.isZero()
                ? DEFAULT_POLL_INTERVAL
                : pollInterval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "remote-post-sync");
            thread.setDaemon(true);
            return thread;
        });
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            scheduler.scheduleWithFixedDelay(this::pollRemote, 0L, pollInterval.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    public void stop() {
        if (running.compareAndSet(true, false)) {
            scheduler.shutdownNow();
        }
    }

    @Override
    public void close() {
        stop();
    }

    private void pollRemote() {
        try {
            repository.refreshFromRemote();
        } catch (Exception e) {
            System.err.println("⚠️ RemotePostSyncClient: polling failed - " + e.getMessage());
        }
    }
}
