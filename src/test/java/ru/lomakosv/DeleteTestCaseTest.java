package ru.lomakosv;

import io.qameta.allure.Owner;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import ru.lomakosv.config.AuthConfig;
import ru.lomakosv.config.OpenBrowserConfig;
import ru.lomakosv.helpers.Attach;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.lomakosv.Specification.requestSpec;
import static ru.lomakosv.Specification.responseSpec;

@Owner("Slomako")
public class DeleteTestCaseTest extends TestBase {

    AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);

    @DisplayName("Удаление созданного тест кейса")
    void testDeleteTestCase() {

        String testCaseID = CreateTestCaseTest.testCaseID;

        String jsonStringDeleteTestCaseRequest = String.format("{\"selection\":{\"inverted\":false,\"groupsInclude\":[]," +
                "\"groupsExclude\":[],\"leafsInclude\":[%s],\"leafsExclude\":[],\"kind\":\"TreeSelectionDto\"," +
                "\"projectId\":%s,\"path\":[]}}", testCaseID, authConfig.projectId()); //todo убрать в pojo

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
