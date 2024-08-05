package store.itpick.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import store.itpick.backend.model.PeriodType;

import java.time.LocalDate;
import java.util.List;

@Component
public class Redis {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    @Scheduled(cron = "0/5 * * * * ?")
//    public void save() {
//        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
//        String key = makeKey("nate", PeriodType.BY_WEEK);
//
//        zSetOperations.add(key, "남자 양궁", 4);
//        zSetOperations.add(key, "여자 양궁", 5);
//        zSetOperations.add(key, "카온플", 2);
//        zSetOperations.add(key, "요거트 스무디", 3);
//        zSetOperations.add(key, "버즈", 6);
//        redisTemplate.expire(key, Duration.ofSeconds(20));
//    }

    public void saveAll(PeriodType periodType, String community, List<String> keywordList) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = makeKey(community, periodType);

        for (int rank = 1; rank <= 10; rank++) {
            System.out.printf("key: %s\tkeyword: %s\t\trank: %d\n", key, keywordList.get(rank - 1), rank);
            zSetOperations.add(key, keywordList.get(rank - 1), rank);
        }
//        redisTemplate.expire(key, Duration.ofSeconds(20));
    }

    private static String makeKey(String community, PeriodType periodType) {
        String key = community + "_";
        switch (periodType) {
            case BY_REAL_TIME -> key += periodType.get();
            case BY_DAY -> key += DateUtils.getCurrentDate();
            case BY_WEEK -> key += DateUtils.getCurrentWeek(LocalDate.now());
        }
        return key;
    }
}
