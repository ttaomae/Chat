# Chat
This is a simple chat application implemented in Java using sockets. It has a simple GUI implemented in Swing.

## Build
To compile the project, navigate to the root directory of the project and run the following commands:
```
javac src/ttaomae/chat/client/*.java
javac src/ttaomae/chat/server/*.java
```

## Run
### Server
To run the server, navigate to the src directory and run the following command:
```
java ttaomae.chat.server.ChatServer &lt;port number&gt;
```

### Client
To run a client, navigate to the src directory and run the following command:
```
java ttaomae.chat.client.ChatClient &lt;hostname&gt; &lt;port number&gt;
```

### Server and Client Together
If you would like to test this by running the server and clients on a single machine, you can run the following commands from three different terminals:
```
java ttaomae.chat.server.ChatServer 4200
java ttaomae.chat.client.ChatClient localhost 4200
java ttaomae.chat.client.ChatClient localhost 4200
```

or you could run the following program:

```java
import java.io.IOException;

import ttaomae.chat.client.ChatClient;
import ttaomae.chat.server.ChatServer;

public class ChatExample {
    public static void main(String[] args) {
        new Thread(new ChatServer(4200)).start();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                try {
                    new ChatClient("localhost", 4200).createAndShowGui();
                    new ChatClient("localhost", 4200).createAndShowGui();
                } catch (IOException e) {
                    System.err.println("Error connecting to server");
                }
            }
        });
    }
}
```

## How to Use
If you've successfully started a client, a window with two fields should pop up. The top field is where messages are output. The bottom field is where you may type your own messages. Press 'enter' to send a message.