package com.YouTubeTools.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/youtube")
public class YouTubeVideoController {
    @PostMapping("/video-details")
    public String VideoDetails(){
        return "home";
    }
}