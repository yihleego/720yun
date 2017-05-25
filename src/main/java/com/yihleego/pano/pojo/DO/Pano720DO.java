package com.yihleego.pano.pojo.DO;

import java.io.Serializable;

/**
 * Created by YihLeego on 17-5-24.
 */
public class Pano720DO implements Serializable {
    private static final long serialVersionUID = 6247128615922791429L;

    private Integer id;//自增长主键
    private String panoId;//'720yun的panoid'
    private String panoUrl;//'720yun的pano的链接'
    private String panoXmlUrl;//'720云的pano的xml的链接'
    private Long createDate;//'创建时间'
    private Long crawlDate;//'爬取时间'
    private Integer status;//状态 0：未爬 1：已爬 2：爬失败

    public Pano720DO() {
    }
    public Pano720DO( String panoId, String panoUrl, String panoXmlUrl, Long createDate, Long crawlDate, Integer status) {
        this.panoId = panoId;
        this.panoUrl = panoUrl;
        this.panoXmlUrl = panoXmlUrl;
        this.createDate = createDate;
        this.crawlDate = crawlDate;
        this.status = status;
    }
    public Pano720DO(Integer id, String panoId, String panoUrl, String panoXmlUrl, Long createDate, Long crawlDate, Integer status) {
        this.id = id;
        this.panoId = panoId;
        this.panoUrl = panoUrl;
        this.panoXmlUrl = panoXmlUrl;
        this.createDate = createDate;
        this.crawlDate = crawlDate;
        this.status = status;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPanoId() {
        return panoId;
    }

    public void setPanoId(String panoId) {
        this.panoId = panoId;
    }

    public String getPanoUrl() {
        return panoUrl;
    }

    public void setPanoUrl(String panoUrl) {
        this.panoUrl = panoUrl;
    }

    public String getPanoXmlUrl() {
        return panoXmlUrl;
    }

    public void setPanoXmlUrl(String panoXmlUrl) {
        this.panoXmlUrl = panoXmlUrl;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getCrawlDate() {
        return crawlDate;
    }

    public void setCrawlDate(Long crawlDate) {
        this.crawlDate = crawlDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
