package cn.edu.ncu.cleo.chatter.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @description 服务器
 */
public class ServerApplication extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/server.fxml"));
        primaryStage.setTitle("服务器");
        primaryStage.setScene(new Scene(root, 717, 593));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
