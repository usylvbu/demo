package com.entity;

public class Loopholes {
    private String type;
    private String path;
    private int lineNum;

    @Override
    public String toString() {
        return "Loopholes{" +
                "type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", lineNum=" + lineNum +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
