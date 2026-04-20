package api.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;

/**
 * Base class that configures RestAssured once before the entire suite runs.
 * All test classes extend this to inherit the shared RequestSpecification.
 */
public class BaseApiTest {

    protected static RequestSpecification requestSpec;

    @BeforeSuite
    public void setUpSuite() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                // Log request/response details only on test failure to keep output clean
                .log(LogDetail.ALL)
                .build();

        // Disable RestAssured's URL-encoding so path params are passed as-is
        RestAssured.urlEncodingEnabled = false;
    }
}
