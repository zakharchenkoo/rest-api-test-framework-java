package com.alona.qa.utils;

import io.qameta.allure.Attachment;

public final class AllureAttachmentUtils {
    private AllureAttachmentUtils() {
    }

    @Attachment(value = "{name}", type = "text/plain")
    public static String attachText(String name, String content) {
        return content == null ? "" : content;
    }

    @Attachment(value = "{name}", type = "application/json")
    public static String attachJson(String name, String json) {
        return json == null ? "" : json;
    }
}
