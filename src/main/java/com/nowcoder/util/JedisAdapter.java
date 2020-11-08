package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/11/2
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

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

        // set集合 -- 无序的
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

        // Zset集合 -- 优先队列、有序集合，应用于排行榜等
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "Jim"); // 添加KV
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 75, "Lucy");
        jedis.zadd(rankKey, 80, "Mei");
        print(34, jedis.zcard(rankKey)); // 元素个数
        print(35, jedis.zcount(rankKey, 61, 100)); // 范围内的元素个数
        print(36, jedis.zscore(rankKey,"Lucy")); // 查询
        jedis.zincrby(rankKey, 2 , "Lucy"); // 增加修改
        print(37, jedis.zscore(rankKey,"Lucy"));
        jedis.zincrby(rankKey, 2 , "Luc"); // 没有该元素时默认score为0
        print(38, jedis.zscore(rankKey,"Luc"));
        print(39,jedis.zrange(rankKey, 0, 100)); // 在一定角标范围内的key
        print(40, jedis.zrange(rankKey, 0, 10));
        print(41, jedis.zrange(rankKey, 1, 3)); // 默认升序排列
        print(42, jedis.zrevrange(rankKey, 1, 3)); // 翻转，变为降序排列

        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")){
            print(43, tuple.getElement() + ":" + String.valueOf(tuple.getScore())); // 60-100分的元素遍历
        }

        print(44, jedis.zrank(rankKey, "Ben")); // 升序时，Ben排在第几位
        print(45, jedis.zrevrank(rankKey, "Ben")); // 降序时， Ben排在第几位

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a"); // score相同时，按member的字典顺序升序排序
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        print(46, jedis.zlexcount(setKey, "-", "+")); // score在正负∞间的元素个数
        print(47, jedis.zlexcount(setKey, "[b", "[d")); // 一定范围内的元素个数  左右闭区间
        print(48, jedis.zlexcount(setKey, "(b", "[d")); // 左开区间，右闭区间
        jedis.zrem(setKey, "b"); // 删除元素
        print(49, jedis.zrange(setKey, 0, 10));
        jedis.zremrangeByLex(setKey, "(c", "+"); // 删除c到∞的元素，c为开区间
        print(50, jedis.zrange(setKey, 0, 2)); // 角标0-2的元素

        JedisPool pool = new JedisPool();
        for (int i = 0; i < 100; i++) {
            Jedis j = pool.getResource();
            j.select(1);
            print(i+1, j.get("pv"));
            j.close();
        }

        User u = new User();
        u.setName("xiaohan");
        u.setPassword("xiaohan");
        u.setHeadUrl("xiaohan.png");
        u.setSalt("Salt");
        u.setId(1);
        print(51,JSONObject.toJSONString(u)); // json序列化
        jedis.set("user1", JSONObject.toJSONString(u));

        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value, User.class);
        print(52, user2);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/2");
    }

    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public List<String> lrange(String key, int start, int end){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public Jedis getJedis(){
        return  pool.getResource();
    }

    public Transaction multi(Jedis jedis){
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis){
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if(tx != null){
                try {
                    tx.close();
                } catch (IOException ioe) {
                    logger.error("发生异常" + ioe.getMessage());
                }
            }

            if(jedis != null){
                jedis.close();;
            }
        }
        return null;
    }

    public long zadd(String key, double score, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public Set<String> zrevrange(String key, int start, int end){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally{
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }
}
