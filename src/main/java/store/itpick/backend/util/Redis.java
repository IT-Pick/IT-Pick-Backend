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
import java.time.format.DateTimeFormatter;
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

    public String saveRealtime(CommunityType communityType, PeriodType periodType, List<String> keywordList) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = makeKey(communityType, periodType, "not needed");

//        List<String> originalKeywordList = new ArrayList<>();
//        for (Object originalKeyword : Objects.requireNonNull(zSetOperations.reverseRange(key, 0, 9))) {
//            originalKeywordList.add((String) originalKeyword);
//        }

        if (keywordList.size() >= 10) {
            redisTemplate.delete(key);
        }

        for (int i = 0; i < 10; i++) {
            int score = 10 - i;
            System.out.printf("key: %s\tscore: %d\tkeyword: %s\n", key, score, keywordList.get(i));
            zSetOperations.add(key, keywordList.get(i), score);
        }
//        redisTemplate.expire(key, Duration.ofSeconds(20));

        return key;
    }

    public void saveDay() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd");
        List<String> realTimeKeyList = getKeyList(PeriodType.BY_REAL_TIME, "not needed");
        List<String> dayKeyList = getKeyList(PeriodType.BY_DAY, localDate.format(dateTimeFormatter));

        for (int i = 0; i < realTimeKeyList.size(); i++) {
            int score = 10;
            for (Object realTimeKeyword : Objects.requireNonNull(zSetOperations.reverseRange(realTimeKeyList.get(i), 0, 9))) {
                zSetOperations.add(dayKeyList.get(i), realTimeKeyword, score--);
            }
        }
    }

    public void saveWeek() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        LocalDate mondayOfPreviousWeek = DateUtils.getMondayOfPreviousWeek();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd");
        List<List<String>> dayKeyListOfPreviousWeek = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate dayOfPreviousWeek = mondayOfPreviousWeek.plusDays(i);
            List<String> dayKeyList = getKeyList(PeriodType.BY_DAY, dayOfPreviousWeek.format(dateTimeFormatter));
            dayKeyListOfPreviousWeek.add(dayKeyList);
        }
        List<String> weekKeyList = getKeyList(PeriodType.BY_WEEK, mondayOfPreviousWeek.format(dateTimeFormatter));

        for (List<String> dayKeyList : dayKeyListOfPreviousWeek) {   // 지난주 월요일부터 일요일까지, 각 커뮤니티의 키 리스트
            for (int i = 0; i < dayKeyList.size(); i++) {  // naver, nate, zum에 대하여
                int score = 10;
                for (Object dayKeyword : Objects.requireNonNull(zSetOperations.reverseRange(dayKeyList.get(i), 0, 9))) {
                    if (!Boolean.TRUE.equals(zSetOperations.addIfAbsent(weekKeyList.get(i), dayKeyword, score))) {
                        zSetOperations.add(weekKeyList.get(i), dayKeyword, score + zSetOperations.score(weekKeyList.get(i), dayKeyword));
                    }
                    score--;
                }
            }
        }
    }

    public void saveTotalRanking(PeriodType periodType) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String date = switch (periodType) {
            case BY_REAL_TIME -> "not needed";
            case BY_DAY -> DateUtils.localDateToString(LocalDate.now());
            case BY_WEEK -> DateUtils.localDateToString(DateUtils.getMondayOfPreviousWeek());
        };
        String totalKey = makeKey(CommunityType.TOTAL, periodType, date);
        List<String> keyList = getKeyList(periodType, date); // key for naver, nate, zum
        for (String key : keyList) {
            int weight = getWeight(key);
            int rank = 1;
            for (Object keyword : Objects.requireNonNull(zSetOperations.reverseRange(key, 0, 9))) {
                int score = (11 - rank) * weight;
                if (!Boolean.TRUE.equals(zSetOperations.addIfAbsent(totalKey, keyword, score))) {
                    zSetOperations.add(totalKey, keyword, score + zSetOperations.score(totalKey, keyword));
                }
                rank++;
            }
        }
    }

    public GetRankingListResponse getRankingList(CommunityType communityType, PeriodType periodType, String date) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = makeKey(communityType, periodType, date);

        List<String> rankingList = new ArrayList<>();
        for (Object keyword : Objects.requireNonNull(zSetOperations.reverseRange(key, 0, 9))) {
            rankingList.add((String) keyword);
        }

        return new GetRankingListResponse(key, rankingList);
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

    private static List<String> getKeyList(PeriodType periodType, String date) {
        List<String> keyList = new ArrayList<>();
        keyList.add(makeKey(CommunityType.NAVER, periodType, date));
        keyList.add(makeKey(CommunityType.NATE, periodType, date));
        keyList.add(makeKey(CommunityType.ZUM, periodType, date));
        return keyList;
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
