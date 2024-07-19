package store.itpick.backend.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Selenium {

    private WebDriver driver;
    public static String WEB_DRIVER_ID = "webdriver.chrome.driver"; // Properties 설정
    public static String WEB_DRIVER_PATH = "C:/chromedriver.exe"; // WebDriver 경로

    //생성자
    public Selenium() {
        chrome();
    }

    //ChromeDriver 연결
    private void chrome() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // webDriver 옵션 설정.
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--lang=ko");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
//        options.setCapability("ignoreProtectedModeSettings", true);

        // weDriver 생성.
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    public void useDriver(String url) {
        driver.get(url) ;
        //driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);  // 페이지 불러오는 여유시간.

//        WebElement searchLabel = driver.findElement(By.className("details-top"));

        try {
            Thread.sleep(1000);

            //html 콘솔창에 모두 띄우기
            System.out.println(driver.getPageSource());
//            System.out.println(searchLabel.getText());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        quitDriver();
    }

    private void quitDriver() {
        driver.quit(); // webDriver 종료
    }
}
