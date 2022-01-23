package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class WordChecker {

    //Not needed.
    public boolean checkWord(String word) throws IOException {
        File file = new File("collins_scrabble_words_2019.txt");
        Scanner sc = new Scanner(file, StandardCharsets.UTF_8);
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(" ");
                if(line[0].toLowerCase().equals(word.toLowerCase())){
                    return true;
                }
            }return false;
    }

}
