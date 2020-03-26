package ca.ucalgary.rules599.datastructure;

import ca.ucalgary.rules599.util.Condition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SortedArraySet<T> implements SortedSet<T> {
    private final SortedArrayList<T> sortedArrayList;
    private final Set<Integer> hashCodes;

    public SortedArraySet() {
        this((Comparator)null);
    }

    public SortedArraySet(Collection<T> items) {
        this((Comparator)null);
        this.addAll(items);
    }

    public SortedArraySet(int initialCapacity) {
        this(initialCapacity, (Comparator)null);
    }

    public SortedArraySet(@Nullable Comparator<? super T> comparator) {
        this.sortedArrayList = new SortedArrayList<>(comparator);
        this.hashCodes = new HashSet<>();
    }

    public SortedArraySet(@NotNull Collection<T> items, @Nullable Comparator<? super T> comparator) {
        this(comparator);
        this.addAll(items);
    }

    public SortedArraySet(int initialCapacity, @Nullable Comparator<? super T> comparator) {
        this.sortedArrayList = new SortedArrayList<>(initialCapacity, comparator);
        this.hashCodes = new HashSet<>(initialCapacity);
    }

    @Nullable
    public Comparator<? super T> comparator() {
        return this.sortedArrayList.comparator();
    }

    @NotNull
    public SortedSet<T> subSet(@NotNull T fromElement, @NotNull T toElement) {
        Condition.ensureNotNull(fromElement, "The fromElement may not be null");
        Condition.ensureNotNull(toElement, "The toElement may not be null");
        int start = this.sortedArrayList.indexOf(fromElement);
        Condition.ensureNotEqual(start, -1, "fromElement not contained by set", NoSuchElementException.class);
        int end = this.sortedArrayList.indexOf(toElement);
        Condition.ensureNotEqual(end, -1, "toElement not contained by set", NoSuchElementException.class);
        Condition.ensureFalse(start > end, "fromElement greater than toElement");
        SortedSet<T> subSet = new SortedArraySet<>(end - start + 1);

        for(int i = start; i <= end; ++i) {
            subSet.add(this.sortedArrayList.get(i));
        }

        return subSet;
    }

    @NotNull
    public SortedSet<T> headSet(@NotNull T toElement) {
        Condition.ensureNotNull(toElement, "The toElement may not be null");
        int end = this.sortedArrayList.indexOf(toElement);
        Condition.ensureNotEqual(end, -1, "toElement not contained by set", NoSuchElementException.class);
        SortedSet<T> headSet = new SortedArraySet<>(end + 1);

        for(int i = 0; i <= end; ++i) {
            headSet.add(this.sortedArrayList.get(i));
        }

        return headSet;
    }

    @NotNull
    public SortedSet<T> tailSet(@NotNull T fromElement) {
        Condition.ensureNotNull(fromElement, "The fromElement may not be null");
        int start = this.sortedArrayList.indexOf(fromElement);
        Condition.ensureNotEqual(start, -1, "fromElement not contained by set", NoSuchElementException.class);
        SortedSet<T> tailSet = new SortedArraySet<>(this.size() - start);

        for(int i = start; i < this.size(); ++i) {
            tailSet.add(this.sortedArrayList.get(i));
        }

        return tailSet;
    }

    @NotNull
    public T first() {
        Condition.ensureFalse(this.isEmpty(), "Set is empty", NoSuchElementException.class);
        return this.sortedArrayList.get(0);
    }

    @NotNull
    public T last() {
        Condition.ensureFalse(this.isEmpty(), "Set is empty", NoSuchElementException.class);
        return this.sortedArrayList.get(this.sortedArrayList.size() - 1);
    }

    public int size() {
        return this.sortedArrayList.size();
    }

    public boolean isEmpty() {
        return this.sortedArrayList.isEmpty();
    }

    public boolean contains(@NotNull Object item) {
        Condition.ensureNotNull(item, "The item may not be null");
        return this.hashCodes.contains(item.hashCode());
    }

    @NotNull
    public Iterator<T> iterator() {
        return this.sortedArrayList.iterator();
    }

    @NotNull
    public Object[] toArray() {
        return this.sortedArrayList.toArray();
    }

    @NotNull
    public <T1> T1[] toArray(@NotNull T1[] array) {
        Condition.ensureNotNull(array, "The array may not be null");
        return this.sortedArrayList.toArray(array);
    }

    public boolean add(@NotNull T item) {
        Condition.ensureNotNull(item, "The item may not be null");
        int hashCode = item.hashCode();
        if (!this.hashCodes.contains(hashCode)) {
            this.sortedArrayList.add(item);
            this.hashCodes.add(hashCode);
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(@NotNull Object item) {
        Condition.ensureNotNull(item, "The item may not be null");
        int hashCode = item.hashCode();
        if (this.hashCodes.remove(hashCode)) {
            this.sortedArrayList.remove(item);
            return true;
        } else {
            return false;
        }
    }

    public boolean containsAll(@NotNull Collection<?> items) {
        Condition.ensureNotNull(items, "The collection may not be null");
        return (Boolean)items.stream().map(this::contains).reduce(true, (a, b) -> {
            return a && b;
        });
    }

    public boolean addAll(@NotNull Collection<? extends T> items) {
        Condition.ensureNotNull(items, "The collection may not be null");
        return !items.isEmpty() ? (Boolean)items.stream().map(this::add).reduce(true, (a, b) -> {
            return a && b;
        }) : false;
    }

    public boolean retainAll(@NotNull Collection<?> items) {
        Condition.ensureNotNull(items, "The collection may not be null");
        if (!items.isEmpty()) {
            boolean result = false;

            for(int i = this.size() - 1; i >= 0; --i) {
                T item = this.sortedArrayList.get(i);
                if (!items.contains(item)) {
                    int hashCode = item.hashCode();
                    this.sortedArrayList.remove(i);
                    this.hashCodes.remove(hashCode);
                    result = true;
                }
            }

            return result;
        } else {
            return false;
        }
    }

    public boolean removeAll(@NotNull Collection<?> items) {
        Condition.ensureNotNull(items, "The collection may not be null");
        return !items.isEmpty() ? (Boolean)items.stream().map(this::remove).reduce(true, (a, b) -> {
            return a && b;
        }) : false;
    }

    public void clear() {
        this.hashCodes.clear();
        this.sortedArrayList.clear();
    }

    public String toString() {
        return this.sortedArrayList.toString();
    }

    public int hashCode() {
        boolean prime = true;
        int result = 1;
        result = 31 * result + this.hashCodes.hashCode();
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            SortedArraySet<?> other = (SortedArraySet)obj;
            return this.hashCodes.equals(other.hashCodes);
        }
    }
}
