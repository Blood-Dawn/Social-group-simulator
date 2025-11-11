package org.campusboard.sgs.controller;

import java.util.*;

public class UndoManager {
  private final Deque<Command> undoStack = new ArrayDeque<>();
  private final Deque<Command> redoStack = new ArrayDeque<>();
  private static final int MAX_HISTORY = 50;

  public void execute(Command command) {
    command.execute();
    undoStack.push(command);
    redoStack.clear();

    // Limit history size
    if (undoStack.size() > MAX_HISTORY) {
      Iterator<Command> it = undoStack.descendingIterator();
      it.next();
      it.remove();
    }
  }

  public void undo() {
    if (!canUndo()) return;
    Command command = undoStack.pop();
    command.undo();
    redoStack.push(command);
  }

  public void redo() {
    if (!canRedo()) return;
    Command command = redoStack.pop();
    command.execute();
    undoStack.push(command);
  }

  public boolean canUndo() {
    return !undoStack.isEmpty();
  }

  public boolean canRedo() {
    return !redoStack.isEmpty();
  }

  public String getUndoDescription() {
    return canUndo() ? undoStack.peek().getDescription() : "";
  }

  public String getRedoDescription() {
    return canRedo() ? redoStack.peek().getDescription() : "";
  }

  public void clear() {
    undoStack.clear();
    redoStack.clear();
  }
}
