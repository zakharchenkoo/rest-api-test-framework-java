package com.alona.qa.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class AllureRequestResponseFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext context) {
        AllureAttachmentUtils.attachText("Request method", requestSpec.getMethod());
        AllureAttachmentUtils.attachText("Request URI", requestSpec.getURI());
        AllureAttachmentUtils.attachText("Request headers", maskSensitiveData(requestSpec.getHeaders().toString()));

        Object body = requestSpec.getBody();
        if (body != null) {
            AllureAttachmentUtils.attachJson("Request body", body.toString());
        }

        Response response = context.next(requestSpec, responseSpec);

        AllureAttachmentUtils.attachText("Response status", String.valueOf(response.getStatusCode()));
        AllureAttachmentUtils.attachText("Response headers", response.getHeaders().toString());
        AllureAttachmentUtils.attachJson("Response body", response.asPrettyString());

        return response;
    }

    private String maskSensitiveData(String content) {
        if (content == null) {
            return "";
        }
        return content.replaceAll("Bearer\\s+[A-Za-z0-9_\\-.]+", "Bearer ***");
    }
}
