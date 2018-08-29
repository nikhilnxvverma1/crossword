package com.nikhil;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by nikhilverma on 8/28/18.
 */
class WordTest {
    @org.junit.jupiter.api.Test
    void containsLettersWithGap() {
        Word word = new Word("approval","the feeling of having a positive opinion of someone or something");

        //negative cases
        // p3a2lP

        ArrayList<LetterGap> p3a2l = new ArrayList<>(3);
        p3a2l.add(new LetterGap('p',3));
        p3a2l.add(new LetterGap('a',2));
        p3a2l.add(new LetterGap('l',-1));

        assertFalse(word.containsLettersWithGap(p3a2l));

        //positive cases
        // p3a2lP

        ArrayList<LetterGap> a2r0o = new ArrayList<>(3);
        a2r0o.add(new LetterGap('a',2));
        a2r0o.add(new LetterGap('r',0));
        a2r0o.add(new LetterGap('o',-1));

        assertTrue(word.containsLettersWithGap(a2r0o));

    }

}