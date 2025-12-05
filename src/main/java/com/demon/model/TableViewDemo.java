package com.demon.model;

import com.demon.ui.TableViewUi;

public class TableViewDemo extends TableViewUi.TableViewData {

    private String index;

    private String name;

    private String age;

    public String getIndex() {
        return index;
    }

    public TableViewDemo setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getName() {
        return name;
    }

    public TableViewDemo setName(String name) {
        this.name = name;
        return this;
    }

    public String getAge() {
        return age;
    }

    public TableViewDemo setAge(String age) {
        this.age = age;
        return this;
    }

}
