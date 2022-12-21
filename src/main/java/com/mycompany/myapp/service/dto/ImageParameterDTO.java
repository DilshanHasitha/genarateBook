package com.mycompany.myapp.service.dto;

public class ImageParameterDTO {

    String imageUrl;
    int x;
    int y;
    int width;
    int height;

    public ImageParameterDTO() {}

    public ImageParameterDTO(String imageUrl, int x, int y, int width, int height) {
        this.imageUrl = imageUrl;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
