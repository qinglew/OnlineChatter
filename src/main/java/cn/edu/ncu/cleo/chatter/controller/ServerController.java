package cn.edu.ncu.cleo.chatter.controller;

import cn.edu.ncu.cleo.chatter.entity.User;
import cn.edu.ncu.cleo.chatter.service.UserService;
import cn.edu.ncu.cleo.chatter.util.ImageSelector;
import cn.edu.ncu.cleo.chatter.util.UserException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description 服务器控制器
 */
public class ServerController {
    public TextField hostnameTextField;
    public TextField ipTextField;
    public TextField portTextField;
    public Button startButton;
    public TextArea messageTextArea;
    public TextArea systemTextArea;
    public ListView<String> userListView;

    private ObservableList<String> observableList;
    private ServerSocket serverSocket;
    private List<ClientThread> clients;

    ImageSelector imageSelector;

    private String hostname;
    private String ipAddress;
    private int port;

    private UserService userService;

    public void startServer(ActionEvent actionEvent) {
        hostname = hostnameTextField.getText().trim();
        ipAddress = ipTextField.getText().trim();
        port = Integer.parseInt(portTextField.getText());

        try {
            serverSocket = new ServerSocket(port);
            new Thread(new ServerThread(serverSocket)).start();
            systemTextArea.appendText(new Date().toLocaleString() + ": 服务器已启动！\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        observableList = FXCollections.observableArrayList();
        userListView.setItems(observableList);
        clients = new ArrayList<ClientThread>();
        imageSelector = new ImageSelector(36);
        userService = new UserService();
    }

    /**
     * 服务器线程任务实体
     */
    class ServerThread implements Runnable {
        private ServerSocket serverSocket;
        private int max = 30;

        ServerThread(ServerSocket serverSocket, int max) {
            this.serverSocket = serverSocket;
            this.max = max;
        }

        ServerThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        public void run() {
            try {
                while (true) {
                    Socket socket = serverSocket.accept();
                    if (clients.size() >= max) {
                        //拒绝连接
                        PrintWriter output = new PrintWriter(socket.getOutputStream());
                        output.write("ERROR|人数已达上限，无法登陆!");
                        output.flush();
                        output.close();
                        socket.close();
                        systemTextArea.appendText(new Date().toLocaleString() + "\n人数已上线，拒绝新用户接入.");
                        continue;
                    }
                    ClientThread clientThread = new ClientThread(socket);
                    clients.add(clientThread);
                    new Thread(clientThread).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public ServerSocket getServerSocket() {
            return serverSocket;
        }
    }

    class ClientThread implements Runnable {
        private User user;
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        ClientThread(Socket socket) {
            this.socket = socket;
            getStreams();
        }

        private void getStreams() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (true) {
                    String info = reader.readLine();
                    String[] parts = info.split("\\|");
                    // 注册
                    if ("REGISTER".equals(parts[0])) {
                        User user = new User();
                        user.setPhone(parts[1]);
                        user.setUsername(parts[2]);
                        user.setPassword(parts[3]);
                        user.setImage(Integer.parseInt(parts[4]));
                        try {
                            userService.register(user);
                            sendMessage("SUCCESS");
                        } catch (UserException e) {
                            sendMessage("ERROR|" + e.getMessage());
                        }
                        close();
                        clients.remove(this);
                        break;
                    }
                    // 登陆
                    if ("LOGIN".equals(parts[0])) {
                        final User user = new User();
                        user.setPhone(parts[1]);
                        user.setPassword(parts[2]);
                        try {
                            userService.login(user);
                            sendMessage("SUCCESS|" + user.getUsername() + "|" + user.getImage());
                            this.user = user;
                            System.out.println(user.getUsername() + ", " + user.getImage());
                            systemTextArea.appendText(new Date().toLocaleString() + ": 用户\"" + user.getUsername() + "\"已上线\n");
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    observableList.add(user.getUsername());
                                }
                            });
                        } catch (UserException e) {
                            sendMessage("ERROR|" + e.getMessage());
                            close();
                            clients.remove(this);
                            break;
                        }
                    }
                    // 登出
                    if ("SIGNOUT".equals(parts[0])) {
                        sendMessage("SOUGOUT");
                        close();
                        Platform.runLater(new Runnable() {
                            public void run() {
                                observableList.remove(user.getUsername());
                            }
                        });
                        imageSelector.retreat(user.getImage());
                        clients.remove(this);
                    }
                    // 发消息
                    if ("MESSAGE".equals(parts[0])) {
                        messageTextArea.appendText(user.getUsername() + "  " + new Date().toLocaleString() + "\n"
                                + parts[1] + "\n");
                        for (ClientThread clientThread: clients) {
                            clientThread.sendMessage("MESSAGE|" + user.getUsername() + "|" + user.getImage() + "|" + parts[1]);
                            System.out.println(user.getUsername() + ", " + user.getImage());
                        }
                    }
                }
            } catch (IOException e) {
                clients.remove(this);
            } catch (Exception e) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        observableList.remove(user.getUsername());
                        systemTextArea.appendText(new Date().toLocaleString() + ": 用户" + user.getUsername() + "已下线\n");
                    }
                });
                clients.remove(this);
            }
        }

        void sendMessage(String message) {
            writer.println(message);
            writer.flush();
        }

        private void close() {
            try {
                if (reader != null)
                    reader.close();
                if (writer != null)
                    writer.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
