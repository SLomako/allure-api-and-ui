package ru.lomakosv;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import ru.lomakosv.config.WebConfig;
import ru.lomakosv.models.CreateTestCaseBody;


public class TestBase {

    public static String testCaseID;
    protected static CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
    protected static WebConfig webConfig = ConfigFactory.create(WebConfig.class);

    @BeforeAll
    static void setIUp() {

        Configuration.browser = webConfig.browser();
        Configuration.browserVersion = webConfig.browserVersion();
        Configuration.browserSize = webConfig.browserSize();
        Configuration.remote = webConfig.remoteUrl();
        Configuration.baseUrl = "https://allure.autotests.cloud";
        RestAssured.baseURI = "https://allure.autotests.cloud";

    }
}
