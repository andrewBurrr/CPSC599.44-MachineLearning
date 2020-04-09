package ca.ucalgary.rules599.util;

import ca.ucalgary.rules599.model.AccidentData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil {
    private static void mkdir(@NotNull File directory, boolean createParents) throws IOException {
        Condition.ensureNotNull(directory, "The directory may not be null");
        boolean result = createParents ? directory.mkdirs() : directory.mkdir();
        if (!result && !directory.exists()) {
            throw new IOException("Failed to create directory \"" + directory + "\"");
        }
    }

    private FileUtil() {
    }

    public static void mkdir(@NotNull File directory) throws IOException {
        mkdir(directory, false);
    }

    public static void mkdirs(@NotNull File directory) throws IOException {
        mkdir(directory, true);
    }

    public static void delete(@NotNull File file) throws IOException {
        Condition.ensureNotNull(file, "The file may not be null");
        boolean result = file.delete();
        if (!result && file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("Directory \"" + file + "\" must be empty before being deleted");
            } else {
                throw new IOException("Failed to deleted file \"" + file + "\"");
            }
        }
    }

    public static void deleteRecursively(@NotNull File file) throws IOException {
        Condition.ensureNotNull(file, "The file or directory may not be null");
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                File[] var2 = files;
                int var3 = files.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File child = var2[var4];
                    deleteRecursively(child);
                }
            }
        }

        delete(file);
    }

    public static void createNewFile(@NotNull File file) throws IOException {
        createNewFile(file, false);
    }

    public static void createNewFile(@NotNull File file, boolean overwrite) throws IOException {
        Condition.ensureNotNull(file, "The file may not be null");
        boolean result = file.createNewFile();
        if (!result) {
            if (!overwrite) {
                if (file.exists()) {
                    throw new IOException("File \"" + file + "\" does already exist");
                }

                throw new IllegalArgumentException("The file must not be a directory");
            }

            try {
                delete(file);
                createNewFile(file, false);
            } catch (IOException var4) {
                throw new IOException("Failed to overwrite file \"" + file + "\"");
            }
        }

    }

    public static void createFile(String file, List<String> arrData) throws IOException {
        FileWriter writer = new FileWriter(file);
        int size = arrData.size();
        for (int i=0;i<size;i++) {
            String str = arrData.get(i);
            writer.write(str);
            if(i < size-1)
            writer.write("\n");
        }
        writer.close();
    }

}

