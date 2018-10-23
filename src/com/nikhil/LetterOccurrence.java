package com.nikhil;

/** Model for storing an index in a word for purposes of tracking a letter */
public class LetterOccurrence {

    private Word word;
    private int index;

    public LetterOccurrence(Word word, int index) {
        this.word = word;
        this.index = index;
    }

    public Word getWord() {
        return word;
    }

    public int getIndex() {
        return index;
    }
}
