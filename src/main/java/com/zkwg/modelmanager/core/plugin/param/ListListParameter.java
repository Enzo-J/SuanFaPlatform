package com.zkwg.modelmanager.core.plugin.param;

import java.util.List;

public class ListListParameter implements IParameter<List<List<String>>> {

    private List<List<String>> lists;

    @Override
    public List<List<String>> getData() {
        return this.lists;
    }

    public List<List<String>> getLists() {
        return lists;
    }

    public void setLists(List<List<String>> lists) {
        this.lists = lists;
    }
}
