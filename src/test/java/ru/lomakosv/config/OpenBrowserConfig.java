package ru.lomakosv.config;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Cookie;
import ru.lomakosv.Authentication;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static ru.lomakosv.CreateTestCaseTest.testCaseID;

public class OpenBrowserConfig {

    private static final AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);

    public static void openBaseUrlBrowser() {
        String allureTestOpsSession = Authentication.authenticate();

        open("/favicon.ico");
        Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", allureTestOpsSession);
        getWebDriver().manage().addCookie(authorizationCookie);
        open(String.format("/project/%s/test-cases/%s", authConfig.projectId(), testCaseID));
        sleep(4000);
    }
}

