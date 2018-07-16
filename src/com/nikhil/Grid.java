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

        boolean placementAllowed = true;

        //check linearly against the word list
        //if any word is intersecting with word to place or not
        for(Word word : wordList){

            //skip this word if it is same as the word to place
            // or word is not placed
            if((word==wordToPlace)||(!word.placed)){
                continue;
            }


            if(word.vertical && wordToPlaceIsVertical){ //both words are vertical
                //check for same column and then coinciding of the words
                if(word.col == c){
                    placementAllowed = !isCoinciding(word,word.row,wordToPlace,r);
                }
            }else if(word.vertical && !wordToPlaceIsVertical){ // only word to place is horizontal

            }else if(!word.vertical && wordToPlaceIsVertical){ // only word to place is vertical

            }else{ // both words horizontal
                //check for same row and then coinciding of the words
                if(word.row == r){
                    placementAllowed = !isCoinciding(word,word.col,wordToPlace,c);
                }
            }

            if(!placementAllowed){
                break;
            }

        }
        return placementAllowed;
    }

    private boolean isCoinciding(Word word1, int word1Start, Word word2, int word2Start) {
        if (word1Start < word2Start) {
            return !((word1Start + word1.name.length()) < (word2Start - 1));
        } else if (word2Start < word1Start) {
            return !((word2Start + word2.name.length()) < (word1Start - 1));
        } else {
            return false;
        }
    }

    /**
     * Checks if intersection of two perpendicular words is allowed given their relative positioning, which is
     * possible if the intersecting point has a common letter or there is no intersection
     * @param base the fixed word of the intersection
     * @param perpendicular the word whose relative coordinates to the base are given
     * @param perpendicularOffset distance along the start of the base word towards the perpendicular
     * @param baseDistance distance of the start of the perpendicular word from the base. Because of relative
     *                     positioning, this value can also be negative
     * @return true if there is common letter or no intersection, false otherwise
     */
    private boolean intersectionAllowed(Word base, Word perpendicular,int perpendicularOffset,int baseDistance){

        boolean perpendicularCrossingBase = perpendicularOffset>=0 && (base.name.length()-perpendicularOffset) >= 0;
        boolean baseCrossingPerpendicular = baseDistance<= 0 && (perpendicular.name.length() - Math.abs(baseDistance)) >= 0;

        //check if there is actually an intersection
        boolean intersectionExists = perpendicularCrossingBase && baseCrossingPerpendicular;

        if(intersectionExists){
            //find the letter for both the words
            char letterAlongBase = base.name.charAt(perpendicularOffset);
            int perpendicularLetterIndex = Math.abs(baseDistance);
            char letterAlongPerpendicular = perpendicular.name.charAt(perpendicularLetterIndex);
            return Character.toUpperCase(letterAlongBase) ==  Character.toUpperCase(letterAlongPerpendicular);
        }else{
            return false;
        }
    }


}
