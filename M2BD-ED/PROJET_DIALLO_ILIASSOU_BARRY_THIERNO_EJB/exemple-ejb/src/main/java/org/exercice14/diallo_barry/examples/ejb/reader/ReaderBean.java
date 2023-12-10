package org.exercice14.diallo_barry.examples.ejb.reader;

import static org.exercice14.diallo_barry.examples.ejb.entity.Compte.QN.FIND_COMPTE;
import static org.exercice14.diallo_barry.examples.ejb.entity.Compte.QN.FIND_COMPTE_BY_ID;
import static org.exercice14.diallo_barry.examples.ejb.entity.Compte.QN.ALL_COMPTE;
import static org.exercice14.diallo_barry.examples.ejb.entity.Operation.QN.ALL_OPERATIONS;


import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.exercice14.diallo_barry.examples.ejb.entity.Compte;
import org.exercice14.diallo_barry.examples.ejb.entity.Operation;

/**
 *Le {@link ReaderBean} EJB est un bean sans état, en lecture seule et sans restriction.
 */
@Stateless
@Local(LocalReader.class)
@Remote(RemoteReader.class)
public class ReaderBean implements LocalReader, RemoteReader {

    /**
     * Gestionnaire d'entités utilisé par ce bean.
     */
    @PersistenceContext
    private EntityManager entityManager = null;

    /**
     * Trouver un {@link Compte} utilisant son nom comme clé
     * @param nom {@link Compte}'s nom.
     */
    @SuppressWarnings("unchecked")
    public Compte findCompte(final String nom) {
        Query query = entityManager.createNamedQuery(FIND_COMPTE);
        query.setParameter("name", nom);
        List<Compte> comptes = query.getResultList();
        if (comptes != null && comptes.size() > 0) {
            return comptes.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Compte findCompteById(final long id) {
        Query query = entityManager.createNamedQuery(FIND_COMPTE_BY_ID);
        query.setParameter("id", id);
        List<Compte> compte = query.getResultList();
        if (compte != null && compte.size() > 0) {
            return compte.get(0);
        }
        return null;
    }

    /**
     * @return la liste de tous les persistants {@link Compte}s.
     */
    @SuppressWarnings("unchecked")
    public List<Compte> listAllComptes() {
        return entityManager.createNamedQuery(ALL_COMPTE).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Operation> listAllOperations() {
        return entityManager.createNamedQuery(ALL_OPERATIONS).getResultList();
    }

}
