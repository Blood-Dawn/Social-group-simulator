package edu.fau.sgs.controller;

public interface Command {
    void execute();
    void undo();
}
