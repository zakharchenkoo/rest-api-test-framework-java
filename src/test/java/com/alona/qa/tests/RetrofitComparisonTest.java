package com.alona.qa.tests;

import com.alona.qa.base.BaseTest;
import com.alona.qa.models.GitHubUser;
import com.alona.qa.utils.AllureAttachmentUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static org.testng.Assert.*;

@Epic("GitHub REST API")
@Feature("RestAssured vs Retrofit")
public class RetrofitComparisonTest extends BaseTest {

    @Test(
            groups = {"regression", "retrofit"},
            description = "Compare the same GitHub user response using RestAssured and Retrofit clients"
    )
    @Story("Client comparison")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnSameUserDataUsingRestAssuredAndRetrofit() throws Exception {
        String username = "octocat";

        Response restAssuredResponse = step("Send GET /users/octocat using RestAssured client", () ->
                githubClient.getUser(username)
        );

        long retrofitStartTime = System.nanoTime();

        retrofit2.Response<GitHubUser> retrofitResponse = step(
                "Send GET /users/octocat using Retrofit typed client",
                () -> retrofitClient.getUser(username).execute()
        );

        long retrofitDurationMs = (System.nanoTime() - retrofitStartTime) / 1_000_000;

        GitHubUser restAssuredUser = restAssuredResponse.as(GitHubUser.class);
        GitHubUser retrofitUser = retrofitResponse.body();

        step("Attach client comparison metadata", () -> {
            AllureAttachmentUtils.attachText(
                    "RestAssured status code",
                    String.valueOf(restAssuredResponse.statusCode())
            );

            AllureAttachmentUtils.attachText(
                    "Retrofit status code",
                    String.valueOf(retrofitResponse.code())
            );

            AllureAttachmentUtils.attachText(
                    "RestAssured response time in ms",
                    String.valueOf(restAssuredResponse.time())
            );

            AllureAttachmentUtils.attachText(
                    "Retrofit response time in ms",
                    String.valueOf(retrofitDurationMs)
            );
        });

        step("Verify both clients returned successful responses", () -> {
            assertEquals(restAssuredResponse.statusCode(), 200);
            assertEquals(retrofitResponse.code(), 200);
            assertTrue(retrofitResponse.isSuccessful(), "Retrofit response should be successful");
            assertNotNull(retrofitUser, "Retrofit response body should not be null");
        });

        step("Verify both clients returned the same user identity", () -> {
            assertEquals(restAssuredUser.login(), retrofitUser.login());
            assertEquals(restAssuredUser.id(), retrofitUser.id());
            assertEquals(restAssuredUser.type(), retrofitUser.type());
        });

        step("Verify both clients returned the same public profile data", () -> {
            assertEquals(restAssuredUser.htmlUrl(), retrofitUser.htmlUrl());
            assertEquals(restAssuredUser.publicRepos(), retrofitUser.publicRepos());
        });
    }
}