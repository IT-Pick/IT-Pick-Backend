package store.itpick.backend.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import store.itpick.backend.util.Selenium;

import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Selenium selenium;

    @GetMapping("/test-redis")
    public String testRedis() {
        // Redis에 값을 저장하고 다시 가져와서 확인
        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");
        return redisTemplate.opsForValue().get("testKey");
    }

    @GetMapping("/test/jsoup")
    void jsoupTest() throws IOException {
        String homeUrl = "https://news.sbs.co.kr/news/newsMain.do";
        Document doc = Jsoup.connect(homeUrl).get();
        Elements elements = doc.select("li > a > p > strong");
        System.out.println(elements);
    }

    @GetMapping("/test/selenium")
    public String seleniumTest() {
        String url = "https://trends.google.com/trends/trendingsearches/daily?geo=KR&hl=ko";
//        String cssSelector = ".summary-text > a";
        String cssSelector = ".details-top > div > span";

        return selenium.useDriver(url, cssSelector);
    }
}
