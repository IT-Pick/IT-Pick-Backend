package store.itpick.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String GOOGLE_TRENDS_KEY = "trends:google";


    public void saveGoogleTrends(List<String> trends) {
        if (trends == null || trends.isEmpty()) {
            throw new IllegalArgumentException("트렌드 리스트는 null 이거나 빈 리스트일 수 없습니다.");
        }
        redisTemplate.opsForList().rightPushAll("googleTrends", trends);
    }

    public List<String> getGoogleTrends() {
        return redisTemplate.opsForList().range(GOOGLE_TRENDS_KEY, 0, -1);
    }
}
