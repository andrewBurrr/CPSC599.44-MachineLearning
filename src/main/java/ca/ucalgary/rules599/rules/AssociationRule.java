package ca.ucalgary.rules599.rules;

import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.ItemSet;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import static ca.ucalgary.rules599.util.Condition.ensureAtLeast;
import static ca.ucalgary.rules599.util.Condition.ensureAtMaximum;
import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class AssociationRule<ItemType extends Item> implements Comparable<AssociationRule>,
        Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private final ItemSet<ItemType> antecedent;
    private final ItemSet<ItemType> consequence;
    private final double support;


    public AssociationRule(@NotNull final ItemSet<ItemType> body,
                           @NotNull final ItemSet<ItemType> head,
                           final double support) {
        ensureNotNull(body, "The body may not be null");
        ensureNotNull(head, "The consequence may not be null");
        ensureAtLeast(support, 0, "The support must be at least 0");
        ensureAtMaximum(support, 1, "The support must be at maximum 1");
        this.antecedent = body;
        this.consequence = head;
        this.support = support;
    }


    @NotNull
    public final ItemSet<ItemType> getBody() {
        return antecedent;
    }

    @NotNull
    public final ItemSet<ItemType> getConsequence() {
        return consequence;
    }

    public final double getSupport() {
        return support;
    }

    @SafeVarargs
    public final <T extends ItemType> boolean covers(@NotNull final T... items) {
        ensureNotNull(items, "The array may not be nul");
        return covers(Arrays.asList(items));
    }

    public final boolean covers(@NotNull final Iterable<? extends ItemType> items) {
        ensureNotNull(items, "The iterable may not be null");
        return covers(items.iterator());
    }

    public final boolean covers(@NotNull final Iterator<? extends ItemType> iterator) {
        ensureNotNull(iterator, "The iterator may not be null");

        for (ItemType bodyItem : antecedent) {
            boolean contains = false;

            while (iterator.hasNext()) {
                if (bodyItem.equals(iterator.next())) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                return false;
            }
        }

        return true;
    }

    @Override
    public final int compareTo(@NotNull final AssociationRule o) {
        return Double.compare(getSupport(), o.getSupport());
    }

    @Override
    public final AssociationRule<ItemType> clone() {
        return new AssociationRule<>(antecedent.clone(), consequence.clone(), support);
    }

    @Override
    public final String toString() {
        return antecedent.toString() + " -> " + consequence.toString();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + antecedent.hashCode();
        result = prime * result + consequence.hashCode();
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AssociationRule other = (AssociationRule) obj;
        return antecedent.equals(other.antecedent) && consequence.equals(other.consequence);
    }

}