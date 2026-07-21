package com.alona.qa.assertions;

import com.alona.qa.models.GitHubUser;

import static org.testng.Assert.*;

public final class GitHubUserAssertions {
    private GitHubUserAssertions() {
    }

    public static void assertUserFields(GitHubUser user, String expectedLogin, String expectedType) {
        assertNotNull(user, "GitHub user should not be null");
        assertEquals(user.login(), expectedLogin);
        assertTrue(user.id() > 0, "User id should be positive");
        assertEquals(user.type(), expectedType);
        assertTrue(user.publicRepos() >= 0, "Public repositories count should not be negative");
        assertNotNull(user.htmlUrl(), "User html_url should not be null");
        assertTrue(user.htmlUrl().contains("github.com/" + expectedLogin));
    }
}