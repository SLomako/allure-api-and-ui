package ru.lomakosv.testdata;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import ru.lomakosv.models.CreateTestCaseBody;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;


public class TestBase {

    public static String allureTestOpsSession;
    public static CreateTestCaseBody testCaseBody = new CreateTestCaseBody();


    @BeforeAll
    static void setIUp() {
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";
        Configuration.baseUrl = "https://allure.autotests.cloud";
        RestAssured.baseURI = "https://allure.autotests.cloud";

        step("Авторизация", () -> {
        allureTestOpsSession = given()
                .header("X-XSRF-TOKEN", TestData.xsrfToken)
                .header("Cookie", "XSRF-TOKEN=" + TestData.xsrfToken)
                .formParam("username", TestData.USERNAME)
                .formParam("password", TestData.PASSWORD)
                .when()
                .post("/api/login/system")
                .then()
                .statusCode(200)
                .extract().response()
                .getCookie("ALLURE_TESTOPS_SESSION");});

        testCaseBody.setName(TestData.testCaseName);

    }
}
