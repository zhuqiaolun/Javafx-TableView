package com.demon.controller;

import com.alibaba.fastjson.JSON;
import com.demon.model.TableViewDemo;
import com.demon.ui.SystemAlert;
import com.demon.ui.TableViewUi;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MainController implements Initializable {

    private static MainController mainController;
    @FXML
    public BorderPane MainPane;
    @FXML
    public HBox btnBox;

    private Stage window;
    private TableViewUi<TableViewDemo> tableViewUi;

    public static MainController getInstance() {
        return mainController;
    }

    public Stage getWindow() {
        return window;
    }

    public void setWindow(Stage window) {
        this.window = window;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController = this;
        // 初始化 表格样式
        TableViewUi<TableViewDemo> tableViewUi = new TableViewUi<>(true);
        // 加载表格列表0
        TableColumn<TableViewDemo, String> nameTableColumn0 = tableViewUi.addTableColumn("序号");
        nameTableColumn0.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIndex()));
        nameTableColumn0.setMinWidth(50);
        nameTableColumn0.setMaxWidth(50);
        // 加载表格列表1
        TableColumn<TableViewDemo, String> nameTableColumn1 = tableViewUi.addTableColumn("姓名");
        nameTableColumn1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nameTableColumn1.setMinWidth(60);
        nameTableColumn1.setMaxWidth(80);
        // 加载表格列表2
        TableColumn<TableViewDemo, String> nameTableColumn2 = tableViewUi.addTableColumn("年龄");
        nameTableColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAge()));
        nameTableColumn2.setMinWidth(60);
        nameTableColumn2.setMaxWidth(80);
        // 加载表格数据
        List<TableViewDemo> tableViewDemoList = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            tableViewDemoList.add(new TableViewDemo().setIndex(String.valueOf(i+1)).setName("张三"+i).setAge(String.valueOf(ThreadLocalRandom.current().nextInt(10, 100))));
        }
        tableViewUi.initData(tableViewDemoList);
        // 将表格添加到组件
        MainPane.setCenter(tableViewUi.getTableView());
        // 添加按钮
        Button selectBtn = new Button("选中的数据");
        Button openDirBtn = new Button("打开目录");
        Button openFileBtn = new Button("打开文件");
        Button exportBtn = new Button("导出数据");
        btnBox.getChildren().addAll(selectBtn, openDirBtn, openFileBtn, exportBtn);
        // 添加按钮事件
        selectBtn.setOnAction(event -> {
            List<TableViewDemo> tableViewToSelectData = tableViewUi.getTableViewToSelectData();
            if (tableViewToSelectData.size() > 0) {
                String jsonString = JSON.toJSONString(tableViewToSelectData);
                Optional<ButtonType> optionalButtonType = SystemAlert.getAlertToConfig(this.getWindow(), "信息", "选中的数据", jsonString);
                if (optionalButtonType.isPresent()) {
                    ButtonType buttonType = optionalButtonType.get();
                    System.out.println(buttonType.getButtonData().name());
                }
            } else {
                Optional<ButtonType> optionalButtonType = SystemAlert.getAlertToInfo(this.window, "提示", "没有选中的数据", null);
                if (optionalButtonType.isPresent()) {
                    ButtonType buttonType = optionalButtonType.get();
                    System.out.println(buttonType.getButtonData().name());
                }
            }
        });
        // 打开目录
        openDirBtn.setOnAction(event -> {
            File file = new File("C:\\Users\\Demon\\Desktop");
            String fileName = "test";
            Map<String, String> filterMap = new LinkedHashMap<>();
            filterMap.put("Excel 工作簿", "*.xlsx");
            filterMap.put("Excel 97-2003 工作簿", "*.xls");
            try {
                File openFileDir = SystemAlert.getOpenDir(this.window, file, fileName, filterMap);
                System.out.println(openFileDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // 打开文件
        openFileBtn.setOnAction(event -> {
            File file = new File("C:\\Users\\Demon\\Desktop\\将近酒.txt");
            Optional<ButtonType> optionalButtonType = SystemAlert.getOpenFile(this.window, file.getName(), file.getAbsolutePath());
            if (optionalButtonType.isPresent()) {
                ButtonType buttonType = optionalButtonType.get();
                System.out.println(buttonType.getButtonData().name());
                if (buttonType.getButtonData() == ButtonType.FINISH.getButtonData()) {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 导出数据
        exportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // 虚拟数据，实际map放自己需要的内容
                Map<String, Object> objectMap = new LinkedHashMap<>();
                objectMap.put("result", new Random().nextInt(2));
                // 导出
                try {
                    Dialog dialog = SystemAlert.getDialog(window, "导出中···", FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Export.fxml"))));
                    dialog.initStyle(StageStyle.UNDECORATED);
                    ExportController exportController = ExportController.getInstance();
                    exportController.loadProgress(dialog.getTitle(), objectMap, dialog);
                    dialog.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

   /* void loadData() {
        List<TableViewDemo> tableViewDemoList = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            tableViewDemoList.add(new TableViewDemo().setIndex(String.valueOf(i+1)).setName("张三"+i).setAge(String.valueOf(ThreadLocalRandom.current().nextInt(10, 100))));
        }
        tableViewUi.initData(tableViewDemoList);
    }*/

}
