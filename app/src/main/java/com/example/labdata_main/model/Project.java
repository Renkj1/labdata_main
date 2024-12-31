package com.example.labdata_main.model;

public class Project {
    private int id;
    private String name;
    private boolean isAccessible;
    private String deadline;
    private long createTime;
    private boolean hasAccess;

    public Project(int id, String name, boolean isAccessible) {
        this.id = id;
        this.name = name;
        this.isAccessible = isAccessible;
    }

    public Project(String name, String deadline) {
        this.name = name;
        this.deadline = deadline;
        this.isAccessible = true;
        this.createTime = System.currentTimeMillis();
    }

    public Project(int id, String name, String deadline, long createTime, boolean isAccessible) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.createTime = createTime;
        this.isAccessible = isAccessible;
    }

    public Project(int id, String name, String deadline, long createTime, boolean isAccessible, boolean hasAccess) {
        this(id, name, deadline, createTime, isAccessible);
        this.hasAccess = hasAccess;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
