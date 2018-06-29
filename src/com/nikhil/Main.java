package com.nikhil;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        String filename = "random.txt";

        try {
            CrosswordFactory.generateCrosswordFromCSV(filename);
        } catch (IOException e) {
            System.out.println("It seems like the file "+filename+" does not exist");
            e.printStackTrace();
        }
    }
}
