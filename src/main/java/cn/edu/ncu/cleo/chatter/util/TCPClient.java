package cn.edu.ncu.cleo.chatter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Carlos Leo
 * @author qinglew@outlook.com
 * @description 客户端网络处理
 */
public class TCPClient {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public TCPClient() {
    }

    /**
     * 初始化客户端套接字
     * @param hostname 服务器域名
     * @param port     服务器端口
     */
    public TCPClient(String hostname, int port) {
        try {
            this.socket = new Socket(hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化客户端套接字
     * @param socket 客户端套接字
     */
    public TCPClient(Socket socket) {
        this.socket = socket;
    }

    /**
     * 初始化输入输出流
     */
    public void initStreams() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送数据
     * @param data 要发送的数据
     */
    public void sendData(String data) {
        writer.println(data);
        writer.flush();
    }

    /**
     * 从服务器接受数据
     * @return 接收的数据
     */
    public String receiveDate() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 一次请求的封装，先向服务器发送数据，再从服务器接收响应
     * @param data 发送的数据
     * @return 响应
     */
    public String request(String data) {
        sendData(data);
        return receiveDate();
    }

    /**
     * 关闭所有资源
     */
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
