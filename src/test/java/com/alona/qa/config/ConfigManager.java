package com.alona.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("config.properties was not found in src/test/resources");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private ConfigManager() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return PROPERTIES.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static Optional<String> getOptionalEnv(String environmentVariableName) {
        if (environmentVariableName == null || environmentVariableName.isBlank()) {
            return Optional.empty();
        }

        String environmentValue = System.getenv(environmentVariableName);
        if (environmentValue == null || environmentValue.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(environmentValue);
    }

    public static Optional<String> getGithubToken() {
        String tokenEnvName = get("github.token.env");
        return getOptionalEnv(tokenEnvName);
    }
}
