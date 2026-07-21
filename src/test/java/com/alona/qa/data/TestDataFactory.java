package com.alona.qa.data;

import com.github.javafaker.Faker;

import java.time.Instant;
import java.util.Locale;

public final class TestDataFactory {
    private final Faker faker = new Faker(Locale.ENGLISH);

    public String nonExistingGitHubUsername() {
        String randomPart = faker.internet().uuid().replace("-", "").substring(0, 12);
        return "qa-user-not-found-" + Instant.now().toEpochMilli() + "-" + randomPart;
    }

    public String repositorySearchQuery() {
        return "rest-assured language:java";
    }
}
