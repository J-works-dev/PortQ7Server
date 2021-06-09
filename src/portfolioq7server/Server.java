package portfolioq7server;

//import javafx.application.Application;
//import javafx.stage.Stage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
//import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

public class Server extends Thread {
//    private Stage stage;
//    private Scene scene;
//    private VBox vBox;
//    private Button openBtn;
//    private Button sendBtn;
//    private TextField textField;
    private Socket socket;
    private InputStream is;
    private BufferedOutputStream bos;
    private boolean isOpen = false;
    
    public void startServer() throws IOException {
        System.out.println("Server started");
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            socket = serverSocket.accept();
//            run();
            receiveFile(socket);
//            ReceiveThread rt = new ReceiveThread();
//            rt.setSocket(socket);
//            rt.setIs(is);
//            rt.setBos(bos);
        }
    }
    @Override
    public void run() {
        try {
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

            is = socket.getInputStream();
            bos = new BufferedOutputStream(new FileOutputStream(fileName, true));

            while (true) {
                int bytesRead = is.read(fileBytes, 0, fileBytes.length);
                bos.write(fileBytes, 0, bytesRead);
                bos.flush();

                if (bytesRead < fileBytes.length) {
                    JOptionPane.showMessageDialog(null, "File received from Client");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        } finally {
            try {
                closeAll();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }
        }
        
    }
    public void receiveFile(Socket socket) throws IOException {
        byte[] fileBytes = new byte[1024];
            String fileName = "data.csv";
//            if (!isOpen) {
//               BufferedReader buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String receivedString = buf.readLine();
//                String[] clientMsg = receivedString.split(",");
//                if (clientMsg[0].equals("fileName:")) {
//                    fileName = clientMsg[1];
//                }
//                isOpen = true;
//            }
            

            is = socket.getInputStream();
            bos = new BufferedOutputStream(new FileOutputStream(fileName));

            while (true) {
//                if (!isOpen) {
//                    BufferedReader buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    String receivedString = buf.readLine();
//                    String[] clientMsg = receivedString.split(",");
//                    if (clientMsg[0].equals("fileName:")) {
//                        fileName = clientMsg[1];
//                    }
//                    isOpen = true;
//                }
                
                int bytesRead = is.read(fileBytes, 0, fileBytes.length);
                bos.write(fileBytes, 0, bytesRead);
                bos.flush();

                if (bytesRead < fileBytes.length) {
                    JOptionPane.showMessageDialog(null, "File received from Client");
                    isOpen = false;
                }
            }
    }
    
    public void sendFile(File file) throws IOException {
//        PrintWriter pw = new PrintWriter(socket.getOutputStream()); 
//        pw.println("fileName:," + file.getName());
//        pw.flush();
//        pw.close();
        System.out.println("Send file");
        System.out.println(file);
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
//        socket.close();
    }    
}
