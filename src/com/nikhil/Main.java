package com.nikhil;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        String filename = "random.txt";

        try {
            Grid grid = CrosswordFactory.generateCrosswordFromCSV(filename);
            grid.placeWordsInGrid();

        } catch (IOException e) {
            System.out.println("It seems like the file "+filename+" does not exist");
            e.printStackTrace();
        }
    }
}
