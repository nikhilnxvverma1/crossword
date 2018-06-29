package com.nikhil;

/** String to be inserted as part of the crossword puzzle.*/
public class Word {
    String name;
    String description;
    int row;
    int col;
    boolean down;

    public Word(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Word{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

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
