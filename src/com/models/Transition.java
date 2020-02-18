package com.models;

/***
 * transition defines the basic implementation standard for available
 * extension rule applications on a given node.
 */
interface Transition {

    Fitness fitness = null; // fitness does not need to be defined the same for each rule type

    void applyRule(Node node);

}
