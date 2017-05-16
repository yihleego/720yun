package yihleego.pano.pojo.DTO;



/**
 * Created by YihLeego on 17-5-15.
 */
public class PanoXmlDTO {
    private String panoId;
    private SceneDTO[] Scenes;

    public String getPanoId() {
        return panoId;
    }

    public void setPanoId(String panoId) {
        this.panoId = panoId;
    }

    public SceneDTO[] getScenes() {
        return Scenes;
    }

    public void setScenes(SceneDTO[] scenes) {
        Scenes = scenes;
    }
}
