package ca.ucalgary.rules599.datastructure;

import ca.ucalgary.rules599.util.Condition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.*;

public class ListenerList<T> implements Iterable<T> {
    private final Object lock;
    private final CompareMethod compareMethod;
    private List<T> listeners;

    private boolean contains(@NotNull Iterable<? extends T> iterable, @NotNull T listener) {
        Iterator iterator = iterable.iterator();
        T t;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            t = (T)iterator.next();
        } while(!this.equals(t, listener));

        return true;
    }

    private boolean equals(@Nullable T listener1, @Nullable T listener2) {
        if (listener1 == null) {
            return listener2 == null;
        } else {
            label38: {
                if (listener2 != null) {
                    if (this.compareMethod == CompareMethod.EQUALITY) {
                        if (listener1.equals(listener2)) {
                            break label38;
                        }
                    } else if (listener1 == listener2) {
                        break label38;
                    }
                }

               return false;
            }
            return true;
        }
    }

    public ListenerList() {
        this(CompareMethod.EQUALITY);
    }

    public ListenerList(@NotNull CompareMethod compareMethod) {
        this.lock = new Object();
        Condition.ensureNotNull(compareMethod, "The compare method may not be null");
        this.compareMethod = compareMethod;
        this.clear();
    }

    @NotNull
    public final CompareMethod getCompareMethod() {
        return this.compareMethod;
    }

    public final boolean isEmpty() {
        synchronized(this.lock) {
            return this.listeners.isEmpty();
        }
    }

    public final int size() {
       synchronized(this.lock) {
            return this.listeners.size();
        }
    }

    public final boolean add(@NotNull T listener) {
        Condition.ensureNotNull(listener, "The listener may not be null");
        Object var2 = this.lock;
        synchronized(this.lock) {
            if (!this.contains(this.listeners, listener)) {
                List<T> newList = new LinkedList(this.listeners);
                newList.add(listener);
                this.listeners = newList;
                return true;
            } else {
                return false;
            }
        }
    }

    public final void addAll(@NotNull Iterable<? extends T> iterable) {
        Condition.ensureNotNull(iterable, "The iterable may not be null");
        synchronized(this.lock) {
            List<T> newList = null;
            Iterator var4 = iterable.iterator();

            while(true) {
                T listener;
                while(true) {
                    if (!var4.hasNext()) {
                        if (newList != null) {
                            this.listeners = newList;
                        }

                        return;
                    }

                    listener = (T)var4.next();
                    Condition.ensureNotNull(listener, "The listener may not be null");
                    if (newList == null) {
                        if (!this.contains(this.listeners, listener)) {
                            break;
                        }
                    } else if (!this.contains(newList, listener)) {
                        break;
                    }
                }

                if (newList == null) {
                    newList = new LinkedList(this.listeners);
                }

                newList.add(listener);
            }
        }
    }

    public final boolean remove(@NotNull T listener) {
        Condition.ensureNotNull(listener, "The listener may not be null");
        synchronized(this.lock) {
            if (this.contains(this.listeners, listener)) {
                List<T> newList = new LinkedList();
                this.listeners.stream().filter((x) -> {
                    return !this.equals(x, listener);
                }).forEach(newList::add);
                this.listeners = newList;
                return true;
            } else {
                return false;
            }
        }
    }

    public final void removeAll(@NotNull Iterable<? extends T> iterable) {
        Condition.ensureNotNull(iterable, "The iterable may not be null");
        synchronized(this.lock) {
            List<T> newList = null;
            Iterator var4 = this.listeners.iterator();

            while(var4.hasNext()) {
                T listener = (T)var4.next();
                if (!this.contains(iterable, listener)) {
                    if (newList == null) {
                        newList = new LinkedList();
                    }

                    newList.add(listener);
                }
            }

            if (newList != null) {
                this.listeners = newList;
            }

        }
    }

    public final void clear() {
        Object var1 = this.lock;
        synchronized(this.lock) {
            this.listeners = Collections.emptyList();
        }
    }

    @NotNull
    public Collection<T> getAll() {
        Object var1 = this.lock;
        synchronized(this.lock) {
            return (Collection)(this.isEmpty() ? Collections.emptyList() : Collections.unmodifiableCollection(new LinkedList(this.listeners)));
        }
    }

    @NotNull
    public Iterator<T> iterator() {
        return this.listeners.iterator();
    }

    public enum CompareMethod {
        EQUALITY,
        IDENTITY;

        private CompareMethod() {
        }
    }
}