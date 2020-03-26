package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.AccidentData;
import ca.ucalgary.rules599.model.ItemSet;
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
                                          @NotNull final List<AccidentData> actualFrequentItemSets,
                                          @NotNull double[] actualSupports) {
        File inputFile = getInputFile(fileName);
        FrequentItemSetMinerModule<AccidentData> frequentItemSetMiner = new FrequentItemSetMinerModule<>();
        Map<Integer, TransactionalItemSet<AccidentData>> frequentItemSets = frequentItemSetMiner
                .findFrequentItemSets(() -> new DataIterator(inputFile), minSupport);
        int frequentItemSetCount = 0;

        for (Map.Entry<Integer, TransactionalItemSet<AccidentData>> entry : frequentItemSets
                .entrySet()) {
            int key = entry.getKey();
            ItemSet<AccidentData> itemSet = entry.getValue();

            for (AccidentData item : itemSet) {
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
        new FrequentItemSetMinerModule<AccidentData>()
                .findFrequentItemSets(() -> new DataIterator(inputFile), -0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testFindFrequentItemSetsThrowsExceptionWhenMinSupportIsGreaterThanOne() {
        File inputFile = getInputFile(INPUT_FILE_1);
        new FrequentItemSetMinerModule<AccidentData>()
                .findFrequentItemSets(() -> new DataIterator(inputFile), 1.1);
    }

}
