package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class Injector {

    private final Properties properties;

    public Injector() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("injection.properties")) {
            if (input == null) {
                throw new RuntimeException("injection.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load injection.properties", e);
        }
    }

    public <T> T inject(T object) {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                String interfaceName = field.getType().getName();
                String implClassName = properties.getProperty(interfaceName);
                if (implClassName == null) {
                    throw new RuntimeException("No implementation found for: " + interfaceName);
                }
                try {
                    Class<?> implClass = Class.forName(implClassName.trim());
                    Object instance = implClass.getDeclaredConstructor().newInstance();
                    field.setAccessible(true);
                    field.set(object, instance);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to inject field: " + field.getName(), e);
                }
            }
        }
        return object;
    }
}
