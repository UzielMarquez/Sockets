import msp.UserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    public static final String CLASS_NAME = ConnectionHandler.class.getSimpleName();
    public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private UserManager users;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private String username;

    public ConnectionHandler(UserManager u, Socket s) {
        users = u;
        clientSocket = s;

        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            output.println("Welcome to MSP Server!");

            String buffer = null;
            while ((buffer = input.readLine()) != null) {
                String command = buffer.trim();
                if (command.startsWith("CONNECT")) {
                    username = command.substring(command.indexOf(' ') + 1).trim();
                    boolean isConnected = users.connect(username, clientSocket);
                    if (isConnected) {
                        output.println("Connected as " + username);
                    } else {
                        output.println("Username already taken.");
                    }
                } else if (command.startsWith("DISCONNECT")) {
                    username = command.substring(command.indexOf(' ') + 1).trim();
                    boolean isDisconnected = users.disconnect(username);
                    if (isDisconnected) {
                        output.println("Disconnected " + username);
                        break;
                    } else {
                        output.println("User not found.");
                    }
                } else if (command.startsWith("SEND")) {
                    String message = command.substring(command.indexOf('#') + 1, command.indexOf('@'));
                    String recipient = command.substring(command.indexOf('@') + 1).trim();
                    users.send(message, recipient);
                } else {
                    output.println("Invalid command");
                }
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Implement run method
    }
}

