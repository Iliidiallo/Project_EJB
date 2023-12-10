package org.exercice14.diallo_barry.examples.ejb.writer;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.exercice14.diallo_barry.examples.ejb.entity.Compte;
import org.exercice14.diallo_barry.examples.ejb.entity.Operation;

/**
 * Ceci est un exemple de Session Bean, sans état, sécurisé, disponible
 * avec une interface locale et distante (avec les mêmes méthodes).
 */
@Stateless
@Remote(RemoteWriter.class)
@Local(LocalWriter.class)
public class WriterBean implements LocalWriter, RemoteWriter {

    /**
     * Gestionnaire d'entités utilisé par ce bean.
     */
    @PersistenceContext
    private EntityManager entityManager = null;


    /**
     * Persiste un nouveau {@lien Compte}.
     * @param compte {@lien Compte} à ajouter.
     */
    public void addCompte(final Compte compte){
        entityManager.persist(compte);
    }

    /**
     * Suppression en cascade {@link Compte}.
     * @param compte {@link Compte} va être supprimé
     */
    public void removeCompte(final Compte compte) {
        entityManager.remove(compte);
    }

    /**
     * modification {@link Compte}.
     * @param compte {@link Compte} va être modifier
     */
    public void updateCompte(final Compte compte){
        entityManager.merge(compte);
    }
    
    /**
     * Ajoute une nouvelle {@link Operation}.
     * @param operation {@link Operation} va être ajouté
     */
    public void addOperation(final Operation operation){
        entityManager.persist(operation);
    }

    /**
     * Suppression en cascade {@link Operation}.
     * @param operation {@link Operation} va être supprimé
     */
    public void removeOperation(final Operation operation){
        entityManager.remove(operation);
    }

    /**
     * modification {@link Compte}.
     * @param compte {@link Compte} va être modifier
     */
    public void updateOperation(final Operation operation){
        entityManager.merge(operation);
    }
    

}
