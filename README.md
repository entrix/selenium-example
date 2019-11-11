# selenium-example
A project of selenium using ChromeWebDriver to translate text via Google Translate


## Prerequisites ##

* Have [java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) installed
* Have [maven](http://maven.apache.org/) installed


## Execute automation tests ##

Supported 
```bash
mvn clean package
```

How to run:
````text
gwt-selenium [-usage] -webdriver-home <Web Driver Home> -webdriver-location=<Web Driver Location> [-f -t <filename>]+
Order of arguments doesn't affect execution
````

And then start translation from chinese to english? for example:
```bash
 java -Dfile.encoding=UTF-8 -jar gwt-selenium.jar -webdriver-home webdriver.chrome.driver -webdriver-location webdriver/chromedriver.exe -f CN -t EN file.txt
```

Translated text will be in the same folder:

```text
file.txtzh-CN.en.out
```

## LICENSE ##

[MIT License](https://raw.githubusercontent.com/leftstick/selenium-example/master/LICENSE)
