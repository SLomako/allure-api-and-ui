package ru.lomakosv;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.lomakosv.authentication.Authentication;
import ru.lomakosv.helpers.Attach;
import ru.lomakosv.testcase.CreateAndDeleteTestCase;
import ru.lomakosv.testdata.TestData;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static ru.lomakosv.spec.Specification.requestSpec;
import static ru.lomakosv.spec.Specification.responseSpec;
import static ru.lomakosv.config.OpenBrowserConfig.openBaseUrlBrowser;
import static ru.lomakosv.testdata.TestData.jsonStringCreateTestCaseRequest;
import static ru.lomakosv.testdata.TestData.jsonStringEditingRequest;

@Owner("Slomako")
@DisplayName("Тесты на AllureTestOpsTest")
public class AllureTestOpsTest extends TestBase {

    private final CreateAndDeleteTestCase createTestCaseTest = new CreateAndDeleteTestCase();
    private String testCaseID;

    @BeforeEach
    void createTestCase() {
        Authentication.getInstance().authenticate();
        createTestCaseTest.createNewTestCase();
        this.testCaseID = CreateAndDeleteTestCase.getTestIdCaseId();
    }

    @AfterEach
    void tearDown() {
        createTestCaseTest.deleteTestCase();
    }

    @DisplayName("Добавление шагов и редактирование")
    @Test
    void testAddEditSteps() {

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
    }

    @Test
    @DisplayName("Добавление комментария к тест кейсу")
    void testAddCommentToTestCase() {

        String jsonStringCommentRequest = String.format("{\"testCaseId\":%s,\"body\":\"%s\"}",
                CreateAndDeleteTestCase.getTestIdCaseId(), TestData.commentProject); //todo убрать pojo

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
    }
}
