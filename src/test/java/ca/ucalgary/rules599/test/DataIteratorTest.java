package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.model.*;
import ca.ucalgary.rules599.util.DataIterator;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;


public class DataIteratorTest extends AbstractDataTest {
    static AccidentData item1;
    static Person person1;
    static Collision collision1;
    static Vehicle vehicle1;
    @Before
    public void setup() {
        person1 = Person.builder().P_ID("1").P_SEX("M").P_AGE("75").P_PSN("11").P_ISEV("2").P_SAFE("NN").P_USER("1").build();
        vehicle1= Vehicle.builder().V_ID("1").V_TYPE("1").V_YEAR("UUUU").build();
        collision1= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("10").C_SEV("2").C_VEHS("1").C_CONF("2").C_RCFG("3").C_WTHR("1").C_RSUR("3").C_RALN("2").C_TRAF("18").build();
        item1 = AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build();

    }


    /**
     * Tests, if the data, which is contained by an input file, is iterated correctly.
     *
     * @param fileName   The file name of the input file as a {@link String}
     * @param actualData First Line of the data which is contained in the the input file. The Data may not be null
     */
    private void testIterator(@NotNull final String fileName,
                              @NotNull final AccidentData actualData) {
        File inputFile = new File(fileName);
        DataIterator iterator = new DataIterator(inputFile);
            Transaction<AccidentData> transaction = iterator.next(); // header
            Transaction<AccidentData> transaction1 = iterator.next();
            for (AccidentData item : transaction1) {
                assertEquals(actualData, item);
            }

    }

    @Test
    public final void testIterator1() {
        testIterator(INPUT_FILE_1, item1);
    }


}