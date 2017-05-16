package yihleego.pano.service;

import yihleego.pano.pojo.DTO.AuthorDTO;

import yihleego.pano.pojo.DTO.PanoDTO;
import yihleego.pano.pojo.DTO.PanoXmlDTO;


import java.util.List;

/**
 * Created by wbt on 17-5-15.
 */
public interface PanoCrawlerService {
    List<AuthorDTO> getAuthorList(int iPage) throws Exception;

    List<PanoDTO> getPanoList(String strAuthorId) throws Exception;

    PanoXmlDTO parsePanoXml(String strPanoId) throws Exception;

    //SpiderPanoSelf parseProductXml(String strProductId,Author720 author720) throws Exception;

    //void testDownload(SpiderPanoSelf spiderPanoSelf)throws Exception;
}
