package store.itpick.backend.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.model.*;
import store.itpick.backend.repository.KeywordRepository;
import store.itpick.backend.service.KeywordService;
import store.itpick.backend.service.CommunityPeriodService;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import store.itpick.backend.util.Redis;
import store.itpick.backend.util.SeleniumUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeleniumService {
    private WebDriver driver;
    private final SeleniumUtil seleniumUtil;

    private final Redis redis;

    private final KeywordService keywordService;
    private final ReferenceService referenceService;
    private final CommunityPeriodService communityPeriodService;



    // ChromeDriver 연결 (WEB_DRIVER_PATH 값 주입되고 사용해야 하므로 PostConstruct)
    @PostConstruct
    private void initDriver() {
        seleniumUtil.initDriver();
        driver = seleniumUtil.getDriver();
    }

    public List<Reference> useDriverForZum(String url) {
        driver.get(url);

        Actions actions = new Actions(driver);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        try {
//            WebElement btn = driver.findElement(By.className("btn-layer-close-day"));
//            actions.click(btn).perform();
//        } catch (NoSuchElementException ignored) {
//        }

        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("issue_keywords")));
        actions.moveToElement(webElement).perform();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement issueLayer = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("layer_issue_keywords")));

        // 키워드 수집
        List<WebElement> webElementByKeyword = issueLayer.findElements(By.cssSelector("a span"));
        System.out.println(webElementByKeyword.size());
        List<String> keywordList = new ArrayList<>();
        for (WebElement element : webElementByKeyword) {
            String keyword = element.getText();
            keywordList.add(keyword);
            System.out.println(keyword);
        }
        redis.saveRealtime(CommunityType.ZUM, PeriodType.BY_REAL_TIME, keywordList);


        // 링크 수집
        List<WebElement> webElementBySearchLink = issueLayer.findElements(By.cssSelector("a"));
        List<String> linksList = new ArrayList<>();
        for (WebElement element : webElementBySearchLink) {
            String searchLink = element.getAttribute("href");
            linksList.add(searchLink);
            System.out.println(searchLink);
        }

        processKeywordsAndReferences("zum", keywordList, linksList);


//        quitDriver();
        return null;
    }



    public List<Reference> useDriverForNaver(String url) {
        driver.get(url);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<WebElement> webElementListByLink = driver.findElements(By.cssSelector(".rank-layer"));

        //키워드 수집 List
        List<String> keywordList = new ArrayList<>();
        //링크 수집 List
        List<String> linksList = new ArrayList<>();

        for (WebElement element : webElementListByLink) {
            String searchLink = element.getAttribute("href");
            WebElement titleElement = element.findElement(By.cssSelector(".rank-text"));
            String keyword = titleElement.getText();
            keywordList.add(keyword);   //키워드 추가
            linksList.add(searchLink);  //링크 추가

        }

        redis.saveRealtime(CommunityType.NAVER, PeriodType.BY_REAL_TIME, keywordList);


        processKeywordsAndReferences("naver", keywordList, linksList);

        return null;
    }

    public List<Reference> useDriverForMnate(String url) {
        driver.get(url);
        Actions actions = new Actions(driver);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("btn_open")));
        actions.click(btn).perform();

        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("rankList")));

        // 키워드 수집
        List<WebElement> webElementByKeyword = webElement.findElements(By.className("kw"));
        List<String> keywordList = new ArrayList<>();
        for (int i = 0; i < webElementByKeyword.size(); i++) {
            WebElement element = webElementByKeyword.get(i);
            String keyword = element.getText();

            // 문자열 맨 앞 1개 문자 제거
            if (!keyword.isEmpty()) {
                keyword = keyword.substring(1);
            }

            // 마지막 루프에서는 맨 앞 2개 문자 제거
            if (i == webElementByKeyword.size() - 1 && keyword.length() > 1) {
                keyword = keyword.substring(1);
            }

            keywordList.add(keyword);
            System.out.println(keyword);
        }

        redis.saveRealtime(CommunityType.NATE, PeriodType.BY_REAL_TIME, keywordList);

        // 링크 수집
        List<WebElement> webElementBySearchLink = webElement.findElements(By.cssSelector("a"));
        List<String> linksList = new ArrayList<>();
        for (WebElement element : webElementBySearchLink) {
            String searchLink = element.getAttribute("href");
            linksList.add(searchLink);
            System.out.println(searchLink);
        }

        processKeywordsAndReferences("nate", keywordList, linksList);

        return null;
    }

    private List<Reference> SearchReference(List<Keyword> keywords, List<String> linksList ) {
        final String emptyImg="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";
        List<Reference> references=new ArrayList<>();
        int i=0;
        for (Keyword keyword : keywords) {
            String searchLink=linksList.get(i++);
            String documentTitle = keyword.getKeyword();
            // Google 뉴스 검색 URL 생성
            String naverSearchUrl = null;
            try {
                naverSearchUrl = "https://search.naver.com/search.naver?where=news&query=" + URLEncoder.encode(documentTitle, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            driver.get(naverSearchUrl);

            try {

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                // 검색 결과 페이지에서 첫 번째 링크 추출
                WebElement webElement = driver.findElement(By.cssSelector(".news_contents"));

                String newsTitle = webElement.findElement(By.cssSelector(".news_tit")).getText();

                String newsContent = webElement.findElement(By.cssSelector(".news_dsc")).getText();

                String newsLink = webElement.findElement(By.cssSelector(".news_tit")).getAttribute("href");

                String imageUrl = webElement.findElement(By.cssSelector(".thumb")). getAttribute("src");


                long startTime = System.currentTimeMillis(); // 시작 시간 기록
                while (imageUrl.equals(emptyImg)){
                    log.info("이미지 비어사 재추출중...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (System.currentTimeMillis() - startTime > 30000) {
                        log.warn("이미지를 30초 이상 로드하지 못했습니다.");
                        break;
                    }
                    webElement = driver.findElement(By.cssSelector(".news_contents"));
                    imageUrl = webElement.findElement(By.cssSelector(".thumb")). getAttribute("src");
                }



                Reference reference=new Reference();

                reference.setNewsTitle(newsTitle);
                reference.setNewsContent(newsContent);
                reference.setNewsLink(newsLink);
                reference.setNewsImage(imageUrl);
                reference.setSearchLink(searchLink);

                references.add(reference);
            }catch (NoSuchElementException e){
                System.out.println("검색 결과가 없습니다");
                Reference reference=new Reference();

                reference.setNewsTitle("No search");
                reference.setNewsContent("No search");
                reference.setNewsLink("No search");
                reference.setNewsImage("No search");
                reference.setSearchLink("No search");

                references.add(reference);

            }

        }

        return references;
    }

    @Transactional
    public void processKeywordsAndReferences(String communityName, List<String> keywordList, List<String> linksList) {
        List<Keyword> keywords = new ArrayList<>();
        List<Keyword> keywordsToUpdate = new ArrayList<>();
        List<Keyword> keywordsToSave = new ArrayList<>();
        int size = Math.min(keywordList.size(), linksList.size());


        for (int i = 0; i < size; i++) {
            String keywordContent = keywordList.get(i);
            Keyword keyword = new Keyword();
            keyword.setKeyword(keywordContent);
            keywords.add(keyword);
        }

        // 2. Reference 객체 검색
        List<Reference> references = SearchReference(keywords, linksList);

        // 3. Reference 객체 저장 (새로 추가되는 경우)
        referenceService.saveAll(references);

        // 4. CommunityPeriod 객체 생성 및 저장
        CommunityPeriod communityPeriod = new CommunityPeriod();
        communityPeriod.setCommunity(communityName);
        communityPeriod.setPeriod("realtime");

        try {
            Optional<CommunityPeriod> existingCommunityPeriodOptional = communityPeriodService.findByCommunityAndPeriod(communityName, "realtime");
            if (existingCommunityPeriodOptional.isPresent()) {
                communityPeriod = existingCommunityPeriodOptional.get();
            } else {
                communityPeriodService.save(communityPeriod);
            }
        } catch (DataIntegrityViolationException e) {
            log.error("CommunityPeriod 저장 중 예외 발생: " + e.getMessage());
            throw new RuntimeException("CommunityPeriod 저장 중 오류가 발생했습니다.", e);
        }

        // 1. 키워드 존재 여부에 따라 구분
        for (int i = 0; i < size; i++) {
            Reference reference = references.get(i);


            String keywordContent = keywordList.get(i);
            Optional<Keyword> existingKeywordOptional = keywordService.findByKeyword(keywordContent);


            if (existingKeywordOptional.isPresent()) {
                //중복되는 키워드가 있는 경우 reference 만 업데이트 해줌
                Keyword existingKeyword = existingKeywordOptional.get();
                existingKeyword.setReference(reference);

                keywordsToUpdate.add(existingKeyword); // 기존 키워드를 업데이트 대상으로 추가
            } else {
                //중복되는 키워드가 없는 경우
                Keyword keyword= new Keyword();
                keyword.setKeyword(keywordContent);
                keyword.setReference(reference);
                keyword.getCommunityPeriods().add(communityPeriod); // CommunityPeriod 추가
                keywordsToSave.add(keyword); // 새로운 키워드를 저장 대상으로 추가
            }
        }







////        // 5. 키워드에 참조 설정 및 기존 키워드 업데이트
//        for (int i = 0; i < keywordsToSave.size(); i++) {
//            Keyword keyword = keywordsToSave.get(i);
//            Reference reference = references.get(i);
//            keyword.setReference(reference);
//            keyword.getCommunityPeriods().add(communityPeriod); // CommunityPeriod 추가
//        }


        try {
            // 모든 키워드와 커뮤니티 기간 저장
            keywordService.saveAll(keywordsToSave); // 새로운 키워드 저장
            for (Keyword keyword : keywordsToUpdate) {
                keywordService.save(keyword); // 기존 키워드 업데이트
            }
            communityPeriodService.save(communityPeriod);
        } catch (DataIntegrityViolationException e) {
            log.error("중복된 키워드가 존재합니다: " + e.getMessage());
            throw new RuntimeException("중복된 키워드가 존재합니다.", e);
        }
    }







    public void quitDriver() {
        seleniumUtil.quitDriver();
    }



    /**
     나무위키 크롤링
     **/

    public String useDriverForNamuwiki(String url) {
        driver.get(url);

        Actions actions = new Actions(driver);

        // class명으로 하면 되지만, 계속 변경됨
//        WebElement webElement = driver.findElement(By.className("jM2TE0NV"));
//        actions.moveToElement(webElement).perform();
//        WebElement ul = new WebDriverWait(driver, Duration.ofSeconds(3))
//                .until(ExpectedConditions.visibilityOfElementLocated(By.className("_0SwtPj9H")));
//        System.out.println(ul.getText());

        // xpath
//        WebElement webElement = driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div/div[6]/div[4]/div"));
//        actions.moveToElement(webElement).perform();
//        WebElement ul = new WebDriverWait(driver, Duration.ofSeconds(3))
//                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[1]/div[2]/div/div[6]/div[4]/div/ul")));
//        System.out.println(ul.getText());

        // until을 통해 준비되는대로 바로 실행
        // 다만, 나무위키의 div 개수가 동적으로 변경되어서 xpath를 어떻게 할지 고민
//        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div/div[4]/div[2]/div")));
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div/div[6]/div[4]/div")));
        actions.moveToElement(webElement).perform();

//        WebElement ul = new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div/div[4]/div[2]/div/ul")));
        WebElement ul = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div/div[6]/div[4]/div/ul")));
        System.out.println(ul.getText());

        quitDriver();
        return null;
    }

}




