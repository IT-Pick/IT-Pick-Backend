package store.itpick.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/test-redis")
    public String testRedis() {
        // Redis에 값을 저장하고 다시 가져와서 확인
        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");
        String value = redisTemplate.opsForValue().get("testKey");
        return value;
    }
}
