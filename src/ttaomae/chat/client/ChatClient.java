package ttaomae.chat.client;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

/**
 * A simple chat client.
 *
 * @author Todd Taomae
 */
public class ChatClient
{
    private Socket chatSocket;

    /**
     * Constructs a ChatClient which will attempt to connect to a socket with the specified host
     * name and port number.
     *
     * @param hostname name of the host to connect to
     * @param portNumber port number to connect to
     * @throws UnknownHostException if the IP address of the host could not be determined
     * @throws IOException if an I/O error occurs when creating the socket
     */
    public ChatClient(String hostname, int portNumber)
            throws UnknownHostException, IOException
    {
        this.chatSocket = new Socket(hostname, portNumber);
    }

    /**
     * Creates and displays the GUI for this ChatClient.
     *
     * @throws IOException if an error occurs while creating the input or output streams for the
     *             socket or if the socket is not connected
     */
    public void createAndShowGui() throws IOException
    {
        JFrame frame = new JFrame("Chat");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChatClientOutputPanel outputPanel = new ChatClientOutputPanel(this.chatSocket);
        frame.add(outputPanel, BorderLayout.CENTER);
        ChatClientInputPanel inputPanel = new ChatClientInputPanel(this.chatSocket);
        frame.add(inputPanel, BorderLayout.SOUTH);


        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        new Thread(outputPanel).start();
    }

    /**
     * Starts a new ChatClient from the specified command line arguments. There should be two
     * arguments which are the host name and the port number, in that order, of the server to
     * connect to.
     *
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        if (args.length != 2) {
            printUsage();
            System.exit(1);
        }

        try {
            ChatClient client = new ChatClient(args[0], Integer.parseInt(args[1]));
            client.createAndShowGui();
        } catch (NumberFormatException e) {
            printUsage();
            System.exit(1);
        } catch (UnknownHostException e) {
            System.err.println("Could not connect to host " + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O error connecting to " + args[0] + ":" + args[1]);
            System.exit(1);
        }
    }

    /**
     * Prints the usage of this class from the command line.
     */
    private static void printUsage()
    {
        System.err.println("Usage: java ChatClient <hostname> <port number>");
    }
}
