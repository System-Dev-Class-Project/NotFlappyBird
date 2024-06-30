package flappymappydeluxe;

import java.io.*;
import java.net.*;

/**
 * GameServer is responsible for handling client connections and managing the game's high score.
 */
public class GameServer {
    // Static variables to store the high score and the name of the player who achieved it
    private static int highScore = 0;
    private static String highScoreName = "";

    /**
     * Main method to start the server.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Try-with-resources statement to automatically close the ServerSocket
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started...");

            // Infinite loop to continuously accept client connections
            while (true) {
                // Try-with-resources statement to automatically close client socket and streams
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String inputLine;
                    // Read input from the client
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Received: " + inputLine);  // Debug print
                        if ("RESET_HIGHSCORE".equals(inputLine)) {
                            // Reset high score if the special command is received
                            highScore = 0;
                            highScoreName = "";
                            out.println("Highscore has been reset.");
                        } else {
                            // Parse the input for name and score
                            String[] parts = inputLine.split(",");
                            if (parts.length == 2) {
                                String name = parts[0];
                                int score = Integer.parseInt(parts[1]);

                                // Update high score if the new score is higher
                                if (score > highScore) {
                                    highScore = score;
                                    highScoreName = name;
                                }
                                System.out.println("Current high score: " + highScore + " by " + highScoreName);  // Debug print
                                out.println(highScoreName + "," + highScore);
                            } else {
                                System.out.println("Invalid input format: " + inputLine);  // Debug print
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Exception caught when trying to listen on port 12345 or listening for a connection");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not listen on port 12345");
            System.out.println(e.getMessage());
        }
    }
}
