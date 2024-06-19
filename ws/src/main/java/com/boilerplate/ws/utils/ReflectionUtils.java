package com.boilerplate.ws.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    /**
     * Copy non-null properties from source object to target object
     *
     * @param source DTO
     * @param target DTO
     * @throws IllegalAccessException
     */
    public static void copyNonNullProperties(Object source, Object target) throws IllegalAccessException {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);  // to access private fields
            Object value = field.get(source);
            if (value != null) {
                try {
                    Field targetField = target.getClass().getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, value);
                } catch (NoSuchFieldException e) {
                    // ignore
                }
            }
        }
    }
}
