package ru.lomakosv;

import io.qameta.allure.Owner;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.lomakosv.config.AuthConfig;
import ru.lomakosv.models.CreateTestCaseBody;
import ru.lomakosv.models.CreateTestCaseResponse;
import ru.lomakosv.testdata.TestData;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.lomakosv.Specification.requestSpec;
import static ru.lomakosv.Specification.responseSpec;

@Owner("SLomako")
public class CreateTestCaseTest extends TestBase{

    static AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);
    protected static CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
    public static String testCaseID;

    @Test
    @DisplayName("Создание нового тест кейса")
    void testCreateNewTestCase() {
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
}
