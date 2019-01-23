package ru.bell.main.stackoverflow;

import cucumber.api.PendingException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Properties;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DriverInit {
    private DriverInit(){}

    synchronized public static WebDriver init(String browserLabel, Properties properties) {
        WebDriver webDriver;
        switch (browserLabel) {
            case ("chrome"): {
                System.setProperty("webdriver.chrome.driver", properties.getProperty("webdriver.chrome.driver"));
                webDriver = new ChromeDriver();
                break;
            }
            case ("firefox"): {
                System.setProperty("webdriver.gecko.driver", properties.getProperty("webdriver.gecko.driver"));
                webDriver = new FirefoxDriver();
                break;
            }
            default: {
                throw new PendingException("невозможно запустить браузер: " + browserLabel);
            }
        }
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(5, SECONDS);
        return webDriver;
    }
}
