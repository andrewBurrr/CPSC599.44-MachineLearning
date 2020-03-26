package ca.ucalgary.rules599.datastructure;

import ca.ucalgary.rules599.util.Condition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class SortedArrayList<T> extends ArrayList<T> {
    private final Comparator<? super T> comparator;

    public SortedArrayList() {
        this((Comparator)null);
    }

    public SortedArrayList(@NotNull Collection<? extends T> items) {
        this((Comparator)null);
        this.addAll(items);
    }

    public SortedArrayList(int initialCapacity) {
        this(initialCapacity, (Comparator)null);
    }

    public SortedArrayList(@Nullable Comparator<? super T> comparator) {
        this.comparator = comparator;
    }

    public SortedArrayList(@NotNull Collection<? extends T> items, @Nullable Comparator<? super T> comparator) {
        this(comparator);
        this.addAll(items);
    }

    public SortedArrayList(int initialCapacity, @Nullable Comparator<? super T> comparator) {
        super(initialCapacity);
        this.comparator = comparator;
    }

    @Nullable
    public Comparator<? super T> comparator() {
        return this.comparator;
    }

    public boolean add(@NotNull T item) {
        Condition.ensureNotNull(item, "The item may not be null");
        int index = Collections.binarySearch(this, item, this.comparator);
        if (index < 0) {
            index = ~index;
        }

        super.add(index, item);
        return true;
    }

    public void add(int index, @NotNull T item) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(@NotNull Collection<? extends T> items) {
        Condition.ensureNotNull(items, "The collection may not be null");
        if (items.size() > 0) {
            items.forEach(this::add);
            return true;
        } else {
            return false;
        }
    }

    public boolean addAll(int index, @NotNull Collection<? extends T> items) {
        throw new UnsupportedOperationException();
    }
}
