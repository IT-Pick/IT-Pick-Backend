package store.itpick.backend.controller;

import org.openqa.selenium.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.itpick.backend.model.RelatedResource;
import store.itpick.backend.util.Selenium;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/rank")
public class RankController {

    @Autowired
    private Selenium selenium;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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

    @GetMapping("/zum")
    public List<RelatedResource> getRankFromZum() {
        String url = "https://zum.com/";
        return executeWithRetries(() -> selenium.useDriverForZum(url), "Zum 데이터 수집");
    }

    @GetMapping("/namu")
    public String getRankFromNamuwiki() {
        String url = "https://namu.wiki/";
        return executeWithRetries(() -> selenium.useDriverForNamuwiki(url), "Namuwiki 데이터 수집");
    }

    @GetMapping("/signal")
    public List<RelatedResource> getRankFromSignal() {
        String url = "https://www.signal.bz/";
        return executeWithRetries(() -> selenium.useDriverForNaver(url), "Signal 데이터 수집");
    }

    @GetMapping("/mnate")
    public List<RelatedResource> getRankFromMnate() {
        String url = "https://m.nate.com/";
        return executeWithRetries(() -> selenium.useDriverForMnate(url), "Mnate 데이터 수집");
    }

//    @GetMapping("/nate")
//    public String getRankFromNate() {
//        String url = "https://nate.com/";
//
//        return selenium.useDriverForMnate(url);
//    }
}
