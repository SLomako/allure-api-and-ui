package ru.lomakosv.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;
import ru.lomakosv.authentication.Authentication;
import ru.lomakosv.config.AuthConfig;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;
import static ru.lomakosv.helpers.CustomApiListener.withCustomTemplates;

public class Specification {

    private static final AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);
    private static final String BASE_URI = "https://allure.autotests.cloud";
    private static final String BASE_PATH = "/api/rs";

    public static RequestSpecification requestSpec =
            with()
                    .header("Authorization", "Api-Token " + authConfig.token())
                    .cookie("ALLURE_TESTOPS_SESSION", Authentication.getInstance().authenticate())
                    .baseUri(BASE_URI)
                    .basePath(BASE_PATH)
                    .log().all()
                    .filter(withCustomTemplates())
                    .contentType(JSON);

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .build();
}

