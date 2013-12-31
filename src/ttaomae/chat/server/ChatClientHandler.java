package ttaomae.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles the interaction between a ChatServer and a single client.
 *
 * @author Todd Taomae
 */
public class ChatClientHandler implements Runnable
{
    private ChatServer server;
    private Socket clientSocket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;
    private String username;

    /**
     * Constructs a new ChatClientHandler which operates between the specified ChatServer and the
     * client connected to the specified socket.
     *
     * @param server the server that this ChatClientHandler is part of
     * @param clientSocket the socket that the client is connected to
     * @throws IOException if an error occurs while creating the input or output streams for the
     *             socket or if the socket is not connected
     */
    public ChatClientHandler(ChatServer server, Socket clientSocket) throws IOException
    {
        this.server = server;
        this.clientSocket = clientSocket;
        this.socketOut =
            new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.socketIn = new BufferedReader(
            new InputStreamReader(this.clientSocket.getInputStream()));

        this.username = null;
    }

    /**
     * Handles interaction between the server and client. First gets a username from the user. Then
     * receives messages and asks the server to broadcast those messages.
     */
    @Override
    public void run()
    {
        try {
            // get a username
            while (this.username == null) {
                this.sendMessage("What is your name?");
                String input = socketIn.readLine();

                if (this.server.usernameExists(input)) {
                    this.sendMessage("The name '" + input + "' already exists.");
                } else if (input.isEmpty()) {
                    this.sendMessage("Username must be non-empty");
                } else {
                    this.username = input;
                }
            }

            System.out.println(this.username + " has been added to server");
            this.sendMessage("Your username is " + this.username);

            while (this.clientSocket.isConnected()) {
                // read message from client then ask server to broadcast
                this.server.broadcastMessage(this.username + ": " + this.socketIn.readLine());
            }
        } catch (IOException e) {
            System.err.println("Client " + this.username + " has disconnected");
        } finally {
            // remove self from servers list of clients
            this.server.removeClient(this);
        }
    }

    /**
     * Sends the specified message to the client.
     * 
     * @param message message to send
     */
    public void sendMessage(String message)
    {
        this.socketOut.println(message);
    }

    /**
     * Returns the username of the client.
     *
     * @return the username of the client
     */
    public String getUsername()
    {
        return this.username;
    }
}