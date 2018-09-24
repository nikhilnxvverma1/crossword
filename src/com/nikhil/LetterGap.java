package com.nikhil;

/**
 * Holder for storing a character and gap after it
 * Created by nikhilverma on 8/23/18.
 */
public class LetterGap {

    /** Character that is supposed to exist in the word */
    char letter;

    /**
     * The gap after the current letter for the next letter in the letter gap sequence. If this is the last lettter
     * in the sequence then this will be -1
     **/
    int gap;

    public LetterGap(char letter, int gap) {

        this.letter = Character.toUpperCase(letter);
        this.gap = gap;
    }
}
