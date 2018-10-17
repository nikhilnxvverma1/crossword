package com.nikhil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/** String to be inserted as part of the crossword puzzle.*/
public class Word {
    String name;
    String description;
    int row;
    int col;
    boolean vertical;
    boolean placed = false;
    private LinkedList<IntersectionOption> unplacedIntersectionOptions;

    public Word(String name, String description) {
        this.name = name.toUpperCase();
        this.description = description;
    }

    @Override
    public String toString() {
        return name + "(" + row + "," + col + "," + (vertical ? "v" : "h") + ","+(placed ? "p" : "u")+")";
    }

    /**
     * Places the word in the list by setting position and placement of flag. Beyond this it also, removes itself
     * as an intersection option from other words by looking at the crossing word's intersection options list.
     * Once placed, the list of unplaced intersection options of this word are entirely nullified.
     * @param row row of the first letter of this word
     * @param col col of the first letter of this word
     * @param vertical alignment of the word
     */
    public void placeAt(int row,int col,boolean vertical){
        this.row = row;
        this.col = col;
        this.vertical = vertical;
        this.placed = true;

        Word lastWord = null;
        // remove all intersection options where this word is the crossing word
        for(IntersectionOption intersectionOption : this.unplacedIntersectionOptions){

            // check for unique words with which it is intersecting
            if(intersectionOption.crossing != lastWord && intersectionOption.crossing.unplacedIntersectionOptions!=null){

                // iterate across the list of the crossing word and eliminate every option that has this
                // word present as the crossing word
                Iterator<IntersectionOption> crossingWordIntersectionOptions = intersectionOption.crossing.unplacedIntersectionOptions.iterator();
                while(crossingWordIntersectionOptions.hasNext()){

                    // when two words intersect, they appear on both the words list. They are thus symmetric
                    IntersectionOption symmetricIntersectionOption = crossingWordIntersectionOptions.next();

                    // check if the crossing word of the other word's intersection option is this word. if so, remove
                    if(symmetricIntersectionOption.crossing==this){
                        crossingWordIntersectionOptions.remove();
                    }
                }

                // there might by multiple intersection options with this word, but we remove all of them in one go
                lastWord = intersectionOption.crossing;
            }
        }

        //once this word is placed, we intentionally nullify unplaced intersection options list
        this.unplacedIntersectionOptions = null;
    }

    /**
     * Shifts the words' location by specified amount
     * @param dRow displacement in row
     * @param dCol displacement in column
     */
    public void shiftBy(int dRow,int dCol){
        this.row += dRow;
        this.col += dCol;
    }

    /**
     * Finds intersection options only for a particular index
     * @param wordList words across which intersections will be checked character by character
     * @param amongstPlacedWordsOnly tells weather to check within the group of placed words(true) or ALL words(false)
     * @param index the index at which the resulting intersection options is to be found
     * @return list of intersection options at a given index
     */
    public LinkedList<IntersectionOption> findAllIntersectionOptions(List<Word> wordList,boolean amongstPlacedWordsOnly,int index){
        return this.findAllIntersectionOptions(wordList,amongstPlacedWordsOnly,index,1);
    }

    /**
     * Finds all intersection options in this word
     * @param wordList words across which intersections will be checked character by character
     * @param amongstPlacedWordsOnly tells weather to check within the group of placed words(true) or ALL words(false)
     * @return list of intersection options for the entire word index by index
     */
    public LinkedList<IntersectionOption> findAllIntersectionOptions(List<Word> wordList, boolean amongstPlacedWordsOnly){
        return this.findAllIntersectionOptions(wordList,amongstPlacedWordsOnly,0,this.name.length());
    }

    /**
     * Finds intersection options for a given range within the length of this word
     * @param wordList words across which intersections will be checked character by character
     * @param amongstPlacedWordsOnly tells weather to check within the group of placed words(true) or ALL words(false)
     * @param start the start index of the range
     * @param length length of the range starting from the starting index
     * @return list of intersection options for the given range of indices
     */
    public LinkedList<IntersectionOption> findAllIntersectionOptions(List<Word> wordList,boolean amongstPlacedWordsOnly, int start,int length){

        LinkedList <IntersectionOption> intersectionOptions = new LinkedList<>();

        for(int i = start; (i < start + length) && (i < this.name.length()); i++){

            char current = this.name.charAt(i);

            // find intersecting words for this character
            for(Word word : wordList){

                // skip word if its unplaced and we only need to check against placed words, also skip this word itself
                if((!word.placed && amongstPlacedWordsOnly)||(word==this)){
                    continue;
                }

                //find all indices of the current character and for each create a new intersection option for this word
                int wordLength = word.name.length();
                for(int j=0;j<wordLength;j++){

                    //if the letters of the two words match, it means there is an intersection
                    if(word.name.charAt(j)==current){

                        //create and add a new intersection option
                        intersectionOptions.add(new IntersectionOption(this,i,word,j));
                    }
                }


            }
        }

        return intersectionOptions;
    }

    public boolean containsLettersWithGap(ArrayList<LetterGap> letterGapList){

        // return false for empty/ null lists
        if(letterGapList==null || letterGapList.size()==0){
            return false;
        }

        //for each letter
        int length = this.name.length();
        for(int i=0;i<length;i++){

            LetterGap firstLetterGap = letterGapList.get(0);
            char current = this.name.charAt(i);

            // check if it is matching with the first letter gap
            if(current == firstLetterGap.letter){

                //iterate across letter gaps to verify if they match
                int j=i;
                boolean letterSequenceBroken = false;
                for(LetterGap letterGap : letterGapList){

                    // for each letter gap the characters at respective indices should match, else break
                    if(this.name.charAt(j)!=letterGap.letter){
                        letterSequenceBroken = true;
                        break;
                    }

                    // move to the next letter in the sequence by adding the gap of this letter gap
                    j+= (letterGap.gap + 1); //add 1 to jump over gap

                    // if counter overflows through the length of the string, naturally, the sequence doesn't exist
                    if(j>=length){
                        letterSequenceBroken = true;
                        break;
                    }
                }

                // return true if letter sequence was not broken throughout the last loop
                if(!letterSequenceBroken){
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * For non intersecting or non coinciding words, this method checks for adjacency. This method does not check for
     * intersection and coincidence. Words are considered not to be touching if one of the words is at least starting
     * diagonally after the end of the other word
     * @param wordToPlace an unplaced word
     * @param row projected row
     * @param col projected column
     * @param wordToPlaceIsVertical true if unnplaced word is vertical, false if horizontal
     * @return true if the unplaced wor is touching.
     */
    public boolean willAdjacentlyTouch(Word wordToPlace, int row, int col, boolean wordToPlaceIsVertical){

        if ((this.vertical && wordToPlaceIsVertical) && //both words are vertical
                ((this.col == col-1)||(this.col == col+1))) { // both words are adjacent

            return doSpansOverlap(this.row,row,wordToPlace.name.length());
        }
        else if (this.vertical && !wordToPlaceIsVertical){  // only this word is vertical
                 // horizontal word is within span of vertical word

            return isHorizontalWordTouching(this.row,this.col,this.name.length(),row,col,wordToPlace.name.length());
        }
        else if (!this.vertical && wordToPlaceIsVertical){ // only word to place is vertical

            return isHorizontalWordTouching(row,col,wordToPlace.name.length(),this.row,this.col,this.name.length());
        }
        else if((!this.vertical && !wordToPlaceIsVertical) && // both words horizontal
                ((this.row == row+1)||(this.row == row-1))) { // both words are adjacent

            return doSpansOverlap(this.col,col,wordToPlace.name.length());
        }
        else{
            return false;
        }
    }

    /**
     * Determines which word starts first and checks if it ends before the beginning of the later word.
     * This assumes that the other word is parallel to this word.
     */
    private boolean doSpansOverlap(int thisWordStart,int otherWordStart,int otherWordLength){

        if(thisWordStart<otherWordStart){
            return !((thisWordStart +this.name.length() - 1) < otherWordStart);
        }else if(thisWordStart>otherWordStart){
            return !((otherWordStart + otherWordLength - 1) < thisWordStart);
        }else{
            return true;
        }
    }

    /**
     * Checks all possible cases of a horizontal word touching a vertical word
     * @param vr vertical word row
     * @param vc vertical word column
     * @param vl vertical word length
     * @param hr horizontal word row
     * @param hc horizontal word column
     * @param hl horizontal word length
     * @return True if the horizontal word touches above, below or around the side, false otherwise.
     * False even if they intersect.
     */
    private boolean isHorizontalWordTouching(int vr,int vc,int vl,int hr,int hc,int hl){

        if(hr>=vr && hr < (vr + vl)){// horizontal word is within span of vertical word

            // either horizontal word is after vertical word or before
            return ((vc+1==hc) || ((hc + hl - 1 + 1)==vc));
        }
        else if(((hr+1)==vr)||(hr==(vr+vl))){ // horizontal word just above or just below the vertical word

            // check if the column of the vertical word lies with the span of the horizontal word
            return vc>=hc && vc< (hc+hl);
        }
        else{ // not touching

            return false;
        }

    }

    /**
     * Finds all corners amongst the placed words in the list. Corners are identified by the right angles formed in the
     * intersection of two crossing words.
     * @param wordList A full list of words.Only placed words will be checked against for intersections
     * @return List of corners amongst the already placed words in the grid. If no corners are present, the list will
     * be of size 0
     */
    public LinkedList<Corner> findAllCorners(List<Word> wordList){
        LinkedList<Corner> cornerList = new LinkedList<>();
        List<IntersectionOption> gridIntersections=this.findAllIntersectionOptions(wordList,true);

        for(IntersectionOption intersectionOption : gridIntersections){
            List<Corner> cornersFromThisIntersection = intersectionOption.computeCorners();
            cornerList.addAll(cornersFromThisIntersection);
        }

        return cornerList;
    }

    /**
     * Finds intersection options amongst all words including placed words. This method is intended to be used at the
     * beginning of the placement algorithm
     * @param wordList the list of words in the grid
     */
    public void computeIntersectionOptions(List<Word> wordList){
        this.unplacedIntersectionOptions = this.findAllIntersectionOptions(wordList,false);
    }

    /**
     * Returns total unplaced intersections
     * @return Placed words return 0 despite their intersection option list being nullified.
     * Unplaced words return size of the total number of intersection options available
     */
    public int getTotalIntersections(){
        if(this.unplacedIntersectionOptions==null){
            return 0;
        }else{
            return this.unplacedIntersectionOptions.size();
        }
    }

    /**
     * Checks if there exists an intersection with another word in the list of unplaced intersection option
     * @param word an possibly intersecting word
     * @return null if no intersection option exist, otherwise the intersection option itself
     */
    public IntersectionOption intersectsWtih(Word word){
        for(IntersectionOption intersectionOption : this.unplacedIntersectionOptions){
            if(intersectionOption.crossing == word){
                return intersectionOption;
            }
        }
        return null;
    }

}
