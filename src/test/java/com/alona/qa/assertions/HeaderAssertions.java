package com.alona.qa.assertions;

import io.restassured.response.Response;

import static org.testng.Assert.*;

public final class HeaderAssertions {

    private HeaderAssertions() {
    }

    public static void assertJsonContentType(Response response) {
        String contentType = response.header("Content-Type");

        assertNotNull(contentType, "Content-Type header should be present");
        assertTrue(
                contentType.toLowerCase().contains("application/json"),
                "Content-Type should contain application/json, but was: " + contentType
        );
    }

    public static void assertGitHubRateLimitHeaders(Response response) {
        assertNotNull(response.header("X-RateLimit-Limit"), "X-RateLimit-Limit header should be present");
        assertNotNull(response.header("X-RateLimit-Remaining"), "X-RateLimit-Remaining header should be present");
        assertNotNull(response.header("X-RateLimit-Reset"), "X-RateLimit-Reset header should be present");
        assertNotNull(response.header("X-RateLimit-Resource"), "X-RateLimit-Resource header should be present");

        int limit = Integer.parseInt(response.header("X-RateLimit-Limit"));
        int remaining = Integer.parseInt(response.header("X-RateLimit-Remaining"));
        long reset = Long.parseLong(response.header("X-RateLimit-Reset"));

        assertTrue(limit > 0, "Rate limit should be greater than 0");
        assertTrue(remaining >= 0, "Remaining rate limit should not be negative");
        assertTrue(reset > 0, "Rate limit reset timestamp should be greater than 0");
    }

    public static void assertETagHeaderExists(Response response) {
        assertNotNull(response.header("ETag"), "ETag header should be present");
        assertFalse(response.header("ETag").isBlank(), "ETag header should not be blank");
    }

    public static void assertPaginationLinkHeaderExistsWhenNeeded(Response response) {
        String linkHeader = response.header("Link");

        assertNotNull(linkHeader, "Link header should be present for paginated response");
        assertTrue(
                linkHeader.contains("rel=\"next\"") || linkHeader.contains("rel=\"last\""),
                "Link header should contain pagination relations, but was: " + linkHeader
        );
    }
}
