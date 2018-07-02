package com.nikhil;

/** String to be inserted as part of the crossword puzzle.*/
public class Word {
    String name;
    String description;
    int row;
    int col;
    boolean vertical;
    boolean placed = false;

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

}
