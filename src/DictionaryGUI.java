import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/*
 * This class acts as a separate thread that handles the client GUI for each client.
 */
public class DictionaryGUI {

	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;
	
	private DictionaryClient client;

	/**
	 * Launch the application.
	 */
	public void startGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//DictionaryGUI window = new DictionaryGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DictionaryGUI(DictionaryClient client) {
		this.client = client;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setBounds(6, 96, 324, 170);
		frame.getContentPane().add(textArea);
		
		JButton btnQueryButton = new JButton("Find Word");
		
		// Button to find word in dictionary file
		btnQueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String word = textField.getText();
				
				if (!word.isBlank()) {
					client.queryWord(word, "FIND");
				} else {
					textArea.setText("Please enter a word to query.\nInput: [word to search]");
				}
				
			}
		});
		
		btnQueryButton.setBounds(333, 6, 117, 29);
		frame.getContentPane().add(btnQueryButton);
		
		JButton btnAddWordButton = new JButton("Add New Word");
		
		// Button to add word in dictionary file
		btnAddWordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String wordAndDef = textField.getText();
				// Split input into word to be added and definition(s) (Handle error)
				String[] parts = wordAndDef.split(",");
				
				if (parts.length > 1) {
					String word = parts[0];
					
					if (!word.isBlank()) {
						// First check if word exists in dictionary 
						client.queryWord(wordAndDef, "ADDW");
					} else {
						textArea.setText("Please enter a word to add.\nInput: [word to add], [meaning(s)]");
					}
				} else {
					textArea.setText("Insufficient amount of inputs. Please add associated meaning(s) to the word provided."
							+ "\nInput: [word to add], [meaning(s)]");
				}
				
			}
		});
		btnAddWordButton.setBounds(333, 36, 117, 29);
		frame.getContentPane().add(btnAddWordButton);
		
		// Button to remove word in dictionary file
		JButton btnRemoveButton = new JButton("Remove Word");
		btnRemoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String word = textField.getText();
				
				// Check if word exists in dictionary
				if (!word.isBlank()) {
					client.queryWord(word, "DELW");
				} else {
					textArea.setText("Please enter a word to remove.\nInput: [word to remove]");
				}
				
			}
		});
		btnRemoveButton.setBounds(333, 66, 117, 29);
		frame.getContentPane().add(btnRemoveButton);
		
		JTextArea txtrWelcomeMsg = new JTextArea();
		txtrWelcomeMsg.setLineWrap(true);
		txtrWelcomeMsg.setWrapStyleWord(true);
		txtrWelcomeMsg.setText("Welcome to the Dictionary Client. Please type a word and select your action with the buttons on the right and look for any following prompts.\n\n");
		txtrWelcomeMsg.setBackground(new Color(238, 238, 238));
		txtrWelcomeMsg.setBounds(6, 0, 324, 62);
		frame.getContentPane().add(txtrWelcomeMsg);
		
		textField = new JTextField();
		textField.setBounds(6, 66, 324, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		// Button to add new meaning to existing word
		JButton btnAddDefButton = new JButton("Add Definition");
		btnAddDefButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String wordAndDef = textField.getText();
				// Split input into word to be added and definition(s) (Handle error)
				String[] parts = wordAndDef.split(",");
				
				if (parts.length == 2) {
					String word = parts[0];
					
					if (!word.isBlank()) {
						// First check if word exists in dictionary 
						client.queryWord(wordAndDef, "ADDM");
					} else {
						textArea.setText("Please enter a word.\nInput: [existing word], [new meaning]");
					}
				} else {
					textArea.setText("Insufficient amount of inputs. Please provide only one word and one additional meaning."
							+ "\nInput: [existing word], [new meaning]");
				}
				
			}
		});
		btnAddDefButton.setBounds(333, 96, 117, 29);
		frame.getContentPane().add(btnAddDefButton);
		
		// Button to update definition of one of existing word's meanings
		JButton btnUpdateButton = new JButton("Update Definition");
		btnUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String wordAndDef = textField.getText();
				// Split input into word to be added and definition(s) (Handle error)
				String[] parts = wordAndDef.split(",");
				
				if (parts.length == 3) {
					String word = parts[0];
					
					if (!word.isBlank()) {
						// First check if word exists in dictionary 
						client.queryWord(wordAndDef, "UPDW");
					} else {
						textArea.setText("Please enter a word to update.\nInput: [word to update], [old meaning], [new meaning]");
					}
				} else {
					textArea.setText("Insufficient amount of inputs. Please provide one word, the old meaning to replace, and the new meaning."
							+ "\nInput: [word to update], [old meaning], [new meaning]");
				}
				
			}
		});
		btnUpdateButton.setBounds(333, 126, 117, 29);
		frame.getContentPane().add(btnUpdateButton);
		
		// Button to exit client
		JButton btnExitButton = new JButton("Exit Client");
		btnExitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminate();
				
			}
		});
		btnExitButton.setBounds(327, 237, 117, 29);
		frame.getContentPane().add(btnExitButton);
	}
	
	/*
	 * This method sets a specified message into the text area box.
	 */
	public void setMessage(String message) {
		EventQueue.invokeLater(() -> textArea.setText(message));
	}
	
	/*
	 * This method closes the GUI window.
	 */
	public void terminate() {
		System.out.println("Closing window.");
		frame.dispose();
		System.exit(0);
	}
}
