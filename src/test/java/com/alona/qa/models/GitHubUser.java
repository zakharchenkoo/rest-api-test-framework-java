package com.alona.qa.models;

import com.google.gson.annotations.SerializedName;

public record GitHubUser(
        String login,
        long id,
        String type,
        @SerializedName("html_url") String htmlUrl,
        @SerializedName("public_repos") int publicRepos,
        int followers,
        int following
) {}