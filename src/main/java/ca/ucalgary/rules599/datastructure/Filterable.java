package ca.ucalgary.rules599.datastructure;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface Filterable<DataStructureType, T> {


    @NotNull
    DataStructureType filter(@NotNull final Predicate<T> predicate);

}