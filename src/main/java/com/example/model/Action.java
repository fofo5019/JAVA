package com.example.model;

import java.util.Map;

public class Action {
    private ActionType type;
    private Map<String, Object> params;

    public Action() {}

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}