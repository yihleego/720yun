package com.yihleego.pano.controller;


import com.yihleego.pano.pojo.DO.Pano720DO;
import com.yihleego.pano.service.PanoCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YihLeego on 17-5-18.
 */
@Controller
@RequestMapping("/pano")
public class PanoController {

    @Autowired
    PanoCrawlerService panoCrawlerService;

    @RequestMapping("/getPano")
    @ResponseBody
    public Pano720DO getPano() {
        Pano720DO pano720DO = new Pano720DO();
        return pano720DO;
    }

}
