package com.alona.qa.assertions;

import io.restassured.response.Response;

import static org.testng.Assert.*;

public final class RepositoryAssertions {

    private RepositoryAssertions() {
    }

    public static void assertRepositoryBasicFields(
            Response response,
            String expectedOwner,
            String expectedRepo
    ) {
        assertEquals(response.jsonPath().getString("owner.login"), expectedOwner);
        assertEquals(response.jsonPath().getString("name"), expectedRepo);
        assertEquals(response.jsonPath().getString("full_name"), expectedOwner + "/" + expectedRepo);

        assertNotNull(response.jsonPath().get("id"), "Repository id should not be null");
        assertNotNull(response.jsonPath().getString("html_url"), "Repository html_url should not be null");
        assertNotNull(response.jsonPath().getString("visibility"), "Repository visibility should not be null");

        assertTrue(
                response.jsonPath().getString("html_url").contains("github.com/" + expectedOwner + "/" + expectedRepo),
                "Repository html_url should contain owner and repository name"
        );
    }

    public static void assertRepositoryIsPublic(Response response) {
        Boolean isPrivate = response.jsonPath().getBoolean("private");

        assertNotNull(isPrivate, "private field should be present");
        assertFalse(isPrivate, "Repository should be public");
    }

    public static void assertRepositoryHasOwner(Response response) {
        assertNotNull(response.jsonPath().getString("owner.login"), "Owner login should be present");
        assertNotNull(response.jsonPath().get("owner.id"), "Owner id should be present");
        assertNotNull(response.jsonPath().getString("owner.type"), "Owner type should be present");
        assertNotNull(response.jsonPath().getString("owner.html_url"), "Owner html_url should be present");
    }

    public static void assertLanguagesResponseIsValid(Response response) {
        assertEquals(response.statusCode(), 200);
        assertFalse(response.asString().isBlank(), "Languages response body should not be blank");
        assertTrue(response.asString().startsWith("{"), "Languages response should be JSON object");
    }

    public static void assertContributorsResponseIsValid(Response response, int maxExpectedSize) {
        assertEquals(response.statusCode(), 200);

        int contributorsCount = response.jsonPath().getList("$").size();

        assertTrue(contributorsCount > 0, "Contributors list should not be empty");
        assertTrue(contributorsCount <= maxExpectedSize, "Contributors list should respect per_page limit");

        assertNotNull(response.jsonPath().getString("[0].login"), "First contributor login should be present");
        assertNotNull(response.jsonPath().get("[0].id"), "First contributor id should be present");
        assertNotNull(response.jsonPath().getString("[0].html_url"), "First contributor html_url should be present");
    }
}