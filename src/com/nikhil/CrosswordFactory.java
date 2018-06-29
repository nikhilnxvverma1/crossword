package com.nikhil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Responsible for reading word list and generating main crossword. */
public class CrosswordFactory {

    public static void generateCrosswordFromCSV(String filename) throws IOException{

        try{
            List<Word> wordList = readWordList(filename);
            printWordList(wordList);

        }catch (IOException ex){
            throw ex;
        }

    }

    private static List<Word> readWordList(String filename) throws IOException{
        ArrayList<Word> wordList = new ArrayList<>();

        //read all words with their meanings/descriptions line by line
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){

            //the words are in CSV format
            String line;
            while ((line = br.readLine()) != null){

                String [] wordMeaning = line.split(",");
                Word word = new Word(wordMeaning[0].trim(),wordMeaning[1].trim());
                wordList.add(word);
            }

        }catch (IOException ex){
            throw ex;
        }


        return wordList;
    }

    private static void printWordList(List<Word> wordList){
        System.out.println("Word list:");
        for(Word word : wordList){
            System.out.println(word.toString());
        }
    }
}
