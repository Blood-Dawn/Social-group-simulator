package org.campusboard.sgs.Persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.campusboard.sgs.model.Post;

/**
 * Remote {@link PostRepository} implementation backed by a lightweight HTTP API.
 * <p>
 * Attempts to persist data to a remote backend but gracefully falls back to an
 * in-memory cache whenever the network is unavailable.  The repository keeps a
 * local snapshot so UI components can continue operating offline and can react
 * to remote changes pushed in by the {@link RemotePostSyncClient}.
 */
public class RemotePostRepository implements PostRepository {

    private static final String POSTS_PATH = "/posts";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final InMemoryPostRepository offlineCache;
    private final Map<UUID, Post> remoteCache = new ConcurrentHashMap<>();
    private final List<Consumer<List<Post>>> remoteListeners = new CopyOnWriteArrayList<>();
    private final AtomicBoolean online = new AtomicBoolean(true);
    private Duration requestTimeout = Duration.ofSeconds(5);

    public RemotePostRepository(String baseUrl) {
        this(baseUrl, new InMemoryPostRepository());
    }

    public RemotePostRepository(String baseUrl, InMemoryPostRepository offlineCache) {
        this.baseUrl = sanitiseBaseUrl(baseUrl);
        this.offlineCache = offlineCache == null ? new InMemoryPostRepository() : offlineCache;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public boolean isOnline() {
        return online.get();
    }

    public void addRemoteUpdateListener(Consumer<List<Post>> listener) {
        if (listener != null) {
            remoteListeners.add(listener);
        }
    }

    public void removeRemoteUpdateListener(Consumer<List<Post>> listener) {
        remoteListeners.remove(listener);
    }

    public void setRequestTimeout(Duration timeout) {
        if (timeout != null && !timeout.isNegative() && !timeout.isZero()) {
            this.requestTimeout = timeout;
        }
    }

    @Override
    public List<Post> findAll() {
        if (online.get()) {
            try {
                List<Post> remotePosts = fetchRemotePosts();
                if (remotePosts != null) {
                    mergeRemotePosts(remotePosts, false);
                    return new ArrayList<>(remotePosts);
                }
            } catch (Exception e) {
                handleRemoteFailure("findAll", e);
            }
        }
        return getCachedOrOfflinePosts();
    }

    @Override
    public Optional<Post> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        Post cached = remoteCache.get(id);
        if (cached != null) {
            return Optional.of(cached);
        }
        return offlineCache.findById(id);
    }

    @Override
    public Post save(Post post) {
        if (post == null) {
            return null;
        }

        if (online.get()) {
            try {
                HttpRequest request = requestBuilder(POSTS_PATH)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(post)))
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (isSuccessful(response.statusCode())) {
                    Post saved = parsePost(response.body(), post);
                    cachePost(saved);
                    online.set(true);
                    return saved;
                }
            } catch (Exception e) {
                handleRemoteFailure("save", e);
            }
        }

        Post savedLocal = offlineCache.save(post);
        cachePost(savedLocal);
        return savedLocal;
    }

    @Override
    public Post update(Post post) {
        if (post == null) {
            return null;
        }

        if (online.get()) {
            try {
                HttpRequest request = requestBuilder(POSTS_PATH + "/" + post.getId())
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(post)))
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (isSuccessful(response.statusCode())) {
                    Post updated = parsePost(response.body(), post);
                    cachePost(updated);
                    online.set(true);
                    return updated;
                }
            } catch (Exception e) {
                handleRemoteFailure("update", e);
            }
        }

        Post updatedLocal = offlineCache.update(post);
        if (updatedLocal != null) {
            cachePost(updatedLocal);
        }
        return updatedLocal;
    }

    @Override
    public boolean delete(UUID id) {
        if (id == null) {
            return false;
        }

        if (online.get()) {
            try {
                HttpRequest request = requestBuilder(POSTS_PATH + "/" + id)
                        .DELETE()
                        .build();
                HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
                if (isSuccessful(response.statusCode())) {
                    remoteCache.remove(id);
                    offlineCache.delete(id);
                    online.set(true);
                    return true;
                }
            } catch (Exception e) {
                handleRemoteFailure("delete", e);
            }
        }

        boolean deletedLocally = offlineCache.delete(id);
        if (deletedLocally) {
            remoteCache.remove(id);
        }
        return deletedLocally;
    }

    @Override
    public Post likePost(UUID id) {
        if (id == null) {
            return null;
        }

        if (online.get()) {
            try {
                HttpRequest request = requestBuilder(POSTS_PATH + "/" + id + "/like")
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (isSuccessful(response.statusCode())) {
                    Post updated = parsePost(response.body(), remoteCache.get(id));
                    cachePost(updated);
                    online.set(true);
                    return updated;
                }
            } catch (Exception e) {
                handleRemoteFailure("likePost", e);
            }
        }

        Post updatedLocal = offlineCache.likePost(id);
        if (updatedLocal != null) {
            cachePost(updatedLocal);
        }
        return updatedLocal;
    }

    @Override
    public Post dislikePost(UUID id) {
        if (id == null) {
            return null;
        }

        if (online.get()) {
            try {
                HttpRequest request = requestBuilder(POSTS_PATH + "/" + id + "/dislike")
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (isSuccessful(response.statusCode())) {
                    Post updated = parsePost(response.body(), remoteCache.get(id));
                    cachePost(updated);
                    online.set(true);
                    return updated;
                }
            } catch (Exception e) {
                handleRemoteFailure("dislikePost", e);
            }
        }

        Post updatedLocal = offlineCache.dislikePost(id);
        if (updatedLocal != null) {
            cachePost(updatedLocal);
        }
        return updatedLocal;
    }

    public boolean refreshFromRemote() {
        try {
            List<Post> remotePosts = fetchRemotePosts();
            if (remotePosts == null) {
                return false;
            }
            boolean changed = mergeRemotePosts(remotePosts, true);
            online.set(true);
            return changed;
        } catch (Exception e) {
            handleRemoteFailure("refreshFromRemote", e);
            return false;
        }
    }

    private void cachePost(Post post) {
        if (post == null) {
            return;
        }
        remoteCache.put(post.getId(), post);
        offlineCache.save(post);
    }

    private HttpRequest.Builder requestBuilder(String path) {
        return HttpRequest.newBuilder(resolveUri(path))
                .timeout(requestTimeout)
                .header("Accept", "application/json");
    }

    private URI resolveUri(String path) {
        if (path == null || path.isBlank()) {
            return URI.create(baseUrl);
        }
        String normalised = path.startsWith("/") ? path : "/" + path;
        return URI.create(baseUrl + normalised);
    }

    private List<Post> fetchRemotePosts() throws IOException, InterruptedException {
        HttpRequest request = requestBuilder(POSTS_PATH)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (isSuccessful(response.statusCode())) {
            String body = response.body();
            if (body == null || body.isBlank()) {
                return Collections.emptyList();
            }
            Post[] posts = objectMapper.readValue(body, Post[].class);
            return List.of(posts);
        }
        throw new IOException("Unexpected response status: " + response.statusCode());
    }

    private boolean mergeRemotePosts(List<Post> remotePosts, boolean notifyOnChange) {
        Map<UUID, Post> newCache = remotePosts.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Post::getId, Function.identity(), (first, second) -> first, ConcurrentHashMap::new));

        boolean changed = hasCacheChanged(newCache);
        if (changed) {
            remoteCache.clear();
            remoteCache.putAll(newCache);
            offlineCache.replaceAll(remotePosts);
            if (notifyOnChange) {
                notifyRemoteListeners(Collections.unmodifiableList(new ArrayList<>(remotePosts)));
            }
        }
        return changed;
    }

    private boolean hasCacheChanged(Map<UUID, Post> newCache) {
        if (newCache.size() != remoteCache.size()) {
            return true;
        }
        for (Map.Entry<UUID, Post> entry : newCache.entrySet()) {
            Post existing = remoteCache.get(entry.getKey());
            if (existing == null || !postsEquivalent(existing, entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean postsEquivalent(Post first, Post second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        return Objects.equals(first.getId(), second.getId())
                && Objects.equals(first.getTitle(), second.getTitle())
                && Objects.equals(first.getBody(), second.getBody())
                && Objects.equals(first.getCategory(), second.getCategory())
                && Objects.equals(first.getAuthor(), second.getAuthor())
                && Objects.equals(first.getCreatedAt(), second.getCreatedAt())
                && first.getLikes() == second.getLikes()
                && first.getDislikes() == second.getDislikes();
    }

    private void notifyRemoteListeners(List<Post> posts) {
        for (Consumer<List<Post>> listener : remoteListeners) {
            try {
                listener.accept(posts);
            } catch (Exception e) {
                System.err.println("⚠️ RemotePostRepository: listener error - " + e.getMessage());
            }
        }
    }

    private Post parsePost(String body, Post fallback) throws JsonProcessingException {
        if (body == null || body.isBlank()) {
            return fallback;
        }
        return objectMapper.readValue(body, Post.class);
    }

    private boolean isSuccessful(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private void handleRemoteFailure(String action, Exception exception) {
        if (exception instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
        if (online.getAndSet(false)) {
            System.err.println("⚠️ RemotePostRepository: Remote " + action + " failed, switching to offline cache: " + exception.getMessage());
        }
    }

    private List<Post> getCachedOrOfflinePosts() {
        if (!remoteCache.isEmpty()) {
            return new ArrayList<>(remoteCache.values());
        }
        return offlineCache.findAll();
    }

    private String sanitiseBaseUrl(String url) {
        if (url == null || url.isBlank()) {
            return "http://localhost:8080";
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
