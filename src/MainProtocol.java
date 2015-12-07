import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainProtocol {
    public static final byte PROCESS_IMAGE = 1;
    public static final byte QUIT = 2;

    public byte[] processInput(byte message, InputStream in) { //function which sees the message received
        switch (message) {
            case PROCESS_IMAGE: {
                return handleProcessImage(in);
            }
        }
        return null;
    }

    private byte[] handleProcessImage(InputStream in) { //take care of the PROCESS_IMAGE message
        try {
            BufferedImage img = getImageFromStream(in); //calls function
            System.out.println("Processing image.");

            ImageIO.write(img, "jpg", new File("me2.jpg")); // this line can be erased or cut and put at the end


            // TODO: do stuff to image

            System.out.println("Finished processing, sending back data.");
            return getImagePacket(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getImagePacket(BufferedImage img) {
        // Transform the image into a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "jpg", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int size = byteArrayOutputStream.size();
        byte[] result = new byte[size + 4];
        // Copy 4 bytes into result, which represent the size of the image.
        System.arraycopy(intToBytes(size), 0, result, 0, 4);
        // Copy the image data into result.
        System.arraycopy(byteArrayOutputStream.toByteArray(), 0, result, 4, size);
        return result;
    }

    public static BufferedImage getImageFromStream(InputStream in) throws IOException
    {
        byte[] intMessage = new byte[4]; // the length of the image data
        // Reads size of image.
        in.read(intMessage);
        int size = bytesToInt(intMessage);

        // Creates array for the image data.
        byte[] imageData = new byte[size]; // create space for the raw image data
        in.read(imageData); //read the raw image data from the stream

        // Convert bytes to BufferedImage.
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
        return img;


    }

    public static int bytesToInt (byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    public static byte[] intToBytes (int value) {
        return new byte[] {
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value };
    }
}

