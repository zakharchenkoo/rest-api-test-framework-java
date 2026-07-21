package com.alona.qa.specs;

import com.alona.qa.config.ConfigManager;
import com.alona.qa.utils.AllureRequestResponseFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.mapper.ObjectMapperType;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public final class RequestSpecFactory {
    private RequestSpecFactory() {
    }

    public static RequestSpecification githubApiSpec() {
        int timeoutInSeconds = Integer.parseInt(ConfigManager.get("request.timeout.seconds"));

        RequestSpecification specification = given()
                .baseUri(ConfigManager.get("base.url"))
                .contentType(ContentType.JSON)
                .accept("application/vnd.github+json")
                .header("X-GitHub-Api-Version", ConfigManager.get("api.version"))
                .config(config()
                        .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                                .defaultObjectMapperType(ObjectMapperType.GSON))
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", timeoutInSeconds * 1000)
                                .setParam("http.socket.timeout", timeoutInSeconds * 1000)))
                .filter(new AllureRestAssured())
                .filter(new AllureRequestResponseFilter());

        ConfigManager.getGithubToken()
                .ifPresent(token -> specification.header("Authorization", "Bearer " + token));

        return specification;
    }
}
