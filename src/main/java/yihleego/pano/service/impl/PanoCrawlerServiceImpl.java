package yihleego.pano.service.impl;


import com.alibaba.fastjson.JSON;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import yihleego.crawler.util.CrawlerUtils;
import yihleego.pano.pojo.DTO.AuthorDTO;
import yihleego.pano.pojo.DTO.ImageDTO;
import yihleego.pano.pojo.DTO.SceneDTO;
import yihleego.pano.pojo.DTO.PanoDTO;
import yihleego.pano.pojo.DTO.PanoXmlDTO;
import yihleego.pano.service.PanoCrawlerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
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
            String sceneId=null;
            String previewUrl = null;
            ImageDTO desktopImage = null;
            ImageDTO mobileImage = null;

            sceneId=sceneElement.attr("pano_id");
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



    private  boolean downloadPanoPic(PanoXmlDTO panoXmlDTO,AuthorDTO authorDTO) throws Exception {
        int iTotal = 0;

        boolean isIncludeZero = false;//图片名称 是否包含 0  true 是  false 否

        String rootPath="/home/wbt/down/mypano/";
        String dirPath=authorDTO.getUid()+File.separator+panoXmlDTO.getPanoId();


        File fileRootPath = new File(rootPath);
        File fileDirPath = new File(rootPath+dirPath);

        if(!fileRootPath.exists())
            fileRootPath.mkdirs();

        if(!fileDirPath.exists())
            fileDirPath.mkdirs();


        if(!fileRootPath.exists() || !fileDirPath.exists()){
            System.out.println("mkdir failed");
            return false;
        }



        int iMax = -1;
        String[] aryStrDirect = {"b", "d", "f", "l", "r", "u"};
        String[] aryStrLevel = {"l1","l2","l3","l4","l5","l6","l7","l8","l9"};
        String[] aryStrLevelSon = null;
        String[] aryStrLevelGrandson = null;//叶子节点
        String[] aryStringTestMaxIndex0 = {"01","02","03","04","05","06","07","08","09"};
        String[] aryStringTestMaxIndex00 = {"1","2","3","4","5","6","7","8","9"};
        String[] aryStringTestMaxIndex = null;

        SceneDTO[] arySceneDTO=panoXmlDTO.getScenes();

        for(SceneDTO sceneDTO :arySceneDTO){
            String strSceneId=sceneDTO.getSceneId();
            String strPreviewUrl=sceneDTO.getPreviewUrl();


            ImageDTO desktopImage = sceneDTO.getDesktopImage();
            if(desktopImage!=null){
                int sceneSize=desktopImage.getCubeUrl().length;
                for(int i=0;i<sceneSize;i++){
                    String cubeUrl=desktopImage.getCubeUrl()[i];

                    Pattern pattern = Pattern.compile("https:(.*?)imgs/");
                    Matcher matcher = pattern.matcher(cubeUrl);
                    String strHeadUrl="https:"+(matcher.find()?matcher.group(1):"")+"imgs/";

                    pattern = Pattern.compile("imgs/(.*?).jpg");
                    matcher = pattern.matcher(cubeUrl);

                    if((matcher.find()?matcher.group(1):"").contains("0")){
                        isIncludeZero = true;
                        aryStringTestMaxIndex = aryStringTestMaxIndex0;
                    }else{
                        isIncludeZero = false;
                        aryStringTestMaxIndex =  aryStringTestMaxIndex00;
                    }

                    /*for(int j = 0;j < aryStrLevel.length;j++){
                        for(int k = 0;k < aryStringTestMaxIndex.length;k++){
                            StringBuffer strBuferHeadTestUrl = new StringBuffer(strHeadUrl);
                            strBuferHeadTestUrl.append("b");
                            strBuferHeadTestUrl.append(File.separator);//http://img.gy720.com/data/pano/181/412/1371/r/l2/2/l2_r_2_1.jpg
                            strBuferHeadTestUrl.append(aryStrLevel[i]);
                            strBuferHeadTestUrl.append(File.separator);
                            strBuferHeadTestUrl.append(aryStringTestMaxIndex[j]);
                            strBuferHeadTestUrl.append(File.separator);
                            strBuferHeadTestUrl.append(aryStrLevel[i]);
                            strBuferHeadTestUrl.append("_");
                            strBuferHeadTestUrl.append("b");
                            strBuferHeadTestUrl.append("_");
                            strBuferHeadTestUrl.append(aryStringTestMaxIndex[j]);
                            strBuferHeadTestUrl.append("_");
                            strBuferHeadTestUrl.append(aryStringTestMaxIndex[0]);
                            strBuferHeadTestUrl.append(".jpg");


                            boolean isPic = FileTools.isPic(strBuferHeadTestUrl.toString(),spiderPanoSelf);
                            if(isPic) {
                                iMax = Integer.parseInt(aryStringTestMaxIndex[j]);
                            }else{
                                break;
                            }
                        }



                }*/

            }}

        }
/*

        Map<String,Integer> levelIndexMap = new HashMap<String,Integer>();
        for(int i = 0;i < aryStrLevel.length;i++){
            for(int j = 0;j < aryStringTestMaxIndex.length;j++){
                StringBuffer strBuferHeadTestUrl = new StringBuffer(strHeadUrl);
                strBuferHeadTestUrl.append("b");
                strBuferHeadTestUrl.append(File.separator);//http://img.gy720.com/data/pano/181/412/1371/r/l2/2/l2_r_2_1.jpg
                strBuferHeadTestUrl.append(aryStrLevel[i]);
                strBuferHeadTestUrl.append(File.separator);
                strBuferHeadTestUrl.append(aryStringTestMaxIndex[j]);
                strBuferHeadTestUrl.append(File.separator);
                strBuferHeadTestUrl.append(aryStrLevel[i]);
                strBuferHeadTestUrl.append("_");
                strBuferHeadTestUrl.append("b");
                strBuferHeadTestUrl.append("_");
                strBuferHeadTestUrl.append(aryStringTestMaxIndex[j]);
                strBuferHeadTestUrl.append("_");
                strBuferHeadTestUrl.append(aryStringTestMaxIndex[0]);
                strBuferHeadTestUrl.append(".jpg");
                boolean isPic = FileTools.isPic(strBuferHeadTestUrl.toString(),spiderPanoSelf);
                if(isPic) {
                    iMax = Integer.parseInt(aryStringTestMaxIndex[j]);
                }else{
                    break;
                }
            }
            if(iMax > 0)
                levelIndexMap.put(aryStrLevel[i],iMax);
            iMax = -1;
        }

        List<String> listLevelSon = null;
        Map<String,List> levelIndexListMap =new HashMap<String,List>();
        for (int i = 0; i < aryStrDirect.length; i++) {
            String strDirect = aryStrDirect[i];
            for (int j = 0; j < aryStrLevel.length; j++) {
                String strLevel = aryStrLevel[j];
                if(!levelIndexMap.containsKey(strLevel)){
                    continue;//不存在的level 要排除
                }
                if(levelIndexListMap.containsKey(strLevel)){
                    listLevelSon = levelIndexListMap.get(strLevel);
                }else{
                    listLevelSon = new ArrayList<String>();
                    iMax = levelIndexMap.get(strLevel);
                    for (int m = 0; m < iMax; m++) {
                        if(isIncludeZero){//包含 0
                            listLevelSon.add("0" + Integer.toString(m + 1));
                        }else{
                            listLevelSon.add(Integer.toString(m + 1));
                        }
                    }
                    levelIndexListMap.put(strLevel,listLevelSon);
                }

                aryStrLevelSon = listLevelSon.toArray(new String[0]);
                aryStrLevelGrandson = listLevelSon.toArray(new String[0]);

                for (int k = 0; k < aryStrLevelSon.length; k++) {
                    String strLevelSon = aryStrLevelSon[k];
                    for (int l = 0; l < aryStrLevelGrandson.length; l++) {
                        String strLevelGrandson = aryStrLevelGrandson[l];
                        StringBuffer strBuferPath = new StringBuffer();
                        strBuferPath.append(strDirect);
                        strBuferPath.append(File.separator);
                        strBuferPath.append(strLevel);
                        strBuferPath.append(File.separator);
                        strBuferPath.append(strLevelSon);
                        strBuferPath.append(File.separator);

                        StringBuffer strBuferFile = new StringBuffer();
                        strBuferFile.append(strLevel);
                        strBuferFile.append("_");
                        strBuferFile.append(strDirect);
                        strBuferFile.append("_");
                        strBuferFile.append(strLevelSon);
                        strBuferFile.append("_");
                        strBuferFile.append(strLevelGrandson);
                        strBuferFile.append(".jpg");


                        StringBuffer strBuferFilePathSave = new StringBuffer(strFilePathSave);
                        strBuferFilePathSave.append(strBuferPath.toString());

                        StringBuffer strBuferHeadUrl = new StringBuffer(strHeadUrl);
                        strBuferHeadUrl.append(strBuferPath.toString());
                        strBuferHeadUrl.append(strBuferFile.toString());
                        CloseableHttpClient httpclient = HttpClients.createDefault();
                        InputStream in = FileTools.download(httpclient,strBuferHeadUrl.toString(), spiderPanoSelf, FinalUtil.CONTENT_TYPE.JPEG);
                        if(in == null){
                            continue;
                        }
                        iTotal++;
                        if(iTotal >= iCntPicsPerSleeep){
                            Thread.currentThread().sleep(iSleepSecondsPerDownloalPics);
                            iTotal = 0;
                        }
                        File file = new File(strBuferFilePathSave.toString());
                        FileTools.mkdir(file);
                        try {
                            StringBuffer strBuferAbsoluteFilePath = new StringBuffer();
                            strBuferAbsoluteFilePath.append(strBuferFilePathSave);
                            strBuferAbsoluteFilePath.append(File.separator);
                            strBuferAbsoluteFilePath.append(strBuferFile.toString());
                            FileOutputStream fout = new FileOutputStream(new File(strBuferAbsoluteFilePath.toString()));
                            int ll = -1;
                            byte[] tmp = new byte[1024];
                            while ((ll = in.read(tmp)) != -1) {
                                fout.write(tmp, 0, ll);
                            }
                            fout.flush();
                            fout.close();
                        }catch(Exception ex) {
                            logger.error("下载图片失败{}",ex);
                        }finally {
                            if(httpclient != null)
                                httpclient.close();
                            if(in != null)
                                in.close();
                        }
                    }
                }
            }
        }
        levelIndexMap.clear();
        levelIndexMap = null;
        levelIndexListMap.clear();
        levelIndexListMap = null;*/
        return isIncludeZero;
    }


    private Document getDocument(String strUrl) throws Exception {
        Document document = null;
        try {
            CrawlerUtils crawlerUtils = new CrawlerUtils();
            document = crawlerUtils.getDocument(strUrl, 0);
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
        return document;
    }
}
