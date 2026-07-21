package com.alona.qa.tests;

import com.alona.qa.assertions.HeaderAssertions;
import com.alona.qa.assertions.RepositoryAssertions;
import com.alona.qa.base.BaseTest;
import com.alona.qa.dataproviders.RepositoryDataProvider;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.*;

@Epic("GitHub REST API")
@Feature("Repositories API")
public class RepositoryApiTest extends BaseTest {

    @Test(
            groups = {"smoke", "repository", "schema"},
            description = "Get public repositories from CSV test data",
            dataProvider = "publicRepositoriesFromCsv",
            dataProviderClass = RepositoryDataProvider.class
    )
    @Story("Get public repository")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldGetPublicRepositoriesFromCsv(String owner, String repo, String expectedName) {
        Response response = step("Send GET /repos/{owner}/{repo}", () ->
                githubClient.getRepository(owner, repo)
        );

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify repository JSON schema", () ->
                response.then().body(matchesJsonSchemaInClasspath("schemas/github-repository.schema.json"))
        );

        step("Verify repository basic fields", () ->
                RepositoryAssertions.assertRepositoryBasicFields(response, owner, expectedName)
        );

        step("Verify repository is public", () ->
                RepositoryAssertions.assertRepositoryIsPublic(response)
        );
    }

    @Test(
            groups = {"regression", "headers", "repository"},
            description = "Validate response headers for repository endpoint"
    )
    @Story("Repository headers")
    @Severity(SeverityLevel.NORMAL)
    public void shouldValidateRepositoryEndpointHeaders() {
        Response response = step("Send GET /repos/octocat/Hello-World", () ->
                githubClient.getRepository("octocat", "Hello-World")
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
            groups = {"regression", "repository"},
            description = "Validate repository owner fields"
    )
    @Story("Repository owner")
    @Severity(SeverityLevel.NORMAL)
    public void shouldValidateRepositoryOwnerFields() {
        Response response = step("Send GET /repos/rest-assured/rest-assured", () ->
                githubClient.getRepository("rest-assured", "rest-assured")
        );

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify repository owner fields", () ->
                RepositoryAssertions.assertRepositoryHasOwner(response)
        );
    }

    @Test(
            groups = {"negative", "repository"},
            description = "Unknown repository should return 404"
    )
    @Story("Negative repository lookup")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturn404ForUnknownRepository() {
        Response response = step("Send GET /repos/octocat/unknown-repository-qa-automation-999999", () ->
                githubClient.getRepository("octocat", "unknown-repository-qa-automation-999999")
        );

        step("Verify status code is 404", () ->
                assertEquals(response.statusCode(), 404)
        );

        step("Verify error response message", () ->
                assertEquals(response.jsonPath().getString("message"), "Not Found")
        );

        step("Verify error response Content-Type header", () ->
                HeaderAssertions.assertJsonContentType(response)
        );
    }

    @Test(
            groups = {"regression", "repository"},
            description = "Get repository languages"
    )
    @Story("Repository languages")
    @Severity(SeverityLevel.NORMAL)
    public void shouldGetRepositoryLanguages() {
        Response response = step("Send GET /repos/rest-assured/rest-assured/languages", () ->
                githubClient.getRepositoryLanguages("rest-assured", "rest-assured")
        );

        step("Verify languages response", () ->
                RepositoryAssertions.assertLanguagesResponseIsValid(response)
        );

        step("Verify headers", () -> {
            HeaderAssertions.assertJsonContentType(response);
            HeaderAssertions.assertGitHubRateLimitHeaders(response);
        });
    }

    @Test(
            groups = {"regression", "pagination", "repository"},
            description = "Get repository contributors with pagination"
    )
    @Story("Repository contributors")
    @Severity(SeverityLevel.NORMAL)
    public void shouldGetRepositoryContributorsWithPagination() {
        Response response = step("Send GET /repos/SeleniumHQ/selenium/contributors?page=1&per_page=3", () ->
                githubClient.getRepositoryContributors("SeleniumHQ", "selenium", 1, 3)
        );

        step("Verify contributors response", () ->
                RepositoryAssertions.assertContributorsResponseIsValid(response, 3)
        );

        step("Verify headers", () -> {
            HeaderAssertions.assertJsonContentType(response);
            HeaderAssertions.assertGitHubRateLimitHeaders(response);
        });
    }
}