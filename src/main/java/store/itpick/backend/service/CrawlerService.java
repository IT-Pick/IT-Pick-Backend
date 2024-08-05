package store.itpick.backend.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlerService {


    public List<String> fetchGoogleTrends() throws IOException {

        Document doc = Jsoup.connect("https://news.sbs.co.kr/news/SectionRssFeed.do?sectionId=14&plink=RSSREADER").get();
        System.out.println(doc);
        Elements elements = doc.select("item title");
        return elements.stream().map(element -> element.text()).collect(Collectors.toList());
    }
}
