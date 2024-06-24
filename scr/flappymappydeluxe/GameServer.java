package flappymappydeluxe;

import java.io.*;
import java.net.*;

public class GameServer {
    private static int highScore = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        if ("RESET_HIGHSCORE".equals(inputLine)) {
                            highScore = 0;
                            out.println("Highscore has been reset.");
                        } else {
                            int score = Integer.parseInt(inputLine);
                            if (score > highScore) {
                                highScore = score;
                            }
                            out.println(highScore);
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

