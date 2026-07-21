package com.alona.qa.client;

import com.alona.qa.client.request.ApiRequest;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class GitHubClient {
    private final RequestSpecification requestSpecification;

    public GitHubClient(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    @Step("Send API request: {request.method} {request.path}")
    public Response send(ApiRequest request) {
        var specification = given()
                .spec(requestSpecification)
                .headers(request.getHeaders())
                .pathParams(request.getPathParams())
                .queryParams(request.getQueryParams());

        if (request.getBody() != null) {
            specification.body(request.getBody());
        }

        return specification
                .request(request.getMethod(), request.getPath())
                .then()
                .extract()
                .response();
    }

    public Response getUser(String username) {
        return send(ApiRequest.builder()
                .method(Method.GET)
                .path("/users/{username}")
                .pathParam("username", username)
                .build());
    }

    public Response getUserRepositories(String username, int page, int perPage) {
        return send(ApiRequest.builder()
                .method(Method.GET)
                .path("/users/{username}/repos")
                .pathParam("username", username)
                .queryParam("page", page)
                .queryParam("per_page", perPage)
                .queryParam("sort", "updated")
                .build());
    }

    public Response getRepository(String owner, String repository) {
        return send(ApiRequest.builder()
                .method(Method.GET)
                .path("/repos/{owner}/{repo}")
                .pathParam("owner", owner)
                .pathParam("repo", repository)
                .build());
    }

    public Response searchRepositories(String query, int page, int perPage) {
        return send(ApiRequest.builder()
                .method(Method.GET)
                .path("/search/repositories")
                .queryParam("q", query)
                .queryParam("page", page)
                .queryParam("per_page", perPage)
                .build());
    }

    public Response getRateLimit() {
        return send(ApiRequest.builder()
                .method(Method.GET)
                .path("/rate_limit")
                .build());
    }

    public Response getRepositoryLanguages(String owner, String repo) {
        return send(ApiRequest.builder()
                .method(Method.GET)
                .path("/repos/{owner}/{repo}/languages")
                .pathParam("owner", owner)
                .pathParam("repo", repo)
                .build());
    }

    public Response getRepositoryContributors(String owner, String repo, int page, int perPage) {
        return send(ApiRequest.builder()
                .method(Method.GET)
                .path("/repos/{owner}/{repo}/contributors")
                .pathParam("owner", owner)
                .pathParam("repo", repo)
                .queryParam("page", page)
                .queryParam("per_page", perPage)
                .build());
    }

    public Response getUserWithIfNoneMatch(String username, String etag) {
        return send(ApiRequest.builder()
                .method(Method.GET)
                .path("/users/{username}")
                .pathParam("username", username)
                .header("If-None-Match", etag)
                .build());
    }

    public Response searchRepositoriesWithoutQuery() {
        return send(ApiRequest.builder()
                .method(Method.GET)
                .path("/search/repositories")
                .build());
    }
}
