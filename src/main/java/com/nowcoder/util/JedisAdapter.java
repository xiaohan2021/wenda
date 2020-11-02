package com.nowcoder.util;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

import java.nio.file.attribute.UserPrincipalLookupService;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/11/2
 */
public class JedisAdapter {
    public static void print(int index, Object obj){
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/1"); // redis连接服务器
        jedis.flushDB(); // 删除数据库记录

        // get  set
        jedis.set("hello", "world"); // 设置key--value
        print(1,jedis.get("hello"));
        jedis.rename("hello", "newHello"); // key重命名
        // print(2,jedis.get("newHello"));
        jedis.setex("hello2", 15, "world2"); // 过期时间15秒

        // 数值操作 加减
        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));
        jedis.decrBy("pv", 2);
        print(3,jedis.get("pv"));

        print(4,jedis.keys("*"));

        // list集合演示
        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }

        print(5,jedis.lrange(listName, 0, 12));
        print(6,jedis.lrange(listName, 0, 3));
        print(7,jedis.llen(listName));
        print(8,jedis.lpop(listName)); // 出栈
        print(9,jedis.llen(listName));
        print(10, jedis.lrange(listName, 2, 6));
        print(11, jedis.lindex(listName, 3));
        print(12, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "bb")); // 指定位置后插入
        print(12, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bb"));// 指定位置前插入
        print(13, jedis.lrange(listName, 0, 12));

        // hash演示
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "3456");
        print(14,jedis.hget(userKey, "name"));
        print(15, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(16, jedis.hgetAll(userKey)); // hash全部取出
        print(17,jedis.hexists(userKey, "email")); // 是否存在key
        print(18,jedis.hexists(userKey, "age"));
        print(19,jedis.hkeys(userKey)); // 取全部key
        print(20,jedis.hvals(userKey)); // 取全部value
        jedis.hsetnx(userKey, "school", "bupt"); // 不存在写入
        jedis.hsetnx(userKey, "name", "tom"); // 存在则不重写
        print(21, jedis.hgetAll(userKey));

        // set集合
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i*i));
        }
        print(22, jedis.smembers(likeKey1)); // 取出全部集合元素
        print(23, jedis.smembers(likeKey2));
        print(24, jedis.sunion(likeKey1, likeKey2)); // 并集
        print(25, jedis.sdiff(likeKey1, likeKey2)); // 我有你没有
        print(26, jedis.sinter(likeKey1, likeKey2)); // 交集
        print(27, jedis.sismember(likeKey1, "12"));
        print(28, jedis.sismember(likeKey2, "16")); // 判断是不是集合中的元素
        jedis.srem(likeKey1, "5"); // 删除元素
        print(29, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25"); // 将likeKey2中的元素25推送到likeKey1中
        print(30, jedis.smembers(likeKey1));
        print(31,jedis.scard(likeKey1)); // set元素数目
        print(32, jedis.srandmember(likeKey1,2)); // 随机取值，例如抽奖


    }
}
