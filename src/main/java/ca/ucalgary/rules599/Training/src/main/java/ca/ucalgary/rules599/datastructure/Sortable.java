package ca.ucalgary.rules599.datastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public interface Sortable<DataStructureType, T> {


    @NotNull
    DataStructureType sort(@Nullable final Comparator<T> comparator);

}