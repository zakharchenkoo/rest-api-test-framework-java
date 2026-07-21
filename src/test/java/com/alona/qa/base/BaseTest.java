package com.alona.qa.base;

import com.alona.qa.client.GitHubClient;
import com.alona.qa.config.ConfigManager;
import com.alona.qa.data.TestDataFactory;
import com.alona.qa.retrofit.GitHubRetrofitService;
import com.alona.qa.retrofit.RetrofitClientFactory;
import com.alona.qa.specs.RequestSpecFactory;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected GitHubClient githubClient;
    protected GitHubRetrofitService retrofitClient;
    protected TestDataFactory testDataFactory;

    @BeforeClass(alwaysRun = true)
    public void setUpClients() {
        RequestSpecification requestSpecification = RequestSpecFactory.githubApiSpec();

        githubClient = new GitHubClient(requestSpecification);
        retrofitClient = RetrofitClientFactory.create(ConfigManager.get("base.url"));
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpTestData() {
        testDataFactory = new TestDataFactory();
    }
}