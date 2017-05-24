package com.yihleego.pano.pojo.DTO;

/**
 * Created by YihLeego on 17-5-15.
 */
public class ImageDTO {
    private String type;
    private String multires;
    private String tileSize;
    private String imageIf;

    private String[] width;
    private String[] height;
    private String[] cubeUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMultires() {
        return multires;
    }

    public void setMultires(String multires) {
        this.multires = multires;
    }

    public String getTileSize() {
        return tileSize;
    }

    public void setTileSize(String tileSize) {
        this.tileSize = tileSize;
    }

    public String getImageIf() {
        return imageIf;
    }

    public void setImageIf(String imageIf) {
        this.imageIf = imageIf;
    }

    public String[] getWidth() {
        return width;
    }

    public void setWidth(String[] width) {
        this.width = width;
    }

    public String[] getHeight() {
        return height;
    }

    public void setHeight(String[] height) {
        this.height = height;
    }

    public String[] getCubeUrl() {
        return cubeUrl;
    }

    public void setCubeUrl(String[] cubeUrl) {
        this.cubeUrl = cubeUrl;
    }
}
