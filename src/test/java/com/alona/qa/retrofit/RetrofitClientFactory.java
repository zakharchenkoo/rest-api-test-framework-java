package com.alona.qa.retrofit;

import com.alona.qa.config.ConfigManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public final class RetrofitClientFactory {

    private RetrofitClientFactory() {
    }

    public static GitHubRetrofitService create(String baseUrl) {
        int timeoutInSeconds = Integer.parseInt(ConfigManager.get("request.timeout.seconds"));

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .readTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .writeTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    okhttp3.Request originalRequest = chain.request();

                    okhttp3.Request.Builder requestBuilder = originalRequest.newBuilder()
                            .header("Accept", "application/vnd.github+json")
                            .header("X-GitHub-Api-Version", ConfigManager.get("api.version"));

                    ConfigManager.getGithubToken()
                            .ifPresent(token -> requestBuilder.header("Authorization", "Bearer " + token));

                    return chain.proceed(requestBuilder.build());
                });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ensureTrailingSlash(baseUrl))
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GitHubRetrofitService.class);
    }

    private static String ensureTrailingSlash(String baseUrl) {
        return baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }
}