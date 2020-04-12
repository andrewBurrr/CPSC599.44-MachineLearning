package ca.ucalgary.rules599.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Constructor;

public final class Condition {
    private Condition() {
    }

    private static void throwException(@Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        RuntimeException exception;
        try {
            Constructor<? extends RuntimeException> constructor = exceptionClass.getConstructor(String.class);
            exception = (RuntimeException)constructor.newInstance(exceptionMessage);
        } catch (Exception var4) {
            exception = new RuntimeException(exceptionMessage);
        }

        throw exception;
    }

    public static void ensureTrue(boolean expression, @Nullable String exceptionMessage) {
        ensureTrue(expression, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureTrue(boolean expression, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (!expression) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureFalse(boolean expression, @Nullable String exceptionMessage) {
        ensureFalse(expression, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureFalse(boolean expression, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (expression) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureEqual(Object object1, Object object2, @Nullable String exceptionMessage) {
        ensureEqual(object1, object2, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureEqual(Object object1, Object object2, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (object1 == null && object2 != null || object1 != null && !object1.equals(object2)) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureNotEqual(Object object1, Object object2, @Nullable String exceptionMessage) {
        ensureNotEqual(object1, object2, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureNotEqual(Object object1, Object object2, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (object1 == null && object2 == null || object1 != null && object1.equals(object2)) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureNotNull(Object object, @Nullable String exceptionMessage) {
        ensureNotNull(object, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureNotNull(Object object, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (object == null) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureNotEmpty(CharSequence text, @Nullable String exceptionMessage) {
        ensureNotEmpty(text, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureNotEmpty(CharSequence text, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (text == null || text.length() == 0) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtLeast(short value, short referenceValue, @Nullable String exceptionMessage) {
        ensureAtLeast(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtLeast(short value, short referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value < referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtLeast(int value, int referenceValue, @Nullable String exceptionMessage) {
        ensureAtLeast(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtLeast(int value, int referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value < referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtLeast(long value, long referenceValue, @Nullable String exceptionMessage) {
        ensureAtLeast(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtLeast(long value, long referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value < referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtLeast(float value, float referenceValue, @Nullable String exceptionMessage) {
        ensureAtLeast(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtLeast(float value, float referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value < referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtLeast(double value, double referenceValue, @Nullable String exceptionMessage) {
        ensureAtLeast(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtLeast(double value, double referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value < referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtMaximum(short value, short referenceValue, @Nullable String exceptionMessage) {
        ensureAtMaximum(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtMaximum(short value, short referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value > referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtMaximum(int value, int referenceValue, @Nullable String exceptionMessage) {
        ensureAtMaximum(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtMaximum(int value, int referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value > referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtMaximum(long value, long referenceValue, @Nullable String exceptionMessage) {
        ensureAtMaximum(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtMaximum(long value, long referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value > referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtMaximum(float value, float referenceValue, @Nullable String exceptionMessage) {
        ensureAtMaximum(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtMaximum(float value, float referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value > referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureAtMaximum(double value, double referenceValue, @Nullable String exceptionMessage) {
        ensureAtMaximum(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureAtMaximum(double value, double referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value > referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureGreater(short value, short referenceValue, @Nullable String exceptionMessage) {
        ensureGreater(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureGreater(short value, short referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value <= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureGreater(int value, int referenceValue, @Nullable String exceptionMessage) {
        ensureGreater(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureGreater(int value, int referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value <= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureGreater(long value, long referenceValue, @Nullable String exceptionMessage) {
        ensureGreater(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureGreater(long value, long referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value <= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureGreater(float value, float referenceValue, @Nullable String exceptionMessage) {
        ensureGreater(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureGreater(float value, float referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value <= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureGreater(double value, double referenceValue, @Nullable String exceptionMessage) {
        ensureGreater(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureGreater(double value, double referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value <= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureSmaller(short value, short referenceValue, @Nullable String exceptionMessage) {
        ensureSmaller(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureSmaller(short value, short referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value >= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureSmaller(int value, int referenceValue, @Nullable String exceptionMessage) {
        ensureSmaller(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureSmaller(int value, int referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value >= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureSmaller(long value, long referenceValue, @Nullable String exceptionMessage) {
        ensureSmaller(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureSmaller(long value, long referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value >= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureSmaller(float value, float referenceValue, @Nullable String exceptionMessage) {
        ensureSmaller(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureSmaller(float value, float referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value >= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureSmaller(double value, double referenceValue, @Nullable String exceptionMessage) {
        ensureSmaller(value, referenceValue, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureSmaller(double value, double referenceValue, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (value >= referenceValue) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureNotEmpty(Iterable<?> iterable, @Nullable String exceptionMessage) {
        ensureNotEmpty(iterable, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureNotEmpty(Iterable<?> iterable, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (!iterable.iterator().hasNext()) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureFileExists(File file, String exceptionMessage) {
        ensureFileExists(file, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureFileExists(@NotNull File file, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (!file.exists()) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureFileIsDirectory(@NotNull File file, @Nullable String exceptionMessage) {
        ensureFileIsDirectory(file, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureFileIsDirectory(@NotNull File file, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (!file.isDirectory()) {
            throwException(exceptionMessage, exceptionClass);
        }

    }

    public static void ensureFileIsNoDirectory(@NotNull File file, @Nullable String exceptionMessage) {
        ensureFileIsNoDirectory(file, exceptionMessage, IllegalArgumentException.class);
    }

    public static void ensureFileIsNoDirectory(@NotNull File file, @Nullable String exceptionMessage, @NotNull Class<? extends RuntimeException> exceptionClass) {
        if (!file.isFile()) {
            throwException(exceptionMessage, exceptionClass);
        }

    }
}
