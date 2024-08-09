package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.model.PeriodType;
import store.itpick.backend.util.Redis;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerService {

    private final SeleniumService seleniumService;
    private final KeywordService keywordService;
    private final Redis redis;


    // 최대 재시도 횟수와 재시도 간격 (초)
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_SECONDS = 5;

    // 재시도 로직을 포함한 함수
    private <T> T executeWithRetries(Callable<T> action, String actionName) {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                return action.call(); // 작업 시도
            } catch (TimeoutException e) {
                log.warn("{} 시도 중 TimeoutException 발생, 재시도 중... ({}/{})", actionName, attempt + 1, MAX_RETRIES);
                if (attempt == MAX_RETRIES - 1) {
                    log.error("모든 {} 시도 실패. 종료합니다.", actionName);
                    return null;
                }
                try {
                    TimeUnit.SECONDS.sleep(RETRY_DELAY_SECONDS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.error("재시도 지연 중 InterruptedException 발생: {}", ie.getMessage());
                    return null;
                }
            } catch (Exception e) {
                log.error("{} 작업 중 예기치 않은 오류 발생: {}", actionName, e.getMessage());
                break;
            }
        }
        return null;
    }



    // 매 시간마다 실행하는 작업
    @Scheduled(cron = "0 0 * * * *")
    public void performScheduledTasks() {
        log.info("Starting scheduled tasks...");
        if (isDailyTaskTime()) {
            performDailyTasks(); // 매일 18시에 호출됨
            if (isMonday()) {
                performWeeklyTasks(); // 월요일 18시에만 호출됨
            }
        } else {
            performHourlyTasks();   //매 시간 정각마다 호출됨
        }
        log.info("Scheduled tasks completed.");
    }

    private boolean isDailyTaskTime() {
        LocalTime now = LocalTime.now();
        return now.getHour() == 18 && now.getMinute() == 0;
    }

    private boolean isMonday() {
        return LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY;
    }

    // 시간별 크롤링 작업
    private void performHourlyTasks() {
        try {
            executeWithRetries(() -> seleniumService.useDriverForNaver("https://www.signal.bz/"), "Naver 데이터 수집");
            executeWithRetries(() -> seleniumService.useDriverForMnate("https://m.nate.com/"), "Nate 데이터 수집");
//            executeWithRetries(() -> seleniumService.useDriverForMnate("https://zum.com/"), "Zum 데이터 수집");

            /** 일간 통합 랭킹 저장 **/
            redis.saveTotalRanking(PeriodType.BY_REAL_TIME);


        } catch (Exception e) {
            log.error("Error during hourly task", e);
        } finally {
            seleniumService.quitDriver();  // 작업 후 드라이버 종료
        }
    }

    // 18시에 실행하는 Daily 작업
    private void performDailyTasks() {
        log.info("Starting scheduled tasks...performing DailyTask");
        performHourlyTasks(); // 매일 18시에 hourlyTask를 포함

        /**
         * Redis에서 일간 랭킹 생성, 계산
         * **/

        redis.saveDay();
        redis.saveTotalRanking(PeriodType.BY_DAY);


        /** DB에 있는 18시 검색어들을 Daily검색어로 Reference 참조할 수 있도록 함 **/
        keywordService.performDailyTasksNate();
        keywordService.performDailyTasksNaver();
//        keywordService.performDailyTasksZum();
        log.info("Scheduled tasks completed DailyTask.");
    }

    // 매주 월요일 18시에 실행하는 Weekly 작업
    private void performWeeklyTasks() {
        log.info("Starting weekly task...");
        /**
         * Redis에서 주간 랭킹 계산, 생성
         * **/
        redis.saveWeek();
        redis.saveTotalRanking(PeriodType.BY_WEEK);

        /**
         * DB에서 주간 랭킹 생성, 계산
         * **/


        log.info("Weekly task completed.");
    }

}
