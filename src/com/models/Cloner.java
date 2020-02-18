package com.models;

import java.io.*;
import java.util.logging.Logger;
/**
 * Privacy management tool for handling generic data types.  Creates deep copies of provided objects. Contains the
 * following attributes:
 * <ul>
 *     <li>{@link #LOGGER}</li>
 * </ul>
 * @author Andrew Burton
 * @version %I%, %G%
 * @since 1.0
 * @see java.io.Serializable
 */
class Cloner<T extends Serializable> {

    /** The {@link Logger} identifier used to setup logger streams for recording data messages.*/
    private static final Logger LOGGER = Logger.getLogger("RuleTrainer");

    /**
     * The clone method takes in an object of a given type and creates a deep copy. Note this will be time consuming.
     * Primary use case is for saving, loading and combining models.
     * @param object Object to clone
     * @return Deep copied object
     * @throws ClassNotFoundException thrown when readObject fails
     * @throws IOException thrown by the object streams or when write object fails
     */
    T clone(T object) throws ClassNotFoundException, IOException {
        LOGGER.entering(Cloner.class.getName(), "clone(T object)");
        // create serial copy of object
        LOGGER.info("Creating a serialized copy of the data object");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(object);
        // create unserial copy of object (detaches memory pointers and other associated references)
        LOGGER.info("Creating a deserialized copy of the data object as a deep copy");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
        LOGGER.exiting(Cloner.class.getName(), "clone(T object)");
        return (T) inputStream.readObject();
    }
}
