package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.*;
import ca.ucalgary.rules599.rules.Apriori;
import ca.ucalgary.rules599.rules.AssociationRule;
import ca.ucalgary.rules599.rules.Output;
import ca.ucalgary.rules599.rules.RuleSet;
import ca.ucalgary.rules599.task.AssociationRuleGeneratorTask;
import ca.ucalgary.rules599.task.FrequentItemSetMinerTask;
import ca.ucalgary.rules599.util.DataIterator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AprioriTest extends AbstractDataTest {
    static AccidentAttribute item1,item2;


    @Before
    public void setup() {

        item1 = AccidentAttribute.builder().name("2006").build();
        item2 = AccidentAttribute.builder().name("2007").build();

    }
    @Test
    public final void testConfigurationDefaultConstructor() {
        Apriori.Configuration configuration = new Apriori.Configuration();
        assertEquals(0.0, configuration.getMinSupport(),0.001);
        assertEquals(1.0, configuration.getMaxSupport(),0.001);
        assertEquals(0.1, configuration.getSupportDelta(),0.001);
        assertEquals(0, configuration.getFrequentItemSetCount());
        assertFalse(configuration.isGeneratingRules());
        assertEquals(0.0, configuration.getMinConfidence(),0.001);
        assertEquals(1.0, configuration.getMaxConfidence(),0.001);
        assertEquals(0.1, configuration.getConfidenceDelta(),0.001);
        assertEquals(0, configuration.getRuleCount());
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetMinSupportThrowsExceptionWhenSupportIsLessThanZero() {
        new Apriori.Configuration().setMinSupport(-0.1);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetMinSupportThrowsExceptionWhenSupportIsGreaterThanMaxSupport() {
        Apriori.Configuration configuration = new Apriori.Configuration();
        configuration.setMaxSupport(0.8);
        configuration.setMinSupport(0.9);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetMaxSupportThrowsExceptionWhenSupportIsGreaterThanOne() {
        new Apriori.Configuration().setMaxSupport(1.1);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetMaxSupportThrowsExceptionWhenSupportIsLessThanMinSupport() {
        Apriori.Configuration configuration = new Apriori.Configuration();
        configuration.setMaxSupport(configuration.getMinSupport() - 0.1);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetSupportDeltaThrowsExceptionWhenDeltaIsNotGreaterThanOne() {
        new Apriori.Configuration().setSupportDelta(0);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetFrequentItemSetCountThrowsExceptionWhenCountIsLessThanZero() {
        new Apriori.Configuration().setFrequentItemSetCount(-1);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetMinConfidenceThrowsExceptionWhenConfidenceIsLessThanZero() {
        new Apriori.Configuration().setMinConfidence(-0.1);
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the minimum confidence, if the confidence is greater than the maximum confidence.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetMinConfidenceThrowsExceptionWhenConfidenceIsGreaterThanMaxConfidence() {
        Apriori.Configuration configuration = new Apriori.Configuration();
        configuration.setMaxConfidence(0.8);
        configuration.setMinConfidence(0.9);
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the maximum confidence, if the confidence is greater than 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetMaxConfidenceThrowsExceptionWhenConfidenceIsGreaterThanOne() {
        new Apriori.Configuration().setMaxConfidence(1.1);
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the maximum confidence, if the confidence is less than the minimum confidence.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetMaxConfidenceThrowsExceptionWhenConfidenceIsLessThanMinConfidence() {
        Apriori.Configuration configuration = new Apriori.Configuration();
        configuration.setMaxConfidence(configuration.getMinConfidence() - 0.1);
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the confidence delta, if the delta is not greater than 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetConfidenceDeltaThrowsExceptionWhenDeltaIsNotGreaterThanZero() {
        new Apriori.Configuration().setConfidenceDelta(0);
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the rule count, if the count is less than 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetRuleCountThrowsExceptionWhenCountIsLessThanZero() {
        new Apriori.Configuration().setRuleCount(-1);
    }

    /**
     * Tests the functionality of the clone-method.
     */
    @Test
    public final void testClone() {
        double minSupport = 0.3;
        double maxSupport = 0.8;
        double supportDelta = 0.2;
        int frequentItemSetCount = 2;
        boolean generateRules = true;
        double minConfidence = 0.8;
        double maxConfidence = 0.9;
        double confidenceDelta = 0.2;
        int ruleCount = 2;
        Apriori.Configuration configuration1 = new Apriori.Configuration();
        configuration1.setMinSupport(minSupport);
        configuration1.setMaxSupport(maxSupport);
        configuration1.setSupportDelta(supportDelta);
        configuration1.setFrequentItemSetCount(frequentItemSetCount);
        configuration1.setGenerateRules(generateRules);
        configuration1.setMinConfidence(minConfidence);
        configuration1.setMaxConfidence(maxConfidence);
        configuration1.setConfidenceDelta(confidenceDelta);
        configuration1.setRuleCount(ruleCount);
        Apriori.Configuration configuration2 = configuration1.clone();
        assertEquals(configuration1.getMinSupport(), configuration2.getMinSupport(),0.001);
        assertEquals(configuration1.getMaxSupport(), configuration2.getMaxSupport(),0.001);
        assertEquals(configuration1.getSupportDelta(), configuration2.getSupportDelta(),0.001);
        assertEquals(configuration1.getFrequentItemSetCount(),
                configuration2.getFrequentItemSetCount());
        assertEquals(configuration1.isGeneratingRules(), configuration2.isGeneratingRules());
        assertEquals(configuration1.getMinConfidence(), configuration2.getMinConfidence(),0.001);
        assertEquals(configuration1.getMaxConfidence(), configuration2.getMaxConfidence(),0.001);
        assertEquals(configuration1.getConfidenceDelta(), configuration2.getConfidenceDelta(),0.001);
        assertEquals(configuration1.getRuleCount(), configuration2.getRuleCount());
    }


    @Test
    public final void testToString() {
        double minSupport = 0.3;
        double maxSupport = 0.8;
        double supportDelta = 0.2;
        int frequentItemSetCount = 2;
        boolean generateRules = true;
        double minConfidence = 0.8;
        double maxConfidence = 0.9;
        double confidenceDelta = 0.2;
        int ruleCount = 2;
        Apriori.Configuration configuration = new Apriori.Configuration();
        configuration.setMinSupport(minSupport);
        configuration.setMaxSupport(maxSupport);
        configuration.setSupportDelta(supportDelta);
        configuration.setFrequentItemSetCount(frequentItemSetCount);
        configuration.setGenerateRules(generateRules);
        configuration.setMinConfidence(minConfidence);
        configuration.setMaxConfidence(maxConfidence);
        configuration.setConfidenceDelta(confidenceDelta);
        configuration.setRuleCount(ruleCount);
        assertEquals("[minSupport=" + minSupport + ", maxSupport=" + maxSupport +
                ", supportDelta=" + supportDelta + ", frequentItemSetCount=" +
                frequentItemSetCount + ", generateRules=" + generateRules + ", minConfidence=" +
                minConfidence + ", maxConfidence=" + maxConfidence + ", confidenceDelta=" +
                confidenceDelta + ", ruleCount=" + ruleCount + "]", configuration.toString());
    }


    @Test
    public final void testHashCode() {
        Apriori.Configuration configuration1 = new Apriori.Configuration();
        Apriori.Configuration configuration2 = new Apriori.Configuration();
        assertEquals(configuration1.hashCode(), configuration1.hashCode());
        assertEquals(configuration1.hashCode(), configuration2.hashCode());
        configuration1.setMinSupport(0.6);
        assertNotSame(configuration1.hashCode(), configuration2.hashCode());
        configuration2.setMinSupport(configuration1.getMinSupport());
        configuration1.setMaxSupport(0.9);
        assertNotSame(configuration1.hashCode(), configuration2.hashCode());
        configuration2.setMaxSupport(configuration1.getMaxSupport());
        configuration1.setSupportDelta(0.2);
        assertNotSame(configuration1.hashCode(), configuration2.hashCode());
        configuration2.setSupportDelta(configuration1.getSupportDelta());
        configuration1.setFrequentItemSetCount(2);
        assertNotSame(configuration1.hashCode(), configuration2.hashCode());
        configuration2.setFrequentItemSetCount(configuration1.getFrequentItemSetCount());
        configuration1.setGenerateRules(true);
        assertNotSame(configuration1.hashCode(), configuration2.hashCode());
        configuration2.setGenerateRules(configuration1.isGeneratingRules());
        configuration1.setMinConfidence(0.6);
        assertNotSame(configuration1.hashCode(), configuration2.hashCode());
        configuration2.setMinConfidence(configuration1.getMinConfidence());
        configuration1.setMaxConfidence(0.9);
        assertNotSame(configuration1.hashCode(), configuration2.hashCode());
        configuration2.setMaxConfidence(configuration1.getMaxConfidence());
        configuration1.setConfidenceDelta(0.2);
        assertNotSame(configuration1.hashCode(), configuration2.hashCode());
        configuration2.setConfidenceDelta(configuration1.getConfidenceDelta());
        configuration1.setRuleCount(2);
        assertNotSame(configuration1.hashCode(), configuration2.hashCode());
    }


    @Test
    public final void testEquals() {
        Apriori.Configuration configuration1 = new Apriori.Configuration();
        Apriori.Configuration configuration2 = new Apriori.Configuration();
        assertFalse(configuration1.equals(null));
        assertFalse(configuration1.equals(new Object()));
        assertTrue(configuration1.equals(configuration1));
        assertTrue(configuration1.equals(configuration2));
        configuration1.setMinSupport(0.6);
        assertFalse(configuration1.equals(configuration2));
        configuration2.setMinSupport(configuration1.getMinSupport());
        configuration1.setMaxSupport(0.9);
        assertFalse(configuration1.equals(configuration2));
        configuration2.setMaxSupport(configuration1.getMaxSupport());
        configuration1.setSupportDelta(0.2);
        assertFalse(configuration1.equals(configuration2));
        configuration2.setSupportDelta(configuration1.getSupportDelta());
        configuration1.setFrequentItemSetCount(2);
        assertFalse(configuration1.equals(configuration2));
        configuration2.setFrequentItemSetCount(configuration1.getFrequentItemSetCount());
        configuration1.setGenerateRules(true);
        assertFalse(configuration1.equals(configuration2));
        configuration2.setGenerateRules(configuration1.isGeneratingRules());
        configuration1.setMinConfidence(0.6);
        assertFalse(configuration1.equals(configuration2));
        configuration2.setMinConfidence(configuration1.getMinConfidence());
        configuration1.setMaxConfidence(0.9);
        assertFalse(configuration1.equals(configuration2));
        configuration2.setMaxConfidence(configuration1.getMaxConfidence());
        configuration1.setConfidenceDelta(0.2);
        assertFalse(configuration1.equals(configuration2));
        configuration2.setConfidenceDelta(configuration1.getConfidenceDelta());
        configuration1.setRuleCount(2);
        assertFalse(configuration1.equals(configuration2));
    }

    /**
     * Tests the functionality of the builder, when configuring the Apriori algorithm to not trying
     * to find a specific number of frequent item sets.
     */
    @Test
    public final void testBuilderWhenNotTryingToFindASpecificNumberOfFrequentItemSets() {
        double minSupport = 0.3;
        double maxSupport = 0.8;
        double supportDelta = 0.2;
        int frequentItemSetCount = 0;
        Apriori<AccidentData> apriori = new Apriori.Builder<AccidentData>(minSupport)
                .maxSupport(maxSupport)
                .supportDelta(supportDelta)
                .frequentItemSetCount(frequentItemSetCount).create();
        Apriori.Configuration configuration = apriori.getConfiguration();
        assertEquals(minSupport, configuration.getMinSupport(),0.001);
        assertEquals(maxSupport, configuration.getMaxSupport(),0.001);
        assertEquals(supportDelta, configuration.getSupportDelta(),0.001);
        assertEquals(frequentItemSetCount, configuration.getFrequentItemSetCount());
        assertFalse(configuration.isGeneratingRules());
    }

    /**
     * Tests the functionality of the builder, when configuring the Apriori algorithm to trying to
     * find a specific number of frequent item sets.
     */
    @Test
    public final void testBuilderWhenTryingToFindASpecificNumberOfFrequentItemSets() {
        double minSupport = 0.3;
        double maxSupport = 0.8;
        double supportDelta = 0.2;
        int frequentItemSetCount = 2;
        Apriori<AccidentData> apriori = new Apriori.Builder<AccidentData>(frequentItemSetCount)
                .minSupport(minSupport)
                .maxSupport(maxSupport)
                .supportDelta(supportDelta).create();
        Apriori.Configuration configuration = apriori.getConfiguration();
        assertEquals(minSupport, configuration.getMinSupport(),0.001);
        assertEquals(maxSupport, configuration.getMaxSupport(),0.001);
        assertEquals(supportDelta, configuration.getSupportDelta(),0.001);
        assertEquals(frequentItemSetCount, configuration.getFrequentItemSetCount());
        assertFalse(configuration.isGeneratingRules());
    }

    /**
     * Tests the functionality of the builder, when configuring the Apriori algorithm to generate
     * association rules.
     */
    @Test
    public final void testBuilderWhenGeneratingAssociationRules() {
        double minSupport = 0.3;
        double maxSupport = 0.8;
        double supportDelta = 0.2;
        int frequentItemSetCount = 2;
        double minConfidence = 0.4;
        double maxConfidence = 0.8;
        double confidenceDelta = 0.2;
        int ruleCount = 0;
        Apriori<AccidentAttribute> apriori = new Apriori.Builder<AccidentAttribute>(frequentItemSetCount)
                .generateRules(minConfidence).minSupport(minSupport).maxSupport(maxSupport)
                .supportDelta(supportDelta).frequentItemSetCount(frequentItemSetCount)
                .maxConfidence(maxConfidence).confidenceDelta(confidenceDelta).ruleCount(ruleCount)
                .create();
        Apriori.Configuration configuration = apriori.getConfiguration();
        assertEquals(minSupport, configuration.getMinSupport(),0.001);
        assertEquals(maxSupport, configuration.getMaxSupport(),0.001);
        assertEquals(supportDelta, configuration.getSupportDelta(),0.001);
        assertEquals(frequentItemSetCount, configuration.getFrequentItemSetCount());
        assertTrue(configuration.isGeneratingRules());
        assertEquals(minConfidence, configuration.getMinConfidence(),0.001);
        assertEquals(maxConfidence, configuration.getMaxConfidence(),0.001);
        assertEquals(confidenceDelta, configuration.getConfidenceDelta(),0.001);
        assertEquals(ruleCount, configuration.getRuleCount());
    }

    /**
     * Tests the functionality of the builder, when configuring the Apriori algorithm to trying to
     * generate a specific number of association rules.
     */
    @Test
    public final void testBuilderWhenTryingToGenerateASpecificNumberOfAssociationRules() {
        double minSupport = 0.3;
        double maxSupport = 0.8;
        double supportDelta = 0.2;
        int frequentItemSetCount = 2;
        double minConfidence = 0.4;
        double maxConfidence = 0.8;
        double confidenceDelta = 0.2;
        int ruleCount = 2;
        Apriori<AccidentAttribute> apriori = new Apriori.Builder<AccidentAttribute>(frequentItemSetCount)
                .generateRules(ruleCount).minSupport(minSupport).maxSupport(maxSupport)
                .supportDelta(supportDelta).frequentItemSetCount(frequentItemSetCount)
                .minConfidence(minConfidence).maxConfidence(maxConfidence)
                .confidenceDelta(confidenceDelta).create();
        Apriori.Configuration configuration = apriori.getConfiguration();
        assertEquals(minSupport, configuration.getMinSupport(),0.001);
        assertEquals(maxSupport, configuration.getMaxSupport(),0.001);
        assertEquals(supportDelta, configuration.getSupportDelta(),0.001);
        assertEquals(frequentItemSetCount, configuration.getFrequentItemSetCount());
        assertTrue(configuration.isGeneratingRules());
        assertEquals(minConfidence, configuration.getMinConfidence(),0.001);
        assertEquals(maxConfidence, configuration.getMaxConfidence(),0.001);
        assertEquals(confidenceDelta, configuration.getConfidenceDelta(),0.001);
        assertEquals(ruleCount, configuration.getRuleCount());
    }

    /**
     * Tests, if all class members are set correctly by the constructor, which expects a
     * configuration as a parameter.
     */
    @Test
    public final void testConstructorWithConfigurationParameter() {
        Apriori.Configuration configuration = mock(Apriori.Configuration.class);
        Apriori<AccidentData> apriori = new Apriori<>(configuration);
        assertEquals(configuration, apriori.getConfiguration());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, which expects
     * a configuration as a parameter, if the configuration is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithConfigurationParameterThrowsException() {
        new Apriori<AccidentData>(null);
    }

    /**
     * Tests, if all class members are set correctly by the constructor, which expects a
     * configuration and tasks as parameters.
     */
    @Test
    public final void testConstructorWithConfigurationAndTaskParameters() {
        Apriori.Configuration configuration = mock(Apriori.Configuration.class);
        Apriori<AccidentData> apriori = new Apriori<>(configuration,
                new FrequentItemSetMinerTask<>(configuration),
                new AssociationRuleGeneratorTask<>(configuration));
        assertEquals(configuration, apriori.getConfiguration());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, which expects
     * a configuration and tasks as parameters, if the configuration is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithConfigurationAndTaskParametersThrowsExceptionWhenConfigurationIsNull() {
        Apriori.Configuration configuration = mock(Apriori.Configuration.class);
        new Apriori<AccidentData>(null, new FrequentItemSetMinerTask<>(configuration),
                new AssociationRuleGeneratorTask<>(configuration));
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, which expects
     * a configuration and tasks as parameters, if the frequent item set miner task is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithConfigurationAndTaskParametersThrowsExceptionWhenFrequentItemSetMinerTaskIsNull() {
        Apriori.Configuration configuration = mock(Apriori.Configuration.class);
        new Apriori<AccidentData>(configuration, null,
                new AssociationRuleGeneratorTask<>(configuration));
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, which expects
     * a configuration and tasks as parameters, if the association rule generator task is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithConfigurationAndTaskParametersThrowsExceptionWhenAssociationRuleGeneratorTaskIsNull() {
        Apriori.Configuration configuration = mock(Apriori.Configuration.class);
        new Apriori<AccidentData>(configuration, new FrequentItemSetMinerTask<>(configuration), null);
    }

    /**
     * Tests the functionality of the method, which allows to execute the Apriori algorithm, if no
     * association rules should be generated.
     */
    @Test
    public final void testExecuteWhenNotGeneratingRules() {
        Apriori.Configuration configuration = mock(Apriori.Configuration.class);
        when(configuration.isGeneratingRules()).thenReturn(false);
        Map<Integer, TransactionalItemSet<AccidentAttribute>> map = new HashMap<>();
        TransactionalItemSet<AccidentAttribute> itemSet1 = new TransactionalItemSet<>();
        itemSet1.add(item1);
        itemSet1.setSupport(1.0);
        TransactionalItemSet<AccidentAttribute> itemSet2 = new TransactionalItemSet<>();
        itemSet2.add(item2);
        itemSet2.setSupport(0.9);
        map.put(itemSet1.hashCode(), itemSet1);
        map.put(itemSet2.hashCode(), itemSet2);
        FrequentItemSetMinerTask<AccidentAttribute> frequentItemSetMinerTask = new FrequentItemSetMinerTask<>(
                configuration, (iterator, minSupport) -> map);
        AssociationRuleGeneratorTask<AccidentAttribute> associationRuleGeneratorTask = new AssociationRuleGeneratorTask<>(
                configuration, (frequentItemSets, minConfidence) -> {
            throw new RuntimeException();
        });
        File file = getInputFile(INPUT_FILE_1);
        Apriori<AccidentAttribute> apriori = new Apriori<>(configuration, frequentItemSetMinerTask,
                associationRuleGeneratorTask);
        Output<AccidentAttribute> output = apriori.execute(() -> new DataIterator(file,1,true));
        assertEquals(configuration, output.getConfiguration());
        assertNull(output.getRuleSet());
        SortedSet<ItemSet<AccidentAttribute>> set = output.getFrequentItemSets();
        assertEquals(map.size(), set.size());
        assertFalse(set.first() instanceof TransactionalItemSet);
        assertEquals(1, set.first().size());
        assertEquals(item1, set.first().first());
        assertFalse(set.last() instanceof TransactionalItemSet);
        assertEquals(1, set.last().size());
        assertEquals(item2, set.last().first());
    }

    /**
     * Tests the functionality of the method, which allows to execute the Apriori algorithm, if
     * association rules should be generated.
     */
    @Test
    public final void testExecuteWhenGeneratingRules() {
        Apriori.Configuration configuration = mock(Apriori.Configuration.class);
        when(configuration.isGeneratingRules()).thenReturn(true);
        Map<Integer, TransactionalItemSet<AccidentAttribute>> map = new HashMap<>();
        TransactionalItemSet<AccidentAttribute> itemSet1 = new TransactionalItemSet<>();
        itemSet1.add(item1);
        itemSet1.setSupport(1.0);
        TransactionalItemSet<AccidentAttribute> itemSet2 = new TransactionalItemSet<>();
        itemSet2.add(item2);
        itemSet2.setSupport(0.9);
        map.put(itemSet1.hashCode(), itemSet1);
        map.put(itemSet2.hashCode(), itemSet2);
        RuleSet<AccidentAttribute> ruleSet = new RuleSet<>(null);
        AssociationRule<AccidentAttribute> associationRule = new AssociationRule<>(new ItemSet<>(),
                new ItemSet<>(), 0.5);
        ruleSet.add(associationRule);
        FrequentItemSetMinerTask<AccidentAttribute> frequentItemSetMinerTask = new FrequentItemSetMinerTask<>(
                configuration, (iterator, minSupport) -> map);
        AssociationRuleGeneratorTask<AccidentAttribute> associationRuleGeneratorTask = new AssociationRuleGeneratorTask<>(
                configuration, (frequentItemSets, minConfidence) -> ruleSet);
        File file = getInputFile(INPUT_FILE_1);
        Apriori<AccidentAttribute> apriori = new Apriori<>(configuration, frequentItemSetMinerTask,
                associationRuleGeneratorTask);
        Output<AccidentAttribute> output = apriori.execute(() -> new DataIterator(file,1,true));
        //
        assertEquals(configuration, output.getConfiguration());
        assertEquals(ruleSet, output.getRuleSet());
        SortedSet<ItemSet<AccidentAttribute>> set = output.getFrequentItemSets();
        assertEquals(map.size(), set.size());
        assertFalse(set.first() instanceof TransactionalItemSet);
        assertEquals(1, set.first().size());
        assertEquals(item1, set.first().first());
        assertFalse(set.last() instanceof TransactionalItemSet);
        assertEquals(1, set.last().size());
        assertEquals(item2, set.last().first());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * execute the Apriori algorithm, if the iterator, which is passed as a parameter, is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testExecuteThrowsException() {
        Apriori<AccidentData> apriori = new Apriori<>(mock(Apriori.Configuration.class));
        apriori.execute(null);
    }

}