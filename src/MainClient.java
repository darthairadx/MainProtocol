import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.*;


public class MainClient {
    public static int PORT = 38257;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];

        InputStream in;
        OutputStream out;

        try {
            Socket socket = new Socket(hostName, PORT); // the client connects to the server
            in = socket.getInputStream();
            out = socket.getOutputStream();
            System.out.println("Connected to " + hostName + " on port " + PORT);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName); // in case the client cannot connect to the host
            System.exit(1);
            return;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + //the client connects to server but cannot send data
                    hostName);
            System.exit(1);
            return;
        }

        try { //sometimes, what is read cannot be compiled by java
            // Read image from file
            String fileName="D:\\me.jpg";
            System.out.println("Reading image from " + fileName);
            BufferedImage img = ImageIO.read(new File(fileName));

            // Tell the server we want to process an image.
            System.out.println("Sending image");
            out.write(MainProtocol.PROCESS_IMAGE);
            // Send image data.
            out.write(MainProtocol.getImagePacket(img));
            out.flush();

            BufferedImage processedImage = MainProtocol.getImageFromStream(in); //read the result from server
            System.out.println("Received processed result.");

            out.write(MainProtocol.QUIT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
