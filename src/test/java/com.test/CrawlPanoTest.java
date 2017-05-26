package com.test;

import com.yihleego.pano.dao.Pano720DAO;
import com.yihleego.pano.pojo.DO.Pano720DO;
import com.yihleego.pano.pojo.DTO.Pano720XmlDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.yihleego.pano.service.PanoCrawlerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

            Pano720DO pano720 = panoCrawlerService.get720Pano(43113);

            Pano720XmlDTO pano720Xml = panoCrawlerService.parse720PanoXml(pano720);

            panoCrawlerService.down720Pano(pano720Xml, pano720);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveImage() {
        String path = "/home/wbt/down/mypano/sss/s.jpg";
        String url = "https://ssl-thumb.720static.com/@/pano/2e7jO7ww5v6/62e1f74bc7a79d62a6263ac39daa9740";
        downImage(url, path);
    }

    private boolean downImage(String strUrl, String strFileImage) {
        boolean flag = true;
        InputStream in = getImage(strUrl);
        if (in == null)
            return false;

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(strFileImage));
            byte[] b = new byte[1024];
            int count = 0;
            while ((count = in.read(b)) != -1)/* 将输入流以字节的形式读取并写入buffer中 */ {
                out.write(b, 0, count);
            }

        } catch (Exception e) {
            logger.error("{}", e);
            flag = false;
        } finally {
            try {
                out.flush();
                out.close();
                in.close();
            } catch (IOException e) {
                logger.error("{}", e);
                return false;
            }
        }

        return flag;
    }

    private InputStream getImage(String strUrl) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        InputStream in = null;
        try {

            HttpGet httpget = new HttpGet(strUrl);

            httpget.setHeader("Accept", "image/webp,image/*,*/*;q=0.8");
            httpget.setHeader("Accept-Encoding", "gzip, deflate");
            httpget.setHeader("Accept-Language", "zh-CN");
            httpget.setHeader("Cache-Control", "no-cache");
            httpget.setHeader("Origin", "http://720yun.com");
            httpget.setHeader("Pragma", "no-cache");
            httpget.setHeader("Proxy-Connection", "keep-alive");
            httpget.setHeader("Upgrade-Insecure-Requests", "1");
            httpget.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 8_4_1 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12H321 MicroMessenger/6.3.9 wxdebugger/0.3.0 Language/zh_CN webviewId/0");

            HttpResponse response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();
            in = entity.getContent();

        } catch (Exception e) {
            System.out.println(e);
        }
        return in;
    }
}
