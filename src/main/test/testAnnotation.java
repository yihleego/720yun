import yihleego.crawler.util.CrawlerUtils;
import yihleego.pano.pojo.DTO.AuthorDTO;
import yihleego.pano.pojo.DTO.ImageDTO;
import yihleego.pano.pojo.DTO.PanoXmlDTO;
import yihleego.pano.pojo.DTO.SceneDTO;
import yihleego.pano.pojo.DTO.PanoDTO;
import yihleego.pano.service.PanoCrawlerService;
import yihleego.pano.service.impl.PanoCrawlerServiceImpl;

import javax.swing.text.Document;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wbt on 17-5-11.
 */
public class testAnnotation {

   public static void main(String[] args){



       /*String url="https://ssl-panoimg4.720static.com/resource/prod/69ai37767n4/3702axigayr/271914/1461064787/imgs/%s/l3/%v/l3_%s_%v_%h.jpg";

       Pattern pattern = Pattern.compile("imgs/%s/(.*?)/%");
       Matcher matcher = pattern.matcher(url);
       if (matcher.find())
           System.out.println(matcher.group(1));
*/

       testDown();


   }


   public static  void testDown(){
       PanoCrawlerService panoCrawlerService=new PanoCrawlerServiceImpl();
       PanoXmlDTO pano=null;
       try {
           pano=panoCrawlerService.parsePanoXml("81022jp8jnw");
           AuthorDTO authorDTO=new AuthorDTO();
           authorDTO.setUid("sb");
           panoCrawlerService.downloadPano(pano,authorDTO);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   public static void testRegx(){
       String cubeUrl="https://ssl-panoimg4.720static.com/resource/prod/69ai37767n4/3702axigayr/271914/1461064787/imgs/%s/l3/%v/l3_%s_%v_%h.jpg";
       Pattern pattern = Pattern.compile("https:(.*?)imgs/");
       Matcher matcher = pattern.matcher(cubeUrl);
       String strHeadUrl="https:"+(matcher.find()?matcher.group(1):"")+"imgs/";

       System.out.println(strHeadUrl);

       pattern = Pattern.compile("imgs/(.*?).jpg");
       matcher = pattern.matcher(cubeUrl );

       if(matcher.find()) System.out.println(matcher.group(1));


       CrawlerUtils crawlerUtils=new                CrawlerUtils();

       try {
           org.jsoup.nodes.Document document= crawlerUtils.getDocument("https://ssl-panoimg4.720static.com/resource/prod/69ai37767n4/3702axigayr/271914/1461064787/imgs/b/l3/1/l3_b_1_6.jpg",0);
           System.out.println(document.text());
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    public static void testParsePanoXml(){
        PanoCrawlerService panoCrawlerService=new PanoCrawlerServiceImpl();
        PanoXmlDTO pano=null;
        try {
            pano=panoCrawlerService.parsePanoXml("623jO5kuOO9");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("author\t"+pano.getPanoId());

        for(SceneDTO scene:pano.getScenes()) {
            System.out.println("<scene scene_id=" + scene.getSceneId() + ">");
            System.out.println("<preview>url"+scene.getPreviewUrl());
            ImageDTO imageDTO=scene.getDesktopImage();
            System.out.println(imageDTO.getType()+imageDTO.getMultires()+imageDTO.getTileSize()+imageDTO.getImageIf());
            System.out.println(imageDTO.getCubeUrl());

        }


    }
   public static void testGetAuthor(){
       PanoCrawlerService panoCrawlerService=new PanoCrawlerServiceImpl();
       List<AuthorDTO> authorDTOList =new ArrayList();
       List<PanoDTO> panoList=new ArrayList();
       for(int i=1;i<=4;i++) {
           try {
               authorDTOList.addAll(panoCrawlerService.getAuthorList(i));
           } catch (Exception e) {
               e.printStackTrace();
           }
       }


       if(authorDTOList ==null)
           return;
       System.out.println("author size : "+ authorDTOList.size());


       for(AuthorDTO authorDTO : authorDTOList){
           try {
               panoList.addAll(panoCrawlerService.getPanoList(authorDTO.getUid()));
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

       if(panoList==null)
           return;
       System.out.println("pano size ： "+panoList.size());
   }
}
