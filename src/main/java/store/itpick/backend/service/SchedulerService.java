package store.itpick.backend.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.TimeoutException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import store.itpick.backend.model.Reference;
import store.itpick.backend.service.SeleniumService;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerService {

    private final SeleniumService seleniumService;

    // 최대 재시도 횟수와 재시도 간격 (초)
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_SECONDS = 5;

    // 재시도 로직을 포함한 함수
    private <T> T executeWithRetries(Callable<T> action, String actionName) {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                return action.call(); // 작업 시도
            } catch (TimeoutException e) {
                System.out.println(actionName + " 시도 중 TimeoutException 발생, 재시도 중... (" + (attempt + 1) + "/" + MAX_RETRIES + ")");
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("모든 " + actionName + " 시도 실패. 종료합니다.");
                    return null;
                }
                try {
                    TimeUnit.SECONDS.sleep(RETRY_DELAY_SECONDS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            } catch (Exception e) {
                System.out.println(actionName + " 작업 중 예기치 않은 오류 발생: " + e.getMessage());
                break;
            }
        }
        return null;
    }


    // 1시간마다 크롤링 작업 수행
    @Scheduled(cron = "0 0 * * * *")  // 매 시간 정각에 실행 (cron 표현식)
    public void CrawlingEveryHour() {
        try {
            log.info("Starting scheduled crawling task...");


            executeWithRetries(() -> seleniumService.useDriverForZum("https://zum.com/"), "Zum 데이터 수집");
            executeWithRetries(() -> seleniumService.useDriverForMnate("https://m.nate.com/"), "Mnate 데이터 수집");
            executeWithRetries(() -> seleniumService.useDriverForNaver("https://www.signal.bz/"), "Naver 데이터 수집");

            log.info("Scheduled crawling task completed.");
        } catch (Exception e) {
            log.error("Error during scheduled crawling task", e);
        } finally {
            seleniumService.quitDriver();  // 작업 후 드라이버 종료
        }
    }

    @Scheduled(cron = "0 0 * * * *")  // 매 시간 정각에 실행 (cron 표현식)
    public void CrawlingEveryDay() {


    }

}
