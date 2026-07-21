package com.alona.qa.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record GitHubSearchRepositoriesResponse(
        @SerializedName("total_count") int totalCount,
        @SerializedName("incomplete_results") boolean incompleteResults,
        List<GitHubRepository> items
) {}