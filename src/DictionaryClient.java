import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * This class handles any client that is trying to connect to the server by using a GUI.
 * Khai Fung Lee, 1242579
 */
public class DictionaryClient {
	
	private DataInputStream input;
	private DataOutputStream output;
	private DictionaryGUI gui;
	private Socket socket;
	
	private static String ip;
	private static int port;
	
	public static void main(String[] args) {
		
		// Create and display the GUI in an Event Dispatch Thread (EDT)
		DictionaryClient client = new DictionaryClient(); 
		client.gui = new DictionaryGUI(client);
		client.gui.startGUI();
		
		try {
			// Take args input as IP address and port number
			ip = args[0];
			port = Integer.parseInt(args[1]);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			System.out.println("Insufficient amount/type of command line parameters. Please try again. Closing client.");
    		System.exit(0);
		}
		
		// Create client socket to connect to server
		client.connectToServer(ip, port);
	}
		
	/*
	 * Create client socket to connect to server.
	 */
	private void connectToServer(String ip, int port) {
		
		try {
			socket = new Socket(ip, port);
			// Input & Output stream
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			
			String sendData = "New Client from " +socket.getInetAddress()+" wants to connect.";
			
			output.writeUTF(sendData);
			//System.out.println(sendData);
			output.flush();
			
			gui.setMessage(input.readUTF());
			
			boolean isConnected=true;
			// Listen for requests from server
			while (isConnected) {
				//gui.setMessage("Waiting for request...");
				
				String response = input.readUTF();
				System.out.println("Received response from server: " + response);
				
				// Check instruction sent by server to determine GUI display
				String instruction = response.substring(0, 4);
				response = response.substring(4);
				
				switch(instruction) {
				case "FIND":
					// Update GUI with definition of queried word
					gui.setMessage(response);
					
					break;
				case "ADDW":
					// Update GUI with success message
					gui.setMessage("New word successfully added.");
					
					break;
				case "ADWE":
					// Update GUI with error message
					gui.setMessage("Unable to add word as this word already exists in the dictionary.");
					
					break;
				case "DELW":
					// Update GUI with success message
					gui.setMessage("Successfully removed word.");
					
					break;
				case "DELE":
					// Update GUI with error message
					gui.setMessage("Unable to remove word as this word doesn't exist in the dictionary.");
					
					break;
					
				case "ADDM":
					// Update GUI with success message
					gui.setMessage("Successfully added new meaning to word.");
					
					break;
				case "ADME":
					// Update GUI with error message
					gui.setMessage("Unable to add meaning to word as this word doesn't exist in the dictionary.");
					
					break;
				case "AMEE":
					// Update GUI with error message
					gui.setMessage("Unable to add meaning to word as the same meaning already exists in the word.");
					
					break;
				case "UPDW":
					// Update GUI with success message
					gui.setMessage("Successfully updated meaning in specified word.");
					
					break;
				case "UPDE":
					// Update GUI with error message
					gui.setMessage("Unable to update meaning to word because word and/or specified meaning doesn't exist.");
					
					break;
				default:
					// Unspecified instruction
					gui.setMessage("Unable to fulfill request, server instructions unclear.");
					
					break;
					
				}
				
			}
			
		} catch (UnknownHostException uhe) {
			System.out.println("Unknown host name. Closing client.");
			gui.setMessage("Unknown host name. Closing client.");
			
			gui.terminate();
		} catch (IOException ioe) {
			System.out.println("Unable to connect to server. Closing client.");
			gui.setMessage("Unable to connect to server. Closing client.");
			
			gui.terminate();
		} 
	}
	
	/*
	 * Method to send query word to server.
	 */
	public void queryWord(String word, String instruction) {
		
		new Thread(() -> {
			try {
	            gui.setMessage("Sending request to query the word " + word);
	            output.writeUTF(instruction + word);
	            output.flush();
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
        }).start();
    }
	
}
