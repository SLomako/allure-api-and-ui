package ru.lomakosv;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import ru.lomakosv.helpers.Attach;
import ru.lomakosv.models.CreateTestCaseResponse;
import org.junit.jupiter.api.Test;
import ru.lomakosv.testdata.TestBase;
import ru.lomakosv.testdata.TestData;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static io.restassured.RestAssured.given;
import static ru.lomakosv.Specification.requestSpec;
import static ru.lomakosv.Specification.responseSpec;
import static ru.lomakosv.config.ConfigBrowser.openBaseUrlBrowser;
import static ru.lomakosv.testdata.TestData.*;


@DisplayName("Тест на AllureTestOpsTest")
public class AllureTestOpsTest extends TestBase {

    public static String testCaseID;


    @Owner("Slomako")
    @DisplayName("Создрание тест кейса, добавление и редактирование шагов, добавление комментария и удаление созданного тест кейса")
    @Test
    void testCreateAndDeleteTestCase() {


        CreateTestCaseResponse testCaseResponse = step("Создаем тест кейс", () -> given(requestSpec)
                .body(testCaseBody)
                .queryParam("projectId", TestData.projectId)
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

        String jsonStringDeleteTestCaseRequest = String.format("{\"selection\":{\"inverted\":false,\"groupsInclude\":[]," +
                "\"groupsExclude\":[],\"leafsInclude\":[%s],\"leafsExclude\":[],\"kind\":\"TreeSelectionDto\"," +
                "\"projectId\":%s,\"path\":[]}}", testCaseID, TestData.projectId); //todo убрать в TestBase

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

            step("Сообщение Тест кейс удален", () -> {
                assertThat(messageDelete).isEqualTo("Test case was deleted");
            });
            Attach.screenshotAs("Screenshot step");
        });


    }


}
