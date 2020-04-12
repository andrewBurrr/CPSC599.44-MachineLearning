package ca.ucalgary.rules599.util;

import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;

public final class StreamUtil {
    private StreamUtil() {
    }

    public static void close(@Nullable Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException var2) {
                ;
            }
        }

    }
}
