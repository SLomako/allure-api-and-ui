package ru.lomakosv.testdata;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import ru.lomakosv.config.AuthConfig;
import ru.lomakosv.config.Project;
import ru.lomakosv.models.CreateTestCaseBody;

import java.io.IOException;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static ru.lomakosv.config.AuthConfig.*;


public class TestBase {

    public static String allureTestOpsSession;
    protected static CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
    static AuthConfig authConfig = new AuthConfig();

    @BeforeAll
    static void setIUp() throws IOException {

        authConfig.getAuthConfig();

        Configuration.browser = Project.config.browser();
        Configuration.browserVersion = Project.config.browserVersion();
        Configuration.browserSize = Project.config.browserSize();
        Configuration.remote = Project.config.remoteDriverUrl();
        Configuration.baseUrl = "https://allure.autotests.cloud";
        RestAssured.baseURI = "https://allure.autotests.cloud";

        step("Авторизация", () -> {
        allureTestOpsSession = given()
                .header("X-XSRF-TOKEN", xsrfToken)
                .header("Cookie", "XSRF-TOKEN=" + xsrfToken)
                .formParam("username", username)
                .formParam("password", password)
                .when()
                .post("/api/login/system")
                .then()
                .statusCode(200)
                .extract().response()
                .getCookie("ALLURE_TESTOPS_SESSION");});

        testCaseBody.setName(TestData.testCaseName);

    }
}
