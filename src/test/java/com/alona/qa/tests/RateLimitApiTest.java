package com.alona.qa.tests;

import com.alona.qa.assertions.HeaderAssertions;
import com.alona.qa.base.BaseTest;
import com.alona.qa.models.RateLimitResponse;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.*;

@Epic("GitHub REST API")
@Feature("Rate Limit API")
public class RateLimitApiTest extends BaseTest {

    @Test(
            groups = {"smoke", "rate-limit", "schema"},
            description = "Get GitHub REST API rate limit status"
    )
    @Story("Rate limit status")
    @Severity(SeverityLevel.NORMAL)
    public void shouldGetRateLimitStatus() {
        Response response = step("Send GET /rate_limit", () ->
                githubClient.getRateLimit()
        );

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify rate limit JSON schema", () ->
                response.then().body(matchesJsonSchemaInClasspath("schemas/rate-limit.schema.json"))
        );

        RateLimitResponse rateLimitResponse = response.as(RateLimitResponse.class);

        step("Verify core rate limit data", () -> {
            assertNotNull(rateLimitResponse.resources(), "resources should not be null");
            assertNotNull(rateLimitResponse.resources().core(), "core rate limit should not be null");
            assertTrue(rateLimitResponse.resources().core().limit() > 0);
            assertTrue(rateLimitResponse.resources().core().remaining() >= 0);
            assertTrue(rateLimitResponse.resources().core().reset() > 0);
        });
    }

    @Test(
            groups = {"regression", "headers", "rate-limit"},
            description = "Validate rate limit response headers"
    )
    @Story("Rate limit headers")
    @Severity(SeverityLevel.NORMAL)
    public void shouldValidateRateLimitHeaders() {
        Response response = step("Send GET /rate_limit", () ->
                githubClient.getRateLimit()
        );

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify Content-Type header", () ->
                HeaderAssertions.assertJsonContentType(response)
        );

        step("Verify GitHub rate limit headers", () ->
                HeaderAssertions.assertGitHubRateLimitHeaders(response)
        );
    }

    @Test(
            groups = {"regression", "rate-limit"},
            description = "Core rate limit values should be logically valid"
    )
    @Story("Rate limit business rules")
    @Severity(SeverityLevel.NORMAL)
    public void shouldValidateCoreRateLimitValues() {
        Response response = step("Send GET /rate_limit", () ->
                githubClient.getRateLimit()
        );

        RateLimitResponse rateLimitResponse = response.as(RateLimitResponse.class);

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify remaining value does not exceed limit", () -> {
            int limit = rateLimitResponse.resources().core().limit();
            int remaining = rateLimitResponse.resources().core().remaining();

            assertTrue(remaining <= limit, "Remaining requests should not exceed total limit");
            assertTrue(limit > 0, "Limit should be greater than 0");
            assertTrue(remaining >= 0, "Remaining requests should not be negative");
        });
    }
}