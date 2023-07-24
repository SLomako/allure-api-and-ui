package ru.lomakosv;

import org.aeonbits.owner.ConfigFactory;
import ru.lomakosv.config.AuthConfig;

import static io.restassured.RestAssured.given;

public class Authentication {

    public static String allureTestOpsSession;
    static AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);

    public static String authenticate() {

        return allureTestOpsSession = given()
                .header("X-XSRF-TOKEN", authConfig.xsrfToken())
                .header("Cookie", "XSRF-TOKEN=" + authConfig.xsrfToken())
                .formParam("username", authConfig.username())
                .formParam("password", authConfig.password())
                .when()
                .post("/api/login/system")
                .then()
                .statusCode(200)
                .extract().response()
                .getCookie("ALLURE_TESTOPS_SESSION");
    }
}
