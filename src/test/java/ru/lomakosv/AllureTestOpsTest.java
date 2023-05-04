package ru.lomakosv;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import ru.lomakosv.helpers.Attach;
import org.junit.jupiter.api.Test;
import ru.lomakosv.models.CreateTestCaseResponse;
import ru.lomakosv.testdata.TestData;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.lomakosv.Specification.requestSpec;
import static ru.lomakosv.Specification.responseSpec;
import static ru.lomakosv.config.AuthConfig.projectId;
import static ru.lomakosv.config.ConfigBrowser.openBaseUrlBrowser;
import static ru.lomakosv.testdata.TestData.*;


@DisplayName("Тест на AllureTestOpsTest")
public class AllureTestOpsTest extends TestBase {

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

    @Owner("Slomako")
    @DisplayName("Добавление шагов и их редактирование")
    @Test
    void testAddingSteps() {

        step("Добавляем шаги в созданный тест кейс", () -> {
            given(requestSpec)
                    .body(jsonStringCreateTestCaseRequest)
                    .when()
                    .post(format("/testcase/%s/scenario", testCaseID))
                    .then()
                    .spec(responseSpec)
                    .statusCode(200);
        });

        step("Открываем браузер и делаем скриншот результата добавление шагов в тест кейс", () -> {
            openBaseUrlBrowser();
            Attach.screenshotAs("Screenshot step");
        });

        step("Меняем местами название шагов", () -> {
            given(requestSpec)
                    .body(jsonStringEditingRequest)
                    .when()
                    .post(format("/testcase/%s/scenario", testCaseID))
                    .then()
                    .spec(responseSpec)
                    .statusCode(200);
        });

        step("Открываем браузер и проверяем, что шаги поменялись местами", () -> {
            openBaseUrlBrowser();
            Attach.screenshotAs("Screenshot step");
        });
    }


    @Owner("Slomako")
    @DisplayName("Добавление комментария")
    @Test
    void testAddComment() {

        String jsonStringCommentRequest = String.format("{\"testCaseId\":%s,\"body\":\"%s\"}",
                testCaseID, TestData.commentProject); //todo убрать в TestBse

        step("Добавляем комментарий к тест кейсу", () -> {
            given(requestSpec)
                    .body(jsonStringCommentRequest)
                    .when()
                    .post("/comment")
                    .then()
                    .spec(responseSpec)
                    .statusCode(200);
        });

        step("Открываем браузер и проверяем, что комментарий добавлен", () -> {
            openBaseUrlBrowser();
            Attach.screenshotAs("Screenshot step");
        });
    }
}
