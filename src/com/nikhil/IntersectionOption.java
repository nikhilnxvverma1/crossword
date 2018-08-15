package com.nikhil;

/**
 * Simple Data Holder for storing intersection point data
 * Created by nikhilverma on 7/9/18.
 */
public class IntersectionOption {
    Word word1;
    int index1;
    Word word2;
    int index2;

    public IntersectionOption(Word word1, int index1, Word word2, int index2) {
        this.word1 = word1;
        this.index1 = index1;
        this.word2 = word2;
        this.index2 = index2;
    }

    @Override
    public String toString() {
        return "IntersectionOption{" +
                "word1=" + word1.name +
                ", index1=" + index1 +
                ", word2=" + word2.name +
                ", index2=" + index2 +
                '}';
    }
}
