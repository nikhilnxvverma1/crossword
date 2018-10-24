package com.nikhil;


import java.util.ArrayList;

/** Holds a the frequency and occurrences of a letter across a word list. */
public class LetterFrequency implements Comparable<LetterFrequency>{
    final char letter;
    final ArrayList<LetterOccurrence> occurrences = new ArrayList<>();

    public LetterFrequency(char letter) {
        this.letter = letter;
    }

    /**
     * Adds this words to the list of letter occurrences (for each letter) if it contains the letter. Also ups the count.
     * @param word the word needed to check for containment of letter
     * @return true if the word contains this letter at least once.
     */
    public boolean addToCountIfLetterIsPresent(Word word){
        boolean letterFound = false;
        for (int i = 0; i < word.name.length(); i++) {
            if(word.name.charAt(i)==this.letter){
                letterFound = true;
                occurrences.add(new LetterOccurrence(word,i));
            }
        }
        return letterFound;
    }

    @Override
    public int compareTo(LetterFrequency other) {
        return this.occurrences.size()-other.occurrences.size();
    }

    public int getFrequency(){
        return occurrences.size();
    }

    /**
     * Finds an intersection option amongst all the occurrences of this letter frequency such that their placement is
     * feasible in the grid. Preference is given to intersection options with both words being unplaced
     * @param grid the grid which will be used to check for placement of the intersection option
     * @return a feasible intersection option with at least one word unplaced. Null if no such intersection option is found.
     */
    public IntersectionOption findAvailableIntersectionOption(Grid grid){

        LetterOccurrence crossingWordFirstOccurrence = null;
        int crossingWordFirstOccurrenceIndex = -1;


        // (priority) look for two distinct unplaced words, otherwise (see below)
        int index =0;
        for(LetterOccurrence letterOccurrence : this.occurrences){

            // find unplaced word(s)
            if(!letterOccurrence.getWord().placed){

                if(crossingWordFirstOccurrence == null){

                    // this is possibly the first node of the many nodes of the same word in the linked list
                    crossingWordFirstOccurrence = letterOccurrence;
                    crossingWordFirstOccurrenceIndex = index;

                } else if(letterOccurrence.getWord()!=crossingWordFirstOccurrence.getWord()){ // source word found

                    return new IntersectionOption(letterOccurrence.getWord(),letterOccurrence.getIndex(),crossingWordFirstOccurrence.getWord(),crossingWordFirstOccurrence.getIndex());
                }
            }
            index++;
        }

        // if not found, check if their is a crossing word occurrence
        if(crossingWordFirstOccurrence==null){
            return null; // this signals that all the occurrences for this letter are placed
        }


        // look for a placed word such that the placement of the crossing word should be allowed in the grid
        for(LetterOccurrence letterOccurrence : this.occurrences){

            // find a placed word that provides a feasible placement in the grid
            // we can safely assert that this letter occurrence will never be the same as any of the crossing word's
            if(letterOccurrence.getWord().placed){

                // we will scan across all the letter occurrences following the first crossing word occurrence
                // as long as they are all the same word as the crossing word
                for (int i = crossingWordFirstOccurrenceIndex;
                     i < occurrences.size() && occurrences.get(i).getWord()==crossingWordFirstOccurrence.getWord();
                     i++) {

                    // check if the the crossing word can be safely placed in the grid or not
                    LetterOccurrence crossingWordOccurrence = occurrences.get(i);
                    IntersectionOption withPlacedWord = new IntersectionOption(
                            letterOccurrence.getWord(),
                            letterOccurrence.getIndex(),
                            crossingWordOccurrence.getWord(),
                            crossingWordOccurrence.getIndex());

                    Location projectedLocationOfCrossingWord = withPlacedWord.projectedLocationOfCrossingWord();
                    int row = projectedLocationOfCrossingWord.row;
                    int col = projectedLocationOfCrossingWord.col;
                    boolean vertical = !withPlacedWord.source.vertical;

                    if(grid.isPlacementOfWordAllowed(withPlacedWord.crossing,row,col,vertical)){
                        return withPlacedWord;
                    }
                }

            }
        }

        return null;
    }
}
