import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author ldq
 * @version 1.0
 * @date 2022/12/26 10:02
 * @Description: spring整合Redis，IOC容器化管理
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SpringRedisTest {

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void testPool(){
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis.ping());
        jedis.close();
    }
}
