package com.procedures;

import com.models.Fact;

import java.util.Set;

/**
 * Driver class for the genetic algorithm as described in the project proposal. The starting population is initialized
 * on C4.5 partial solutions, and use an OR-Tree model as described in Dr. Denzingers slides from CPSC 433 Set-Based
 * Search slides.  This class controls the flow of the following attributes:
 * <ul>
 *     <li>{@link #population}</li>
 * </ul>
 */
class Genetic {

    private Set<Fact> population;

    private int maxPop;
    private double killPercent;
    private double threshold;

    /**
     * Receives environment attributes for the Process to use as guides for the search control
     */
    protected Genetic(int maxPop, double killPercent, double threshold) {
        this.maxPop = maxPop;
        this.killPercent = killPercent;
        this.threshold = threshold;
    }

    /**
     * search control provide a timeout or fitness minimum
     */
    private void process(int threadCount, int timeout, double entropy) {
        do {
            // thread pool with priority  that takes into account optimal fitness scoring and improvement
            System.out.println("test");
        } while (population.isEmpty()); // TODO change this condition to represent a derivation/time-out/max-generations
    }

    /**
     * TODO
     * @return 0
     */
    private int fWert() {
        return 0;
    }

    /**
     * TODO
     * @return 0
     */
    private int fSelect() {
        return 0;
    }
}

