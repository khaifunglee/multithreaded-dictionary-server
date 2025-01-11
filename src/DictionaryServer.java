import java.io.*;
import java.net.*;
import java.util.Arrays;

import javax.net.ServerSocketFactory;

/*
 * This class acts as a server that reads a dictionary file and accepts connections from multiple clients.
 * Khai Fung Lee, 1242579
 */
public class DictionaryServer {
	
	private static DictionaryProcessor dictionaryProcessor;

    public static void main(String[] args) {
    	// Take args[1] input as port number
    	// Error handling if input isn't int
    	int port = 0;
    	String filePath = "";
    	try {
    		port = Integer.parseInt(args[0]);
    		// Take args[1] as file path containing dictionary data & handle error
            //String filePath = "/Users/kf/old-eclipse-workspace/Assignment2/src/dictionary.txt";
        	filePath = args[1];
        	
    	} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
    		System.out.println("Insufficient amount/type of command line parameters. Please try again. Closing server.");
    		System.exit(0);
    	}
    	// Process dictionary in a separate thread
        dictionaryProcessor = new DictionaryProcessor(filePath);
        Thread tDict = new Thread(dictionaryProcessor);
        tDict.start(); 
        
        // Create serverSocket with factory
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        
        try(ServerSocket server = factory.createServerSocket(port)) {
        	
        	System.out.println("Waiting for client connection...");
        	
        	// Wait for connections to accept
        	while(true) {
        		Socket client = server.accept();
        		System.out.println("Client from " + client.getInetAddress() + " applying for connection");
        		
        		// Create a new thread to handle connection
        		Thread t = new Thread(() -> serveClient(client));
        		t.start();
        	}
        	
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        }
    }
        
    /*
     * Method used to confirm connection between client & server
     */
    private static void serveClient(Socket client) {
    	
    	try (Socket clientSocket = client) {
    		// Input stream
    		DataInputStream input = new DataInputStream(clientSocket.getInputStream());
    		// Output stream
    		DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
    		
    		// Print incoming message from connecting client
    		String initialMsg = input.readUTF();  
    		System.out.println("Client says at first: " + initialMsg);

    		// Send outgoing message to connecting client
    		output.writeUTF("Client with IP address " + client.getInetAddress() + " successfully connected to server.");
    		output.flush();
    		
    		boolean isConnected = true;
    		while (isConnected) {
    			try {
		    		// Print incoming message from connecting client
    				String request = input.readUTF();
		    		System.out.println("Client says: " + request);
		    		
		    		// Check message to see what action should be performed 
		    		String instruction = request.substring(0, 4);
		    		request = request.substring(4);
		    		// Initialize String variables for switch case
		    		String response = "";
		    		String word = "";
		    		String definition = "";
		    		String[] parts;
		    		String[] definitions;
		    		
		    		// Determine action to perform, then send instruction back to client
		    		switch(instruction) {
		    		// Find word
		    		case "FIND":
		    			// Query incoming word and return definition
		    			definition = dictionaryProcessor.getDefinition(request);
		    			System.out.println("Sending back definition of "+request+": "+definition);
		    			
		    			response = "FIND"+definition;
		    			break;
		    		// Add word
		    		case "ADDW":
		    			// Check if word exists, if yes then return error, if no then add new word
		    			parts = request.split(",");
		    			word = parts[0];
						definitions = Arrays.copyOfRange(parts, 1, parts.length); // what if definitions are 'blank'?
						
		    			if (dictionaryProcessor.getDefinition(word).equals("Word not found")) {
		    				
		    				// Add new word and meanings to dictionary
		    				System.out.println("Adding "+word+" with "+definitions[0]+" into dictionary...");
		    				dictionaryProcessor.addWord(word, definitions);
		    				
		    				response = "ADDW";
		    			} else {
		    				response = "ADWE";
		    			}
		    			
		    			break;
		    		case "DELW":
		    			// Check if word exists, if yes then remove word, if no then return error
		    			if (dictionaryProcessor.getDefinition(request).equals("Word not found")) {
		    				// Return error message that there is no word to remove in dictionary
		    				response = "DELE";
		    			} else {
		    				// Remove word from dictionary
		    				System.out.println("Removing "+request+" from dictionary...");
		    				dictionaryProcessor.removeWord(request);
		    				
		    				response = "DELW";
		    			}
		    			
		    			break;
		    		case "ADDM":
		    			// Check if word exists, if yes then add meaning(s), if no then return error
		    			parts = request.split(",");
		    			word = parts[0];
						definition = parts[1]; // what if definitions are 'blank'?
						
		    			if (dictionaryProcessor.getDefinition(word).equals("Word not found")) {
		    				// Return error message that there is no word to add meaning to
		    				response = "ADME";
		    			} else if (dictionaryProcessor.getDefinition(word).contains(definition)){
		    				// Return error message that the same meaning already exists
		    				response = "AMEE";
		    			} else {
		    				// Add meaning to existing word in dictionary
		    				System.out.println("Adding def"+definition+" to the word "+word);
		    				dictionaryProcessor.addMeaning(word, definition);
		    				
		    				response = "ADDM";
		    			}
		    			
		    			break;
		    		case "UPDW":
		    			// Check if word exists, if yes then update meaning, if no then return error
		    			parts = request.split(",");
		    			word = parts[0];
		    			String existingDef = dictionaryProcessor.getDefinition(word);
						String oldDef = parts[1]; // what if definitions are 'blank'?
						String newDef = parts[2];
						
		    			if (existingDef.equals("Word not found") || !existingDef.contains(oldDef)) {
		    				// Return error message that there is no word to add meaning to
		    				response = "UPDE";
		    			} else {
		    				// Update existing meaning to new meaning in dictionary
		    				System.out.println("Updating old def"+oldDef+" to the word "+newDef);
		    				dictionaryProcessor.updateMeaning(word, oldDef, newDef);
		    				
		    				response = "UPDW";
		    			}
		    			
		    			break;
		    		default:
						// Unspecified instruction
						System.out.println("Unable to fulfill request, client instructions unclear.");
						
						break;
		    		}
		    		// Send response back to client depending on the specified instruction
		    		output.writeUTF(response);
		    		output.flush();
		    		
    			} catch (EOFException | SocketException e) {
    				// Disconnect client
    				isConnected = false;
    				System.out.println("Client disconnected.");
    				input.close();
    				output.close();
    				clientSocket.close();
    			}
    		}
    		System.out.println("No more incoming messages.");
    		
    	} catch (IOException ioe) {
    		ioe.printStackTrace();
    	}
    	
        
    }
    
    
}
