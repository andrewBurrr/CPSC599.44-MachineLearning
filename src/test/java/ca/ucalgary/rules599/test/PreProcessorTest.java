package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.Training.Preprocessor;
import ca.ucalgary.rules599.Training.Processor;
import ca.ucalgary.rules599.model.*;
import ca.ucalgary.rules599.rules.Apriori;
import ca.ucalgary.rules599.rules.Output;
import ca.ucalgary.rules599.rules.RuleSet;
import ca.ucalgary.rules599.util.FileUtil;
import org.junit.Before;
import org.junit.Test;


import java.io.*;
import java.util.List;

import static ca.ucalgary.rules599.test.AbstractDataTest.INPUT_FILE_1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PreProcessorTest {

    private Preprocessor preprocessor;
    private Processor processor;

    @Before
    public void setup() {

        try {
          preprocessor = new Preprocessor();
          processor = new Processor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public final void InitialLoadTest() {
        File file = new File(INPUT_FILE_1);
        List<AccidentData> actualData = preprocessor.processInitialData(file);
        assertEquals(actualData.size(), 749);
    }

    @Test
    public final void InitialProcessingTest() {
        RuleSet actualData = processor.generateRules("src/test/resources/drivers.csv",0.5, 0.5);
        assertNotNull(actualData);
    }


    /**
    *
    *
    *
    *
    * ***/
    @Test
    public final void InitialProcessingTestApriori() {
        Apriori.Configuration configuration = new Apriori.Configuration();
        double minSupport = 0.3;
        double maxSupport = 0.8;
        double supportDelta = 0.2;
        int frequentItemSetCount = 1000;
        boolean generateRules = true;
        double minConfidence = 0.8;
        double maxConfidence = 0.9;
        double confidenceDelta = 0.2;
        int ruleCount = 1200;
        configuration.setMinSupport(minSupport);
        configuration.setMaxSupport(maxSupport);
        configuration.setSupportDelta(supportDelta);
        configuration.setFrequentItemSetCount(frequentItemSetCount);
        configuration.setGenerateRules(generateRules);
        configuration.setMinConfidence(minConfidence);
        configuration.setMaxConfidence(maxConfidence);
        configuration.setConfidenceDelta(confidenceDelta);
        configuration.setRuleCount(ruleCount);

        Output actualData = processor.generateApriori("src/test/resources/drivers.csv",null, configuration);
        //FileUtil.createFile("",actualData);
        try {
            FileOutputStream fileOutputStream= new FileOutputStream("src/test/resources/output.txt");
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(actualData);
            objectOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        assertNotNull(actualData);
    }


}
