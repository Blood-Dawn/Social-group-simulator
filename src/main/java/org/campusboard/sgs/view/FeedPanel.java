package org.campusboard.sgs.view;

import java.awt.BorderLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.campusboard.sgs.controller.PostController;
import org.campusboard.sgs.filter.SortByNew;
import org.campusboard.sgs.controller.CommentController;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.util.EventBus;
import org.campusboard.sgs.util.Events;
import org.campusboard.sgs.util.Session;

/** Scrollable feed that avoids full rebuilds to stop jumping to top. */
public class FeedPanel extends JPanel {
  private final PostController controller;
  private final CommentController commentController;
  private final Session session;
  private final EventBus bus;
  private final JScrollPane scroll;
  private final JPanel content = new JPanel();
  private final Map<UUID, PostCard> cards = new LinkedHashMap<>();
  private final javax.swing.Timer debounce = new javax.swing.Timer(120, e -> doRefresh()); // EDT-safe debounce

  public FeedPanel(PostController controller, org.campusboard.sgs.controller.CommentController commentController, Session session, EventBus bus) {
    super(new BorderLayout());
    this.controller = controller;
    this.commentController = commentController;
    this.session = session;
    this.bus = bus;
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    this.scroll = new JScrollPane(content);
    this.scroll.getVerticalScrollBar().setUnitIncrement(24); // faster scroll
    this.scroll.getHorizontalScrollBar().setUnitIncrement(24);
    add(scroll, BorderLayout.CENTER);

    debounce.setRepeats(false); // classic debounce
    bus.subscribe(Events.POSTS_REPLACED, e -> schedule());
    bus.subscribe(Events.POST_UPDATED, e -> handlePostUpdated(e.data));
    bus.subscribe(Events.FILTER_CHANGED, e -> schedule());
    bus.subscribe(Events.SEARCH_CHANGED, e -> schedule());
    schedule();
  }

  private void schedule() {
    if (debounce.isRunning())
      debounce.restart();
    else
      debounce.start();
  }

  private void handlePostUpdated(Object data) {
    if (!(data instanceof UUID postId)) {
      schedule();
      return;
    }
    // If sort requires reordering (e.g., trending), fall back to a full refresh
    if (!(controller.getSort() instanceof SortByNew)) {
      schedule();
      return;
    }
    // If the card is not present, rebuild the feed (likely due to filtering or new post)
    if (!cards.containsKey(postId)) {
      schedule();
      return;
    }
    controller.findById(postId).ifPresentOrElse(updated -> {
      // Ensure the post still passes the current filters/search
      boolean stillVisible = controller.current().stream().anyMatch(p -> p.id().equals(postId));
      if (!stillVisible) {
        schedule();
        return;
      }
      cards.get(postId).bind(updated);
      content.revalidate();
      content.repaint();
    }, this::schedule);
  }

  private void doRefresh() {
    int y = scroll.getVerticalScrollBar().getValue(); // remember position
    List<Post> now = controller.current();
    Set<UUID> incoming = now.stream().map(Post::id).collect(Collectors.toSet());

    // Remove missing
    var it = cards.keySet().iterator();
    while (it.hasNext()) {
      var id = it.next();
      if (!incoming.contains(id)) {
        content.remove(cards.get(id));
        it.remove();
      }
    }
    // Rebuild order + bind
    content.removeAll();
    for (Post p : now) {
      PostCard card = cards.computeIfAbsent(p.id(), id -> new PostCard(controller, commentController, session));
      card.bind(p);
      content.add(card);
      content.add(Box.createVerticalStrut(8));
    }
    content.revalidate();
    content.repaint();

    // restore scroll to avoid jump to top
    SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(y));
  }

  public EventBus getEventBus() {
    return bus;
  }
}
