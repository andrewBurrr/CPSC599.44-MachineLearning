package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.Training.GAImplementation;
import ca.ucalgary.rules599.Training.Preprocessor;
import ca.ucalgary.rules599.Training.Processor;
import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.*;
import ca.ucalgary.rules599.rules.Apriori;
import ca.ucalgary.rules599.rules.Output;
import ca.ucalgary.rules599.rules.RuleSet;
import org.junit.Before;
import org.junit.Test;


import java.io.File;
import java.util.List;
import java.util.Map;

import static ca.ucalgary.rules599.test.AbstractDataTest.INPUT_FILE_1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PreProcessorTest {

    private Preprocessor preprocessor;
    private Processor processor;
    private GAImplementation gaImplementation;

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
        List<AccidentData> actualData = preprocessor.processInitialData(file,"src/test/resources/output.txt", 0.3f);
        assertEquals(actualData.size(), 749);
    }

    @Test
    public final void InitialProcessingTest() {
        RuleSet actualData = processor.generateRules("src/test/resources/driversTest.csv",0.8, 0.8);
        assertNotNull(actualData);
    }


    @Test
    public final void InitialProcessingGenerateFrequentItemsTest() {
        Map<Integer, TransactionalItemSet<AccidentAttribute>> actualData = processor.findFrequentItemSets("src/test/resources/driversTest.csv",0.8);
        assertNotNull(actualData);
    }

    @Test
    public final void generateSpecificFrequentItemsTest() {
        Map<Integer, TransactionalItemSet<AccidentAttribute>> actualData = processor.findnItemFrequentItemSets("src/test/resources/driversTest.csv",0.8,1, 5);
        assertNotNull(actualData);
    }



    @Test
    public final void InitialProcessingGenerateGeneticFrequentItemsTest() {
        List<Map<String, int[]>> actualData = processor.findGeneticFrequentItemSets("src/test/resources/driversTest.csv",0.8,"src/test/resources/accidentAttributeValues.yml");
        assertNotNull(actualData);
    }



    @Test
    public final void GATestLoadingExitingRules() {
        Population population =  new Population();
        gaImplementation = new GAImplementation(population);
        Map<Integer,TransactionalItemSet<AccidentAttribute>> actualData=gaImplementation.updatedFrequentItemSets("");
        assertNotNull(actualData);
    }


    @Test
    public final void GACrossOverAndMutationTest() {
        Population population =  new Population();
        population.initializePopulation(processor.findnItemFrequentItemSets("src/test/resources/driversTest.csv",0.8,1, 5),5);
        gaImplementation = new GAImplementation(population);
        Population actualData =gaImplementation.runGA();
        assertNotNull(actualData);
    }


    @Test
    public final void GAOutputTest() {
        Apriori.Configuration configuration = new Apriori.Configuration();
        double minSupport = 0.5;
        double maxSupport = 0.8;
        double supportDelta = 0.2;
        int frequentItemSetCount = 500;
        boolean generateRules = true;
        double minConfidence = 0.8;
        double maxConfidence = 0.9;
        double confidenceDelta = 0.2;
        int ruleCount = 400;
        configuration.setMinSupport(minSupport);
        configuration.setMaxSupport(maxSupport);
        configuration.setSupportDelta(supportDelta);
        configuration.setFrequentItemSetCount(frequentItemSetCount);
        configuration.setGenerateRules(generateRules);
        configuration.setMinConfidence(minConfidence);
        configuration.setMaxConfidence(maxConfidence);
        configuration.setConfidenceDelta(confidenceDelta);
        configuration.setRuleCount(ruleCount);


        Population population =  new Population();
        population.initializePopulation(processor.findnItemFrequentItemSets("src/test/resources/driversTest.csv",0.8,1, 5),5);
        gaImplementation = new GAImplementation(population);
        Output<AccidentAttribute> actualData =gaImplementation.outputSortedRules("src/test/resources/driversTest.csv", "src/test/resources/knownRulest.txt", configuration);
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
        double minSupport = 0.5;
        double maxSupport = 0.8;
        double supportDelta = 0.2;
        int frequentItemSetCount = 500;
        boolean generateRules = true;
        double minConfidence = 0.8;
        double maxConfidence = 0.9;
        double confidenceDelta = 0.2;
        int ruleCount = 400;
        configuration.setMinSupport(minSupport);
        configuration.setMaxSupport(maxSupport);
        configuration.setSupportDelta(supportDelta);
        configuration.setFrequentItemSetCount(frequentItemSetCount);
        configuration.setGenerateRules(generateRules);
        configuration.setMinConfidence(minConfidence);
        configuration.setMaxConfidence(maxConfidence);
        configuration.setConfidenceDelta(confidenceDelta);
        configuration.setRuleCount(ruleCount);

        Output actualData = processor.generateApriori("src/test/resources/driversTest.csv",null, configuration);
        assertNotNull(actualData);
    }


}
