package dict4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WordNetConverter {

    // List of input files
    private static final List<String> INPUT_FILES = Arrays.asList(
    	"/Users/kf/old-eclipse-workspace/Assignment2/src/dict4/data.adj",
    	"/Users/kf/old-eclipse-workspace/Assignment2/src/dict4/data.adv",
    	"/Users/kf/old-eclipse-workspace/Assignment2/src/dict4/data.noun",
    	"/Users/kf/old-eclipse-workspace/Assignment2/src/dict4/data.verb"
        // Add paths to additional files as needed
    );

    // Path to the output file
    private static final String OUTPUT_FILE = "src/dictionary.txt";

    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            for (String filePath : INPUT_FILES) {
                processFile(filePath, writer);
            }
            System.out.println("Combining completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(String filePath, FileWriter writer) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String formattedLine = processLine(line);
                if (formattedLine != null) {
                    writer.write(formattedLine + System.lineSeparator());
                }
            }
        }
    }

    private static String processLine(String line) {
        // Customize this method based on the specific format of your WordNet files
        // Example: Assume each line starts with the word followed by a definition
        String[] parts = line.split("\\s+", 2);
        if (parts.length < 2) return null;

        String word = parts[0];
        String definition = parts[1];

        // Example format: word|definition
        return word + "|" + definition;
    }
}
