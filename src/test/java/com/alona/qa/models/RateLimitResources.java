package com.alona.qa.models;

public record RateLimitResources(
        RateLimit core,
        RateLimit search
) {}