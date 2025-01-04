package com.example.labdata_main.model;

public class MoldingMethod {
    public static final int TYPE_CUBE = 1;
    public static final int TYPE_CYLINDER = 2;
    public static final int TYPE_PRISM = 3;

    private int type;
    private float size;        // 立方体边长
    private float diameter;    // 圆柱体直径
    private float length;      // 棱柱体长度
    private float width;       // 棱柱体宽度
    private float height;      // 圆柱体/棱柱体高度
    private int count;        // 试块数量

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getDiameter() {
        return diameter;
    }

    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case TYPE_CUBE:
                sb.append(String.format("立方体试块 %.0f×%.0f×%.0f mm", size, size, size));
                break;
            case TYPE_CYLINDER:
                sb.append(String.format("圆柱体试块 Φ%.0f×%.0f mm", diameter, height));
                break;
            case TYPE_PRISM:
                sb.append(String.format("棱柱体试块 %.0f×%.0f×%.0f mm", length, width, height));
                break;
        }
        sb.append(String.format(" %d个", count));
        return sb.toString();
    }
}
