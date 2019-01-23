package ru.bell.main.stackoverflow.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class StackTagPage extends MainPage{
    private static final By TAG_FILTER = By.id("tagfilter");
    private static final By TAG_RESULT = By.xpath("//div[@id='tags_list']//a[@class='post-tag']");
    private WebDriver driver;

    public StackTagPage(WebDriver webDriver) {
        this.driver = webDriver;
    }

    public void search(String searchQuery) {
        WebElement search = driver.findElement(TAG_FILTER);
        search.sendKeys(searchQuery);
        WebDriverWait wait = new WebDriverWait(this.driver, 3);
        wait.until(e -> e.findElement(TAG_RESULT).getText().contains(searchQuery));
    }

    public List<WebElement> getAllTagsResults() {
        return driver.findElements(TAG_RESULT);
    }

    public List<WebElement> getAllResults() {
        return null;
    }

    public String getSearchCondition() {
        return driver.findElement(TAG_FILTER).getAttribute("value").trim();
    }
}
