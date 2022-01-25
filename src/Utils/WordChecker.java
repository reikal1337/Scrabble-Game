package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

public class WordChecker {
    private static ArrayList<String> blackList;

    //All words that can be used for game.
    private static ArrayList<String> checkWords;

    public WordChecker(){
        checkWords = new ArrayList<String>();
        blackList = new ArrayList<String>();
        Collections.addAll(blackList,"II","III","IV","VI","VII","VIII","IX","XI","XII","XIII");
        papulateCheckWords();
    }

    //From document takes words and adds them to checkWords array.
    private void papulateCheckWords() {
        File file = new File("src\\Utils\\collins_scrabble_words_2019.txt");
        Scanner sc = null;

        try {
            sc = new Scanner(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (sc.hasNext()) {
            String[] line = sc.next().split(" ");
            String theWord = line[0].replace("\t", "");
            theWord = theWord.replace(",", "");
            if (wordIsUpperCase(theWord) && theWord.length() > 1 && !blackList.contains(theWord)) {
                checkWords.add(theWord);
            }
        }sc.close();

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
