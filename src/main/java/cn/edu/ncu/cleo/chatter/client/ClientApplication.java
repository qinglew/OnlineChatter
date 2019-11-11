package cn.edu.ncu.cleo.chatter.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @description 客户端
 */
public class ClientApplication extends Application {
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/client.fxml"));
        primaryStage.setTitle("服务器");
        primaryStage.setScene(new Scene(root, 520, 477));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_logo.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
