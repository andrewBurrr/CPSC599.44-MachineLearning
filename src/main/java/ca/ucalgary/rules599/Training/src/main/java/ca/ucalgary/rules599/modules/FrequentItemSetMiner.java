package ca.ucalgary.rules599.modules;

import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface FrequentItemSetMiner<ItemType extends Item> {


    @NotNull
    Map<Integer, TransactionalItemSet<ItemType>> findFrequentItemSets(
            @NotNull Iterable<Transaction<ItemType>> iterable, double minSupport);

}