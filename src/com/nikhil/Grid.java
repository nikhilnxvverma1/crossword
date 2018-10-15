package com.nikhil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Final rendition of the crossword puzzle.
 */
public class Grid {

    private List<Word> wordList;
    private int wordsPlaced = 0;
    private static Random random = new Random();

    public Grid(List<Word> wordList) {
        this.wordList = wordList;
    }

    public void placeWordsInGrid() {

        // calling this method more than once will be a no-op
        if(this.wordsPlaced==this.wordList.size()){
            return;
        }

        // compute the intersection options of all the words in the list
        for(Word word : this.wordList){
            word.computeIntersectionOptions(this.wordList);
        }

        //sort this list in increasing order of their number of intersection options
        Collections.sort(this.wordList,new CompareTotalIntersections());

        //print the list
        for(Word word : this.wordList){
            System.out.println(word.name+" : "+word.getTotalIntersections());
        }


    }

    private Word getLongestUnplacedWord() {
        Word longestUnplacedWord = null;
        int longestLength = 0;

        for (Word word : wordList) {
            if (!word.placed) {

                if ((longestUnplacedWord == null) && (word.name.length() > longestLength)) {
                    longestLength = word.name.length();
                    longestUnplacedWord = word;
                }

            }
        }

        return longestUnplacedWord;
    }

    private Word getRandomUnplacedWord() {
        Word randomUnplacedWord = null;

        int randomIndex = random.nextInt(wordList.size());
        int reachRandomIndexByModulo = 0;
        int index = 0;

        while (reachRandomIndexByModulo != randomIndex) {

            if (!wordList.get(index).placed) {

                reachRandomIndexByModulo++;
                randomUnplacedWord = wordList.get(index); // by the end of the loop this will have reached random index
            }

            if (++index >= wordList.size()) {
                index = 0;
            }
        }

        assert !randomUnplacedWord.placed : "Random unplaced word is actually placed";

        return randomUnplacedWord;
    }

    /**
     * Checks if the word to place actually is possible given the current status of the word list.
     * This does not place the word.
     *
     * @param wordToPlace           the word to place
     * @param r                     row
     * @param c                     column
     * @param wordToPlaceIsVertical weather the word is vertical or horizontal
     * @return true if it is possible, false otherwise
     */
    public boolean isPlacementOfWordAllowed(Word wordToPlace, int r, int c, boolean wordToPlaceIsVertical) {

        //check linearly against the word list
        //if any word is intersecting with word to place or not
        for (Word word : wordList) {

            boolean placementAllowed = true;

            //skip this word if it is same as the word to place
            // or word is not placed
            if ((word == wordToPlace) || (!word.placed)) {
                continue;
            }


            if (word.vertical && wordToPlaceIsVertical) { //both words are vertical

                //check for same column and then coinciding of the words
                if (word.col == c) {
                    placementAllowed = !isCoinciding(word, word.row, wordToPlace, r);
                }
            }
            else if (word.vertical && !wordToPlaceIsVertical) { // only word to place is horizontal

                placementAllowed = intersectionAllowed(wordToPlace, word, word.col - c, word.row - r);
            }
            else if (!word.vertical && wordToPlaceIsVertical) { // only word to place is vertical

                placementAllowed = intersectionAllowed(wordToPlace, word, word.row - r, word.col - c);
            }
            else { // both words horizontal

                //check for same row and then coinciding of the words
                if (word.row == r) {
                    placementAllowed = !isCoinciding(word, word.col, wordToPlace, c);
                }
            }

            if (!placementAllowed || word.willAdjacentlyTouch(wordToPlace,r,c,wordToPlaceIsVertical)) {
                return false;
            }

        }
        return true;
    }

    /**
     * Checks if two parallel words coincide given their initial starting position. Coincidence also occurs if the
     * words follow immediately in continuation.
     * @param word1 first word
     * @param word1Start starting position of the first word
     * @param word2 second word
     * @param word2Start starting position of the second word
     * @return true if the words coincide, false otherwise
     */
    private boolean isCoinciding(Word word1, int word1Start, Word word2, int word2Start) {
        if (word1Start < word2Start) {
            return !((word1Start + word1.name.length() - 1) < (word2Start - 1));
        } else if (word2Start < word1Start) {
            return !((word2Start + word2.name.length() - 1) < (word1Start - 1));
        } else {
            return true;
        }
    }

    /**
     * Checks if intersection of two perpendicular words is allowed given their relative positioning, which is
     * possible if the intersecting point has a common letter or there is no intersection
     *
     * @param base                the fixed word of the intersection
     * @param perpendicular       the word whose relative coordinates to the base are given
     * @param relativeDisplacementFromBase distance along the start of the base word towards the perpendicular
     * @param relativeDisplacementFromPerpendicular distance of the start of the perpendicular word from the base.
     *                                             Because of relative positioning, this value can also be negative
     * @return true if there is common letter or no intersection, false otherwise
     */
    private boolean intersectionAllowed(Word base, Word perpendicular, int relativeDisplacementFromBase,
                                        int relativeDisplacementFromPerpendicular) {

        boolean perpendicularCrossingBase =
                relativeDisplacementFromBase >= 0 &&
                (base.name.length() - relativeDisplacementFromBase) > 0;

        boolean baseCrossingPerpendicular =
                relativeDisplacementFromPerpendicular <= 0 &&
                (perpendicular.name.length() - Math.abs(relativeDisplacementFromPerpendicular)) > 0;

        //check if there is actually an intersection
        boolean intersectionExists = perpendicularCrossingBase && baseCrossingPerpendicular;

        if (intersectionExists) {
            //find the letter for both the words
            char letterAlongBase = base.name.charAt(relativeDisplacementFromBase);
            int perpendicularLetterIndex = Math.abs(relativeDisplacementFromPerpendicular);
            char letterAlongPerpendicular = perpendicular.name.charAt(perpendicularLetterIndex);
            return Character.toUpperCase(letterAlongBase) == Character.toUpperCase(letterAlongPerpendicular);
        } else {
            return true;
        }
    }

    /**
     * Shifts the position of each word by just enough an equal amount such that no position is negative and the top
     * left corner of the grid is at (0,0)
     */
    public void normalize() {

        //find the min row and col values.
        int minRow = 99999;
        int minCol = 99999;

        for (Word word : wordList) {

            if (word.row < minRow) {
                minRow = word.row;
            }

            if (word.col < minCol) {
                minCol = word.col;
            }
        }

        // Since the minimum row and column values denote the top left coordinate of the grid,
        // we shift the entire grid by negative the amount of that
        for (Word word : wordList) {
            word.shiftBy(-minRow, -minCol);
        }
    }

    /**
     * Prints the status of the grid by traversing each word and placing its characters in a square grid
     */
    public void print() {

        System.out.println("Grid:");

        //find the min row and col values.
        int minRow = 99999;
        int minCol = 99999;

        //also find the max row and col values.
        int maxRow = -99999;
        int maxCol = -99999;

        for (Word word : this.wordList) {

            if(word.placed){
                if (word.row < minRow) {
                    minRow = word.row;
                }

                if (word.col < minCol) {
                    minCol = word.col;
                }

                // for max row and column find the end of the word

                if(word.vertical){
                    if (word.row + word.name.length() > maxRow) {
                        maxRow = word.row + word.name.length();
                    }

                    if (word.col > maxCol) {
                        maxCol = word.col;
                    }
                }else{
                    if (word.row > maxRow) {
                        maxRow = word.row;
                    }

                    if (word.col + word.name.length() > maxCol) {
                        maxCol = word.col + word.name.length();
                    }
                }
            }

        }


        int height = Math.abs(maxRow - minRow);
        int width = Math.abs(maxCol - minCol);

        int side = height > width ? height : width;

        char[][] squareGrid = buildEmptySquareGridForCurrentPlacement(side);

        // place words in this square grid
        for (Word word : this.wordList) {
            if (word.placed) {
                int length = word.name.length();
                for (int i = 0; i < length; i++) {

                    // holds index location of the characters in the word grid
                    int row, col;
                    if (word.vertical) {
                        //place each character in the same column
                        row = Math.abs(word.row - minRow) + i;
                        col = Math.abs(word.col - minCol);
                    } else {
                        //place each character in the same row
                        row = Math.abs(word.row - minRow);
                        col = Math.abs(word.col - minCol) + i;
                    }

                    // place this character at the identified spot
                    squareGrid[row][col] = word.name.charAt(i);
                }
            }
        }

        // print this 2d char array
        print(squareGrid);
    }

    private void print(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println("");
        }
    }

    private char[][] buildEmptySquareGridForCurrentPlacement(int side) {

        // find the length and the width of the grid and b/w the maximum of two, use that as width of grid
//        int side = sideOfGrid();

        // initialize all values of the square grid with letter '0'
        char[][] square = new char[side][side];

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                square[i][j] = '0';
            }
        }
        return square;
    }

    private int nextCircularIndex(int current,int length){
        if(current+1>=length){
            return 0;
        }else{
            return current + 1;
        }
    }

    @Deprecated
    private int sideOfGrid() {

        //find the min row and col values.
        int minRow = 99999;
        int minCol = 99999;

        //also find the max row and col values.
        int maxRow = -99999;
        int maxCol = -99999;

        for (Word word : wordList) {

            if (word.row < minRow) {
                minRow = word.row;
            }

            if (word.col < minCol) {
                minCol = word.col;
            }

            // for max row and column find the end of the word

            if(word.vertical){
                if (word.row + word.name.length() > maxRow) {
                    maxRow = word.row + word.name.length();
                }

                if (word.col > maxCol) {
                    maxCol = word.col;
                }
            }else{
                if (word.row > maxRow) {
                    maxRow = word.row;
                }

                if (word.col + word.name.length() > maxCol) {
                    maxCol = word.col + word.name.length();
                }
            }

        }

        int height = Math.abs(maxRow - minRow);
        int width = Math.abs(maxCol - minCol);

        return height > width ? height : width;
    }

    private class CompareTotalIntersections implements Comparator<Word> {
        @Override
        public int compare(Word word1, Word word2) {
            return word1.getTotalIntersections()-word2.getTotalIntersections();
        }
    }
}
