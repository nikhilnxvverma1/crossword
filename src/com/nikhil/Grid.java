package com.nikhil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Final rendition of the crossword puzzle.*/
public class Grid {

    List<Word> wordList;
    private static Random random = new Random();

    public Grid(List<Word> wordList) {
        this.wordList = wordList;
    }

    private void placeWordsInGrid(){

        //place a random word in the grid
        int randomIndex = random.nextInt(wordList.size());
        Word trunk = wordList.get(randomIndex);

        // TODO place this word vertically in the grid
    }

    private Word getLongestUnplacedWord(){
        Word longestUnplacedWord = null;
        int longestLength = 0;

        for(Word word : wordList){
            if(!word.placed) {

                if((longestUnplacedWord==null)&&(word.name.length()>longestLength)){
                    longestLength = word.name.length();
                    longestUnplacedWord = word;
                }

            }
        }

        return longestUnplacedWord;
    }

    private Word getRandomUnplacedWord(){
        Word randomUnplacedWord = null;

        int randomIndex = random.nextInt(wordList.size());
        int reachRandomIndexByModulo = 0;
        int index = 0;

        while(reachRandomIndexByModulo!=randomIndex){

            if (!wordList.get(index).placed) {

                reachRandomIndexByModulo++;
                randomUnplacedWord = wordList.get(index); // by the end of the loop this will have reached random index
            }

            if(++index>=wordList.size()){
                index=0;
            }
        }

        assert !randomUnplacedWord.placed : "Random unplaced word is actually placed";

        return randomUnplacedWord;
    }


}
