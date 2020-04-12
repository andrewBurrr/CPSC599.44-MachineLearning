package ca.ucalgary.rules599.model;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;

import static ca.ucalgary.rules599.util.Condition.ensureAtLeast;
import static ca.ucalgary.rules599.util.Condition.ensureAtMaximum;
import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class ItemSet<ItemType> implements SortedSet<ItemType>, Comparable<ItemSet<ItemType>>,
        Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private final SortedSet<ItemType> items;
    private double support;

    public ItemSet() {
        this.items = new TreeSet<>();
        setSupport(0);
    }

    public ItemSet(@NotNull final ItemSet<ItemType> itemSet) {
        ensureNotNull(itemSet, "The item set may not be null");
        this.items = new TreeSet<>(itemSet.items);
        setSupport(itemSet.support);
    }

    public final double getSupport() {
        return support;
    }
    public final void setSupport(final double support) {
        ensureAtLeast(support, 0, "The support must be at least 0");
        ensureAtMaximum(support, 1, "The support must be at least 1");
        this.support = support;
    }

    @Nullable
    public final Comparator<? super ItemType> comparator() {
        return items.comparator();
    }

    @NotNull
    public final SortedSet<ItemType> subSet(final ItemType fromElement, final ItemType toElement) {
        return items.subSet(fromElement, toElement);
    }

    @NotNull
    public final SortedSet<ItemType> headSet(final ItemType toElement) {
        return items.headSet(toElement);
    }

    @NotNull
    public final SortedSet<ItemType> tailSet(final ItemType fromElement) {
        return items.tailSet(fromElement);
    }

    public final ItemType first() {
        return items.first();
    }

    public final ItemType last() {
        return items.last();
    }

    public final int size() {
        return items.size();
    }
    public final boolean isEmpty() {
        return items.isEmpty();
    }

    public final boolean contains(final Object o) {
        return items.contains(o);
    }

    @NotNull
    public final Iterator<ItemType> iterator() {
        return items.iterator();
    }

    @NotNull
    public final Object[] toArray() {
        return items.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @NotNull
    public final <T> T[] toArray(@NotNull final T[] a) {
        return items.toArray(a);
    }

    public final boolean add(final ItemType item) {
        return items.add(item);
    }

    public final boolean remove(final Object o) {
        return items.remove(o);
    }

    public final boolean containsAll(@NotNull final Collection<?> c) {
        return items.containsAll(c);
    }


    public final boolean addAll(@NotNull final Collection<? extends ItemType> c) {
        return items.addAll(c);
    }


    public final boolean retainAll(@NotNull final Collection<?> c) {
        return items.retainAll(c);
    }


    public final boolean removeAll(@NotNull final Collection<?> c) {
        return items.removeAll(c);
    }


    public final void clear() {
        items.clear();
    }


    public final int compareTo(@NotNull final ItemSet<ItemType> o) {
        return Double.compare(support, o.getSupport());
    }

    @Override
    public final ItemSet<ItemType> clone() {
        return new ItemSet(this);
    }

    @Override
    public final String toString() {
        return items.toString();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + items.hashCode();
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
        ItemSet<?> other = (ItemSet<?>) obj;
        return items.equals(other.items);
    }

}