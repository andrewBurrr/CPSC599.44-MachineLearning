package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.Rules599Application;
import ca.ucalgary.rules599.model.AccidentData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.rule.OutputCapture;

import java.io.File;
import java.util.List;

import static ca.ucalgary.rules599.test.AbstractDataTest.INPUT_FILE_1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Rules599ApplicationTest {

    private Rules599Application rules599Application;

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Before
    public void setup() {

        try {
            rules599Application = new Rules599Application();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public final void Rules599ApplicationTest() {
        rules599Application.run("-pre","-c","src/test/resources/config.yml","-i","src/test/resources/data_lite.csv","-o", "src/test/resources/drivers.csv");
        String output = this.outputCapture.toString();
        assertTrue(output, output.contains("PreProcessing Completed"));

        rules599Application.run("-pro","-c","src/test/resources/config.yml","-i","src/test/resources/driversTest.csv","-o", "src/test/resources/Output.txt");
        output = this.outputCapture.toString();
        assertTrue(output, output.contains("support"));

//        rules599Application.run("-help");
//        output = this.outputCapture.toString();
//        assertNotNull(output);
    }

}
