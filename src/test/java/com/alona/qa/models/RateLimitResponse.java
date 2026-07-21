package com.alona.qa.models;

public record RateLimitResponse(
        RateLimitResources resources,
        RateLimit rate
) {}