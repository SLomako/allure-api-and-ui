package ru.lomakosv;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;
import ru.lomakosv.config.AuthConfig;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;
import static ru.lomakosv.helpers.CustomApiListener.withCustomTemplates;

public class Specification {

    private static final AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);

    public static RequestSpecification requestSpec =
            with()
                    .header("Authorization", "Api-Token " + authConfig.token())
                    .cookie("ALLURE_TESTOPS_SESSION", Authentication.authenticate())
                    .baseUri("https://allure.autotests.cloud")
                    .basePath("/api/rs")
                    .log().all()
                    .filter(withCustomTemplates())
                    .contentType(JSON);

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder().
            log(STATUS).
            log(BODY).
            build();

}

