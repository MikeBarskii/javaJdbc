package com.github.mbarskiy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ConnectionProperties extends Properties {
    private static Properties properties = new Properties();

    public static Properties getConnectionProperties() throws IOException {
        if (properties.isEmpty()) {
            String propertiesFilename = "config.properties";
            try (InputStream input = ConnectionProperties.class.getClassLoader().getResourceAsStream(propertiesFilename)) {
                properties.load(Objects.requireNonNull(input));
            }
        }
        return properties;
    }
}
