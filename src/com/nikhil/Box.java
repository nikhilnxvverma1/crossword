package com.nikhil;

public class Box {
    Word word;
    int index;

    public char getLetter(){
        return this.word.name.charAt(this.index);
    }
}
