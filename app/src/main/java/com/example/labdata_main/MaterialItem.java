package com.example.labdata_main;

public class MaterialItem {
    private String name;
    private float percentage;
    private boolean isSelected;

    public MaterialItem(String name, float percentage) {
        this.name = name;
        this.percentage = percentage;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
