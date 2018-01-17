package com.ytu.model;

import com.ytu.constant.JSONConstant;

public class Field {
    private JSONConstant type;
    private String index;

    public Field() {
    }

    public Field(JSONConstant type, String index) {
        this.type = type;
        this.index = index;
    }

    public JSONConstant getType() {
        return type;
    }

    public void setType(JSONConstant type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
