package ca.ucalgary.rules599.util;

import org.jetbrains.annotations.NotNull;

public final class ArrayUtil {
    private ArrayUtil() {
    }

    public static int indexOf(@NotNull boolean[] array, boolean value) {
        Condition.ensureNotNull(array, "The array may not be null");

        for(int i = 0; i < array.length; ++i) {
            if (array[i] == value) {
                return i;
            }
        }

        return -1;
    }

    public static int lastIndexOf(@NotNull boolean[] array, boolean value) {
        Condition.ensureNotNull(array, "The array may not be null");

        for(int i = array.length - 1; i >= 0; --i) {
            if (array[i] == value) {
                return i;
            }
        }

        return -1;
    }

    public static boolean contains(@NotNull boolean[] array, boolean value) {
        return indexOf(array, value) != -1;
    }
}

