package com.qgstudio.imageencoder.image;

import com.google.gson.Gson;

public class ImageData {
    private int height;
    private int width;
    private Object data = null;

    public ImageData(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
