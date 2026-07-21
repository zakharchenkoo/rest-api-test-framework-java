package com.alona.qa.retrofit;

import com.alona.qa.models.GitHubRepository;
import com.alona.qa.models.GitHubSearchRepositoriesResponse;
import com.alona.qa.models.GitHubUser;
import com.alona.qa.models.RateLimitResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubRetrofitService {
    @GET("users/{username}")
    Call<GitHubUser> getUser(@Path("username") String username);

    @GET("repos/{owner}/{repo}")
    Call<GitHubRepository> getRepository(@Path("owner") String owner, @Path("repo") String repo);

    @GET("search/repositories")
    Call<GitHubSearchRepositoriesResponse> searchRepositories(@Query("q") String query,
                                                              @Query("sort") String sort,
                                                              @Query("order") String order,
                                                              @Query("per_page") int perPage);

    @GET("rate_limit")
    Call<RateLimitResponse> getRateLimit();
}
