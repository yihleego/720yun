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
    }

     public PanoXmlDTO parsePanoXml(String strPanoId) throws Exception {
        PanoXmlDTO panoXmlDTO = new PanoXmlDTO();

        SceneDTO[] arySceneDTO = null;
        String strPanoUrl = "http://xml.qncdn.720static.com/@/" + strPanoId + "/" + strPanoId + ".xml?" + new Date().getTime();
        Document document = getDocument(strPanoUrl);
        Element krpano = document.select("krpano").first();
        Elements sceneElements = krpano.select("scene");
        arySceneDTO = new SceneDTO[sceneElements.size()];
        int sceneIndex = 0;

        for (Element sceneElement : sceneElements) {
            SceneDTO sceneDTO = new SceneDTO();
            String sceneId = null;
            String previewUrl = null;
            ImageDTO desktopImage = null;
            ImageDTO mobileImage = null;

            sceneId = sceneElement.attr("pano_id");
            previewUrl = sceneElement.select("preview").first().attr("url");
            Elements imageElements = sceneElement.select("image");
            if (imageElements.size() > 0) {
                Element desktopImageElement = sceneElement.select("image").get(0);
                Elements levelElements = desktopImageElement.select("level");
                desktopImage = new ImageDTO();
                String[] width = new String[levelElements.size()];
                String[] height = new String[levelElements.size()];
                String[] cubeUrl = new String[levelElements.size()];
                for (int i = 0; i < levelElements.size(); i++) {
                    width[i] = levelElements.get(i).attr("tiledimagewidth");
                    height[i] = levelElements.get(i).attr("tiledimageheight");
                    cubeUrl[i] = levelElements.get(i).select("cube").first().attr("url");
                }

                desktopImage.setType(desktopImageElement.attr("type"));
                desktopImage.setMultires(desktopImageElement.attr("multires"));
                desktopImage.setTileSize(desktopImageElement.attr("tilesize"));
                desktopImage.setImageIf(desktopImageElement.attr("if"));
                desktopImage.setWidth(width);
                desktopImage.setHeight(height);
                desktopImage.setCubeUrl(cubeUrl);

            }
            if (imageElements.size() > 1) {
                Element mobileImageElement = sceneElement.select("image").get(1);
                mobileImage = new ImageDTO();
                String[] cubeUrl = new String[1];
                cubeUrl[0] = mobileImageElement.select("cube").first().attr("url");
                mobileImage.setImageIf(mobileImageElement.attr("if"));
                mobileImage.setCubeUrl(cubeUrl);

            }
            sceneDTO.setSceneId(sceneId);
            sceneDTO.setDesktopImage(desktopImage);
            sceneDTO.setMobileImage(mobileImage);
            sceneDTO.setPreviewUrl(previewUrl);
            arySceneDTO[sceneIndex++] = sceneDTO;

        }


        panoXmlDTO.setPanoId(strPanoId);
        panoXmlDTO.setScenes(arySceneDTO);


        return panoXmlDTO;
    }
    */
}
