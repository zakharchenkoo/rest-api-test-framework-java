package com.alona.qa.client.request;

import io.restassured.http.Method;

import java.util.HashMap;
import java.util.Map;

public class ApiRequest {
    private final Method method;
    private final String path;
    private final Object body;
    private final Map<String, Object> pathParams;
    private final Map<String, Object> queryParams;
    private final Map<String, String> headers;

    private ApiRequest(Builder builder) {
        this.method = builder.method;
        this.path = builder.path;
        this.body = builder.body;
        this.pathParams = builder.pathParams;
        this.queryParams = builder.queryParams;
        this.headers = builder.headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Method getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Object getBody() {
        return body;
    }

    public Map<String, Object> getPathParams() {
        return pathParams;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static final class Builder {
        private Method method;
        private String path;
        private Object body;
        private final Map<String, Object> pathParams = new HashMap<>();
        private final Map<String, Object> queryParams = new HashMap<>();
        private final Map<String, String> headers = new HashMap<>();

        private Builder() {
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public Builder pathParam(String key, Object value) {
            this.pathParams.put(key, value);
            return this;
        }

        public Builder queryParam(String key, Object value) {
            this.queryParams.put(key, value);
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public ApiRequest build() {
            if (method == null) {
                throw new IllegalArgumentException("HTTP method must be provided");
            }
            if (path == null || path.isBlank()) {
                throw new IllegalArgumentException("Request path must be provided");
            }
            return new ApiRequest(this);
        }
    }
}
