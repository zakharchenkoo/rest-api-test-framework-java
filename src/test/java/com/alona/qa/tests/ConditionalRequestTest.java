package com.alona.qa.tests;

import com.alona.qa.assertions.HeaderAssertions;
import com.alona.qa.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static org.testng.Assert.*;

@Epic("GitHub REST API")
@Feature("Conditional Requests")
public class ConditionalRequestTest extends BaseTest {

    @Test(
            groups = {"regression", "headers", "conditional"},
            description = "GitHub should return ETag header for user endpoint"
    )
    @Story("ETag header")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnETagForUserRequest() {
        Response response = step("Send GET /users/octocat", () ->
                githubClient.getUser("octocat")
        );

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify ETag header exists", () ->
                HeaderAssertions.assertETagHeaderExists(response)
        );
    }

    @Test(
            groups = {"regression", "headers", "conditional"},
            description = "GitHub should return 304 when If-None-Match matches current ETag"
    )
    @Story("If-None-Match header")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturn304ForMatchingETag() {
        Response firstResponse = step("Send first GET /users/octocat", () ->
                githubClient.getUser("octocat")
        );

        String etag = firstResponse.header("ETag");

        step("Verify first response is 200 and ETag exists", () -> {
            assertEquals(firstResponse.statusCode(), 200);
            assertNotNull(etag, "ETag header should be present");
            assertFalse(etag.isBlank(), "ETag should not be blank");
        });

        Response secondResponse = step("Send second GET /users/octocat with If-None-Match header", () ->
                githubClient.getUserWithIfNoneMatch("octocat", etag)
        );

        step("Verify status code is 304 Not Modified", () ->
                assertEquals(secondResponse.statusCode(), 304)
        );
    }
}