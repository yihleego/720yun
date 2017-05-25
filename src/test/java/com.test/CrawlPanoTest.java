package com.test;

import com.yihleego.pano.dao.Pano720DAO;
import com.yihleego.pano.pojo.DO.Pano720DO;
import com.yihleego.pano.pojo.DTO.Pano720XmlDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.yihleego.pano.service.PanoCrawlerService;

/**
 * Created by YihLeego on 17-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class CrawlPanoTest {
    private transient static Logger logger = LoggerFactory.getLogger(CrawlPanoTest.class);

    @Autowired
    ApplicationContext ac;

    @Autowired
    PanoCrawlerService panoCrawlerService;

    @Autowired
    Pano720DAO pano720DAO;

    @Test
    public void crawl720yun() {
        try {
            panoCrawlerService.save720Pano(1, 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void save720yun() {
        try {

            Pano720DO pano720 = panoCrawlerService.get720Pano(43111);

            Pano720XmlDTO pano720Xml = panoCrawlerService.parse720PanoXml(pano720);

            panoCrawlerService.down720Pano(pano720Xml, pano720);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
