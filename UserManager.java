
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class UserManager {

    public static final String CLASS_NAME = UserManager.class.getSimpleName();
    public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private Map<String, Socket> connections;

    public UserManager() {
        connections = new HashMap<>();
    }

    public boolean connect(String user, Socket socket) {
        if (connections.containsKey(user)) {
            return false;
        } else {
            connections.put(user, socket);
            return true;
        }
    }

    public boolean disconnect(String user) {
        if (connections.containsKey(user)) {
            connections.remove(user);
            return true;
        } else {
            return false;
        }
    }

    public Set<String> getConnectedUsers() {
        return connections.keySet();
    }

    public void send(String message, String recipient) {
        Socket recipientSocket = connections.get(recipient);
        if (recipientSocket != null) {
            try {
                PrintWriter output = new PrintWriter(recipientSocket.getOutputStream(), true);
                output.println(message);
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
