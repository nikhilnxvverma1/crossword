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
        Word manager = new Word("Manager","One who coordinates workflow in a team");


    }
}