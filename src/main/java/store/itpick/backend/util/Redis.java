package store.itpick.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import store.itpick.backend.dto.redis.GetRankingListResponse;
import store.itpick.backend.model.CommunityType;
import store.itpick.backend.model.PeriodType;
import store.itpick.backend.model.RankingWeight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void saveAll(CommunityType communityType, PeriodType periodType, String date, List<String> keywordList) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = makeKey(communityType, periodType, date);

        for (int rank = 1; rank <= 10; rank++) {
            System.out.printf("key: %s\tkeyword: %s\t\trank: %d\n", key, keywordList.get(rank - 1), rank);
            zSetOperations.add(key, keywordList.get(rank - 1), rank);
        }
//        redisTemplate.expire(key, Duration.ofSeconds(20));
    }

    private static String makeKey(CommunityType communityType, PeriodType periodType, String date) {
        String key = communityType.get() + "_";
        switch (periodType) {
            case BY_REAL_TIME -> key += periodType.get();
            case BY_DAY -> key += DateUtils.getDate(DateUtils.getLocalDate(date));
            case BY_WEEK -> key += DateUtils.getWeek(DateUtils.getLocalDate(date));
        }
        return key;
    }

    public void calculateTotalRanking(PeriodType periodType, String date) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String totalKey = makeKey(CommunityType.TOTAL, periodType, date);

        List<String> keyList = new ArrayList<>();
        keyList.add(makeKey(CommunityType.NAVER, periodType, date));
        keyList.add(makeKey(CommunityType.NATE, periodType, date));
        keyList.add(makeKey(CommunityType.ZUM, periodType, date));

        for (String key : keyList) {
            int weight = getWeight(key);
            int rank = 1;
            for (Object keyword : Objects.requireNonNull(zSetOperations.range(key, 0, 9))) {
                int score = rank++ * weight;
                if (!Boolean.TRUE.equals(zSetOperations.addIfAbsent(totalKey, keyword, score))) {
                    zSetOperations.add(totalKey, keyword, score + zSetOperations.score(totalKey, keyword));
                }
            }
        }
    }

    public GetRankingListResponse getRankingList(CommunityType communityType, PeriodType periodType, String date) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = makeKey(communityType, periodType, date);

        List<String> rankingList = new ArrayList<>();
        for (Object keyword : Objects.requireNonNull(zSetOperations.range(key, 0, 9))) {
            rankingList.add((String) keyword);
        }

        return new GetRankingListResponse(key, rankingList);
    }

    private static int getWeight(String key) {
        if (key.startsWith(CommunityType.NAVER.get())) {
            return RankingWeight.NAVER.get();
        }
        if (key.startsWith(CommunityType.NATE.get())) {
            return RankingWeight.NATE.get();
        }
        if (key.startsWith(CommunityType.ZUM.get())) {
            return RankingWeight.ZUM.get();
        }
        return -1;
    }
}
