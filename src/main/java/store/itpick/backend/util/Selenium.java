package store.itpick.backend.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class Selenium {

    private WebDriver driver;
    public static String WEB_DRIVER_ID = "webdriver.chrome.driver"; // Properties 설정
    public static String WEB_DRIVER_PATH = "C:/chromedriver.exe"; // WebDriver 경로
//    public static String WEB_DRIVER_PATH = "/Users/minseok/Documents/3rd/chromedriver"; // WebDriver 경로


    // 생성자
    public Selenium() {
        chrome();
    }

    // ChromeDriver 연결
    private void chrome() {
        // 웹드라이버 ID 및 경로 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // 웹드라이버 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 브라우저 UI 없이 실행
        options.addArguments("--lang=ko");  // 브라우저 언어를 한국어로 설정
        /*
            @ 샌드박스 비활성화
            외부에서 바이러스 감염이나 해킹 등의 도구로 사용이 의심되는 파일을 받게 된 경우,
            샌드박스 기능을 가진 솔루션은 해당 자료를 독립된 가상 공간으로 옮기고, 거기서 보안을 위한 검사를 수행
         */
        options.addArguments("--no-sandbox");   // 샌드박스 비활성화 -> 리소스 절약
        options.addArguments("--disable-dev-shm-usage");    // /dev/shm 사용 비활성화
        options.addArguments("--disable-gpu");  //  GPU 가속 비활성화
//        options.setCapability("ignoreProtectedModeSettings", true);   // 보호 모드 설정 무시

        // 웹드라이버 생성
        driver = new ChromeDriver(options);

        // 페이지 로드 타임아웃 설정 (30초)
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    public String useDriver(String url, String cssSelector) {
        driver.get(url);
//        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);  // 페이지 불러오는 여유시간.

        List<WebElement> webElementList = driver.findElements(By.cssSelector(cssSelector));

        try {
            Thread.sleep(1000);

//            // html 콘솔창에 모두 띄우기
//            System.out.println(driver.getPageSource());

            // 크롤링 결과 반환
            StringBuilder sb = new StringBuilder();
            int count = 1;
            for (WebElement webElement : webElementList) {
                sb.append(count++).append(": ").append(webElement.getText()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        quitDriver();
        return null;
    }

    private void quitDriver() {
        driver.quit(); // webDriver 종료
    }
}
