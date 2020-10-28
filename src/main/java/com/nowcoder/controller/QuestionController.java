package com.nowcoder.controller;


import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.service.QuestionService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.management.HotspotThreadMBean;

import java.util.Date;


/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/27
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
        try{
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser() == null){
                // question.setUserId(WendaUtil.ANONYMOUS_USERID); // 匿名用户
                return WendaUtil.getJSONString(999);
            }  else {
                question.setUserId(hostHolder.getUser().getId()); // 指定用户
            }

            if(questionService.addQuestion(question) > 0){
                return WendaUtil.getJSONString(0);
            }
        } catch (Exception e){
            logger.error("增加题目失败" + e.getMessage());
        }

        return WendaUtil.getJSONString(1, "失败");

    }
}
