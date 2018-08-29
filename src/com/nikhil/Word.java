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
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Word{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
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

}
