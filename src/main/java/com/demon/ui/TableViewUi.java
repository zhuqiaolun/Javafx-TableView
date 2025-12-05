package com.demon.ui;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 *  author: Demon
 *  date: 2025-12-04 004 14:48
 *  description: 表格UI
 */ 
public class TableViewUi<T extends TableViewUi.TableViewData> {

    private TableView<T> tableView = new TableView<>();

    private CheckBox checkBoxAll = new CheckBox("全选");

    public TableViewUi(boolean isNeedCheckBoxAll) {
        tableView.getStylesheets().add("css/table.css");
        this.tableView.setEditable(false);
        this.tableView.setColumnResizePolicy((param) -> true);
        this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.tableView.setFocusTraversable(false);
        if (isNeedCheckBoxAll) {
            this.checkBoxAll.setText("");
            this.checkBoxAll.setSelected(false);
            this.checkBoxAll.setOnAction(event -> this.getSelectAllEvent());
            // 第一列
            TableColumn<T, CheckBox> checkBoxTableColumn = new TableColumn<>();
            checkBoxTableColumn.setCellValueFactory(cellData -> cellData.getValue().getCb().getCheckBox());
            checkBoxTableColumn.setCellFactory(this.getTableCellFactoryToCheckBox());
            checkBoxTableColumn.setMinWidth(50);
            checkBoxTableColumn.setMaxWidth(50);
            //checkBoxTableColumn.setStyle("-fx-alignment:center");
            checkBoxTableColumn.setGraphic(checkBoxAll);
            checkBoxTableColumn.setSortable(false);
            tableView.getColumns().add(checkBoxTableColumn);
        }
    }

    public TableView<T> getTableView() {
        return tableView;
    }

    /**
     * 添加列：
     * tableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
     * tableColumn.setMinWidth(60);
     * tableColumn.setMaxWidth(80);
     *
     * @param tableColumnName 列名
     */
    public TableColumn<T, String> addTableColumn(String tableColumnName) {
        TableColumn<T, String> tableColumn = new TableColumn<>(tableColumnName);
        tableColumn.setVisible(true);
        this.tableView.getColumns().add(tableColumn);
        return tableColumn;
    }

    public void initData(List<T> tableViewList) {
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(tableViewList));
    }

    public List<T> getTableViewToSelectData() {
        List<T> dataList = new ArrayList<>();
        ObservableList<T> tableViewItems = tableView.getItems();
        if (tableViewItems.size() > 0) {
            for (T t : tableViewItems) {
                if (t.getCb().isSelected()) {
                    dataList.add(t);
                }
            }
        }
        return dataList;
    }


    /**
     * 初始化复选框-监听-所有checkbox复选框都勾选则全选框勾选，
     * 所有checkbox复选框不勾选则全选框不勾选
     *
     * @return 返回
     */
    private Callback<TableColumn<T, CheckBox>, TableCell<T, CheckBox>> getTableCellFactoryToCheckBox() {
        return new Callback<TableColumn<T, CheckBox>, TableCell<T, CheckBox>>() {
            @Override
            public TableCell<T, CheckBox> call(TableColumn<T, CheckBox> param) {
                return new TableCell<T, CheckBox>() {
                    @Override
                    public void updateItem(CheckBox item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(item);
                        if (item != null) {
                            item.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                boolean flagSelectAll = true;
                                ObservableList<T> items2 = tableView.getItems();
                                for (T t : items2) {
                                    if (!t.getCb().isSelected()) {
                                        flagSelectAll = false;
                                        break;
                                    }
                                }
                                checkBoxAll.setSelected(flagSelectAll);
                            });
                        }
                    }
                };
            }
        };
    }

    private void getSelectAllEvent() {
        ObservableList<T> items = tableView.getItems();
        if (checkBoxAll.isSelected()) {
            for (T t : items) {
                t.getCb().setCheckbox(true);
            }
            checkBoxAll.setSelected(true);
        } else {
            for (T t : items) {
                t.getCb().setCheckbox(false);
            }
            checkBoxAll.setSelected(false);
        }
    }

    public static class TableViewData {

        private CheckboxUi cb = new CheckboxUi();

        CheckboxUi getCb() {
            return cb;
        }
    }


    public static class CheckboxUi {

        private CheckBox checkbox = new CheckBox();

        ObservableValue<CheckBox> getCheckBox() {
            return new ObservableValue<CheckBox>() {
                @Override
                public CheckBox getValue() {
                    return checkbox;
                }

                @Override
                public void addListener(ChangeListener<? super CheckBox> listener) {
                }

                @Override
                public void removeListener(ChangeListener<? super CheckBox> listener) {
                }

                @Override
                public void addListener(InvalidationListener listener) {
                }

                @Override
                public void removeListener(InvalidationListener listener) {
                }
            };
        }

        Boolean isSelected() {
            return checkbox.isSelected();
        }

        void setCheckbox(boolean value) {
            checkbox.setSelected(value);
        }
    }

}
