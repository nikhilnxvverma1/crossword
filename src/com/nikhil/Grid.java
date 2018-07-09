package com.nikhil;

import java.util.List;
import java.util.Random;

/** Final rendition of the crossword puzzle.*/
public class Grid {

    private List<Word> wordList;
    private static Random random = new Random();

    public Grid(List<Word> wordList) {
        this.wordList = wordList;
    }

    private void placeWordsInGrid(){

        //place the longest word in the grid
        Word trunk = getLongestUnplacedWord();

        // place this word vertically in the grid
        trunk.placed = true;
        trunk.vertical = true;
        trunk.row = 0;
        trunk.col = 0;
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

    /**
     * Checks if the word to place actually is possible given the current status of the word list.
     * This does not place the word.
     * @param wordToPlace the word to place
     * @param r row
     * @param c column
     * @param wordToPlaceIsVertical weather the word is vertical or horizontal
     * @return true if it is possible, false otherwise
     */
    private boolean isPlacementOfWordAllowed(Word wordToPlace,int r,int c,boolean wordToPlaceIsVertical){

        //check linearly against the word list
        //if any word is intersecting with word to place or not
        for(Word word : wordList){

            //skip this word if it is same as the word to place
            if(word==wordToPlace){
                continue;
            }


            if(word.vertical && wordToPlaceIsVertical){ //both words are vertical

            }else if(word.vertical && !wordToPlaceIsVertical){ // only word to place is horizontal

            }else if(!word.vertical && wordToPlaceIsVertical){ // only word to place is vertical

            }else{ // both words horizontal

            }

        }
        return false;
    }


}
