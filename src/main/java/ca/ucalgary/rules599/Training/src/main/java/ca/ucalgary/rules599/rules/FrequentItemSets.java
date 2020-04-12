package ca.ucalgary.rules599.rules;

import ca.ucalgary.rules599.datastructure.Filterable;
import ca.ucalgary.rules599.datastructure.Sortable;
import ca.ucalgary.rules599.datastructure.SortedArraySet;
import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.ItemSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class FrequentItemSets<ItemType extends Item> extends
        SortedArraySet<ItemSet<ItemType>> implements Sortable<FrequentItemSets<ItemType>, ItemSet>,
        Filterable<FrequentItemSets<ItemType>, ItemSet>, Serializable, Cloneable {


    private static final long serialVersionUID = 1L;


    public FrequentItemSets(@Nullable final Comparator<? super ItemSet<ItemType>> comparator) {
        super(comparator);
    }


    public FrequentItemSets(@NotNull final Collection<ItemSet<ItemType>> itemSets,
                            @Nullable final Comparator<? super ItemSet<ItemType>> comparator) {
        super(itemSets, comparator);
    }


    @NotNull
    public static <T> String formatFrequentItemSets(
            @NotNull final Collection<? extends ItemSet<T>> frequentItemSets) {
        ensureNotNull(frequentItemSets, "The collection may not be null");
        StringBuilder stringBuilder = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(1);
        decimalFormat.setMaximumFractionDigits(2);
        Iterator<? extends ItemSet<T>> iterator = frequentItemSets.iterator();
        stringBuilder.append("[");

        while (iterator.hasNext()) {
            ItemSet<T> itemSet = iterator.next();
            stringBuilder.append(itemSet.toString());
            stringBuilder.append(" (support = ");
            stringBuilder.append(decimalFormat.format(itemSet.getSupport()));
            stringBuilder.append(")");

            if (iterator.hasNext()) {
                stringBuilder.append(",\n");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @NotNull
    @Override
    public final FrequentItemSets<ItemType> sort(@Nullable final Comparator<ItemSet> comparator) {
        return new FrequentItemSets<ItemType>(this, comparator);
    }

    @NotNull
    @Override
    public final FrequentItemSets<ItemType> filter(@NotNull final Predicate<ItemSet> predicate) {
        ensureNotNull(predicate, "The predicate may not be null");
        FrequentItemSets<ItemType> filteredFrequentItemSets = new FrequentItemSets<>(comparator());

        for (ItemSet<ItemType> itemSet : this) {
            if (predicate.test(itemSet)) {
                filteredFrequentItemSets.add(itemSet);
            }
        }

        return filteredFrequentItemSets;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public final FrequentItemSets<ItemType> clone() {
        return new FrequentItemSets<>(this, comparator());
    }

    @Override
    public final String toString() {
        return formatFrequentItemSets(this);
    }

}