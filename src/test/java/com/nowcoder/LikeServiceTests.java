package com.nowcoder;

import com.nowcoder.service.LikeService;
import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/11/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class LikeServiceTests {
     @Autowired
    LikeService likeService;

     @Before // 某些测试的初始化放这里
     public void setUp(){
         System.out.println("setUp");
     }

     @After // 某些测试的清理放这里
     public void tearDown(){
         System.out.println("tearDown");
     }

     @BeforeClass // 所有测试的初始化放在这里
     public static void beforeClass(){
         System.out.println("beforeClass");
     }

    @AfterClass // 所有单元测试的清理放在这里
    public static void afterClass(){
        System.out.println("afterClass");
    }

     @Test // 测试业务逻辑
     public void testLike(){
         System.out.println("testLike");
         likeService.like(123, 1, 1);
         Assert.assertEquals(1, likeService.getLikeStatus(123, 1, 1));

         likeService.dislike(123, 1, 1);
         Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
     }

     @Test
    public void testXXX(){
         System.out.println("testXXX");
     }

     @Test(expected = IllegalArgumentException.class) // 异常
    public void testException(){
         System.out.println("testException");
         throw new IllegalArgumentException("异常发生了");
     }
}
