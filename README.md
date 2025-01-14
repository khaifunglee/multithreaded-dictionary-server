# multithreaded-dictionary-server

**Description:** This repository is to practice my understanding of a basic client-server architecture by using sockets and threads to implement the dictionary server.

**Features:**
  - Multi-threaded server to handle multiple clients concurrently
  - TCP connection between client and server
  - Client GUI window
  - Dictionary functions: QUERY word, ADD word/meaning, REMOVE word, UPDATE meaning)

**Technologies Used:** Java, Swing, TCP/IP, threading, etc.

**Prerequisites**: Ensure you have JDK 8 or higher installed.

**Setup Instructions:**
  - Building the project: open a terminal and navigate to the project directory. Then, compile the java source files located in the `src` directory: `javac -d bin src/*.java`
  - Running the project: Start the server and client(s) on separate terminal tabs. Then, start the server with `java -cp bin DictionaryServer` and client with `java -cp bin DictionaryClient`
