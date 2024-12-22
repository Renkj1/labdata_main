package com.example.labdata_main.model;

public class Experimenter {
    private int id;
    private String name;
    private boolean canAssignTasks;
    private boolean canManageDevices;
    private boolean isSelected;

    public Experimenter(int id, String name) {
        this.id = id;
        this.name = name;
        this.canAssignTasks = false;
        this.canManageDevices = false;
        this.isSelected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCanAssignTasks() {
        return canAssignTasks;
    }

    public void setCanAssignTasks(boolean canAssignTasks) {
        this.canAssignTasks = canAssignTasks;
    }

    public boolean isCanManageDevices() {
        return canManageDevices;
    }

    public void setCanManageDevices(boolean canManageDevices) {
        this.canManageDevices = canManageDevices;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
