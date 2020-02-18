package com.models;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Node class provides the default data container for building information structures in the learning application. It is
 * comprised of the following attributes:
 * <ul>
 *     <li>{@link #key}</li>
 *     <li>{@link #inputAssociations}</li>
 *     <li>{@link #outputAssociations}</li>
 * </ul>
 *
 * TODO configure distinction between incoming/outgoing associations
 * TODO decide whether or not to serialize the model
 * TODO provide an activation function to determine node behaviour (bias application function)
 * TODO Add documentation
 * TODO configure rules and valid operations on the nodes
 *
 * @author Andrew Burton
 * @version %I%, %G%
 * @since 1.0
 * @see com.models
 */
class Node<K extends Serializable> implements Serializable {
    /** Unique serialization identifier(this initialization prevents compatibility conflicts if this class gets updated) */
    protected static final long serialVersionUID = 1L;
    /** {@link String} identifier to distinguish nodes from each other */
    protected String id;
    /** The {@link K } structure for containing the information constructed by the more complex data containers */
    private K key;
    /** The {@link java.util.List} of input associations represents connections to other compound data objects */
    private LinkedList<? extends K> inputAssociations;
    /** The {@link java.util.List} of input associations represents connections to other compound data objects  */
    private LinkedList<? extends K> outputAssociations;
    /** TODO Rule operative */
    private Transition transition;

    /**
     * Default constructor for Node, takes no arguments, and initializes null typed data and associations
     * (generally should not use this).
     *
     * @see K
     * @see java.util.List
     */
    protected Node() {
        this(null, null);
    }


    protected Node(K key) {
        this(key, null);
    }


    private Node(K key, LinkedList<? extends K> inputAssociations) {
        setKey(key);
        setInputAssociations(inputAssociations);
    }

    private void setKey(K key) {
        this.key = key;
    }

    private void setInputAssociations(LinkedList<? extends K> inputAssociations) {
        if (inputAssociations != null) this.inputAssociations = inputAssociations;
        else if (this.inputAssociations == null) this.inputAssociations = new LinkedList<>();
    }

    private void setOutputAssociations() {
        // apply tree function and get activation function operative
    }

    protected K getKey() throws ClassNotFoundException, IOException {
        // build a generic object cloner
        Cloner<K> cloner = new Cloner<>();
        return cloner.clone(this.key);
    }

    protected LinkedList getInputAssociations() throws ClassNotFoundException, IOException {
        // build a generic list cloner
        Cloner<LinkedList<? extends K>> cloner = new Cloner<>();
        return cloner.clone(this.inputAssociations);
    }

    protected List<? extends K> getOutputAssociations() throws ClassNotFoundException, IOException {
        Cloner<LinkedList<? extends K>> cloner = new Cloner<>();
        return cloner.clone(this.inputAssociations);
    }
}
