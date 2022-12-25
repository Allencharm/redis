import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.pojo.User;
import com.redis.utils.JsonUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ldq
 * @version 1.0
 * @date 2022/12/24 16:15
 * @Description:
 */
public class RedisTest {

    //测试是否联通
    @Test
    public void testPing(){
        //建立连接
        Jedis jedis = new Jedis("192.168.157.135", 6379);

        //实现连接测试
        String ping = jedis.ping();
        System.out.println(ping);

        //释放资源
        jedis.close();
    }

    //向redis库中添加String数据
    @Test
    public void testAddString(){
        //建立连接
        Jedis jedis = new Jedis("192.168.157.135", 6379);

        //实现添加
        jedis.set("name1", "八戒");

        //释放资源
        jedis.close();
    }

    //取redis库中String数据
    @Test
    public void testGetString(){
        //建立连接
        Jedis jedis = new Jedis("192.168.157.135", 6379);

        //实现添加
        String name1 = jedis.get("name1");
        System.out.println(name1);

        //释放资源
        jedis.close();
    }

    //redis库中存list数据
    @Test
    public void testAddList(){
        //建立连接
        Jedis jedis = new Jedis("192.168.157.135", 6379);

        //实现添加
        jedis.select(1);
        jedis.lpush("list3", "八戒","沙悟净");
        jedis.rpush("list4", "八戒","沙悟净");
        List<String> list4 = jedis.lrange("list4", 0, -1);
        System.out.println(list4.toString());
        //释放资源
        jedis.close();
    }

    //redis库中取list数据
    @Test
    public void testRangeList(){
        //建立连接
        Jedis jedis = new Jedis("192.168.157.135", 6379);

        //实现添加
        jedis.select(1);
        List<String> list4 = jedis.lrange("list4", 0, -1);
        System.out.println(list4.toString());
        //释放资源
        jedis.close();
    }

    @Test
    public void testAddHash(){
        //建立连接
        Jedis jedis = new Jedis("192.168.157.135", 6379);

        //实现添加
        jedis.select(3);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "张三");
        jedis.hmset("h2",map);
        //释放资源
        jedis.close();
    }

    @Test
    public void testGetHash(){
        //建立连接
        Jedis jedis = new Jedis("192.168.157.135", 6379);

        //实现添加
        jedis.select(3);
        List<String> hmget = jedis.hmget("h2", "name");
        System.out.println(hmget.toString());
        //释放资源
        jedis.close();
    }

    //池化操作
    @Test
    public void testPool(){
        //使用连接池管理连接对象
        //建立连接池
        JedisPool jedisPool = new JedisPool("192.168.157.135", 6379);

        //从池子中获取连接对象
        Jedis jedis = jedisPool.getResource();

        //实现功能
        String ping = jedis.ping();
        System.out.println(ping);

        //释放资源
        jedis.close();

    }

    //json操作
    @Test
    public void testJsonObject(){
        Logger logger = LoggerFactory.getLogger(RedisTest.class);

        //建立连接池
        JedisPool jedisPool = new JedisPool("192.168.157.135", 6379);

        //从池子中获取连接对象
        Jedis jedis = jedisPool.getResource();

        //实现功能
        User user = new User();
        user.setUid(1001);
        user.setUname("牛魔王");
        user.setUpwd("123");
        //使用json串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(user);

            logger.info("user:" , user.toString());
            logger.info("success");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        jedis.set("user1", json);

        //释放资源
        jedis.close();
    }

    //取json操作
    @Test
    public void testGetJsonObject(){
        Logger logger = LoggerFactory.getLogger(RedisTest.class);

        //建立连接池
        JedisPool jedisPool = new JedisPool("192.168.157.135", 6379);

        //从池子中获取连接对象
        Jedis jedis = jedisPool.getResource();
        //使用json串

        String json = jedis.get("user1");
        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;

        try {
            user = objectMapper.readValue(json,User.class);

            System.out.println(user);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //释放资源
        jedis.close();
    }

    //存list结构数据
    @Test
    public void testAddList1()throws Exception{
        //采用连接池管理连接对象
        //建立连接池
        JedisPool pool = new JedisPool("192.168.157.135",6379);

        //从池子中获取连接对象
        Jedis jedis = pool.getResource();

        //实现功能
        User user1 = new User();
        user1.setUid(1001);
        user1.setUname("牛魔王");
        user1.setUpwd("123");

        User user2 = new User();
        user2.setUid(1002);
        user2.setUname("铁扇公主");
        user2.setUpwd("123");

        User user3 = new User();
        user3.setUid(1003);
        user3.setUname("红孩儿");
        user3.setUpwd("123");
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        //使用json串方式
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(list);
        jedis.set("userlist1",json);

        //释放资源
        jedis.close();
    }

    //获取list数据
    @Test
    public void testGetList1()throws Exception{
        //采用连接池管理连接对象
        //建立连接池
        JedisPool pool = new JedisPool("192.168.157.135",6379);

        //从池子中获取连接对象
        Jedis jedis = pool.getResource();

        //实现功能
        //使用json串方式
        ObjectMapper objectMapper = new ObjectMapper();
        String json = jedis.get("userlist1");
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, User.class);
        List<User> list = objectMapper.readValue(json, javaType);
        System.out.println(list);

        //释放资源
        jedis.close();
    }

    //使用hash存对象数据
    @Test
    public void testAddObjectHash(){
        //采用连接池管理连接对象
        //建立连接池
        JedisPool pool = new JedisPool("192.168.157.135",6379);

        //从池子中获取连接对象
        Jedis jedis = pool.getResource();

        //实现功能
        User user = new User();
        user.setUid(1001);
        user.setUname("牛魔王");
        user.setUpwd("123");

        Map<String,String> map = new HashMap<>();
        map.put("uid",user.getUid()+"");
        map.put("uname",user.getUname());
        map.put("upwd",user.getUpwd());
        jedis.hmset("user2",map);

        //释放资源
        jedis.close();
    }


    @Test
    public void testAddPerson(){
        //创建JedisPoolConfig对象
        JedisPoolConfig poolConfig=new JedisPoolConfig();
        //最大空闲连接数
        poolConfig.setMaxIdle(3);
        //大连接数
        poolConfig.setMaxTotal(5);
        //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        poolConfig.setBlockWhenExhausted(true);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常
        poolConfig.setMaxWaitMillis(30000);
        //在获取连接的时候检查有效性
        poolConfig.setTestOnBorrow(true);
        JedisPool jedisPool=new        JedisPool(poolConfig,"192.168.18.200",6379,30000,"123456");
        User user= new User(100,"张三","123");
        Jedis jedis = jedisPool.getResource();
        // jedis.set("person", JsonUtils.objectToJson(person));
        jedis.hset("user",user.getUname(),JsonUtils.objectToJson(user));
        jedis.close();
    }

    @Test
    public void testAddPerson2(){

        //创建JedisPoolConfig对象
        JedisPoolConfig poolConfig=new JedisPoolConfig();
        //最大空闲连接数
        poolConfig.setMaxIdle(3);
        //大连接数
        poolConfig.setMaxTotal(5);
        //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        poolConfig.setBlockWhenExhausted(true);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常
        poolConfig.setMaxWaitMillis(30000);
        //在获取连接的时候检查有效性
        poolConfig.setTestOnBorrow(true);
        JedisPool jedisPool=new JedisPool(poolConfig,"192.168.18.200",6379,30000,"123456");
        Jedis jedis = jedisPool.getResource();
        String jsonData=jedis.get("user");
        User user= JsonUtils.jsonToEntity(jsonData,User.class);
        System.out.println(user);
        jedis.close();
    }
}
