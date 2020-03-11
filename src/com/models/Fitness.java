package com.models;

import java.io.Serializable;

/**
 * TODO Define fitness function as per the paper recommended by Dr. Denzinger
 * Note: must make the score and calculations volatile for concurrency to work properly
 * Note: probably dont need this class to be abstract if we are only needing a single fitness function that behaves
 * uniformly for all types/shapes of facts.
 */
abstract class Fitness implements Serializable {
    private volatile int score;
}
