package org.exercice14.diallo_barry.examples.ejb.reader;

import java.util.List;

import org.exercice14.diallo_barry.examples.ejb.entity.Compte;
import org.exercice14.diallo_barry.examples.ejb.entity.Operation;

/**
 * The {@link Reader} l'interface métier est une interface
 * vu en lecture seule sans restriction sur les entités
 */
public interface Reader {

    /**
     * @return la liste de tous les {@link Compte}s.
     */
    List<Compte> listAllComptes();

    /**
     * Trouver un {@link Compte} utilisant son nom comme clé
     * @param nom {@link Compte}'s nom.
     */
    Compte findCompte(final String nom);

    /**
     * Trouver un {@link Compte} utilisant son id comme clé
     * @param nom {@link Compte}'s id.
     */
    Compte findCompteById(final long id);

    /**
     * @return la liste de toutes les {@link Operation}s.
     */
    List<Operation> listAllOperations();
}
