package org.exercice14.diallo_barry.examples.ejb.writer;

import org.exercice14.diallo_barry.examples.ejb.entity.Compte;
import org.exercice14.diallo_barry.examples.ejb.entity.Operation;


/**
 * Interface distante pour le bean Writer.
 */
public interface Writer {

    /**
     * Ajout d'un nouveau {@lien Compte}.
     * @param compte {@lien Compte} à ajouter.
     */
    void addCompte(final Compte compte);

    /**
     * Suppression en cascade {@link Compte}.
     * @param compte {@link Compte} va être supprimé
     */
    void removeCompte(final Compte compte);

    /**
     * modification {@link Compte}.
     * @param compte {@link Compte} va être modifier
     */
    public void updateCompte(final Compte compte);

    /**
     * Ajoute une nouvelle {@link Operation}.
     * @param operation {@link Operation} va être ajouté
     */
    void addOperation(final Operation operation);

    /**
     * Suppression en cascade {@link Operation}.
     * @param operation {@link Operation} va être supprimé
     */
    void removeOperation(final Operation operation);

    /**
     * modification {@link Compte}.
     * @param compte {@link Compte} va être modifier
     */
    public void updateOperation(final Operation operation);
}
