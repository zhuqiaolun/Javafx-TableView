package com.demon.controller;

import cn.hutool.core.util.StrUtil;
import com.demon.ui.SystemAlert;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Window;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ExportController implements Initializable {

    private static ExportController exportController;

    @FXML
    public ProgressIndicator downloadProgressIndicator;
    @FXML
    public Label exportStatus;
    @FXML
    public Button downloadYes;

    private Map<String, Object> dataMap;
    /**
     * 异步 Service
     */
    private Service<Integer> serviceExecute = new Service<Integer>() {
        @Override
        protected Task<Integer> createTask() {
            return new Task<Integer>() {
                @Override
                protected Integer call() {
                    //业务逻辑，每执行一次 updateProgress 则可以 调用监听事件
                    try {
                        // 导出操作
                        int result = (int)dataMap.get("result");
                        System.out.println(result);
                        TimeUnit.SECONDS.sleep(5); // 休眠10s 模拟导出过程，真实场景不需要
                        // 以上为自己业务的逻辑
                        if (result == 0) {
                            System.out.println("导出完成！！！");
                            downloadYes.setUserData(true);
                            this.updateProgress(1, 1);
                        } else {
                            System.out.println("导出失败！！！");
                            exportStatus.setUserData("自定义是失败原因");
                            downloadYes.setUserData(false);
                            this.updateProgress(0, 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        downloadYes.setUserData(false);
                        exportStatus.setUserData(String.valueOf(e.toString()));
                        System.out.println("导出异常");
                        updateProgress(0, 0);
                    } finally {
                        downloadYes.setDisable(false);
                    }
                    return null;
                }
            };
        }
    };

    static ExportController getInstance() {
        return exportController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exportController = this;
    }

    void loadProgress(String title, Map<String, Object> dataMap, Dialog dialog) {
        this.dataMap = dataMap;
        exportStatus.setText(title);
        downloadYes.setOnMouseClicked(event -> SystemAlert.getCloseDialogPane(downloadYes));
        // 执行  调用 updateProgress 的 监听事件
        serviceExecute.progressProperty().addListener((observable, oldValue, newValue) -> {
            Window window = downloadYes.getScene().getWindow();
            System.out.println(newValue);
            if ("NaN".equals(String.valueOf(newValue))) {
                //导出异常-关闭loading
                dialog.close();
                exportStatus.setText("导出异常");
                SystemAlert.getAlertToInfo(window, "提示", "导出异常", String.valueOf(exportStatus.getUserData()));
                SystemAlert.getCloseDialogPane(downloadYes);
            } else {
                if (!StrUtil.isNullOrUndefined(String.valueOf(downloadYes.getUserData())) && Boolean.parseBoolean(String.valueOf(downloadYes.getUserData()))) {
                    //导出完成-关闭loading
                    dialog.close();
                    exportStatus.setText("导出成功");
                    // 导出完成后的操作··
                    MainController.getInstance().loadData();
                    // 以上为自己业务的逻辑
                    SystemAlert.getCloseDialogPane(downloadYes);
                } else {
                    exportStatus.setText("导出中···");
                }
            }
        });
        serviceExecute.start();
    }

}
