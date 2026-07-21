package com.alona.qa.models;

import com.google.gson.annotations.SerializedName;

public record GitHubRepository(
        long id,
        String name,
        @SerializedName("full_name") String fullName,
        @SerializedName("private") boolean privateRepository,
        GitHubOwner owner,
        @SerializedName("html_url") String htmlUrl,
        String description,
        @SerializedName("stargazers_count") int stargazersCount,
        @SerializedName("forks_count") int forksCount,
        @SerializedName("open_issues_count") int openIssuesCount,
        @SerializedName("default_branch") String defaultBranch
) {}