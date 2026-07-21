package com.alona.qa.tests;

import com.alona.qa.assertions.GitHubUserAssertions;
import com.alona.qa.assertions.HeaderAssertions;
import com.alona.qa.base.BaseTest;
import com.alona.qa.models.GitHubUser;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.*;

@Epic("GitHub REST API")
@Feature("Users API")
public class GitHubUserApiTest extends BaseTest {

    @Test(
            groups = {"smoke", "user", "schema"},
            description = "Get public GitHub user and validate response schema"
    )
    @Story("Get public user")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldGetPublicUserAndValidateSchema() {
        Response response = step("Send GET /users/octocat", () ->
                githubClient.getUser("octocat")
        );

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify response JSON schema", () ->
                response.then().body(matchesJsonSchemaInClasspath("schemas/github-user.schema.json"))
        );

        GitHubUser user = response.as(GitHubUser.class);

        step("Verify user fields", () ->
                GitHubUserAssertions.assertUserFields(user, "octocat", "User")
        );
    }

    @Test(
            groups = {"regression", "headers", "user"},
            description = "Validate headers for public user endpoint"
    )
    @Story("Validate user endpoint headers")
    @Severity(SeverityLevel.NORMAL)
    public void shouldValidateUserEndpointHeaders() {
        Response response = step("Send GET /users/octocat", () ->
                githubClient.getUser("octocat")
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

        step("Verify ETag header exists", () ->
                HeaderAssertions.assertETagHeaderExists(response)
        );
    }

    @Test(
            groups = {"regression", "pagination", "user"},
            description = "Get user repositories with pagination parameters"
    )
    @Story("User repositories pagination")
    @Severity(SeverityLevel.NORMAL)
    public void shouldGetUserRepositoriesWithPagination() {
        Response response = step("Send GET /users/octocat/repos?page=1&per_page=2", () ->
                githubClient.getUserRepositories("octocat", 1, 2)
        );

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify response is not empty", () ->
                assertTrue(response.jsonPath().getList("$").size() > 0)
        );

        step("Verify no more than 2 repositories are returned", () ->
                assertTrue(response.jsonPath().getList("$").size() <= 2)
        );

        step("Verify headers", () -> {
            HeaderAssertions.assertJsonContentType(response);
            HeaderAssertions.assertGitHubRateLimitHeaders(response);
        });
    }

    @Test(
            groups = {"regression", "headers", "pagination"},
            description = "Validate Link header for paginated repositories response"
    )
    @Story("Pagination headers")
    @Severity(SeverityLevel.NORMAL)
    public void shouldValidatePaginationLinkHeaderForUserRepositories() {
        Response response = step("Send GET /users/octocat/repos?page=1&per_page=1", () ->
                githubClient.getUserRepositories("octocat", 1, 1)
        );

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify Link header exists for paginated response", () ->
                HeaderAssertions.assertPaginationLinkHeaderExistsWhenNeeded(response)
        );
    }
}