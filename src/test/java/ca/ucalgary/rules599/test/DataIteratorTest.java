package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.model.*;
import ca.ucalgary.rules599.util.DataIterator;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;


public class DataIteratorTest extends AbstractDataTest {


    /**
     * Tests, if the data, which is contained by an input file, is iterated correctly.
     *
     * @param fileName   The file name of the input file as a {@link String}
     *
     */
    private void testIterator(@NotNull final String fileName) {
        File inputFile = new File(fileName);
        DataIterator iterator = new DataIterator(inputFile,1,true);
            Transaction<AccidentAttribute> transaction1 = iterator.next();
            assertNotNull(transaction1);


    }

    @Test
    public final void testIterator1() {
        testIterator(INPUT_FILE_1);
    }


}