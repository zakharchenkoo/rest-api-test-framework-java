package com.alona.qa.models;

public record RateLimit(
        int limit,
        int used,
        int remaining,
        long reset
) {}