package cn.edu.ncu.cleo.chatter;

import cn.edu.ncu.cleo.chatter.controller.ServerController;

import java.io.IOException;

/**
 * @author Carlos Leo
 * @author qinglew@outlook.com
 * @date {2019/12/1} {15}:{42}:{15}
 * @description {服务器程序入口}
 */
public class ServerApplication {
    public static void main(String[] args) throws IOException {
        ServerController controller = new ServerController(8888);
        controller.startServer();
    }
}
