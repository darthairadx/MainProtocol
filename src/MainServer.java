import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.net.*;
import java.io.*;

public class MainServer {
    public static int PORT = 38257;

    public static void main(String[] args) throws IOException {

        InputStream in;
        OutputStream out;
        // Setup connection;
        try {
            ServerSocket serverSocket = new ServerSocket(PORT); // Create a socket.
            System.out.println("Server opened on port " + PORT);
            Socket clientSocket = serverSocket.accept(); // Wait for someone to connect to the socket.
            System.out.println("Received connection from " + clientSocket.getInetAddress().toString());
            in = clientSocket.getInputStream(); // Get the input stream on the socket.
            out = clientSocket.getOutputStream(); // Get the output stream.
        } catch (IOException e) {
            // Crash and burn if something fails in the above process.
            System.out.println("Exception caught when trying to listen on port "
                    + PORT + " or listening for a connection");
            System.out.println(e.getMessage());
            System.exit(1);
            return; // Darn you java compiler, we exit anyway, but this line is here so we don't get warnings about
            // uninitialized in and out.
        }

        MainProtocol protocol = new MainProtocol();
        System.out.println("Initialized protocol. Waiting for packets.");

        while (true) {
            int message = in.read();
            System.out.println("Packet received with code: " + message);

            if (message == MainProtocol.QUIT) {
                System.out.println("Stopping server");
                break;
            }

            byte[] result = protocol.processInput((byte)message, in); // if the message is not QUIT, ask the protocol to handle the rest
            if (result != null) {
                out.write(result);
            }
        }
    }
}
