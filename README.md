
// 选中的数据
```html
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
```
<img width="872" height="517" alt="image" src="https://github.com/user-attachments/assets/62a4af89-de43-4b27-8d3e-4e955653b3af" />

// 打开目录
<code>
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
</code>
<img width="872" height="517" alt="image" src="https://github.com/user-attachments/assets/ae764699-c7bc-4af2-a77c-5ca97abe20f7" />

// 打开文件
<code>
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
</code>

// 导出数据
<code>
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
</code>
<img width="872" height="517" alt="image" src="https://github.com/user-attachments/assets/d79c01a4-dfcd-4612-8097-cdce4af970e5" />

