package store.itpick.backend.controller;

import org.jsoup.Connection;
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

    @GetMapping("/test-redis")
    public String testRedis() {
        // Redis에 값을 저장하고 다시 가져와서 확인
        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");
        String value = redisTemplate.opsForValue().get("testKey");
        return value;
    }

    @GetMapping("/test20")
    void testJsoup20() throws IOException {
        String homeUrl = "https://news.sbs.co.kr/news/newsMain.do";
        Document doc = Jsoup.connect(homeUrl).get();
        Elements elements = doc.select("li > a > p > strong");
        System.out.println(elements);
    }

    @GetMapping("/test21")
    void testJsoup21() throws IOException {
        String homeUrl = "https://trends.google.com/trends/trendingsearches/daily?geo=KR&hl=ko";
        Connection.Response response = Jsoup.connect(homeUrl).method(Connection.Method.GET).execute();
        Document doc = response.parse();
        System.out.println(doc.html());
    }

    @GetMapping("/test22")
    public String test() {

        //JsoupFun um = new JsoupFun();

        Selenium sel = new Selenium();

        String url = "https://trends.google.com/trends/trendingsearches/daily?geo=KR&hl=ko";

        sel.useDriver(url);

        return "main";
    }

    @GetMapping("/*")
    void pathMatch() {
        System.out.println("favicon.ico.");
    }
}
