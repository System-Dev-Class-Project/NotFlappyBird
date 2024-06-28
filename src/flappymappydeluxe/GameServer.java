package flappymappydeluxe;

import java.io.*;
import java.net.*;

public class GameServer {
    private static int highScore = 0;
    private static String highScoreName = "";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Received: " + inputLine);  // Debug print
                        if ("RESET_HIGHSCORE".equals(inputLine)) {
                            highScore = 0;
                            highScoreName = "";
                            out.println("Highscore has been reset.");
                        } else {
                            String[] parts = inputLine.split(",");
                            if (parts.length == 2) {
                                String name = parts[0];
                                int score = Integer.parseInt(parts[1]);

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
