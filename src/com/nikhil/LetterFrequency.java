package com.nikhil;


import java.util.LinkedList;

/** Holds a the frequency and occurrences of a letter across a word list. */
public class LetterFrequency implements Comparable<LetterFrequency>{
    final char letter;
    final LinkedList<LetterOccurrence> occurrences = new LinkedList<>();

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
}
