package store.itpick.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import store.itpick.backend.service.CrawlerService;
import store.itpick.backend.service.RedisService;

import java.io.IOException;
import java.util.List;

@RestController
public class TrendsController {

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private RedisService redisService;

    @GetMapping("/fetch-google-trends")
    public ResponseEntity<?> fetchGoogleTrends() {
        try {
            List<String> trends = crawlerService.fetchGoogleTrends();
            if (trends == null || trends.isEmpty()) {
                return ResponseEntity.badRequest().body("트렌드 데이터가 유효하지 않습니다.");
            }
            redisService.saveGoogleTrends(trends);
            return ResponseEntity.ok().body("트렌드 데이터 저장 완료");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("트렌드 데이터를 가져오는 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/trends/google")
    public ResponseEntity<List<String>> getGoogleTrends() {
        List<String> trends = redisService.getGoogleTrends();
        if (trends == null || trends.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trends);
    }
}
