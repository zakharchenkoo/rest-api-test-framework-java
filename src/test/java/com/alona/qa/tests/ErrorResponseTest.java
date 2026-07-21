package com.alona.qa.tests;

import com.alona.qa.assertions.HeaderAssertions;
import com.alona.qa.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static org.testng.Assert.*;

@Epic("GitHub REST API")
@Feature("Error Responses")
public class ErrorResponseTest extends BaseTest {

    @Test(
            groups = {"negative", "error"},
            description = "Unknown user should return standard GitHub 404 error response"
    )
    @Story("404 user error")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnStandardErrorForUnknownUser() {
        Response response = step("Send GET /users/unknown-user-qa-automation-999999", () ->
                githubClient.getUser("unknown-user-qa-automation-999999")
        );

        step("Verify status code is 404", () ->
                assertEquals(response.statusCode(), 404)
        );

        step("Verify error message", () ->
                assertEquals(response.jsonPath().getString("message"), "Not Found")
        );

        step("Verify documentation_url exists", () ->
                assertNotNull(response.jsonPath().getString("documentation_url"))
        );

        step("Verify headers", () -> {
            HeaderAssertions.assertJsonContentType(response);
            HeaderAssertions.assertGitHubRateLimitHeaders(response);
        });
    }
}