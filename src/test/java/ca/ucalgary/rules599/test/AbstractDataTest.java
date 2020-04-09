package ca.ucalgary.rules599.test;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static ca.ucalgary.rules599.util.Condition.ensureNotEmpty;
import static ca.ucalgary.rules599.util.Condition.ensureNotNull;
import static org.junit.Assert.assertTrue;


public abstract class AbstractDataTest {

    /**
     * The name of the first input file, which is used by the tests.
     */
    protected static final String INPUT_FILE_1 = "src/test/resources/data_lite.csv";

    @NotNull
    protected final File getInputFile(@NotNull final String fileName) {
        ensureNotNull(fileName, "The file name may not be null");
        ensureNotEmpty(fileName, "The file name may not be null");
        try
        {
            File file = new File(fileName);
             assertTrue(file.isFile());
             return file;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read input file", e);
        }
    }

}