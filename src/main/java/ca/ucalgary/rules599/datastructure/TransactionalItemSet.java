package ca.ucalgary.rules599.datastructure;

import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.model.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class TransactionalItemSet<ItemType extends Item> extends ItemSet<ItemType> {


    private Map<Integer, Transaction<ItemType>> transactions;

    public TransactionalItemSet() {
        super();
        this.transactions = new HashMap<>();
    }

    public TransactionalItemSet(@NotNull final TransactionalItemSet<ItemType> itemSet) {
        super(itemSet);
        this.transactions = new HashMap<>(itemSet.transactions);
    }

    @NotNull
    public final Map<Integer, Transaction<ItemType>> getTransactions() {
        return transactions;
    }

    public final void setTransactions(
            @NotNull final Map<Integer, Transaction<ItemType>> transactions) {
        ensureNotNull(transactions, "The map may not be null");
        this.transactions = transactions;
    }

}
