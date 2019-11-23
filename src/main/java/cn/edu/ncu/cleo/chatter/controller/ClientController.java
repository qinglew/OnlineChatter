package cn.edu.ncu.cleo.chatter.controller;

import cn.edu.ncu.cleo.chatter.util.TCPClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Date;
import java.util.Random;

/**
 * @author Carlos Leo
 * @author qinglew@outlook.com
 * @description PC客户端控制器
 */
public class ClientController {

    /**
     * 配置模块
     */
    public TextField serverIpTF;          // 服务器ip地址文本框
    public TextField portTF;              // 服务器端口号文本框
    public Button configBT;               // 配置按钮

    /**
     * 注册模块
     */
    public TextField regPhoneTF;          // 注册手机号文本框
    public TextField regUsernameTF;       // 注册用户名文本框
    public PasswordField regPasswordTF;   // 注册密码文本框
    public Button registerBT;             // 注册按钮

    /**
     * 登陆模块
     */
    public TextField loginPhoneTF;        // 登陆手机号输入框
    public PasswordField loginPasswordTF; // 登陆密码输入框
    public Button loginBT;                // 登陆按钮
    public Button logoutBT;               // 登陆按钮，下线

    /**
     * 消息模块
     */
    public TextField messageTF;           // 消息框
    public Button sendBT;                 // 发送消息按钮
    public TextArea documentTF;           // 消息记录文本域

    /**
     * 登陆提示
     */
    public Circle status;                 // 登陆状态
    public Label usernameTip;             // 用户名提示
    public ImageView iconImage;           // 用户头像

    /**
     * 全局变量
     */
    private String phone;                 // 手机号
    private String username;              // 用户名
    private String password;              // 密码
    private int image;                    // 图片
    private String serverIp;              // 服务器ip地址
    private int port;                     // 服务器端口号
    private TCPClient tcpClient;          // tcp客户端
    private static boolean online;        // 在线状态
    private Random random;

    /**
     * 初始化
     */
    public void initialize() {
        online = false;
        random = new Random();
    }

    /**
     * 配置服务器
     * @param actionEvent 事件绑定
     */
    public void config(ActionEvent actionEvent) {
        serverIp = serverIpTF.getText().trim();
        port = Integer.parseInt(portTF.getText());
        makeTip("Success", "服务器配置成功!");
    }

    /**
     * 用户注册
     * @param actionEvent 事件绑定
     */
    public void register(ActionEvent actionEvent) {
        serverIp = serverIpTF.getText().trim();
        port = Integer.parseInt(portTF.getText());
        phone = regPhoneTF.getText();
        if (phone.trim().equals("")) {
            makeTip("WARNING", "手机号不能为空!");
            return;
        }
        username = regUsernameTF.getText();
        if (username.trim().equals("")) {
            makeTip("WARNING", "用户名不能为空!");
            return;
        }
        password = regPasswordTF.getText();
        if (password.trim().equals("")) {
            makeTip("WARNING", "密码不能为空!");
            return;
        }
        image = random.nextInt(36);
        tcpClient = new TCPClient(serverIp, port);
        tcpClient.initStreams();
        String ret = tcpClient.request("REGISTER|" + phone + "|" + username + "|" + password + "|" + image);
        if ("SUCCESS".equals(ret)) {
            makeTip("SUCCESS", "注册成功!");
            regPhoneTF.setText("");
            regPasswordTF.setText("");
            regUsernameTF.setText("");
            loginPhoneTF.setText(phone);
            loginPasswordTF.setText(password);
        } else {
            System.out.println("received UserException!");
            makeTip("ERROR", ret.split("\\|")[1]);
        }
        tcpClient.close();
    }

    /**
     * 用户登陆
     * @param actionEvent 事件绑定
     */
    public void login(ActionEvent actionEvent) {
        serverIp = serverIpTF.getText().trim();
        port = Integer.parseInt(portTF.getText());
        phone = loginPhoneTF.getText();
        password = loginPasswordTF.getText();
        if (phone.trim().equals("")) {
            makeTip("WARNING", "请输入手机号!");
            return;
        }
        if (password.trim().equals("")) {
            makeTip("WARNING", "请输入密码!");
            return;
        }
        tcpClient = new TCPClient(serverIp, port);
        tcpClient.initStreams();
        String ret = tcpClient.request("LOGIN|" + phone + "|" + password);
        if (ret.contains("SUCCESS")) {
            // 登陆成功
            String[] parts = ret.split("\\|");
            username = parts[1];
            image = Integer.parseInt(parts[2]);
            status.setFill(Color.LIGHTGREEN);
            usernameTip.setText(username);
            online = true;
            iconImage.setVisible(true);
            String imageUrl = "/images/icon" + (image + 1) + ".png";
            iconImage.setImage(new Image(imageUrl));
            documentTF.appendText(new Date().toLocaleString() + " 登陆成功!\n");
            new Thread(new ClientTask()).start();
        } else if (ret.contains("ERROR")) {
            // 登陆出错
            makeTip("ERROR", ret.split("\\|")[1]);
            tcpClient.close();
        }
    }

    /**
     * 登出
     * @param actionEvent 事件绑定
     */
    public void logout(ActionEvent actionEvent) {
        tcpClient.sendData("LOGOUT");
    }

    /**
     * 发送按钮点击事件：发送消息
     * @param actionEvent 事件绑定
     */
    public void sendMessage(ActionEvent actionEvent) {
        if (online) {
            // 消息
            String message = messageTF.getText();
            if (message.trim().equals("")) {
                makeTip("WARNING", "消息不能为空!");
                return;
            }
            tcpClient.sendData("MESSAGE|" + message);
            messageTF.setText("");
        } else {
            makeTip("Warning!", "请先登陆!");
        }
    }

    /**
     * 弹出消息框，提示信息
     * @param message 提示信息
     */
    private void makeTip(String title, String message) {
        Stage stage = new Stage();
        stage.setTitle(title);
        Label label = new Label(resolveMessage(message));
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 200, 150);
        stage.setScene(scene);
        stage.show();
    }

    private String resolveMessage(String message) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            builder.append(message.charAt(i));
            if ((i + 1) % 10 == 0)
                builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * 客户端进程实体
     */
    class ClientTask implements Runnable {
        public void run() {
            while (true) {
                String receivedData = tcpClient.receiveDate();
                if (receivedData == null) {
                    break;
                }
                if (receivedData.equals("LOGOUT")) {
                    Platform.runLater(() -> {
                        documentTF.appendText(new Date().toLocaleString() + " 断开连接\n");
                        usernameTip.setText("未登陆");
                        status.setFill(Color.GRAY);
                        online = false;
                        iconImage.setVisible(false);
                    });
                    break;
                }
                String[] parts = receivedData.split("\\|");
                if ("MESSAGE".equals(parts[0])) {
                    Platform.runLater(() -> {
                        documentTF.appendText("\"" + parts[1] + "\"  " + new Date().toLocaleString() + "\n" +
                                parts[3] + "\n");
                    });
                }
            }
            tcpClient.close();
        }
    }
}
