package ru.lomakosv.testcase;

import io.qameta.allure.Owner;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import ru.lomakosv.TestBase;
import ru.lomakosv.config.AuthConfig;
import ru.lomakosv.config.OpenBrowserConfig;
import ru.lomakosv.helpers.Attach;
import ru.lomakosv.models.CreateTestCaseBody;
import ru.lomakosv.models.CreateTestCaseResponse;
import ru.lomakosv.testdata.TestData;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.lomakosv.spec.Specification.requestSpec;
import static ru.lomakosv.spec.Specification.responseSpec;

@Owner("SLomako")
public class CreateAndDeleteTestCase extends TestBase {

    private static String testCaseID;
    private final AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);
    private final CreateTestCaseBody testCaseBody = new CreateTestCaseBody();

    @DisplayName("Создание нового тест кейса")
    public void createNewTestCase() {
        testCaseBody.setName(TestData.testCaseName);

        CreateTestCaseResponse testCaseResponse = step("Отправляем запрос на создание тест кейса", () -> given(requestSpec)
                .body(testCaseBody)
                .queryParam("projectId", authConfig.projectId())
                .when()
                .post("/testcasetree/leaf")
                .then()
                .spec(responseSpec)
                .statusCode(200).extract().as(CreateTestCaseResponse.class));

        step("Проверяем, что имя созданного тест кейса соответствует заданному", () ->
                assertThat(testCaseResponse.getName()).isEqualTo(TestData.testCaseName));

        step("Проверяем, что созданный тест кейс не автоматизирован", () ->
                assertThat(testCaseResponse.getAutomated()).isEqualTo(false));

        step("Проверяем, что статус созданного тест кейса равен 'Draft'", () ->
                assertThat(testCaseResponse.getStatusName()).isEqualTo("Draft"));

        testCaseID = testCaseResponse.getId();
    }

    public static String getTestIdCaseId() {
        return testCaseID;
    }

    @DisplayName("Удаление созданного тест кейса")
    public void deleteTestCase() {

        String jsonStringDeleteTestCaseRequest = String.format("{\"selection\":{\"inverted\":false,\"groupsInclude\":[]," +
                "\"groupsExclude\":[],\"leafsInclude\":[%s],\"leafsExclude\":[],\"kind\":\"TreeSelectionDto\"," +
                "\"projectId\":%s,\"path\":[]}}", getTestIdCaseId(), authConfig.projectId()); //todo убрать в pojo

        step("Удаление созданного тейст кейса", () ->
                given(requestSpec)
                        .body(jsonStringDeleteTestCaseRequest)
                        .when()
                        .post("/testcase/bulk/remove")
                        .then()
                        .spec(responseSpec)
                        .statusCode(204));

        step("Проверка отсутствия тест кейса в списке", OpenBrowserConfig::openBaseUrlBrowser);

        String messageDelete = $("[class='Alert Alert_status_failed Alert_center']").innerText();

        step("Проверка сообщения об успешном удалении тест кейса", () ->
                assertThat(messageDelete).isEqualTo("Test case was deleted"));
        Attach.screenshotAs("Screenshot step");
    }
}
