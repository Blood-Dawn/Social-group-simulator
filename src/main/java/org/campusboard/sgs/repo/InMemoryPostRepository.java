package org.campusboard.sgs.repo;

import org.campusboard.sgs.model.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryPostRepository implements PostRepository {
  private final Map<UUID, Post> map = new ConcurrentHashMap<>();

  public InMemoryPostRepository() {
  }

  @Override
  public List<Post> findAll() {
    return new ArrayList<>(map.values());
  }

  @Override
  public Optional<Post> findById(UUID id) {
    return Optional.ofNullable(map.get(id));
  }

  @Override
  public void save(Post p) {
    map.put(p.id(), p);
  }

  @Override
  public void update(Post p) {
    map.put(p.id(), p);
  }

  @Override
  public void delete(UUID id) {
    map.remove(id);
  }

  @Override
  public List<Post> find(Category category, String search) {
    return map.values().stream().filter(p -> {
      boolean catOk = (category == null) || p.category() == category;
      boolean sOk = (search == null || search.isBlank()) ||
          p.title().toLowerCase().contains(search.toLowerCase()) ||
          p.body().toLowerCase().contains(search.toLowerCase());
      return catOk && sOk;
    }).sorted(Comparator.comparing(Post::id)).collect(Collectors.toList());
  }
}
