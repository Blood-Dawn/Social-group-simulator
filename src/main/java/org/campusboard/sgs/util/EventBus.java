package org.campusboard.sgs.util;

import java.util.*;
import java.util.function.Consumer;

public class EventBus {
  private final Map<Events, List<Consumer<Events.Payload>>> map = new EnumMap<>(Events.class);
  public void subscribe(Events e, Consumer<Events.Payload> h){
    map.computeIfAbsent(e, k->new ArrayList<>()).add(h);
  }
  public void publish(Events e){ publish(e, null); }
  public void publish(Events e, Object data){
    List<Consumer<Events.Payload>> ls = map.get(e);
    if (ls==null) return;
    var p = e.of(data);
    for (var h: ls) h.accept(p);
  }
}
