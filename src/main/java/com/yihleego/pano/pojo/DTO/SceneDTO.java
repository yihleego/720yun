package com.yihleego.pano.pojo.DTO;

/**
 * Created by YihLeego on 17-5-15.
 */
public class SceneDTO {
    private String sceneId;
    private String previewUrl;
    private ImageDTO desktopImage;
    private ImageDTO mobileImage;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public ImageDTO getDesktopImage() {
        return desktopImage;
    }

    public void setDesktopImage(ImageDTO desktopImage) {
        this.desktopImage = desktopImage;
    }

    public ImageDTO getMobileImage() {
        return mobileImage;
    }

    public void setMobileImage(ImageDTO mobileImage) {
        this.mobileImage = mobileImage;
    }
}
