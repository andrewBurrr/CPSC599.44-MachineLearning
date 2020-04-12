package ca.ucalgary.rules599.task;

import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.Transaction;
import ca.ucalgary.rules599.modules.FrequentItemSetMiner;
import ca.ucalgary.rules599.modules.FrequentItemSetMinerModule;
import ca.ucalgary.rules599.rules.Apriori;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class FrequentItemSetMinerTask<ItemType extends Item> extends AbstractTask<ItemType> {


    private final FrequentItemSetMiner<ItemType> frequentItemSetMiner;


    public FrequentItemSetMinerTask(@NotNull final Apriori.Configuration configuration) {
        this(configuration, new FrequentItemSetMinerModule<>());
    }


    public FrequentItemSetMinerTask(@NotNull final Apriori.Configuration configuration,
                                    @NotNull final FrequentItemSetMiner<ItemType> frequentItemSetMiner) {
        super(configuration);
        ensureNotNull(frequentItemSetMiner, "The frequent item set miner may not be null");
        this.frequentItemSetMiner = frequentItemSetMiner;
    }

    @NotNull
    public final Map<Integer, TransactionalItemSet<ItemType>> findFrequentItemSets(
            @NotNull final Iterable<Transaction<ItemType>> iterable) {
        if (getConfiguration().getFrequentItemSetCount() > 0) {
            Map<Integer, TransactionalItemSet<ItemType>> result = new HashMap<>();
            double currentMinSupport = getConfiguration().getMaxSupport();

            while (currentMinSupport >= getConfiguration().getMinSupport() &&
                    result.size() < getConfiguration().getFrequentItemSetCount()) {
                Map<Integer, TransactionalItemSet<ItemType>> frequentItemSets = frequentItemSetMiner
                        .findFrequentItemSets(iterable, currentMinSupport);

                if (frequentItemSets.size() >= result.size()) {
                    result = frequentItemSets;
                }

                currentMinSupport -= getConfiguration().getSupportDelta();
            }

            return result;
        } else {
            return frequentItemSetMiner
                    .findFrequentItemSets(iterable, getConfiguration().getMinSupport());
        }
    }

}