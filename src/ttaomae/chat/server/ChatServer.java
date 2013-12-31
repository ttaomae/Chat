package ttaomae.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple chat server. Each time a message is received from a client, that message will be
 * broadcast to all connected clients, including the client that sent the message.
 *
 * @author Todd Taomae
 */
public class ChatServer implements Runnable
{
    private int portNumber;
    private List<ChatClientHandler> clients;

    /**
     * Constructs a new ChatServer with the specified port number
     *
     * @param portNumber port number to connect to
     */
    public ChatServer(int portNumber)
    {
        this.portNumber = portNumber;
        this.clients = new ArrayList<ChatClientHandler>();
    }

    @Override
    public void run()
    {
        try (
            ServerSocket serverSocket = new ServerSocket(this.portNumber);
        ) {
            System.out.println("Starting server on port " + this.portNumber);

            while (true) {
                // wait for client to connect, then add it
                addClient(serverSocket.accept());
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a new ChatClientHandler from the specified socket.
     *
     * @param clientSocket socket that the client is connected to
     */
    private void addClient(Socket clientSocket)
    {
        System.out.println("New client connected...");
        try {
            ChatClientHandler client = new ChatClientHandler(this, clientSocket);
            this.clients.add(client);
            new Thread(client).start();
        } catch (IOException e) {
            System.err.println("Error: could not connect to client");
        }
    }

    /**
     * Removes a client from the server.
     *
     * @param client client to remove
     */
    public void removeClient(ChatClientHandler client)
    {
        System.out.println("removing client: " + client.getUsername());
        this.clients.remove(client);
    }

    /**
     * Returns true if the specified username exists.
     *
     * @param username username to search for
     * @return true if the specified username exists
     */
    public boolean usernameExists(String username)
    {
        System.out.println("searching for username: " + username);
        for (ChatClientHandler c : clients) {
            if (username.equals(c.getUsername())) {
                System.out.println("found username: " + c.getUsername());
                return true;
            }
        }

        return false;
    }

    /**
     * Sends a message to all clients who have entered a valid username.
     *
     * @param message message to send
     */
    public void broadcastMessage(String message)
    {
        System.out.println("Sending message to all clients: " + message);
        for (ChatClientHandler c : clients) {
            if (c.getUsername() != null) {
                c.sendMessage(message);
            }
        }
    }

    /**
     * Starts a ChatServer based on the specified command line arguments. There should be a single
     * argument which is an integer that is the port that the server operates on.
     *
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        if (args.length != 1) {
            printUsage();
            System.exit(1);
        }

        try {
            new ChatServer(Integer.parseInt(args[0])).run();
        } catch (NumberFormatException e) {
            printUsage();
            System.exit(1);
        }
    }

    /**
     * Prints the usage of this class from the command line.
     */
    private static void printUsage()
    {
        System.err.println("Usage: java ChatServer <port number>");
    }
}