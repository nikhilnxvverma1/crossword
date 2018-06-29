package com.nikhil;

/** String to be inserted as part of the crossword puzzle.*/
public class Word {
    String name;
    String description;
    int row;
    int col;
    boolean down;

    private boolean checkAgainstBoundaries(){
        return false;// TODO
    }

    private boolean checkAgainstOtherWords(){
        return false;// TODO
    }

    private boolean checkAgainstAdjacencyWithOtherWords(){
        return false;// TODO
    }
}
