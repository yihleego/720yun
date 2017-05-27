package com.yihleego.pano.service.impl;


import com.yihleego.crawler.util.CrawlerUtils;
import com.yihleego.pano.dao.Pano720DAO;
import com.yihleego.pano.pojo.DO.Pano720DO;
import com.yihleego.pano.pojo.DTO.*;
import com.yihleego.pano.service.PanoCrawlerService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YihLeego on 17-5-15.
 */


@Service("panoCrawlerService")
public class PanoCrawlerServiceImpl implements PanoCrawlerService {
    private transient static Logger logger = LoggerFactory.getLogger(PanoCrawlerServiceImpl.class);

    @Autowired
    Pano720DAO pano720DAO;

    public boolean save720Pano(int startPage, int endPage) throws Exception {

        boolean flag = true;
        List<String> authorList = new ArrayList();
        List<String> panoIdList = new ArrayList();

        for (int i = startPage; i <= endPage; i++) {
            authorList.addAll(getAuthorIdList(i));
        }

        for (int i = 0, len = authorList.size(); i < len; i++) {
            panoIdList.addAll(getPanoIdList(authorList.get(i)));
        }

        System.out.println(panoIdList.size());

        for (int i = 0, len = panoIdList.size(); i < len; i++) {
            Pano720DO pano720 = new Pano720DO();
            Long getTime = new Date().getTime();
            String panoId = panoIdList.get(i);
            String panoUrl = "http://720yun.com/t/" + panoId;
            String panoXmlUrl = "http://xml.qncdn.720static.com/@/" + panoId + "/" + panoId + ".xml?" + getTime;

            pano720.setPanoId(panoId);
            pano720.setPanoUrl(panoUrl);
            pano720.setPanoXmlUrl(panoXmlUrl);
            pano720.setCreateDate(getTime);
            try {
                pano720DAO.insertSelective(pano720);
            } catch (Exception e) {
                logger.error("insert failed {}", e);
            }

        }

        return flag;
    }


    public Pano720XmlDTO parse720PanoXml(Pano720DO pano720) throws Exception {

        List<String> sceneIdList = new ArrayList();// scene id list like 185856
        List<String> panoIdList = new ArrayList();// same as sceneIdList
        Map<String, String> sceneNameMap = new HashMap();// sceneId -> sceneName like 185856 -> scene_185856
        Map<String, String> panoNameMap = new HashMap();// panoName -> panoId like pano185856 -> 185856
        Map<String, String> sceneTitleMap = new HashMap();
        Map<String, String> thumbUrlMap = new HashMap();// sceneId -> thumbUrl like 185856 -> /imgs/preview.jpg
        Map<String, String> previewUrlMap = new HashMap();
        Map<String, Pano720XmlDesktopImageDTO> desktopImageMap = new HashMap();
        Map<String, Pano720XmlMobileImageDTO> mobileImageMap = new HashMap();


        Pano720XmlDTO pano720Xml = new Pano720XmlDTO(sceneIdList, sceneNameMap, panoIdList, panoNameMap, sceneTitleMap, thumbUrlMap, previewUrlMap, desktopImageMap, mobileImageMap);

        String panoXmlUrl = pano720.getPanoXmlUrl();
        Document document = getDocument(panoXmlUrl);
        Element krpanoElement = document.select("krpano").first();
        Elements sceneElements = krpanoElement.select("scene");
        Elements panoElements = krpanoElement.select("config").select("category").select("pano");

        for (Element sceneElement : sceneElements) {
            String panoId = sceneElement.attr("pano_id");
            sceneIdList.add(panoId);
            panoIdList.add(panoId);
            sceneNameMap.put(panoId, "scene_" + panoId);
            panoNameMap.put("pano" + panoId, panoId);
            previewUrlMap.put(panoId, sceneElement.select("preview").attr("url"));

            Element desktopImageElement = sceneElement.select("image").get(0);
            Element mobileImageElement = sceneElement.select("image").get(1);

            if (desktopImageElement != null) {
                Pano720XmlDesktopImageDTO desktopImage = new Pano720XmlDesktopImageDTO();
                desktopImageMap.put(panoId, desktopImage);

                desktopImage.setLevelWidthList(new ArrayList());
                desktopImage.setLevelHightList(new ArrayList());
                desktopImage.setLevelCubeUrlList(new ArrayList());

                desktopImage.setType(desktopImageElement.attr("type"));
                desktopImage.setMultires(desktopImageElement.attr("multires"));
                desktopImage.setTileSize(desktopImageElement.attr("tilesize"));
                desktopImage.setImageIf(desktopImageElement.attr("if"));
                Elements levelElements = desktopImageElement.select("level");
                for (Element levelElement : levelElements) {
                    desktopImage.getLevelWidthList().add(levelElement.attr("tiledimagewidth"));
                    desktopImage.getLevelHightList().add(levelElement.attr("tiledimageheight"));
                    desktopImage.getLevelCubeUrlList().add(levelElement.select("cube").first().attr("url"));
                }
            }

            if (mobileImageElement != null) {
                Pano720XmlMobileImageDTO mobileImage = new Pano720XmlMobileImageDTO();
                mobileImageMap.put(panoId, mobileImage);
                mobileImage.setImageIf(mobileImageElement.attr("if"));
                mobileImage.setLevelCubeUrl(mobileImageElement.select("cube").first().attr("url"));
            }
        }


        for (Element panoElement : panoElements) {
            String panoName = panoElement.attr("name");
            String panoId = panoNameMap.get(panoName);
            String panoTitle = panoElement.attr("title");
            String thumbUrl = panoElement.attr("thumb");
            sceneTitleMap.put(panoId, panoTitle);
            thumbUrlMap.put(panoId, thumbUrl);
        }

        return pano720Xml;
    }


    public Pano720DO get720Pano(int id) throws Exception {
        return pano720DAO.selectByPrimaryKey(id);
    }


    public List<String> getAuthorIdList(int iPage) throws Exception {
        List<String> authorIdList = new ArrayList();


        String strAuthorListUrl = "https://ssl-api.720yun.com/api/author/1/1/99999999/" + iPage;
        Document document = getDocument(strAuthorListUrl);
        String strJSONAuthor = document.body().text().toString();


        Pattern pattern = Pattern.compile("\"members\":\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(strJSONAuthor);

        String[] authorArray = null;
        if (matcher.find())
            authorArray = matcher.group(1).split("(?<=\\}),(?=\\{)");

        pattern = Pattern.compile("\"uid\":\"(.*?)\"");

        if (authorArray != null) {
            for (String authorInfo : authorArray) {
                matcher = pattern.matcher(authorInfo);
                if (matcher.find())
                    authorIdList.add(matcher.group(1));
            }
        }

        return authorIdList;
    }

    public List<String> getPanoIdList(String strAuthorId) throws Exception {
        List<String> panoIdList = new ArrayList();

        String strAuthorInfoApiUrl = "https://ssl-api.720yun.com/api/member/" + strAuthorId;
        Document document = getDocument(strAuthorInfoApiUrl);
        String strJSONProduct = document.body().text().toString();
        Pattern pattern = Pattern.compile("\"products\":\\[(.*?)\\],\"wx");
        Matcher matcher = pattern.matcher(strJSONProduct);
        String[] productArray = null;
        if (matcher.find())
            productArray = matcher.group(1).split("(?<=\\}),(?=\\{)");

        pattern = Pattern.compile("\"pid\":\"(.*?)\"");
        if (productArray != null) {
            for (String productInfo : productArray) {
                matcher = pattern.matcher(productInfo);
                if (matcher.find()) {
                    panoIdList.add(matcher.group(1));
                }
            }
        }

        return panoIdList;
    }

    public boolean down720Pano(Pano720XmlDTO pano720Xml, Pano720DO pano720) throws Exception {
        logger.info("crawler pano url : {}  panoId : {}", pano720.getPanoUrl(),pano720.getPanoId());
        boolean flag = true;


        // 读取path.properties文件中的相关路径配置
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream("/path.properties");
        try {
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String domain = prop.getProperty("domain").trim();
        String panoImageProject = prop.getProperty("pano_image_project").trim();
        String panoImagePath = prop.getProperty("pano_image_path").trim();
        String webContainerPath = prop.getProperty("web_container_path").trim();
        String webapp = prop.getProperty("webapp").trim();
        String panoId = pano720.getPanoId();


        // 设置各种路径参数配置

        // /home/yihleego/path/apache-tomcat/webapp/image/pano/
        String rootPath = (webContainerPath + webapp + panoImageProject+panoImagePath).replace("/", File.separator) + File.separator;

        // /home/yihleego/path/apache-tomcat/webapp/image/pano/fe628jaOwcv/
        String panoPath = rootPath+ panoId + File.separator;

        // http://yihleego.com/image/pano/fe628jaOwcv/
        String panoImageHeadUrl = domain + (panoImageProject+panoImagePath).replace("/", File.separator) + File.separator + panoId + File.separator;

        // 以panoId创建文件目录
        File panoDirectory = new File(panoPath);
        if (!panoDirectory.exists())
            panoDirectory.mkdirs();


        List<String> sceneIdList = pano720Xml.getSceneIdList();// scene id list like 185856
        List<String> panoIdList = pano720Xml.getPanoIdList();// same as sceneIdList
        Map<String, String> sceneNameMap = pano720Xml.getSceneNameMap();// sceneId -> sceneName like 185856 -> scene_185856
        Map<String, String> sceneTitleMap = pano720Xml.getSceneTitleMap();// sceneId -> sceneTitle like 185856 -> 银河系
        Map<String, String> panoNameMap = pano720Xml.getPanoNameMap();// panoName -> panoId like pano185856 -> 185856
        Map<String, String> thumbUrlMap = pano720Xml.getThumbUrlMap();// sceneId -> thumbUrl like 185856 -> /imgs/thumb.jpg
        Map<String, String> previewUrlMap = pano720Xml.getPreviewUrlMap();// sceneId -> previewUrl like 185856 -> /imgs/preview.jpg
        Map<String, Pano720XmlDesktopImageDTO> desktopImageMap = pano720Xml.getDesktopImageMap();// sceneId -> desktopImage
        Map<String, Pano720XmlMobileImageDTO> mobileImageMap = pano720Xml.getMobileImageMap();// sceneId -> mobileImage
        Map<String, List<String>> desktopImageLevelCubeFootUrlMap = new HashMap();// sceneId -> sceneCubeUrlList
        Map<String, String> downMap = new HashMap();

        String[] S = {"b", "d", "f", "l", "r", "u"};
        String[] L = {"l1", "l2", "l3", "l4", "l5", "l6", "l7", "l8", "l9"};
        String[] V = null;
        String[] H = null;
        String[] withZero = {"01", "02", "03", "04", "05", "06", "07", "08", "09"};
        String[] withoutZero = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for (String sceneId : sceneIdList) {
            logger.info("crawler scene : {}" , sceneId);

            //初始化V和H
            V = null;
            H = null;
            List<String> cubeFootUrlList = new ArrayList();
            List<String> levelWidthList = desktopImageMap.get(sceneId).getLevelWidthList();
            List<String> levelHightList = desktopImageMap.get(sceneId).getLevelHightList();
            List<String> levelCubeUrlList = desktopImageMap.get(sceneId).getLevelCubeUrlList();

            desktopImageLevelCubeFootUrlMap.put(sceneId, cubeFootUrlList);

            for (int i = 0, len = levelCubeUrlList.size(); i < len; i++) {

                String cubeUrl = levelCubeUrlList.get(i);
                Pattern patternHead = Pattern.compile("https:(.*?)imgs/");
                Matcher matcherHead = patternHead.matcher(cubeUrl);
                Pattern patternFoot = Pattern.compile("imgs/(.*?).jpg");
                Matcher matcherFoot = patternFoot.matcher(cubeUrl);

                String headUrl = matcherHead.find() ? "https:" + (matcherHead.group(1)) + "imgs/" : "";
                String footUrl = matcherFoot.find() ? matcherFoot.group(1) + ".jpg" : "";
                cubeFootUrlList.add(footUrl);
                int maxV = 0;
                int maxH = 0;
                String targetV = null;
                String targetH = null;

                if (footUrl.contains("%v")) {
                    V = withoutZero;
                    targetV = "%v";
                } else {
                    V = withZero;
                    targetV = "%0v";
                }
                if (footUrl.contains("%h")) {
                    H = withoutZero;
                    targetH = "%h";
                } else {
                    H = withZero;
                    targetH = "%0h";
                }


                for (int v = 0; v < V.length; v++) {
                    String testUrl = cubeUrl.replace("%s", "b").replace(targetV, V[v]).replace(targetH, H[0]);
                    boolean isImage = isImage(testUrl);
                    if (isImage) {
                        maxV = v;
                    } else {
                        break;
                    }
                }
                for (int h = 0; h < H.length; h++) {
                    String testUrl = cubeUrl.replace("%s", "b").replace(targetV, V[0]).replace(targetH, H[h]);
                    boolean isImage = isImage(testUrl);
                    if (isImage) {
                        maxH = h;
                    } else {
                        break;
                    }
                }

                logger.info("get {} maxV : {}  maxH : {}" ,footUrl, V[maxV],H[maxH]);



                for (int s = 0; s < S.length; s++) {
                    for (int v = 0; v <= maxV; v++) {
                        for (int h = 0; h <= maxH; h++) {
                            Pattern pattern = Pattern.compile("imgs/%s/(.*?)/%");
                            Matcher matcher = pattern.matcher(cubeUrl);
                            String level = matcher.find() ? matcher.group(1) : "l0";

                            StringBuffer panoImageSavePath = new StringBuffer(panoPath);
                            panoImageSavePath.append(sceneId);
                            panoImageSavePath.append(File.separator);
                            panoImageSavePath.append(S[s]);
                            panoImageSavePath.append(File.separator);
                            panoImageSavePath.append(level);
                            panoImageSavePath.append(File.separator);
                            panoImageSavePath.append(V[v]);
                            panoImageSavePath.append(File.separator);


                            StringBuffer panoImageFullPath = new StringBuffer(panoImageSavePath.toString());
                            panoImageFullPath.append(level);
                            panoImageFullPath.append("_");
                            panoImageFullPath.append(S[s]);
                            panoImageFullPath.append("_");
                            panoImageFullPath.append(V[v]);
                            panoImageFullPath.append("_");
                            panoImageFullPath.append(H[h]);
                            panoImageFullPath.append(".jpg");

                            File fileImagePath = new File(panoImageSavePath.toString());
                            if (!fileImagePath.exists())
                                fileImagePath.mkdirs();

                            String strImageUrl = cubeUrl.replace("%s", S[s]).replace(targetV, V[v]).replace(targetH, H[h]);

                            downMap.put(strImageUrl, panoImageFullPath.toString());

                        }
                    }
                }
            }

            // create path
            String scenePath = panoPath + sceneId + File.separator;
            File fileImagePath = new File(scenePath);
            if (!fileImagePath.exists())
                fileImagePath.mkdirs();


            //get preview.jpg
            String previewUrl = previewUrlMap.get(sceneId);
            String previewPath = scenePath + "preview.jpg";
            downMap.put(previewUrl, previewPath);


            //get thumb.jpg
            String thumbUrl = thumbUrlMap.get(sceneId);
            String thumbPath = scenePath + "thumb.jpg";
            downMap.put(thumbUrl, thumbPath);


            //get mobile.jpg
            String mobileUrl = mobileImageMap.get(sceneId).getLevelCubeUrl();
            for (int s = 0; s < S.length; s++) {
                String mobilePath = scenePath + "mobile_" + S[s] + ".jpg";
                downMap.put(mobileUrl.replace("%s", S[s]), mobilePath);
            }
        }


        //down
        for (Map.Entry<String, String> entry : downMap.entrySet()) {
            if (downImage(entry.getKey(), entry.getValue()))
                logger.info(entry.getKey() + " ==> " + entry.getValue());
            else
                logger.error(entry.getKey() + " =/=> " + entry.getValue());
        }




        // build xml
        Map<String, String> typeMap = new HashMap();
        Map<String, String> multiresMap = new HashMap();
        Map<String, String> tileSizeMap = new HashMap();
        Map<String, String> desktopImageIfMap = new HashMap();
        Map<String, Map<String, String>> sceneLevelMap = new HashMap();
        Map<String, String> mobileImageIfMap = new HashMap();
        Map<String, String> titleMap = new HashMap();
        for (String sceneId : sceneIdList) {

            Pano720XmlDesktopImageDTO desktopImage = desktopImageMap.get(sceneId);
            Pano720XmlMobileImageDTO mobileImage = mobileImageMap.get(sceneId);

            Map<String, String> levelMap = new HashMap();
            List<String> levelWidthList = desktopImage.getLevelWidthList();
            List<String> levelHightList = desktopImage.getLevelHightList();
            List<String> levelCubeUrlList = desktopImage.getLevelCubeUrlList();

            titleMap.put(sceneId, sceneTitleMap.get(sceneId));
            typeMap.put(sceneId, desktopImage.getType());
            multiresMap.put(sceneId, desktopImage.getMultires());
            tileSizeMap.put(sceneId, desktopImage.getTileSize());
            desktopImageIfMap.put(sceneId, desktopImage.getImageIf());
            mobileImageIfMap.put(sceneId, mobileImage.getImageIf());
            sceneLevelMap.put(sceneId, levelMap);

            for (int i = 0, len = levelWidthList.size(); i < len; i++) {
                levelMap.put(levelWidthList.get(i), desktopImageLevelCubeFootUrlMap.get(sceneId).get(i));
            }
        }

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("panoImageHeadUrl", panoImageHeadUrl);
        velocityContext.put("sceneIdList", sceneIdList);
        velocityContext.put("titleMap", titleMap);
        velocityContext.put("typeMap", typeMap);
        velocityContext.put("multiresMap", multiresMap);
        velocityContext.put("tileSizeMap", tileSizeMap);
        velocityContext.put("desktopImageIfMap", desktopImageIfMap);
        velocityContext.put("sceneLevelMap", sceneLevelMap);
        velocityContext.put("mobileImageIfMap", mobileImageIfMap);


        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init(p);
        Template template = velocityEngine.getTemplate("pano_templet.vm", "UTF-8");

        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);

        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        File distFile = new File(panoPath + "pano.xml");
        if (!distFile.getParentFile().exists()) distFile.getParentFile().mkdirs();
        bufferedReader = new BufferedReader(new StringReader(stringWriter.getBuffer().toString()));
        bufferedWriter = new BufferedWriter(new FileWriter(distFile));
        char buf[] = new char[1024];
        int len;
        while ((len = bufferedReader.read(buf)) != -1) {
            bufferedWriter.write(buf, 0, len);
        }
        bufferedWriter.flush();
        bufferedReader.close();
        bufferedWriter.close();



        // end


        return flag;
    }

    public boolean downloadPano(PanoXmlDTO panoXmlDTO, AuthorDTO authorDTO) throws Exception {
        logger.info("crawler pano : " + panoXmlDTO.getPanoId());
        boolean flag = true;

        try {
            String strAuthorId = authorDTO.getUid();
            String strPanoId = panoXmlDTO.getPanoId();

            String strRootPath = "/home/wbt/down/mypano/" + strAuthorId + File.separator + strPanoId + File.separator;

            File filePano = new File(strRootPath);
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
                ImageDTO mobileImage = sceneDTO.getMobileImage();
                if (desktopImage == null)
                    continue;

                for (int i = 0; i < desktopImage.getCubeUrl().length; i++) {
                    String cubeUrl = desktopImage.getCubeUrl()[i];
                    Pattern patternHead = Pattern.compile("https:(.*?)imgs/");
                    Matcher matcherHead = patternHead.matcher(cubeUrl);
                    Pattern patternFoot = Pattern.compile("imgs/(.*?).jpg");
                    Matcher matcherFoot = patternFoot.matcher(cubeUrl);

                    String strHeadUrl = "https:" + (matcherHead.find() ? matcherHead.group(1) : "") + "imgs/";
                    String footUrl = matcherFoot.find() ? matcherFoot.group(1) : "";
                    int maxV = 0;
                    int maxH = 0;
                    String targetV = null;
                    String targetH = null;


                    if (footUrl.contains("%v")) {
                        V = withoutZero;
                        targetV = "%v";
                    } else {
                        V = withZero;
                        targetV = "%0v";
                    }
                    if (footUrl.contains("%h")) {
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

                                StringBuffer strBufFileImagePath = new StringBuffer(strRootPath);
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

                StringBuffer strBufFileImagePath = new StringBuffer(strRootPath);
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

                String strMobileUrl = mobileImage.getCubeUrl()[0];

                for (int s = 0; s < S.length; s++) {
                    StringBuffer strBufFileMobileImage = new StringBuffer(strBufFileImagePath.toString());
                    strBufFileMobileImage.append("mobile_");
                    strBufFileMobileImage.append(S[s]);
                    strBufFileMobileImage.append(".jpg");

                    downImage(strMobileUrl.replace("%s", S[s]), strBufFileMobileImage.toString());
                }


            }
            //build xml

            Document document = Jsoup.parse(new File("/home/wbt/work/pano/src/main/webapp/xml/templet.xml"), "utf-8");

            Element body = document.select("body").first();
            Element krpano = body.select("krpano").first();

            StringBuffer strbufScene = new StringBuffer();

            for (SceneDTO sceneDTO : arySceneDTO) {

                String strSceneId = sceneDTO.getSceneId();
                String strPreviewUrl = sceneDTO.getPreviewUrl();


                ImageDTO desktopImage = sceneDTO.getDesktopImage();
                ImageDTO mobileImage = sceneDTO.getMobileImage();
                if (desktopImage == null)
                    continue;

                for (int i = 0; i < desktopImage.getCubeUrl().length; i++) {


                }
            }
            krpano.prepend(strbufScene.toString());


            FileWriter fw = null;

            fw = new FileWriter("/home/wbt/ppp.xml");


            fw.write(body.html());
            fw.flush();
            fw.close();


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

