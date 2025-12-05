package com.demon;

import com.demon.controller.MainController;
import com.demon.util.Log4j2Util;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class App extends Application {

    public static void main(String[] args) {

        // 定义日志配置2：控制台输出，指定日志级别输出写入文件，过滤自定义包类输出写入文件
        Log4j2Util.newBuilder().withTraceFlag(true)
                .withDebugFlag(true).withInfoFlag(true).withWarnFlag(true).withErrorFlag(true).withFatalFlag(true)
                .build().init();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 加载主界面
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Main.fxml")));
        // 主窗口控制器
        MainController.getInstance().setWindow(primaryStage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/style.css")).toExternalForm());
        primaryStage.setTitle("Javafx-TableView");
        // 设置界面标题图标
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/favicon_64x64.ico")));
        // 设置窗体最小尺寸
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        // 设置窗口不可调整大小
        primaryStage.setResizable(false);
        // 置顶
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        // 防止隐式退出
        Platform.setImplicitExit(false);
        // 处理窗口关闭事件
        primaryStage.setOnCloseRequest(event -> {
            event.consume(); // 阻止默认的窗口关闭操作
            // 创建一个确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("退出程序");
            alert.setHeaderText(null);
            alert.setContentText("您是否需要退出程序?");
            // 显示对话框，并等待用户响应
            Optional<ButtonType> optionalButtonType = alert.showAndWait();
            // 如果用户点击了确认按钮，则退出程序
            if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.OK) {
                // 退出 JavaFX 应用程序
                Platform.exit();
            }
        });
        // 显示
        primaryStage.show();
        // 取消置顶
        primaryStage.setAlwaysOnTop(false);
    }

}
