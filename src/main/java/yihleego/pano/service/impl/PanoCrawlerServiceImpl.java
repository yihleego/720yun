package yihleego.pano.service.impl;


import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import yihleego.crawler.util.CrawlerUtils;
import yihleego.pano.pojo.DTO.*;
import yihleego.pano.service.PanoCrawlerService;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YihLeego on 17-5-15.
 */
@Service("panoCrawlerService")
public class PanoCrawlerServiceImpl implements PanoCrawlerService {
    private transient static Logger logger = LoggerFactory.getLogger(PanoCrawlerServiceImpl.class);


    public List<AuthorDTO> getAuthorList(int iPage) throws Exception {
        List<AuthorDTO> authorDTOList = new ArrayList();

        try {
            String strAuthorListUrl = "https://ssl-api.720yun.com/api/author/1/1/99999999/" + iPage;
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


    public boolean downloadPano(PanoXmlDTO panoXmlDTO, AuthorDTO authorDTO) throws Exception {
        logger.info("crawler pano : " + panoXmlDTO.getPanoId());
        boolean flag = true;

        try {
            String strAuthorId = authorDTO.getUid();
            String strPanoId = panoXmlDTO.getPanoId();

            String sreRootPath = "/home/wbt/down/mypano/" + strAuthorId + File.separator + strPanoId + File.separator;

            File filePano = new File(sreRootPath);
            if (!filePano.exists())
                filePano.mkdirs();
            if (!filePano.exists())
                return false;


            SceneDTO[] arySceneDTO = panoXmlDTO.getScenes();
            for (SceneDTO sceneDTO : arySceneDTO) {

                logger.info("crawler scene : " + sceneDTO.getSceneId());

                String[] S = {"b", "d", "f", "l", "r", "u"};
                String[] L = {"l1", "l2", "l3", "l4", "l5", "l6", "l7", "l8", "l9"};
                String[] V = null;
                String[] H = null;
                String[] withZero = {"01", "02", "03", "04", "05", "06", "07", "08", "09"};
                String[] withoutZero = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};


                String strSceneId = sceneDTO.getSceneId();
                String strPreviewUrl = sceneDTO.getPreviewUrl();
                ImageDTO desktopImage = sceneDTO.getDesktopImage();
                if (desktopImage == null)
                    continue;

                for (int i = 0; i < desktopImage.getCubeUrl().length; i++) {
                    String cubeUrl = desktopImage.getCubeUrl()[i];
                    Pattern patternHead = Pattern.compile("https:(.*?)imgs/");
                    Matcher matcherHead = patternHead.matcher(cubeUrl);
                    Pattern patternFoot = Pattern.compile("imgs/(.*?).jpg");
                    Matcher matcherFoot = patternFoot.matcher(cubeUrl);

                    String strHeadUrl = "https:" + (matcherHead.find() ? matcherHead.group(1) : "") + "imgs/";
                    String strFootUrl = matcherFoot.find() ? matcherFoot.group(1) : "";
                    int maxV = 0;
                    int maxH = 0;
                    String targetV = null;
                    String targetH = null;


                    if (strFootUrl.contains("%v")) {
                        V = withoutZero;
                        targetV = "%v";
                    } else {
                        V = withZero;
                        targetV = "%0v";
                    }
                    if (strFootUrl.contains("%h")) {
                        H = withoutZero;
                        targetH = "%h";
                    } else {
                        H = withZero;
                        targetH = "%0h";
                    }


                    for (int v = 0; v < V.length; v++) {
                        String strTestUrl = cubeUrl.replace("%s", "b").replace(targetV, V[v]).replace(targetH, H[0]);
                        boolean isImage = isImage(strTestUrl);
                        if (isImage) {
                            maxV = v;
                        } else {
                            logger.info("max %v : " + V[maxV]);
                            break;
                        }

                    }
                    for (int h = 0; h < H.length; h++) {
                        String strTestUrl = cubeUrl.replace("%s", "b").replace(targetV, V[0]).replace(targetH, H[h]);
                        boolean isImage = isImage(strTestUrl);
                        if (isImage) {
                            maxH = h;
                        } else {
                            logger.info("max %h : " + H[maxH]);
                            break;
                        }
                    }


                    logger.info("crawler level : " + cubeUrl);
                    for (int s = 0; s < S.length; s++) {
                        for (int v = 0; v <= maxV; v++) {
                            for (int h = 0; h <= maxH; h++) {
                                Pattern pattern = Pattern.compile("imgs/%s/(.*?)/%");
                                Matcher matcher = pattern.matcher(cubeUrl);
                                String level = matcher.find() ? matcher.group(1) : "l0";

                                StringBuffer strBufFileImagePath = new StringBuffer(sreRootPath);
                                strBufFileImagePath.append(strSceneId);
                                strBufFileImagePath.append(File.separator);
                                strBufFileImagePath.append(S[s]);
                                strBufFileImagePath.append(File.separator);
                                strBufFileImagePath.append(level);
                                strBufFileImagePath.append(File.separator);
                                strBufFileImagePath.append(V[v]);
                                strBufFileImagePath.append(File.separator);


                                StringBuffer strBufFileImage = new StringBuffer(strBufFileImagePath.toString());
                                strBufFileImage.append(level);
                                strBufFileImage.append("_");
                                strBufFileImage.append(S[s]);
                                strBufFileImage.append("_");
                                strBufFileImage.append(V[v]);
                                strBufFileImage.append("_");
                                strBufFileImage.append(H[h]);
                                strBufFileImage.append(".jpg");

                                File fileImagePath = new File(strBufFileImagePath.toString());
                                if (!fileImagePath.exists())
                                    fileImagePath.mkdirs();

                                String strImageUrl = cubeUrl.replace("%s", S[s]).replace(targetV, V[v]).replace(targetH, H[h]);

                                if (downImage(strImageUrl, strBufFileImage.toString()))
                                    logger.info(strImageUrl + " ==> " + strBufFileImage.toString());
                                else
                                    logger.error(strImageUrl + " =/=> " + strBufFileImage.toString());
                            }
                        }





                    }
                }
                //get preview.jpg

                StringBuffer strBufFileImagePath = new StringBuffer(sreRootPath);
                strBufFileImagePath.append(strSceneId);
                strBufFileImagePath.append(File.separator);

                StringBuffer strBufFileImage = new StringBuffer(strBufFileImagePath.toString());
                strBufFileImage.append("preview.jpg");


                File fileImagePath = new File(strBufFileImagePath.toString());
                if (!fileImagePath.exists())
                    fileImagePath.mkdirs();

                if (downImage(strPreviewUrl, strBufFileImage.toString()))
                    logger.info(strPreviewUrl + " ==> " + strBufFileImage.toString());
                else
                    logger.error(strPreviewUrl + " =/=> " + strBufFileImage.toString());

                //get mobile.jpg
                ImageDTO mobileImage=sceneDTO.getMobileImage();
                String strMobileUrl= mobileImage.getCubeUrl()[0];

                for (int s = 0; s < S.length; s++) {
                    StringBuffer strBufFileMobileImage = new StringBuffer(strBufFileImagePath.toString());
                    strBufFileMobileImage.append("mobile_");
                    strBufFileMobileImage.append(S[s]);
                    strBufFileMobileImage.append(".jpg");

                    downImage(strMobileUrl.replace("%s",S[s]),strBufFileMobileImage.toString());
                }


                //build xml




            }
        } catch (Exception e) {
            logger.error("{}", e);
            flag = false;
        }
        return flag;
    }


    public Document getDocument(String strUrl) throws Exception {
        Document document = null;
        try {
            CrawlerUtils crawlerUtils = new CrawlerUtils();
            document = crawlerUtils.getDocument(strUrl, 0);
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
        return document;
    }

    /*private static boolean judgeImage(String strUrl) {
        Document document = null;
        try {
            CrawlerUtils crawlerUtils = new CrawlerUtils();
            document = crawlerUtils.getDocument(strUrl, 0);
        } catch (Exception ex) {

        }
        if (document == null)
            return false;
        String strHtml = document.html();

        if (strHtml.indexOf("error") >= 0 || strHtml.indexOf("E502") >= 0) {
            return false;
        }


        return true;
    }*/


    private boolean isImage(String strUrl) {
        boolean flag = true;

        InputStream in = null;
        try {
            in = getImage(strUrl);
            String strResult = inputStream2String(in);
            if (strResult.indexOf("403 Forbidden") >= 0 || strResult.indexOf("error") >= 0 || strResult.indexOf("非法操作") >= 0) {
                flag = false;
            }

        } catch (Exception e) {
            System.out.println(e);
            flag = false;
        }
        return flag;
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

    private String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

}

