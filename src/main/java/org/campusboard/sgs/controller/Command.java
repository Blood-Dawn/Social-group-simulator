package org.campusboard.sgs.controller;

public interface Command {
  void execute();
  void undo();
  String getDescription();
}
