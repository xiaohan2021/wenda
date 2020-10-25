package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description: AOP 面向切面
 * @Author: 小韩同学
 * @Date: 2020/10/23
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object arg : joinPoint.getArgs()){
            sb.append("arg : " + arg.toString() + "|");
        }
        logger.info("before method" + sb.toString());
    }

    @After("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void afterMethod(){
        logger.info("after method" + new Date());
    }

}
