package ru.lomakosv;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import ru.lomakosv.config.WebConfig;

public class TestBase {

    protected static final WebConfig webConfig = ConfigFactory.create(WebConfig.class);

    @BeforeAll
    static void setUp() {

        Configuration.browser = webConfig.browser();
        Configuration.browserVersion = webConfig.browserVersion();
        Configuration.browserSize = webConfig.browserSize();
        Configuration.remote = webConfig.remoteUrl();
        Configuration.baseUrl = "https://allure.autotests.cloud";

        RestAssured.baseURI = "https://allure.autotests.cloud";
    }
}
