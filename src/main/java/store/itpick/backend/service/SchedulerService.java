package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.TimeoutException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerService {

    private final SeleniumService seleniumService;
    private final KeywordService keywordService;


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
    @Scheduled(cron = "0 47 * * * *")
    public void performScheduledTasks() {
        log.info("Starting scheduled tasks...");
        if (isDailyTaskTime()) {
            performDailyTasks();
        } else {
            performHourlyTasks();
        }
        log.info("Scheduled tasks completed.");
    }

    private boolean isDailyTaskTime() {
        LocalTime now = LocalTime.now();
        return now.getHour() == 18 && now.getMinute() == 0;
    }

    // 시간별 크롤링 작업
    private void performHourlyTasks() {
        try {
//            executeWithRetries(() -> seleniumService.useDriverForZum("https://zum.com/"), "Zum 데이터 수집");
            executeWithRetries(() -> seleniumService.useDriverForMnate("https://m.nate.com/"), "Mnate 데이터 수집");
            executeWithRetries(() -> seleniumService.useDriverForNaver("https://www.signal.bz/"), "Naver 데이터 수집");
        } catch (Exception e) {
            log.error("Error during hourly task", e);
        } finally {
            seleniumService.quitDriver();  // 작업 후 드라이버 종료
        }
    }

    //18시에 하는 DB작업
    private void performDailyTasks() {
        log.info("Starting scheduled tasks...performing DailyTask");
        performHourlyTasks(); // 매일 18시에 hourlyTask를 포함
        // Daily task 로직
        keywordService.performDailyTasksNate();
        keywordService.performDailyTasksNaver();
//        keywordService.performDailyTasksZum();
        log.info("Scheduled tasks completed DailyTask.");

    }

    // 매주 월요일 18시에 실행하는 작업
    @Scheduled(cron = "0 0 18 * * MON")  // 매주 월요일 18시에 실행
    public void weeklyTask() {
        log.info("Starting weekly task...");
        // Weekly 작업 로직 추가
        log.info("Weekly task completed.");
    }
}
