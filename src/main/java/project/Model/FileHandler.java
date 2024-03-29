package project.Model;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileHandler {
    private File file = new File("src/main/java/project/Model/metadata.txt");

    public void writeToFile(String filename, String extension, String key1, String key2, String key3) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        if(file.exists()) {
            lines = new ArrayList<>(Files.readAllLines(file.toPath()));

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(filename)) {
                    lines.set(i, filename + "," + extension + "," + key1 + "," + key2 + "," + key3);
                    found = true;
                    System.out.println("Znaleziono plik w bazie");
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("Dodano plik do bazy");
            lines.add(filename + "," + extension + "," + key1 + "," + key2 + "," + key3);
        }

        Files.write(file.toPath(), lines);
    }

    public String[] getMetadata(String filename) throws IOException {
        if(file.exists()) {
            List<String> lines = new ArrayList<>(Files.readAllLines(file.toPath()));

            for (String line : lines) {
                if (line.split(",")[0].equals(filename)) {
                    return line.split(",");
                }
            }
        }

        return null;
    }
}
