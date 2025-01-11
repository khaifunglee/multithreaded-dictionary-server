import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * This class acts as a separate thread that handles all functionalities of the dictionary file for the server.
 * Khai Fung Lee, 1242579 
 */
public class DictionaryProcessor implements Runnable {
    private String filePath;
    private Map<String, String> dictionary;

    public DictionaryProcessor(String filePath) {
        this.filePath = filePath;
        this.dictionary = new HashMap<>();
    }

    @Override
    public void run() {
        loadDictionary();
    }

    private void loadDictionary() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
            	
                String[] parts = line.split("\\|");
                if (parts.length < 3) continue; 
                
                String[] rawWords = parts[1].split(" ");
                String word = rawWords[3];
                
                String definition = parts[2];
                
                // Store index & definition of word into hash map
                dictionary.put(word, definition);
                
            }
            
        } catch (FileNotFoundException fnfe) {
        	System.out.println("Dictionary file not found. Please try again. Closing server.");
        	System.exit(0);
        } catch (IOException e) {
        	System.out.println("Dictionary file could not be loaded. Please try again. Closing server.");
        	System.exit(0);
        } 
    }
    /*
     * This method searches for a word's definition.
     */
    public String getDefinition(String word) {
    	return dictionary.getOrDefault(word, "Word not found");
    }
    /*
     * This method adds a specified word and associated definition(s) into the dictionary.
     */
    public void addWord(String word, String[] definitions) {
    	// Combine the list of definitions into one string
    	String definition = "";
    	for (String def : definitions) {
    		definition += def + ";";
    	}
    	dictionary.put(word, definition);
    }
    /*
     * This method removes a specified word from the dictionary.
     */
    public void removeWord(String word) {
    	dictionary.remove(word);
    }
    /*
     * This method adds a meaning to a specified word in the dictionary.
     */
    public void addMeaning(String word, String definition) {
    	// Append new meaning to current list of definitions
    	String newDefinitions = dictionary.get(word) + ";"+definition;
    	
    	dictionary.put(word, newDefinitions);
    }
    /*
     * This method updates a specified word's old meaning to a new meaning in the dictionary. 
     */
    public void updateMeaning(String word, String oldDef, String newDef) {
    	// Replace old meaning with new meaning
    	String updatedDef = (dictionary.get(word)).replace(oldDef, newDef);
    	
    	dictionary.put(word, updatedDef);
    }

    public Map<String, String> getDictionary() {
        return dictionary;
    }
}
