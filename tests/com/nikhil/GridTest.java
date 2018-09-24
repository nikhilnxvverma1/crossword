package com.nikhil;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    @Test
    void isPlacementOfWordAllowed() {

        // data setup

        // words
        Word mars = new Word("Mars","Fourth Planet");
        Word earth = new Word("Earth","Third Planet");
        Word ear = new Word("Ear","Organ for auditory reception");
        Word eyes = new Word("Eyes","Organ for visual perception");
        Word august = new Word("August","Eighth month");
        Word mugger = new Word("Mugger","A person who attacks and robs another in a public place");

        // put words in list
        ArrayList<Word> wordList = new ArrayList<>();
        wordList.add(mars);
        wordList.add(earth);
        wordList.add(ear);
        wordList.add(eyes);
        wordList.add(august);
        wordList.add(mugger);

        // word placement
        mars.placeAt(1,0,true);
        earth.placeAt(7,0,false);
        ear.placeAt(0,5,false);
        eyes.placeAt(4,8,true);
        august.placeAt(0,3,true);
        mugger.placeAt(3,2,false);

        // unplaced words / words to test
        Word one = new Word("One","First number after 0");
        Word tapestry = new Word("Tapestry","Used in reference to an intricate or complex sequence" +
                " of events");
        Word bad = new Word("Bad","Opposite of good");
        Word lies = new Word("Lies","Numerous deceitful facts");
        Word tut = new Word("Tut","Short for tutorial");
        Word me = new Word("Me","Used by speaker to refer to himself or herself");
        Word brer = new Word("Brer","Used as an informal title before a man's name");
        Word grey = new Word("Grey","Of a colour intermediate between black and white, as of ashes" +
                " or lead");
        Word marsh = new Word("Marsh","an area of low-lying land which is flooded in wet seasons or" +
                " at high tide, and typically remains waterlogged at all times");

        // add these unplaced words to the list
        wordList.add(one);
        wordList.add(tapestry);
        wordList.add(bad);
        wordList.add(lies);
        wordList.add(tut);
        wordList.add(me);
        wordList.add(brer);
        wordList.add(grey);
        wordList.add(marsh);

        // create a new grid to house these words
        Grid grid = new Grid(wordList);

        // one
        assertTrue(grid.isPlacementOfWordAllowed(one,6,6,false));
        assertTrue(grid.isPlacementOfWordAllowed(one,5,6,true));
        assertTrue(grid.isPlacementOfWordAllowed(one,-1,5,true));
        assertTrue(grid.isPlacementOfWordAllowed(one,7,6,true));
        assertTrue(grid.isPlacementOfWordAllowed(one,0,8,false));

        assertFalse(grid.isPlacementOfWordAllowed(one,7,-2,false));
        assertFalse(grid.isPlacementOfWordAllowed(one,1,8,true));
        assertFalse(grid.isPlacementOfWordAllowed(one,7,5,false));
        assertFalse(grid.isPlacementOfWordAllowed(one,3,4,false));


        // tapestry
        assertTrue(grid.isPlacementOfWordAllowed(tapestry,0,6,true));
        assertTrue(grid.isPlacementOfWordAllowed(tapestry,6,5,false));
        assertTrue(grid.isPlacementOfWordAllowed(tapestry,6,1,true));
        assertTrue(grid.isPlacementOfWordAllowed(tapestry,7,3,true));

        assertFalse(grid.isPlacementOfWordAllowed(tapestry,5,3,false));
        assertFalse(grid.isPlacementOfWordAllowed(tapestry,5,1,false));
        assertFalse(grid.isPlacementOfWordAllowed(tapestry,4,5,false));
        assertFalse(grid.isPlacementOfWordAllowed(tapestry,4,-4,false));

        // bad
        assertTrue(grid.isPlacementOfWordAllowed(bad,3,-1,false));
        assertTrue(grid.isPlacementOfWordAllowed(bad,6,1,true));
        assertTrue(grid.isPlacementOfWordAllowed(bad,0,2,false));
        assertTrue(grid.isPlacementOfWordAllowed(bad,5,6,true));

        assertFalse(grid.isPlacementOfWordAllowed(bad,0,6,true));
        assertFalse(grid.isPlacementOfWordAllowed(bad,5,3,false));
        assertFalse(grid.isPlacementOfWordAllowed(bad,0,4,true));
        assertFalse(grid.isPlacementOfWordAllowed(bad,7,5,false));

        // lies
        assertTrue(grid.isPlacementOfWordAllowed(lies,6,6,false));
        assertTrue(grid.isPlacementOfWordAllowed(lies,3,-3,false));
        assertTrue(grid.isPlacementOfWordAllowed(lies,-1,5,false));

        assertFalse(grid.isPlacementOfWordAllowed(lies,0,8,true));
        assertFalse(grid.isPlacementOfWordAllowed(lies,-1,5,true));
        assertFalse(grid.isPlacementOfWordAllowed(lies,7,5,false));
        assertFalse(grid.isPlacementOfWordAllowed(lies,5,4,false));
        assertFalse(grid.isPlacementOfWordAllowed(lies,0,5,false));

        // tut
        assertTrue(grid.isPlacementOfWordAllowed(tut,5,1,false));
        assertTrue(grid.isPlacementOfWordAllowed(tut,5,3,false));
        assertTrue(grid.isPlacementOfWordAllowed(tut,7,3,true));
        assertTrue(grid.isPlacementOfWordAllowed(tut,5,6,true));

        assertFalse(grid.isPlacementOfWordAllowed(tut,5,3,true));
        assertFalse(grid.isPlacementOfWordAllowed(tut,3,3,true));
        assertFalse(grid.isPlacementOfWordAllowed(tut,1,2,false));
        assertFalse(grid.isPlacementOfWordAllowed(tut,3,2,false));

        // me
        assertTrue(grid.isPlacementOfWordAllowed(me,1,0,false));
        assertTrue(grid.isPlacementOfWordAllowed(me,6,0,true));
        assertTrue(grid.isPlacementOfWordAllowed(me,6,7,false));
        assertTrue(grid.isPlacementOfWordAllowed(me,0,5,true));

        assertFalse(grid.isPlacementOfWordAllowed(me,3,2,true));
        assertFalse(grid.isPlacementOfWordAllowed(me,3,2,false));
        assertFalse(grid.isPlacementOfWordAllowed(me,2,6,true));
        assertFalse(grid.isPlacementOfWordAllowed(me,3,8,true));

        // brer
        assertTrue(grid.isPlacementOfWordAllowed(brer,0,7,true));
        assertTrue(grid.isPlacementOfWordAllowed(brer,6,2,true));

        assertFalse(grid.isPlacementOfWordAllowed(brer,5,0,true));

        // grey
        assertTrue(grid.isPlacementOfWordAllowed(grey,5,5,false));
        assertTrue(grid.isPlacementOfWordAllowed(grey,6,6,false));
        assertTrue(grid.isPlacementOfWordAllowed(grey,3,5,true));
        assertTrue(grid.isPlacementOfWordAllowed(grey,6,2,true));

        assertFalse(grid.isPlacementOfWordAllowed(grey,2,3,false));
        assertFalse(grid.isPlacementOfWordAllowed(grey,3,4,true));
        assertFalse(grid.isPlacementOfWordAllowed(grey,5,2,false));

        // marsh
        assertTrue(grid.isPlacementOfWordAllowed(marsh,6,1,true));

        assertFalse(grid.isPlacementOfWordAllowed(marsh,1,0,true));
        assertFalse(grid.isPlacementOfWordAllowed(marsh,5,2,true));
        assertFalse(grid.isPlacementOfWordAllowed(marsh,7,5,false));
        assertFalse(grid.isPlacementOfWordAllowed(marsh,3,2,false));
        assertFalse(grid.isPlacementOfWordAllowed(marsh,1,0,false));

    }
}