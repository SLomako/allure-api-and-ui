package ru.lomakosv;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import ru.lomakosv.helpers.Attach;
import org.junit.jupiter.api.Test;
import ru.lomakosv.testdata.TestBase;
import ru.lomakosv.testdata.TestData;

import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static io.restassured.RestAssured.given;
import static ru.lomakosv.Specification.requestSpec;
import static ru.lomakosv.Specification.responseSpec;
import static ru.lomakosv.config.ConfigBrowser.openBaseUrlBrowser;
import static ru.lomakosv.testdata.TestData.*;


@DisplayName("Тест на AllureTestOpsTest")
public class AllureTestOpsTest extends TestBase {

    @Owner("Slomako")
    @DisplayName("Добавление шагов и редактирование")
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
