package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.model.AccidentAttribute;
import ca.ucalgary.rules599.modules.FrequentItemSetMinerModule;
import ca.ucalgary.rules599.util.DataIterator;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FrequentItemSetMinerTest extends AbstractDataTest {


    private void testFindFrequentItemSets(@NotNull final String fileName,
                                          final double minSupport,
                                          @NotNull final List<AccidentAttribute> actualFrequentItemSets,
                                          @NotNull double[] actualSupports) {
        File inputFile = getInputFile(fileName);
        FrequentItemSetMinerModule<AccidentAttribute> frequentItemSetMiner = new FrequentItemSetMinerModule<>();
        Map<Integer, TransactionalItemSet<AccidentAttribute>> frequentItemSets = frequentItemSetMiner
                .findFrequentItemSets(() -> new DataIterator(inputFile,1,true), minSupport);
        int frequentItemSetCount = 0;

        for (Map.Entry<Integer, TransactionalItemSet<AccidentAttribute>> entry : frequentItemSets
                .entrySet()) {
            int key = entry.getKey();
            ItemSet<AccidentAttribute> itemSet = entry.getValue();

            for (AccidentAttribute item : itemSet) {
                assertTrue(actualFrequentItemSets.contains(item));
            }

            assertEquals(actualSupports[frequentItemSetCount], itemSet.getSupport(), 0);
            assertEquals(itemSet.hashCode(), key);
            frequentItemSetCount++;
        }

        assertEquals(actualFrequentItemSets.size(), frequentItemSetCount);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testFindFrequentItemSetsThrowsExceptionWhenIteratorIsNull() {
        new FrequentItemSetMinerModule<>().findFrequentItemSets(null, 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testFindFrequentItemSetsThrowsExceptionWhenMinSupportIsLessThanZero() {
        File inputFile = getInputFile(INPUT_FILE_1);
        new FrequentItemSetMinerModule<AccidentAttribute>()
                .findFrequentItemSets(() -> new DataIterator(inputFile,1,true), -0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testFindFrequentItemSetsThrowsExceptionWhenMinSupportIsGreaterThanOne() {
        File inputFile = getInputFile(INPUT_FILE_1);
        new FrequentItemSetMinerModule<AccidentAttribute>()
                .findFrequentItemSets(() -> new DataIterator(inputFile,1,true), 1.1);
    }


}
