package com.models;

import java.util.LinkedHashSet;

/**
 * Data container / state manager for an association rule, of the form ((a_1,...,a_n),(b_1,...,b_m))
 * TODO add data access/manipulation methods
 * TODO add documentation
 */
public class Fact {

    private LinkedHashSet<Predicate> conditions;
    private LinkedHashSet<Predicate> conclusions;

    public Fact() {
        // TODO decide on initialization of a fact
    }
}
