package ca.ucalgary.rules599.modules;

import ca.ucalgary.rules599.datastructure.Pair;
import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.Transaction;

import ca.ucalgary.rules599.rules.FrequentItemSets;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;

import static ca.ucalgary.rules599.util.Condition.ensureAtLeast;
import static ca.ucalgary.rules599.util.Condition.ensureAtMaximum;
import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class FrequentItemSetMinerModule<ItemType extends Item> implements FrequentItemSetMiner<ItemType> {


    private static final Logger LOGGER = LoggerFactory.getLogger(FrequentItemSetMinerModule.class);


    @NotNull
    private Pair<Collection<TransactionalItemSet<ItemType>>, Integer> generateInitialItemSets(
            @NotNull final Iterable<Transaction<ItemType>> iterable) {
        Map<Integer, TransactionalItemSet<ItemType>> itemSets = new HashMap<>();
        int transactionCount = 0;

        for (Transaction<ItemType> transaction : iterable) {
            for (ItemType item : transaction) {
                TransactionalItemSet<ItemType> itemSet = new TransactionalItemSet<>();
                itemSet.add(item);
                TransactionalItemSet<ItemType> previous = itemSets.putIfAbsent(itemSet.hashCode(), itemSet);

                if (previous != null) {
                    previous.getTransactions().put(transactionCount, transaction);
                } else {
                    itemSet.getTransactions().put(transactionCount, transaction);
                }
            }

            transactionCount++;
        }

        return Pair.create(itemSets.values(), transactionCount);
    }


    @NotNull
    private List<TransactionalItemSet<ItemType>> filterFrequentItemSets(
            @NotNull final Collection<TransactionalItemSet<ItemType>> itemSets,
            final int transactionCount, final int k, final double minSupport) {
        List<TransactionalItemSet<ItemType>> frequentCandidates = new ArrayList<>(itemSets.size());

        for (TransactionalItemSet<ItemType> candidate : itemSets) {
            if (k > 1) {
                Set<Map.Entry<Integer, Transaction<ItemType>>> transactionSet = candidate
                        .getTransactions()
                        .entrySet();
                Map<Integer, Transaction<ItemType>> transactions = new HashMap<>(transactionSet.size());

                for (Map.Entry<Integer, Transaction<ItemType>> entry : transactionSet) {
                    Transaction<ItemType> transaction = entry.getValue();
                    Set<Integer> containedItemHashes = new HashSet<>();

                    for (ItemType item : transaction) {
                        if (candidate.contains(item)) {
                            containedItemHashes.add(item.hashCode());
                        }
                    }

                    if (containedItemHashes.size() >= candidate.size()) {
                        transactions.put(entry.getKey(), transaction);
                    }
                }

                candidate.setTransactions(transactions);
            }

            int occurrences = candidate.getTransactions().size();
            double support = calculateSupport(transactionCount, occurrences);
            candidate.setSupport(support);

            if (support >= minSupport) {
                frequentCandidates.add(candidate);
            }
        }

        return frequentCandidates;
    }


    @NotNull
    private Collection<TransactionalItemSet<ItemType>> combineItemSets(
            @NotNull final List<TransactionalItemSet<ItemType>> itemSets,
            final int k) {
        Set<TransactionalItemSet<ItemType>> combinedItemSets = new HashSet<>(IntStream.range(1, itemSets.size()).reduce(0, (x, y) -> (x + y)), 1);

        for (int i = 0; i < itemSets.size(); i++) {
            for (int j = i + 1; j < itemSets.size(); j++) {
                TransactionalItemSet<ItemType> itemSet1 = itemSets.get(i);
                TransactionalItemSet<ItemType> itemSet2 = itemSets.get(j);
                Iterator<ItemType> iterator1 = itemSet1.iterator();
                Iterator<ItemType> iterator2 = itemSet2.iterator();
                ItemType itemToAdd = null;
                int index = 0;

                while (iterator1.hasNext() && iterator2.hasNext()) {
                    ItemType item2 = iterator2.next();

                    if (index < k - 1) {
                        if (!item2.equals(iterator1.next())) {
                            itemToAdd = null;
                            break;
                        }
                    } else {
                        itemToAdd = item2;
                    }

                    index++;
                }

                if (itemToAdd != null) {
                    TransactionalItemSet<ItemType> combinedItemSet = new TransactionalItemSet<>(
                            itemSet1);
                    combinedItemSet.add(itemToAdd);
                    combinedItemSet.getTransactions().putAll(itemSet2.getTransactions());
                    combinedItemSets.add(combinedItemSet);
                }
            }
        }

        return combinedItemSets;
    }


    private double calculateSupport(final int transactions, final int occurrences) {
        return transactions > 0 ? (double) occurrences / (double) transactions : 0;
    }

    @NotNull
    public final Map<Integer, TransactionalItemSet<ItemType>> findFrequentItemSets(
            @NotNull final Iterable<Transaction<ItemType>> iterable, final double minSupport) {
        ensureNotNull(iterable, "The iterable may not be null");
        ensureAtLeast(minSupport, 0, "The minimum support must be at least 0");
        ensureAtMaximum(minSupport, 1, "The minimum support must be at maximum 1");
        LOGGER.debug("Searching for frequent item sets");
        Map<Integer, TransactionalItemSet<ItemType>> frequentItemSets = new HashMap<>();
        int k = 1;
        Pair<Collection<TransactionalItemSet<ItemType>>, Integer> pair = generateInitialItemSets(
                iterable);
        Collection<TransactionalItemSet<ItemType>> candidates = pair.first;
        int transactionCount = pair.second;

        while (!candidates.isEmpty()) {
            LOGGER.trace("k = {}", k);
            LOGGER.trace("C_{} = {}", k, candidates);
            List<TransactionalItemSet<ItemType>> frequentCandidates = filterFrequentItemSets(
                    candidates, transactionCount, k, minSupport);
            LOGGER.trace("S_{} = {}", k, frequentCandidates);
            candidates = combineItemSets(frequentCandidates, k);
            frequentCandidates.forEach(x -> frequentItemSets.put(x.hashCode(), x));
            k++;
        }

        LOGGER.debug("Found {} frequent item sets", frequentItemSets.size());
        LOGGER.debug("Frequent item sets = {}",
                FrequentItemSets.formatFrequentItemSets(frequentItemSets.values()));
        return frequentItemSets;
    }

}
