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

    public void placeAt(int row,int col,boolean vertical){
        this.row = row;
        this.col = col;
        this.vertical = vertical;
        this.placed = true;
    }

    public void shiftBy(int dRow,int dCol){
        this.row += dRow;
        this.col += dCol;
    }

}
