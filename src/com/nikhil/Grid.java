package com.nikhil;

import java.util.*;

/**
 * Final rendition of the crossword puzzle.
 */
public class Grid implements Corner.DoubleIntersectionFound{

    private static Random random = new Random();

    private LinkedList<Word> wordList;
    private int wordsPlaced = 0;
    private ArrayList<LetterFrequency> letterFrequencyPointers = new ArrayList<>(30);
    private ArrayList<LetterFrequency> sortedLetterFrequencies = new ArrayList<>(30);
    private LinkedList<Corner> generatedCorners = new LinkedList<>();

    public Grid(LinkedList<Word> wordList) {
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

        while(wordsPlaced<wordList.size()){

            // find the highest and lowest words that actually match with an intersection option
            IntersectionOption crossingAtRareLetter = this.findAvailableIntersectionPreferringRareLetters();

            if(crossingAtRareLetter==null){ // no available intersections, only disjoint words
                // place all remaining words at such locations that they fill up the deficient dimension and
                // try to make an even square grid

            }else if(!crossingAtRareLetter.source.placed){
                // place at a location such that, it fills the deficient dimension of the total area covered by the
                // current grid configuration and the extreme end of this intersection option touches the boundary
                // of the current configuration.
            }else{
                // just place the crossing word as usual and go by filling in as many corners as possible
                generatedCorners.addAll(crossingAtRareLetter.placeCrossingWord(this.wordList));
            }

//            crossingAtRareLetter.source.placeAt(0,0,true);

            //exhaust out the stack
            while(!generatedCorners.isEmpty()){
                Corner corner = generatedCorners.pop();
                if(corner.isTooSmall()){
                    continue;
                }

                // scan the area covered by this corner
                while(corner.moveToNextIfPossible()){

                    // look for double intersection that can be placed in this grid
                    boolean foundDoubleIntersection = corner.findPossibleIntersections(this);

                    if(!foundDoubleIntersection){
                        // get the least intersecting word in the list of single intersections and
                        // place them if possible

                    }

                }


            }

        }


        print();
    }

    @Override
    public boolean onDoubleIntersection(Corner corner, IntersectionOption fromSourceWord, IntersectionOption fromCrossingWord) {

        // make sure that the crossing coming from the two intersection options is not the same
        if(fromSourceWord.crossing==fromCrossingWord.crossing){
            return false;
        }

        // whereabouts of the crossing word coming from the source word intersection of the corner
        Location sourceCrossingProjectedLocation = fromSourceWord.projectedLocationOfCrossingWord();
        Word sourceCrossingWord = fromSourceWord.crossing;
        int rsc = sourceCrossingProjectedLocation.row;
        int csc = sourceCrossingProjectedLocation.col;
        boolean vsc = !fromSourceWord.source.vertical;

        // whereabouts of the crossing word coming from the crossing word intersection of the corner
        Location crossingCrossingProjectedLocation = fromCrossingWord.projectedLocationOfCrossingWord();
        Word crossingCrossingWord = fromCrossingWord.crossing;
        int rcc = crossingCrossingProjectedLocation.row;
        int ccc = crossingCrossingProjectedLocation.col;
        boolean vcc = !fromCrossingWord.source.vertical;

        // check to see if both the words can be placed in the grid or not
        if(
                isPlacementOfWordAllowed(sourceCrossingWord,rsc,csc,vsc) &&
                        isPlacementOfWordAllowed(crossingCrossingWord,rcc,ccc,vcc)){

            //place booth the words
            generatedCorners.addAll(fromSourceWord.placeCrossingWord(this.wordList));
            generatedCorners.addAll(fromCrossingWord.placeCrossingWord(this.wordList));
            this.wordsPlaced+=2;
            return true;
        }
        return false;
    }


    /**
     * Traverse the letter frequency in the increasing order of their frequency to look for an intersection option
     * @return a feasible intersection option preferring an uncommon letter
     */
    private IntersectionOption findAvailableIntersectionPreferringRareLetters(){

        IntersectionOption firstDisjointIntersectionOption = null;

        // traverse the letter frequency in the increasing order of their frequency
        for(LetterFrequency letterFrequency : sortedLetterFrequencies){

           IntersectionOption intersectionOption = letterFrequency.findAvailableIntersectionOption(this);

            // Disjoint intersection options can be placed freely in the grid because both words are unplaced
           if(!intersectionOption.source.placed && firstDisjointIntersectionOption==null){
               firstDisjointIntersectionOption = intersectionOption;
           }
           // we prefer joint intersection options because we want to avoid creating a new generation point
           else if(intersectionOption.source.placed && !intersectionOption.crossing.placed){
               return intersectionOption; // joint intersection option
           }

        }

        return firstDisjointIntersectionOption;
    }

    /**
     * Finds intersection between 2 unplaced crossing words assuming the word list is sorted
     * @return 2 crossing words in the word list starting from the opposite ends of the list
     */
    private IntersectionOption findIntersectionBetweenLowestAndHighestIntersectingWords(){

        Iterator<Word> ascendingIterator = wordList.iterator();

        while(ascendingIterator.hasNext()){

            // word with lesser intersections
            Word lowWord = ascendingIterator.next();

            // skip words with no intersections and ones that have already been placed
            if(lowWord.getTotalIntersections()<=0 || lowWord.placed){
                continue;
            }

            // starting from the highest number of intersection options (high word),
            // go down to get a match with a low word
            Iterator<Word> descendingIterator = wordList.descendingIterator();
            while(descendingIterator.hasNext()){

                // word with greater intersections
                Word highWord = descendingIterator.next();

                //skip placed words
                if(highWord.placed){
                    continue;
                }

                // same word means that highest and lowest ends have met in the middle, therefore return nil
                if(highWord==lowWord){
                    return null;
                }

                //check if intersection exists, and if so, return that intersection
                IntersectionOption intersectionExists = lowWord.intersectsWtih(highWord);
                if(intersectionExists!=null){
                    return intersectionExists;
                }
            }
        }
        return null;
    }

    /**
     * Counts and keeps track of the frequency of each alphabet in the word list,
     * sorts them and makes them addressable by index
     */
    private void computeAndSortLetterFrequencies(){

        // we are only doing it for each (capital) alphabet, case can be made for outer characters
        for(char alphabet = 'A';alphabet<='Z';alphabet++){

            LetterFrequency letterFrequency = new LetterFrequency(alphabet);
            sortedLetterFrequencies.add(letterFrequency);

            // check for frequency of this alphabet
            for(Word word: wordList){
                letterFrequency.addToCountIfLetterIsPresent(word);
            }
        }

        Collections.sort(sortedLetterFrequencies);

        // assign the sorted list to correct pointers so that they these letter frequencies are addressable by index
        for(LetterFrequency letterFrequency : sortedLetterFrequencies){
            letterFrequencyPointers.add(letterFrequency.letter-'A',letterFrequency);
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
    private void print() {

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
