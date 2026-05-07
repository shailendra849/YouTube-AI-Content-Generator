package com.YouTubeTools.Controller;

import com.YouTubeTools.Model.SearchVideo;
import com.YouTubeTools.Model.Video;
import com.YouTubeTools.Service.AIService;
import com.YouTubeTools.Service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/youtube")
public class YouTubeTagsController {

    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    private AIService aiService;

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${openai.api.key:}")
    private String openAiKey;

    private boolean isApiKeyConfigured() {
        return apiKey != null && !apiKey.isEmpty();
    }

    private boolean isOpenAiKeyConfigured() {
        return openAiKey != null && !openAiKey.isEmpty();
    }

    @PostMapping("/search")
    public String videoTags(@RequestParam("videoTitle") String videoTitle, Model model) {

        if (!isApiKeyConfigured()) {
            model.addAttribute("error", "YouTube API key is not configured.");
            return "home";
        }

        if (videoTitle == null || videoTitle.isEmpty()) {
            model.addAttribute("error", "Video Title is required.");
            return "home";
        }

        try {
            SearchVideo result = youTubeService.searchVideos(videoTitle);
            model.addAttribute("primaryVideo", result.getPrimaryVideo());
            model.addAttribute("relatedVideos", result.getRelatedVideos());
            model.addAttribute("searchedTitle", videoTitle);

            // Collect all existing tags to give AI full context
            List<String> allExistingTags = new ArrayList<>();
            if (result.getPrimaryVideo() != null) {
                allExistingTags.addAll(result.getPrimaryVideo().getTags());
            }
            if (result.getRelatedVideos() != null) {
                for (Video v : result.getRelatedVideos()) {
                    allExistingTags.addAll(v.getTags());
                }
            }

            // Call AI only if key is configured
            if (isOpenAiKeyConfigured()) {
                List<String> aiTags = aiService.enhanceTags(videoTitle, allExistingTags);
                model.addAttribute("aiSuggestedTags", aiTags);
            } else {
                model.addAttribute("aiKeyMissing", true);
            }

            return "home";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "home";
        }
    }
}

