package com.kries.autotool.network;

public class TaskModel {
    private String taskId;
    private String scriptId;
    private String paramsId;
    private String name;
    private String params;

    public String GetTaskId() {
        return this.taskId;
    }

    public String GetScriptId() {
        return this.scriptId;
    }

    public String GetParamsId() {
        return this.paramsId;
    }

    public String GetName() {
        return this.name;
    }

    public void SetName(String name) {
        this.name = name;
    }

    public String GetParams() {
        return this.params;
    }

    public void SetParams(String params) {
        this.params = params;
    }
}
