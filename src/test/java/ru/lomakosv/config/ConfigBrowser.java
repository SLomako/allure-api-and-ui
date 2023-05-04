package ru.lomakosv.config;

import org.openqa.selenium.Cookie;
import ru.lomakosv.testdata.TestBase;
import ru.lomakosv.testdata.TestData;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static ru.lomakosv.AllureTestOpsTest.testCaseID;

public class ConfigBrowser extends TestBase {

    public static void openBaseUrlBrowser() {
        open("/favicon.ico");
        Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", allureTestOpsSession);
        getWebDriver().manage().addCookie(authorizationCookie);
        open(String.format("/project/%s/test-cases/%s", TestData.projectId, testCaseID));
        sleep(4000);
    }
}
