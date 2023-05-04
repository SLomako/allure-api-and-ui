package ru.lomakosv.testdata;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import ru.lomakosv.config.AuthConfig;
import ru.lomakosv.config.Project;
import ru.lomakosv.helpers.Attach;
import ru.lomakosv.models.CreateTestCaseBody;
import ru.lomakosv.models.CreateTestCaseResponse;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.lomakosv.Specification.requestSpec;
import static ru.lomakosv.Specification.responseSpec;
import static ru.lomakosv.config.AuthConfig.*;
import static ru.lomakosv.config.ConfigBrowser.openBaseUrlBrowser;

public class TestBase {

    public static String allureTestOpsSession;
    protected static CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
    static AuthConfig authConfig = new AuthConfig();
    public static String testCaseID;

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
                    .getCookie("ALLURE_TESTOPS_SESSION");
        });

        testCaseBody.setName(TestData.testCaseName);
    }

    @Owner("Slomako")
    @DisplayName("Создание тест кейса")
    @BeforeEach
    void testCreateTestCase() {

        CreateTestCaseResponse testCaseResponse = step("Создаем тест кейс", () -> given(requestSpec)
                .body(testCaseBody)
                .queryParam("projectId", projectId)
                .when()
                .post("/testcasetree/leaf")
                .then()
                .spec(responseSpec)
                .statusCode(200).extract().as(CreateTestCaseResponse.class));

        step("Проверка имени тест кейса", () -> {
            assertThat(testCaseResponse.getName()).isEqualTo(TestData.testCaseName);
        });

        step("Проверка, что тест не автоматизирован", () -> {
            assertThat(testCaseResponse.getAutomated()).isEqualTo(false);
        });

        step("Проверка статуса", () -> {
            assertThat(testCaseResponse.getStatusName()).isEqualTo("Draft");
        });

        testCaseID = testCaseResponse.getId();
    }

    @AfterEach
    @Owner("Slomako")
    @DisplayName("Удаление созданного тест кейса")
    void testDeleteTEstCase() {

        String jsonStringDeleteTestCaseRequest = String.format("{\"selection\":{\"inverted\":false,\"groupsInclude\":[]," +
                "\"groupsExclude\":[],\"leafsInclude\":[%s],\"leafsExclude\":[],\"kind\":\"TreeSelectionDto\"," +
                "\"projectId\":%s,\"path\":[]}}", testCaseID, projectId); //todo убрать в TestBase

        step("Удаление созданного тейст кейса", () -> {
            given(requestSpec)
                    .body(jsonStringDeleteTestCaseRequest)
                    .when()
                    .post("/testcase/bulk/remove")
                    .then()
                    .spec(responseSpec)
                    .statusCode(204);
        });

        step("Открываем браузер и проверяем, что тест кейc удален", () -> {
            openBaseUrlBrowser();
            String messageDelete = $("[class='Alert Alert_status_failed Alert_center']").innerText();

            step("Сообщение - тест кейс удален", () -> {
                assertThat(messageDelete).isEqualTo("Test case was deleted");
            });
            Attach.screenshotAs("Screenshot step");
        });
    }

}
