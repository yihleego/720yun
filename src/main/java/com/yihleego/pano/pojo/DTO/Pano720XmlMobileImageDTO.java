package com.yihleego.pano.pojo.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YihLeego on 17-5-15.
 */
public class Pano720XmlMobileImageDTO implements Serializable {
    private static final long serialVersionUID = -6479554680355258530L;

    private String imageIf;

    private String levelCubeUrl;

    public Pano720XmlMobileImageDTO() {
    }

    public Pano720XmlMobileImageDTO(String imageIf, String levelCubeUrl) {
        this.imageIf = imageIf;
        this.levelCubeUrl = levelCubeUrl;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getImageIf() {
        return imageIf;
    }

    public void setImageIf(String imageIf) {
        this.imageIf = imageIf;
    }

    public String getLevelCubeUrl() {
        return levelCubeUrl;
    }

    public void setLevelCubeUrl(String levelCubeUrl) {
        this.levelCubeUrl = levelCubeUrl;
    }
}
