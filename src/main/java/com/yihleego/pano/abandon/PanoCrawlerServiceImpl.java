package com.yihleego.pano.abandon;

import com.alibaba.fastjson.JSON;
import com.yihleego.pano.pojo.DTO.AuthorDTO;
import com.yihleego.pano.pojo.DTO.PanoDTO;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YihLeego on 17-5-25.
 */
public class PanoCrawlerServiceImpl {


   /* public List<AuthorDTO> getAuthorList(int iPage) throws Exception {
        List<AuthorDTO> authorDTOList = new ArrayList();

        try {
            String strAuthorListUrl = "https://ssl-api.720yun.com/api/author/1/0/99999999/" + iPage;
            Document document = getDocument(strAuthorListUrl);
            String strJSONAuthor = document.body().text().toString();


            Pattern pattern = Pattern.compile("\"members\":\\[(.*?)\\]");
            Matcher matcher = pattern.matcher(strJSONAuthor);

            String[] authorArray = null;
            if (matcher.find())
                authorArray = matcher.group(1).split("(?<=\\}),(?=\\{)");

            for (String authorInfo : authorArray) {
                try {
                    AuthorDTO AuthorDTO = JSON.parseObject(authorInfo, AuthorDTO.class);
                    authorDTOList.add(AuthorDTO);
                } catch (Exception e) {
                    logger.error("failed to object" + authorInfo + "{}:", e);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authorDTOList;
    }


    public List<PanoDTO> getPanoList(String strAuthorId) throws Exception {
        List<PanoDTO> panoList = new ArrayList();
        try {
            String strAuthorInfoApiUrl = "https://ssl-api.720yun.com/api/member/" + strAuthorId;
            Document document = getDocument(strAuthorInfoApiUrl);
            String strJSONProduct = document.body().text().toString();
            Pattern pattern = Pattern.compile("\"products\":\\[(.*?)\\],\"wx");
            Matcher matcher = pattern.matcher(strJSONProduct);
            String[] productArray = null;
            if (matcher.find())
                productArray = matcher.group(1).split("(?<=\\}),(?=\\{)");
            for (String product : productArray) {
                try {
                    PanoDTO pano = JSON.parseObject(product, PanoDTO.class);
                    panoList.add(pano);
                } catch (Exception e) {
                    logger.error("failed to object" + product + "{}:", e);
                }

            }

        } catch (Exception ex) {
            logger.error("{}", ex);
        }
        return panoList;
    }*/
}
