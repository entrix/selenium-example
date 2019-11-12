package li.entrix.translate;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class Main {

    public static String WEBDRIVER_HOME = "webdriver.chrome.driver";
    public static String WEBDRIVER_LOCATION = "webdriver/chromedriver.exe";

    private static void translateFile(LanguageCode sourceCode, LanguageCode targetCode, String filename) throws IOException {
        try (Translator translator = new Translator(sourceCode, targetCode)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename
                        .concat(sourceCode.getCode())
                        .concat(".")
                        .concat(targetCode.getCode())
                        .concat(".out")))) {
                    String nextLine = null;
                    StringBuilder inputText = new StringBuilder();

                    for (int i = 0; (nextLine = reader.readLine()) != null; ++i) {
                        if (inputText.length() >= 4900) {
                            writer.write(translator.translate(inputText.toString()));
                            inputText = new StringBuilder();
                        }
                        inputText.append(nextLine).append("\n");
                    }
                    if (inputText.length() > 0) {
                        writer.write(translator.translate(inputText.toString()));
                    }
                }
            }
        }
    }

    private static void printUsage() {
        log.info("gwt-selenium [-usage] -webdriver-home <Web Driver Home> -webdriver-location=<Web Driver Location> [-f -t <filename>]+");
        log.info("Order of arguments doesn't affect execution");
    }
    public static void main(String[] args) {
        try {
            int key = 0;
            LanguageCode sourceCode = null, targetCode = null;
            String filename = null;

            if (args.length == 0) {
                printUsage();
                return;
            }

            for (int i = 0; i < args.length; ++i) {
                if (args[i].equals("-usage")) {
                    printUsage();
                    return;
                } else if (args[i].equals("-webdriver-home") && i + 1 < args.length) {
                    WEBDRIVER_LOCATION = args[i + 1];
                    i++;
                } else if (args[i].equals("-webdriver-location") && i + 1 < args.length) {
                    WEBDRIVER_LOCATION = args[i + 1];
                    i++;
                } else if (args[i].equals("-f") && i + 1 < args.length) {
                    sourceCode = LanguageCode.valueOf(args[i + 1]);
                    key |= 0x1;
                    i++;
                } else if (args[i].equals("-t") && i + 1 < args.length) {
                    targetCode = LanguageCode.valueOf(args[i + 1]);
                    key |= 0x2;
                    i++;
                } else {
                    key |= 0x4;
                    filename = args[i];
                }

                if (key == 0x7) {
                    translateFile(sourceCode, targetCode, filename);
                }
            }
        } catch (Exception ex) {
            printUsage();
            log.warn("Something went wrong...", ex);
        }
    }
}
