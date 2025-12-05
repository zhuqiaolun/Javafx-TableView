package com.demon.ui;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * author: Demon
 * date: 2025-12-04 004 15:31
 * description: 提示框   Alert.AlertType  自行扩展
 */
public class SystemAlert {

    // 提示 INFORMATION
    public static Optional<ButtonType> getAlertToInfo(Window window, String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getButtonTypes().set(0, ButtonType.CLOSE);
        alert.initOwner(window);
        return alert.showAndWait();
    }

    // 提示 CONFIRMATION
    public static Optional<ButtonType> getAlertToConfig(Window window, String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getButtonTypes().set(0, new ButtonType("确定", ButtonType.OK.getButtonData()));
        alert.getButtonTypes().set(1, new ButtonType("取消", ButtonType.NO.getButtonData()));
        alert.initOwner(window);
        return alert.showAndWait();
    }


    public static Optional<ButtonType> getOpenFile(Window window, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("打开文件");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        ButtonType finalButtonType = new ButtonType("打开", ButtonType.FINISH.getButtonData());
        ButtonType closeButtonType = new ButtonType("关闭", ButtonType.CLOSE.getButtonData());
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(0, finalButtonType);
        alert.getButtonTypes().add(1, closeButtonType);
        alert.initOwner(window);
        return alert.showAndWait();
    }

    /**
     * 弹窗选择目录框
     *
     * @param fileName  文件名（不含后缀）
     * @param filterMap key=描述,value=过滤
     * @return 返回
     */
    public static File getOpenDir(Window window, File file, String fileName, Map<String, String> filterMap) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择目录");
        //打开选择目录，设置默认文件名
        fileChooser.setInitialFileName(fileName);
        //打开选择目录，设置默认路径为桌面
        //fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setInitialDirectory(file);
        //打开选择目录，设置过滤目录文件
        if (filterMap != null && filterMap.size() > 0) {
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                String description = entry.getKey(), extensions = entry.getValue();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extensions));
            }
        }
        return fileChooser.showSaveDialog(window);
    }


    public static Dialog getDialog(Window window, String title, Node content) {
        Dialog dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        ButtonType closeButtonType = new ButtonType("关闭", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType);
        //添加组件
        dialog.getDialogPane().setContent(content);
        dialog.initOwner(window);
        //隐藏按钮
        Node closeButton = dialog.getDialogPane().lookupButton(closeButtonType);
        closeButton.setVisible(false);
        return dialog;
    }


    /**
     * 关闭当前弹窗面板
     */
    public static void getCloseDialogPane(Button closeBtn) {
        DialogPane dialogPane = (DialogPane) closeBtn.getScene().getRoot();
        for (ButtonType bt : dialogPane.getButtonTypes()) {
            if (bt.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                closeBtn = (Button) dialogPane.lookupButton(bt);
                closeBtn.fire();
                break;
            }
        }
    }
}
