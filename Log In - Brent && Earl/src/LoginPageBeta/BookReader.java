package LoginPageBeta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookReader {

    public static List<String[]> readBooks(String filename) {
        List<String[]> books = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookInfo = line.split(","); //important stuff, delimiter rahhhhhhhhhhhhhhh
                books.add(bookInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }
}
