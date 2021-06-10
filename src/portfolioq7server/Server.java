package portfolioq7server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Server {
    private Socket socket;
    private InputStream is;
    private BufferedOutputStream bos;
    
    public void startServer() throws IOException {
        System.out.println("Server started");
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            socket = serverSocket.accept();
            receiveFile(socket);
            closeAll();
        }
    }
    
    public void receiveFile(Socket socket) throws IOException {
        byte[] fileBytes = new byte[1024];
        String fileName = "data.csv";            

        is = socket.getInputStream();
        bos = new BufferedOutputStream(new FileOutputStream(fileName));

        while (true) {                
            int bytesRead = is.read(fileBytes, 0, fileBytes.length);
            bos.write(fileBytes, 0, bytesRead);
            bos.flush();

            if (bytesRead < fileBytes.length) {
                JOptionPane.showMessageDialog(null, "File received from Client");
            }
        }
    }
    
    public void sendFile(File file) throws IOException {
        byte[] fileBytes = new byte[(int) file.length()];
        OutputStream os = socket.getOutputStream();
        
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(fileBytes, 0, fileBytes.length);

        os.write(fileBytes, 0, fileBytes.length);
        os.flush();
        os.close();
        bis.close();
    }
    
    public void closeAll() throws IOException {
        bos.close();
        is.close();
        socket.close();
    }    
}
