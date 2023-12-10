package org.exercice14.diallo_barry.examples.ejb.init;

/**
 * The {@link Initializer} Bean initialize the entities data.
 */
public interface Initializer {

    /**
     * Initialize the minimal set of entities needed by the sample.
     */
    void initializeEntities() throws Exception;

}
