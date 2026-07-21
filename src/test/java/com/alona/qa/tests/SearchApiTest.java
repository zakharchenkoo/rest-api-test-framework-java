package com.alona.qa.tests;

import com.alona.qa.assertions.HeaderAssertions;
import com.alona.qa.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.*;

@Epic("GitHub REST API")
@Feature("Search API")
public class SearchApiTest extends BaseTest {

    @Test(
            groups = {"regression", "search", "schema"},
            description = "Search repositories and validate response schema"
    )
    @Story("Search repositories")
    @Severity(SeverityLevel.NORMAL)
    public void shouldSearchRepositoriesAndValidateSchema() {
        Response response = step("Send GET /search/repositories?q=rest-assured+language:java", () ->
                githubClient.searchRepositories("rest-assured language:java", 1, 10)
        );

        step("Verify status code is 200", () ->
                assertEquals(response.statusCode(), 200)
        );

        step("Verify search response schema", () ->
                response.then().body(matchesJsonSchemaInClasspath("schemas/github-search-repositories.schema.json"))
        );

        step("Verify search results are present", () -> {
            assertTrue(response.jsonPath().getInt("total_count") > 0);
            assertTrue(response.jsonPath().getList("items").size() > 0);
            assertNotNull(response.jsonPath().getString("items[0].name"));
            assertNotNull(response.jsonPath().getString("items[0].owner.login"));
        });
    }

    @Test(
            groups = {"negative", "search", "error"},
            description = "Search repositories without query should return validation error"
    )
    @Story("Search validation error")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturn422WhenSearchQueryIsMissing() {
        Response response = step("Send GET /search/repositories without q parameter", () ->
                githubClient.searchRepositoriesWithoutQuery()
        );

        step("Verify status code is 422", () ->
                assertEquals(response.statusCode(), 422)
        );

        step("Verify error response fields", () -> {
            assertNotNull(response.jsonPath().getString("message"));
            assertNotNull(response.jsonPath().getString("documentation_url"));
        });

        step("Verify headers", () -> {
            HeaderAssertions.assertJsonContentType(response);
            HeaderAssertions.assertGitHubRateLimitHeaders(response);
        });
    }
}