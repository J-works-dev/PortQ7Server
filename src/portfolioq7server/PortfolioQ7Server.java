/**
 * Portfolio Question 7
 * SangJoon Lee
 * 30024165
 * 08/06/2021
 */
package portfolioq7server;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import java.io.IOException;

public class PortfolioQ7Server extends Application {
    private Stage stage;
    private Scene scene;
    private VBox vBox;
    private Button openBtn;
    private Button sendBtn;
    private TextField textField;
    private File file;
    static Server server = new Server();
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("File Transfer [SERVER]");
        
        // Buttons
        Button startBtn = new Button("Start Server");
        startBtn.setOnAction(e -> startButtonClicked());
        
        openBtn = new Button("Open File");
        openBtn.setOnAction(e -> openButtonClicked());

        sendBtn = new Button("Send File");
        sendBtn.setOnAction(e -> sendButtonClicked());
                
        // TextField
        textField = new TextField();
        
        vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(startBtn, openBtn, textField, sendBtn);
        
        new Thread() {
            public void run() {
                try {
                    server.startServer();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e);
                }
                
            }
        }.start();
        
        scene = new Scene(vBox, 300, 150);
        stage.setScene(scene);
        stage.show();
    }
    
    public void startButtonClicked() {
        try {
            server.closeAll();
            server.startServer();
        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        
    }
    
    public void openButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            textField.setText(file.getName());
        }
    }
    
    public void sendButtonClicked() {
        try {
            if (file != null) {
                server.sendFile(file);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
    }
    
    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
