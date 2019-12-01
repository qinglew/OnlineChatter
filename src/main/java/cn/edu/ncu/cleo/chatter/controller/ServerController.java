package cn.edu.ncu.cleo.chatter.controller;

import cn.edu.ncu.cleo.chatter.entity.User;
import cn.edu.ncu.cleo.chatter.service.UserService;
import cn.edu.ncu.cleo.chatter.util.UserException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Carlos Leo
 * @author qinglew@outlook.com
 * @description 服务器控制器
 */
public class ServerController {
    private ServerSocket serverSocket;
    private List<ClientThread> clients;
    private String hostname;
    private String ipAddress;
    private int port;

    private UserService userService;

    /**
     * 初始化
     */
    public ServerController(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
        hostname = "localhost";
        ipAddress = Arrays.toString(serverSocket.getInetAddress().getAddress());
        userService = new UserService();
    }

    public void startServer() {
        new Thread(new ServerThread(serverSocket)).start();
        System.out.println(new Date().toLocaleString() + " " + ipAddress);
        System.out.println(new Date().toLocaleString() + ": 服务器已启动！");
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
                        System.out.println(new Date().toLocaleString() + "\n人数已上线，拒绝新用户接入.");
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
    }

    /**
     * 面向客户端连接的线程实体
     */
    class ClientThread implements Runnable {
        private User user;
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        ClientThread(Socket socket) {
            this.socket = socket;
            getStreams();
        }

        /**
         * 获取输入输出流
         */
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
                            sendMessage("SUCCESS|" + user.getPhone() + "|" + user.getUsername() + "|" + user.getPassword()
                            + "|" + user.getImage() + "|" + user.getDesc());
                            this.user = user;
                            System.out.println(new Date().toLocaleString() + ": 用户\"" + user.getUsername() + "\"已上线");
                        } catch (UserException e) {
                            sendMessage("ERROR|" + e.getMessage());
                            close();
                            clients.remove(this);
                            break;
                        }
                    }
                    // 登出
                    if ("LOGOUT".equals(parts[0])) {
                        sendMessage("LOGOUT");
                        close();
                        System.out.println(new Date().toLocaleString() + ": 用户" + user.getUsername() + "已下线");
                        clients.remove(this);
                    }
                    // 发消息
                    if ("MESSAGE".equals(parts[0])) {
                        System.out.println(user.getUsername() + "  " + new Date().toLocaleString() + "  " + parts[3]);
                        for (ClientThread clientThread: clients) {
                            clientThread.sendMessage("MESSAGE|" + parts[1] + "|" + user.getUsername() + "|" + user.getImage() + "|" + parts[3]);
                        }
                    }
                    // 更新用户信息
                    if ("UPDATE".equals(parts[0])) {
                        final User user = new User();
                        user.setPhone(parts[2]);
                        user.setUsername(parts[3]);
                        user.setPassword(parts[4]);
                        user.setImage(Integer.parseInt(parts[5]));
                        user.setDesc(parts[6]);
                        try {
                            userService.update(user);
                            this.user = user;
                            if ("IMAGE".equals(parts[1]))
                                // 通知所有客户端修改UI
                                for (ClientThread clientThread : clients) {
                                    clientThread.sendMessage("IMAGE_SUCCESS|" + user.getPhone() + "|" + user.getImage());
                                }
                            if ("USERNAME".equals(parts[1]))
                                // 通知所有客户端修改UI
                                for (ClientThread clientThread : clients) {
                                    clientThread.sendMessage("USERNAME_SUCCESS|" + user.getPhone() + "|" + user.getUsername());
                                }
                            if ("DESC".equals(parts[1]))
                                sendMessage("DESC_SUCCESS");
                            if ("PASSWORD".equals(parts[1]))
                                sendMessage("PASSWORD_SUCCESS");
                        } catch (UserException e) {
                            if ("IMAGE".equals(parts[1]))
                                sendMessage("IMAGE_ERROR|" + e.getMessage());
                            if ("USERNAME".equals(parts[1]))
                                sendMessage("USERNAME_ERROR|" + e.getMessage());
                            System.out.println("Rewrite error data to client successfully.");
                            if ("DESC".equals(parts[1]))
                                sendMessage("DESC_ERROR|" + e.getMessage());
                            if ("PASSWORD".equals(parts[1]))
                                sendMessage("PASSWORD_ERROR|" + e.getMessage());
                        }
                    }
                    // 获取用户列表
                    if ("LIST".equals(parts[0])) {
                        StringBuilder builder = new StringBuilder("LIST|");
                        for (int i = 0, size = clients.size(); i < size; i++) {
                            User user = clients.get(i).user;
                            builder.append(user.getPhone()).append(",");
                            builder.append(user.getUsername()).append(",");
                            builder.append(user.getImage()).append(",");
                            builder.append(user.getDesc());
                            if (i != (size - 1))
                                builder.append("!");
                        }
                        sendMessage(builder.toString());
                    }
                }
            } catch (IOException e) {
                clients.remove(this);
            } catch (Exception e) {
                System.out.println(new Date().toLocaleString() + ": 用户" + user.getUsername() + "已下线");
                clients.remove(this);
            }
        }

        /**
         * 发送消息
         * @param message 消息
         */
        void sendMessage(String message) {
            writer.println(message);
            writer.flush();
        }

        /**
         * 关闭资源
         */
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
