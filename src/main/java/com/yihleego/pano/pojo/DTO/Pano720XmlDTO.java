package com.yihleego.pano.pojo.DTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by YihLeego on 17-5-25.
 */
public class Pano720XmlDTO implements Serializable {
    private static final long serialVersionUID = -6454866234208305622L;
    private List<String> sceneIdList;
    private Map<String, String> sceneNameMap;
    private List<String> panoIdList;
    private Map<String, String> panoNameMap;
    private Map<String, String> sceneTitleMap;

    private Map<String, String> thumbUrlMap;
    private Map<String, String> previewUrlMap;

    private Map<String,Pano720XmlDesktopImageDTO> desktopImageMap;

    private Map<String,Pano720XmlMobileImageDTO> mobileImageMap;


    public Pano720XmlDTO() {
    }


    public Pano720XmlDTO(List<String> sceneIdList, Map<String, String> sceneNameMap, List<String> panoIdList, Map<String, String> panoNameMap, Map<String, String> sceneTitleMap, Map<String, String> thumbUrlMap, Map<String, String> previewUrlMap, Map<String, Pano720XmlDesktopImageDTO> desktopImageMap, Map<String, Pano720XmlMobileImageDTO> mobileImageMap) {
        this.sceneIdList = sceneIdList;
        this.sceneNameMap = sceneNameMap;
        this.panoIdList = panoIdList;
        this.panoNameMap = panoNameMap;
        this.sceneTitleMap = sceneTitleMap;
        this.thumbUrlMap = thumbUrlMap;
        this.previewUrlMap = previewUrlMap;
        this.desktopImageMap = desktopImageMap;
        this.mobileImageMap = mobileImageMap;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<String> getSceneIdList() {
        return sceneIdList;
    }

    public void setSceneIdList(List<String> sceneIdList) {
        this.sceneIdList = sceneIdList;
    }

    public Map<String, String> getSceneNameMap() {
        return sceneNameMap;
    }

    public void setSceneNameMap(Map<String, String> sceneNameMap) {
        this.sceneNameMap = sceneNameMap;
    }

    public List<String> getPanoIdList() {
        return panoIdList;
    }

    public void setPanoIdList(List<String> panoIdList) {
        this.panoIdList = panoIdList;
    }

    public Map<String, String> getPanoNameMap() {
        return panoNameMap;
    }

    public void setPanoNameMap(Map<String, String> panoNameMap) {
        this.panoNameMap = panoNameMap;
    }

    public Map<String, String> getSceneTitleMap() {
        return sceneTitleMap;
    }

    public void setSceneTitleMap(Map<String, String> sceneTitleMap) {
        this.sceneTitleMap = sceneTitleMap;
    }

    public Map<String, String> getThumbUrlMap() {
        return thumbUrlMap;
    }

    public void setThumbUrlMap(Map<String, String> thumbUrlMap) {
        this.thumbUrlMap = thumbUrlMap;
    }

    public Map<String, String> getPreviewUrlMap() {
        return previewUrlMap;
    }

    public void setPreviewUrlMap(Map<String, String> previewUrlMap) {
        this.previewUrlMap = previewUrlMap;
    }

    public Map<String, Pano720XmlDesktopImageDTO> getDesktopImageMap() {
        return desktopImageMap;
    }

    public void setDesktopImageMap(Map<String, Pano720XmlDesktopImageDTO> desktopImageMap) {
        this.desktopImageMap = desktopImageMap;
    }

    public Map<String, Pano720XmlMobileImageDTO> getMobileImageMap() {
        return mobileImageMap;
    }

    public void setMobileImageMap(Map<String, Pano720XmlMobileImageDTO> mobileImageMap) {
        this.mobileImageMap = mobileImageMap;
    }
}
