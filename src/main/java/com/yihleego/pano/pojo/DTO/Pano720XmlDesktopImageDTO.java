package com.yihleego.pano.pojo.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YihLeego on 17-5-15.
 */
public class Pano720XmlDesktopImageDTO implements Serializable {
    private static final long serialVersionUID = -6479554680355258530L;
    private String type;
    private String multires;
    private String tileSize;
    private String imageIf;

    private List<String> levelWidthList;
    private List<String> levelHightList;
    private List<String> levelCubeUrlList;

    public Pano720XmlDesktopImageDTO() {
    }

    public Pano720XmlDesktopImageDTO(String type, String multires, String tileSize, String imageIf, List<String> levelWidthList, List<String> levelHightList, List<String> levelCubeUrlList) {
        this.type = type;
        this.multires = multires;
        this.tileSize = tileSize;
        this.imageIf = imageIf;
        this.levelWidthList = levelWidthList;
        this.levelHightList = levelHightList;
        this.levelCubeUrlList = levelCubeUrlList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public List<String> getLevelWidthList() {
        return levelWidthList;
    }

    public void setLevelWidthList(List<String> levelWidthList) {
        this.levelWidthList = levelWidthList;
    }

    public List<String> getLevelHightList() {
        return levelHightList;
    }

    public void setLevelHightList(List<String> levelHightList) {
        this.levelHightList = levelHightList;
    }

    public List<String> getLevelCubeUrlList() {
        return levelCubeUrlList;
    }

    public void setLevelCubeUrlList(List<String> levelCubeUrlList) {
        this.levelCubeUrlList = levelCubeUrlList;
    }
}
