package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import com.nowcoder.util.WendaUtil;
import com.sun.webkit.dom.EntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.security.action.PutAllAction;

import java.util.*;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/11/4
 */
@Component
public class FeedHandler implements EventHandler {
    @Autowired
    QuestionService questionService;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    private String buildFeedData(EventModel model){
        Map<String, String> map = new HashMap<String, String>();
        User actor = userService.getUser(model.getActorId());
        if(actor == null){
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if(model.getType().equals(EventType.COMMENT)  ||
                (model.getType().equals(EventType.FOLLOW)  && model.getEntityType() == EntityType.ENTITY_QUESTION)){
            Question question = questionService.getById(model.getEntityId());
            if(question == null){
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;

    }

    @Override
    public void doHandler(EventModel model) {
        // 方便测试数据
        Random r = new Random();
        model.setActorId(1 + r.nextInt(10));

        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            return;
        }

        feedService.addFeed(feed);

        // 给事件的粉丝推
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        followers.add(0);
        for(int follower : followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
