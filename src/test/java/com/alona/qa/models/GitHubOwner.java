package com.alona.qa.models;

public record GitHubOwner(
        String login,
        long id,
        String type
) {}