package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yihleego.pano.service.PanoCrawlerService;

/**
 * Created by YihLeego on 17-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class testCrawlPano {
    private transient static Logger logger = LoggerFactory.getLogger(testCrawlPano.class);

    @Autowired
    ApplicationContext ac;

    @Autowired
    PanoCrawlerService panoCrawlerService;

    @Test
    public void main() {
        try {
            panoCrawlerService.save720yunPano(1, 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
