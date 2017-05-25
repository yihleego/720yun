package com.yihleego.pano.service;

import com.yihleego.pano.pojo.DO.Pano720DO;
import com.yihleego.pano.pojo.DTO.AuthorDTO;
import com.yihleego.pano.pojo.DTO.Pano720XmlDTO;
import com.yihleego.pano.pojo.DTO.PanoDTO;
import com.yihleego.pano.pojo.DTO.PanoXmlDTO;
import org.jsoup.nodes.Document;


import java.util.List;

/**
 * Created by YihLeego on 17-5-15.
 */
public interface PanoCrawlerService {
    boolean save720Pano(int startPage, int endPage) throws Exception;

    Pano720XmlDTO parse720PanoXml(Pano720DO pano720) throws Exception;

    public boolean down720Pano(Pano720XmlDTO pano720Xml, Pano720DO pano720) throws Exception;

    Pano720DO get720Pano(int id) throws Exception;

  /*  PanoXmlDTO parsePanoXml(String strPanoId) throws Exception;

    boolean downloadPano(PanoXmlDTO panoXmlDTO, AuthorDTO authorDTO) throws Exception;

    Document getDocument(String strUrl) throws Exception;*/
}
