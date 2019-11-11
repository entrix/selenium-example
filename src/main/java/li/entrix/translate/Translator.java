package li.entrix.translate;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static li.entrix.translate.Main.WEBDRIVER_HOME;
import static li.entrix.translate.Main.WEBDRIVER_LOCATION;

@Slf4j
public class Translator implements AutoCloseable {

    private ChromeDriver driver;

    private LanguageCode sourceLangCode;
    private LanguageCode targetLangCode;

    private void init() {
        System.setProperty(WEBDRIVER_HOME, WEBDRIVER_LOCATION);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-web-security");
        driver = new ChromeDriver();
    }

    public Translator(LanguageCode sourceLangCode, LanguageCode targetLangCode) {
        if (sourceLangCode == null || targetLangCode == null) {
            throw  new RuntimeException("Unsupported language code");
        }
        this.sourceLangCode = sourceLangCode;
        this.targetLangCode = targetLangCode;
        init();
    }

    public String translate(String text) {
        try {
            driver.get(String.format("https://translate.google.com/#view=home&op=translate&sl=%s&tl=%s",
                    sourceLangCode.getCode(), targetLangCode.getCode()));
            WebElement input = driver.findElementById("source");
            input.sendKeys(text);

            WebElement output = null;

            Thread.sleep(10000);

            try {
                output = driver.findElementByXPath("//span[@lang=\"en\"]");
            }
            catch (Exception ex) {
                output = driver.findElementByXPath("//span[@lang=\"en\"]/span");
            }

            return output.getText();
        } catch (Exception ex) {
            log.error("somrething wrong with translation: ", ex);
        }
        return null;
    }

    public void close() {
        driver.quit();
    }
}
