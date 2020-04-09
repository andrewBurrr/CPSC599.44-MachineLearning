package ca.ucalgary.rules599.util;

import org.jetbrains.annotations.NotNull;

public final class ClassUtil {
    private ClassUtil() {
    }

    public static String getTruncatedName(@NotNull Class<?> clazz) {
        Condition.ensureNotNull(clazz, "The class may not be null");
        String fullQualifiedName = clazz.getName();
        String[] qualifiers = fullQualifiedName.split("\\.");
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < qualifiers.length; ++i) {
            if (i != qualifiers.length - 1) {
                stringBuilder.append(qualifiers[i].substring(0, 1));
                stringBuilder.append(".");
            } else {
                stringBuilder.append(qualifiers[i]);
            }
        }

        return stringBuilder.toString();
    }

}
