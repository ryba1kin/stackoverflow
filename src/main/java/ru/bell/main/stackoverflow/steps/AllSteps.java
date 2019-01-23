package ru.bell.main.stackoverflow.steps;

import cucumber.api.PendingException;
import cucumber.api.java.*;
import cucumber.api.java.ru.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.bell.main.stackoverflow.DriverInit;
import ru.bell.main.stackoverflow.pages.StackOverflowPage;
import org.junit.Assert;
import ru.bell.main.stackoverflow.pages.StackTagPage;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class AllSteps {


    private static final String PROPERTIES_FILE_NAME = "tests.properties";
    private Properties properties;
    private WebDriver webDriver;
    private StackOverflowPage page;
    private StackTagPage tagsPage;

    @Когда("^пользователь переходит на внешний ресурс: \"([^\"]*)\"$")
    public void goToUri(String uri) {
        try {
            this.webDriver.navigate().to(uri);
            this.page = new StackOverflowPage(this.webDriver);
        } catch (Exception exception) {
            throw new PendingException(exception.getMessage());
        }
    }

    @Когда("^в строку поиска статей вводит значение \"([^\"]*)\"$")
    public void search(String searchQuery) {
        this.page.search(searchQuery);
    }

    @Когда("^в строку поиска тегов вводит значение \"([^\"]*)\"$")
    public void searchTags(String searchQuery) {
        this.tagsPage.search(searchQuery);
    }

    @Тогда("^проверяет, что в каждом результате представлено слово \"([^\"]*)\"$")
    public void assertWordInEachResult(String expected) {
        List<WebElement> searchResults = this.page.getAllResults();
        searchResults.forEach((element) -> Assert.assertTrue(
                String.format("Результат \"%s\" не содержит \"%s\"", element, expected),
                element.getText().toLowerCase().contains(expected.toLowerCase())
        ));
    }

    @Тогда("^в каждом обсуждении из выборки заголовок обсуждения соответствует теме$")
    public void assertHeader() {
        String currentPage = this.webDriver.getWindowHandle();
        List<WebElement> results = this.page.getAllResultsLink();
        results.forEach(webElement -> {
                    String header = webElement.getText().replace("Q: ", "");
                    String title = page.getNewWindowTitle(webElement);
                    Assert.assertTrue(
                            String.format("заголовок обсуждения: \'%s\' не соответствует теме: \'%s\'", header, title),
                            header.equalsIgnoreCase(title)
                    );
                }
        );
    }

    @Когда("^переходит в раздел \"([^\"]*)\"$")
    public void goToSection(String label) {
        tagsPage = (StackTagPage) page.getPage(label);
    }

    @Тогда("^проверяет, что в результате присутствуют элементы содержащее слово \"([^\"]*)\"$")
    public void assertTags(String label) {
        List<WebElement> tags = tagsPage.getAllTagsResults();
        for (WebElement tag : tags) {
            if (tag.getText().contains(label.toLowerCase())) return;
        }
        throw new PendingException(String.format("в результате нет элементов содержащих слово:\'%s\'", label));
    }

    @Когда("^пользователь находит в результатах тэг по точному совпадению поискового запроса и переходит в него$")
    public void getAbsolutelyEqualsTag() {
        for (WebElement tag : tagsPage.getAllTagsResults()) {
            if (tag.getText().equalsIgnoreCase(tagsPage.getSearchCondition())) {
                tag.click();
                return;
            }
        }
        throw new PendingException(String.format("нет тега :\'%s\'", tagsPage.getSearchCondition()));
    }

    @Тогда("^проверяет, что после перехода отображаются обсуждения помеченные тэгом \"([^\"]*)\"$")
    public void assertTagInQuestions(String tag) {
        Assert.assertEquals(
                page.getNumberOfResultsFromTagSearch(),
                page.getNumberOfTags(tag)
        );
    }


    @Before
    public void beforeScenario() {
        try {
            this.properties = new Properties();
            this.properties.load(this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME));
            this.webDriver = DriverInit.init("chrome", this.properties);
        } catch (Exception ex) {
            throw new PendingException("невозможно прочитать свойства из файла: " + PROPERTIES_FILE_NAME);
        }
    }

    @After
    public void afterScenario() {
        this.webDriver.quit();
    }
}
