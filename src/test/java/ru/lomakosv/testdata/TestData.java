package ru.lomakosv.testdata;

import com.github.javafaker.Faker;
import org.checkerframework.checker.units.qual.A;
import ru.lomakosv.AllureTestOpsTest;
import ru.lomakosv.models.CreateTestCaseBody;
import ru.lomakosv.models.CreateTestCaseResponse;
import ru.lomakosv.testdata.TestBase;
import static ru.lomakosv.AllureTestOpsTest.testCaseID;


public class TestData {

    static Faker faker = new Faker();
    public final static String
            testCaseName = faker.rockBand().name(),
            nameStepTestCaseOne = faker.funnyName().name(),
            nameStepTestCaseTwo = faker.artist().name(),
            commentProject = "всё отлично";

    public final static String
            USERNAME = "allure8",
            PASSWORD = "allure8",
            TOKEN = "378eab16-c82b-4fb1-9784-089a0c16be6a",
            xsrfToken = "12345",
            projectId = "2208";

    public final static String
            jsonStringCreateTestCaseRequest = String.format("{\"steps\":[{\"name\":\"%s\",\"spacing\":\"\"}, " +
            "{\"name\":\"%s\",\"spacing\":\"\"}] }", nameStepTestCaseOne, nameStepTestCaseTwo);

    public final static String jsonStringEditingRequest = String.format("{\"steps\":[{\"name\":\"%s\",\"attachments\":[],\"steps\":[]," +
            "\"leaf\":true,\"stepsCount\":0,\"hasContent\":false,\"spacing\":\"\"}," + "{\"name\":\"%s\",\"attachments\":[]," +
            "\"steps\":[],\"leaf\":true,\"stepsCount\":0," +
            "\"hasContent\":false,\"spacing\":\"\"}],\"workPath\":[1]}",nameStepTestCaseTwo, nameStepTestCaseOne);


}
