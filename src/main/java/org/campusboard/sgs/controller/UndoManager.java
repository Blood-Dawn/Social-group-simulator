package org.campusboard.sgs.controller;

import java.util.ArrayDeque;
import java.util.Deque;

/** Keeps history of commands for undo/redo */
public class UndoManager {
    private final Deque<Command> undo = new ArrayDeque<>();
    private final Deque<Command> redo = new ArrayDeque<>();

    public void doCommand(Command c) throws Exception{
        c.execute();
        undo.push(c);
        redo.clear();
    }
    public boolean canUndo(){
        return !undo.isEmpty();
    }
    public boolean canRedo(){
        return !redo.isEmpty();
    }
    public void undo() throws Exception{
        if(!canUndo()) return;
        var c = undo.pop();
        c.undo();
        redo.push(c);
    }
    public void redo() throws Exception{
        if(!canRedo()) return;
        var c = redo.pop();
        c.execute();
        undo.push(c);
    }
    
}
