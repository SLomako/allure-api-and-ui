package ru.lomakosv;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.lomakosv.helpers.Attach;
import ru.lomakosv.testdata.TestData;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static ru.lomakosv.Specification.requestSpec;
import static ru.lomakosv.Specification.responseSpec;
import static ru.lomakosv.config.OpenBrowserConfig.openBaseUrlBrowser;
import static ru.lomakosv.testdata.TestData.jsonStringCreateTestCaseRequest;
import static ru.lomakosv.testdata.TestData.jsonStringEditingRequest;

@Owner("Slomako")
@DisplayName("Тесты на AllureTestOpsTest")
public class AllureTestOpsTest extends TestBase {

    CreateTestCaseTest createTestCaseTest = new CreateTestCaseTest();
    DeleteTestCaseTest deleteTestCaseTest = new DeleteTestCaseTest();

    @DisplayName("Добавление шагов и редактирование")
    @Test
    void testAddEditSteps() {
        Authentication.authenticate();
        createTestCaseTest.testCreateNewTestCase();

        step("Добавление шагов в тест-кейс", () ->
                given(requestSpec)
                        .body(jsonStringCreateTestCaseRequest)
                        .when()
                        .post(format("/testcase/%s/scenario", testCaseID))
                        .then()
                        .spec(responseSpec)
                        .statusCode(200)
        );

        step("Открытие страницы тест-кейса в браузере и создание скриншота", () ->
                openBaseUrlBrowser());

        Attach.screenshotAs("Screenshot step");

        step("Изменение порядка шагов в тест-кейсе", () ->
                given(requestSpec)
                        .body(jsonStringEditingRequest)
                        .when()
                        .post(format("/testcase/%s/scenario", testCaseID))
                        .then()
                        .spec(responseSpec)
                        .statusCode(200));

        step("Открытие страницы тест-кейса в браузере и создание скриншота", () ->
                openBaseUrlBrowser());

        Attach.screenshotAs("Screenshot step");
        deleteTestCaseTest.testDeleteTestCase();
    }

    @Test
    @DisplayName("Добавление комментария к тест кейсу")
    void testAddCommentToTestCase() {

        String jsonStringCommentRequest = String.format("{\"testCaseId\":%s,\"body\":\"%s\"}",
                testCaseID, TestData.commentProject); //todo убрать pojo

        step("Добавляем комментарий к тест кейсу", () ->
                given(requestSpec)
                        .body(jsonStringCommentRequest)
                        .when()
                        .post("/comment")
                        .then()
                        .spec(responseSpec)
                        .statusCode(200));

        step("Открытие страницы тест-кейса в браузере и создание скриншота", () ->
                openBaseUrlBrowser());

        Attach.screenshotAs("Screenshot step");
        deleteTestCaseTest.testDeleteTestCase();
    }
}
