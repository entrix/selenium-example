package li.entrix.translate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;
import java.util.stream.Collectors;

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

            Thread.sleep(2000);

            try {
                output = driver.findElementByXPath("//span[@lang=\"en\"]");
            }
            catch (Exception ex) {
                log.error("cannot get element at //span[@lang=\"en\"]");
                if (isError() && StringUtils.countMatches(text, '\n') > 0) {
                    return divideAndTranslateAgain(text);
                } else {
                    return text;
                }
            }

            return output.getText();
        } catch (Exception ex) {
            log.error("somrething wrong with translation: ", ex);
        }
        return null;
    }

    private String divideAndTranslateAgain(String text) {
        String[] strings = text.split("\n");

        String firstPart = Arrays.asList(strings).stream()
                .limit(strings.length / 2)
                .collect(Collectors.joining("\n"));
        String secondPart = Arrays.asList(strings).stream()
                .skip(strings.length / 2)
                .collect(Collectors.joining("\n"));

        return translate(firstPart)
                .concat("\n")
                .concat(translate(secondPart))
                .concat("\n")
                .replace("\n\n", "\n");
    }

    private boolean isError() {
        try {
            WebElement webElement = driver.findElementByXPath("//span[@class=\"tlid-result-error\"]");
            if (webElement.getText() != null) {
                log.error(webElement.getText());
                return webElement.getText() != null;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public void close() {
        driver.quit();
    }
}
