package Modeles;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TCP {

    public static Socket createClientSocket(String serverIp, int serverPort) throws IOException {
        Socket clientSocket = new Socket(serverIp, serverPort);
        return clientSocket;
    }

    public static int send(Socket socket, byte[] data) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
        return data.length;
    }

    public static String receive(Socket socket) throws IOException {
        DataInputStream dis = new DataInputStream(new  BufferedInputStream(socket.getInputStream()));

        // Lecture de la requête
        StringBuffer buffer = new StringBuffer();
        boolean EOT = false;
        while(!EOT) // boucle de lecture byte par byte
        {
            byte b1 = dis.readByte();
            // System.out.println("b1 : --" + (char)b1 + "--");
            if (b1 == (byte)'#')
            {
                byte b2 = dis.readByte();
                // System.out.println("b2 : --" + (char)b2 + "--");
                if (b2 == (byte)')') EOT = true;
                else
                {
                    buffer.append((char)b1);
                    buffer.append((char)b2);
                }
            }
            else buffer.append((char)b1);
        }

        String requete = buffer.toString();
        System.out.println("Reçu : --" + requete + "--");
        return requete;
    }
}