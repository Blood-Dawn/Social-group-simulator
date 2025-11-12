package org.campusboard.sgs.view;

import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.model.*;
import org.campusboard.sgs.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/** Scrollable feed that avoids full rebuilds to stop jumping to top. */
public class FeedPanel extends JPanel {
  private final PostController controller;
  private final EventBus bus;
  private final JScrollPane scroll;
  private final JPanel content = new JPanel();
  private final Map<UUID, PostCard> cards = new LinkedHashMap<>();
  private final javax.swing.Timer debounce = new javax.swing.Timer(120, e -> doRefresh()); // EDT-safe debounce

  public FeedPanel(PostController controller, EventBus bus) {
    super(new BorderLayout());
    this.controller = controller;
    this.bus = bus;
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    this.scroll = new JScrollPane(content);
    add(scroll, BorderLayout.CENTER);

    debounce.setRepeats(false); // classic debounce
    bus.subscribe(Events.POSTS_REPLACED, e -> schedule());
    bus.subscribe(Events.POST_UPDATED, e -> schedule());
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
      PostCard card = cards.computeIfAbsent(p.id(), id -> new PostCard(controller));
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
