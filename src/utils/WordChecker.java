package utils;

import java.io.*;
import java.util.ArrayList;

public class WordChecker {

    //All words that can be used for game.
    private static ArrayList<String> checkWords;
    private final String PATH = "src\\Utils\\collins_scrabble_words_2019.txt";

    public WordChecker() {
        checkWords = new ArrayList<String>();
        papulateCheckWords();
    }
    //Before 556982  After 279496
    private void papulateCheckWords() {
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(PATH));
            while ((line = br.readLine()) != null) {
                String[] seperatedWords = line.split("\t");
                if (seperatedWords.length == 2) {
                    if (wordIsUpperCase(seperatedWords[0])) {
                        checkWords.add(seperatedWords[0]);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            try {
                br.close();
            } catch (IOException ex) {
            }

        } catch (IOException e) {
            System.out.println("Can't load word file!");
            e.printStackTrace();
        }

    }

    //Checks if word is uppercase if yes returns true otherwise false.
    public boolean wordIsUpperCase(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
            } else {
                return false;
            }
        }
        return true;
    }

    //checks if word exists in checkWords array.
    public boolean checkWord(String word) {
        if (checkWords.contains(word.toUpperCase())) {
            return true;
        }
        return false;
    }

}
