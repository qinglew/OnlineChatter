package cn.edu.ncu.cleo.chatter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @description
 */
public class TCPClient {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public TCPClient() {

    }

    public TCPClient(String hostname, int port) {
        try {
            this.socket = new Socket(hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TCPClient(Socket socket) {
        this.socket = socket;
    }

    public void initStreams() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data) {
        writer.println(data);
        writer.flush();
    }

    public String receiveDate() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String request(String data) {
        sendData(data);
        return receiveDate();
    }

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
