package com.nikhil;

import java.util.ArrayList;
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

    public Word(String name, String description) {
        this.name = name.toUpperCase();
        this.description = description;
    }

    @Override
    public String toString() {
        return name + "(" + row + "," + col + "," + (vertical ? "v" : "h") + ","+(placed ? "p" : "u")+")";
    }

    public void placeAt(int row,int col,boolean vertical){
        this.row = row;
        this.col = col;
        this.vertical = vertical;
        this.placed = true;
    }

    public void shiftBy(int dRow,int dCol){
        this.row += dRow;
        this.col += dCol;
    }

    public List<IntersectionOption> findAllIntersectionOptions(List<Word> wordList, boolean skipPlacedWords){

        List <IntersectionOption> intersectionOptions = new LinkedList<>();

        int length = this.name.length();
        for(int i=0;i<length;i++){

            char current = this.name.charAt(i);

            // find intersecting words for this character
            for(Word word : wordList){

                // skip placed words (if required) or skip if this word is actually itself
                if((word.placed && skipPlacedWords)||(word==this)){
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

}
