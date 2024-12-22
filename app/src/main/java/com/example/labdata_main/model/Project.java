package com.example.labdata_main.model;

public class Project {
    private int id;
    private String name;
    private boolean isAccessible;

    public Project(int id, String name, boolean isAccessible) {
        this.id = id;
        this.name = name;
        this.isAccessible = isAccessible;
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

    public boolean isAccessible() {
        return isAccessible;
    }

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }
}
