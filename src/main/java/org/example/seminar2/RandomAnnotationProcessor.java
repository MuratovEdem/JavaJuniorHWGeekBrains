package org.example.seminar2;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Random;

public class RandomAnnotationProcessor {
    public static void processAnnotation(Object obj) {
        // найти все поля класса, над которыми стоит аннотация @Random
        // вставить туда рандомное число в диапазоне [0, 100)

        java.util.Random random = new java.util.Random();
        Class<?> objClass = obj.getClass();
        for (Field field : objClass.getDeclaredFields()) {
            // obj.getClass().isAssignableFrom(AnnotationsMain.Person.class)

            if (field.isAnnotationPresent(RandomDate.class) && field.getType().isAssignableFrom(Date.class)) {
                RandomDate annotation = field.getAnnotation(RandomDate.class);
                long min = annotation.min();
                long max = annotation.max();
                if (min > max) {
                    return;
                }

                try {
                    field.setAccessible(true); // чтобы можно было изменять финальные поля
                    field.set(obj, new Date(random.nextLong(min, max)));
                } catch (IllegalAccessException e) {
                    System.err.println("Не удалось вставить значение в поле: " + e.getMessage());
                }
            }
        }
    }
}
