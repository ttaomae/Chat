package ttaomae.chat.client;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A JPanel which will display the contents read from a socket.
 *
 * @author Todd Taomae
 */
public class ChatClientOutputPanel extends JPanel implements Runnable
{
    private static final long serialVersionUID = 1L;

    private JScrollPane scrollPane;
    private JTextArea textArea;
    private BufferedReader socketIn;

    public ChatClientOutputPanel(Socket chatSocket) throws IOException
    {
        this.textArea = new JTextArea(10, 30);
        this.textArea.setEditable(false);
        this.scrollPane = new JScrollPane(this.textArea);
        this.add(scrollPane, BorderLayout.CENTER);

        this.socketIn = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
    }

    @Override
    public void run()
    {
        try {
            String line;

            while ((line = socketIn.readLine()) != null) {
                this.textArea.append(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading from socket");
            e.printStackTrace();
        }
    }
}