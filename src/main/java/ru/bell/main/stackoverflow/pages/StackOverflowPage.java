package ru.bell.main.stackoverflow.pages;

import cucumber.api.PendingException;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StackOverflowPage extends MainPage{
    private static final By SEARCH_AREA = By.xpath("//input[@name = 'q']");
    private static final By SEARCH_RESULTS = By.xpath("//div[@class='flush-left js-search-results']//div[@class='summary']");
    private static final By SEARCH_RESULTS_FROM_TAG = By.xpath("//div[@id='questions']//div[@class='summary']");
    private static final By SEARCH_RESULTS_LINK = By.xpath("//div[@class='flush-left js-search-results']//div[@class='summary']/div[@class='result-link']/h3/a");
    private static final By SEARCH_RESULTS_TAGGED = By.xpath("//div[@id='questions']//a[text()='webdriver']");
    private static final By QUESTION_HEADER = By.xpath("//h1[@itemprop='name']");
    private static final By MENU_TAG = By.id("nav-tags");
    private WebDriver driver;

    public StackOverflowPage(WebDriver webDriver) {
        this.driver = webDriver;
    }

    public void search(String searchQuery) {
        WebElement search = driver.findElement(SEARCH_AREA);
        search.sendKeys(searchQuery);
        search.submit();
    }

    public List<WebElement> getAllResults() {
        return driver.findElements(SEARCH_RESULTS);
    }

    public List<WebElement> getAllResultsLink() {
        return driver.findElements(SEARCH_RESULTS_LINK);
    }

    public String getNewWindowTitle(WebElement element) {
        String openInNewTab = Keys.chord(Keys.CONTROL,Keys.SHIFT,Keys.RETURN);
        element.sendKeys(openInNewTab);
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        String label = driver.findElement(QUESTION_HEADER).getText();
        this.driver.close();
        this.driver.switchTo().window(tabs.get(0));
        return label;
    }

    public MainPage getPage(String label) {
        switch (label) {
            case("Tags") : {
                this.driver.findElement(MENU_TAG).click();
                return new StackTagPage(this.driver);
            }
            default: {
                throw new PendingException(String.format("невозможно открыть страницу \'%s\' со страницы \'%s\'", label,driver.getTitle()));
            }
        }
    }

    public int getNumberOfTags(String tag) {
        return driver.findElements(SEARCH_RESULTS_TAGGED).size();
    }

    public int getNumberOfResultsFromTagSearch() {
        return driver.findElements(SEARCH_RESULTS_FROM_TAG).size();
    }
}
