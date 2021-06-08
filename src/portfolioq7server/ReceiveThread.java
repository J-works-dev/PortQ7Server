package portfolioq7server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ReceiveThread extends Thread {
    private Socket socket;
    private InputStream is;
    private BufferedOutputStream bos;
    
    @Override
    public void run() {
        try {
            System.out.println("Thread started");
//            ServerSocket serverSocket = new ServerSocket(9999);
//            socket = serverSocket.accept();
            byte[] fileBytes = new byte[1024];
            String fileName = "data.csv";
            BufferedReader buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedString = buf.readLine();
            String[] clientMsg = receivedString.split(",");
            if (clientMsg[0].equals("fileName:")) {
                fileName = clientMsg[1];
            }

            setIs(getSocket().getInputStream());
            setBos(new BufferedOutputStream(new FileOutputStream(fileName)));

            while (true) {
                int bytesRead = getIs().read(fileBytes, 0, fileBytes.length);
                getBos().write(fileBytes, 0, bytesRead);
                getBos().flush();

                if (bytesRead < fileBytes.length) {
                    JOptionPane.showMessageDialog(null, "File received from Client");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public BufferedOutputStream getBos() {
        return bos;
    }

    public void setBos(BufferedOutputStream bos) {
        this.bos = bos;
    }
}