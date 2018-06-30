package com.nikhil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Final rendition of the crossword puzzle.*/
public class Grid {

    List<Word> wordList;

    public Grid(List<Word> wordList) {
        this.wordList = wordList;
    }

    private void placeWordsInGrid(){

        Random random = new Random();

        //place a random word in the grid
        int randomIndex = random.nextInt(wordList.size());
        Word trunk = wordList.get(randomIndex);

        // TODO place this word vertically in the grid
    }
}
