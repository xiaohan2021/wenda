package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/11/8
 */
@Controller
public class FeedController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET})
    private String getPullFeeds(Model model){
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();

        List<Integer> followees = new ArrayList<>();

        if(localUserId != 0){
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";

    }

    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET})
    private String getPushFeeds(Model model){
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();

        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<Feed>();
        for(String feedId : feedIds){
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if(feed == null){
                continue;
            }
            feeds.add(feed);
        }

        model.addAttribute("feeds", feeds);
        return "feeds";

    }

}
