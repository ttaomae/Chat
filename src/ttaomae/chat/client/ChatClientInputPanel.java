package ttaomae.chat.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * A JPanel that will take input from a JTextField and send it to a socket.
 *
 * @author Todd Taomae
 */
public class ChatClientInputPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private JScrollPane scrollPane;
    private JTextField textField;
    private PrintWriter socketOut;

    public ChatClientInputPanel(Socket chatSocket) throws IOException
    {
        this.textField = new JTextField(30);
        this.textField.setEditable(true);
        this.textField.addActionListener(this);
        this.scrollPane = new JScrollPane(this.textField);
        this.add(scrollPane, BorderLayout.CENTER);

        this.socketOut = new PrintWriter(chatSocket.getOutputStream(), true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        this.socketOut.println(this.textField.getText());
        this.textField.setText("");

    }
}