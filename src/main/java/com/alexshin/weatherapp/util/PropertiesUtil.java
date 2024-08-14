package com.alexshin.weatherapp.util;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    public static final String PROPERTIES_FILENAME = "application.properties";
    private static final Properties properties = new Properties();


    static {
        load();
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static Properties getAllProperties(){
        return (Properties) properties.clone();
    }

    private PropertiesUtil() {
        throw new UnsupportedOperationException("PropertiesUtil cannot be instantiated");
    }


    private static void load() {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {

            if (inputStream == null){
                throw new ExceptionInInitializerError(PROPERTIES_FILENAME + " not found in classpath");
            }

            properties.load(inputStream);

        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load properties file " + PROPERTIES_FILENAME);
        }
    }



}
