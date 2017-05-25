package com.yihleego.pano.service;

import com.yihleego.pano.pojo.DTO.AuthorDTO;
import com.yihleego.pano.pojo.DTO.PanoDTO;
import com.yihleego.pano.pojo.DTO.PanoXmlDTO;
import org.jsoup.nodes.Document;


import java.util.List;

/**
 * Created by YihLeego on 17-5-15.
 */
public interface PanoCrawlerService {
    boolean save720yunPano(int startPage, int endPage) throws Exception;



  /*  PanoXmlDTO parsePanoXml(String strPanoId) throws Exception;

    boolean downloadPano(PanoXmlDTO panoXmlDTO, AuthorDTO authorDTO) throws Exception;

    Document getDocument(String strUrl) throws Exception;*/
}
