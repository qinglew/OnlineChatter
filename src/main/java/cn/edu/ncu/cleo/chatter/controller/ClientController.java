package cn.edu.ncu.cleo.chatter.controller;

import cn.edu.ncu.cleo.chatter.util.TCPClient;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Date;

/**
 * @description
 */
public class ClientController {
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public TextField serverIpTextField;
    public TextField portTextField;
    public Button startButton;
    public TextField messageTextField;
    public Button sendButton;
    public TextArea documentTF;
    public Button logoutButton;

    private String username;
    private String password;
    private String image;
    private String serverIp;
    private int port;
    private String message;
    private TCPClient tcpClient;

    public void initialize() {

    }

    public void startClient(ActionEvent actionEvent) {
        username = usernameTextField.getText();
        password = passwordTextField.getText();
        serverIp = serverIpTextField.getText();
        port = Integer.parseInt(portTextField.getText());
        tcpClient = new TCPClient("localhost", port);
        tcpClient.initStreams();
        tcpClient.sendData("LOGIN|" + username + "|" + image);
        new Thread(new ClientTask()).start();
    }

    public void sendMessage(ActionEvent actionEvent) {
        message = messageTextField.getText();
        tcpClient.sendData("MESSAGE|" + message);
        messageTextField.setText("");
    }

    public void logout(ActionEvent actionEvent) {
        tcpClient.sendData("SIGNOUT");
    }

    class ClientTask implements Runnable {
        public void run() {
            while (true) {
                String receivedData = tcpClient.receiveDate();
                if (receivedData == null) {
                    break;
                }
                if (receivedData.equals("SIGNOUT")) {
                    documentTF.appendText(new Date().toLocaleString() + " 断开连接\n");
                    break;
                }
                String[] parts = receivedData.split("\\|");
                if ("MESSAGE".equals(parts[0])) {
                    documentTF.appendText("\"" + parts[1] + "\"  " + new Date().toLocaleString() + "\n" +
                            parts[3] + "\n");
                }
            }
            tcpClient.close();
        }
    }
}
