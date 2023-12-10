package org.exercice14.diallo_barry.examples.ejb.entity;

import static org.exercice14.diallo_barry.examples.ejb.entity.Compte.QN.ALL_COMPTE;
import static org.exercice14.diallo_barry.examples.ejb.entity.Compte.QN.FIND_COMPTE;
import static org.exercice14.diallo_barry.examples.ejb.entity.Compte.QN.FIND_COMPTE_BY_ID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * Compte
 */
@Entity
@NamedQueries({@NamedQuery(name=ALL_COMPTE, query="SELECT c FROM Compte c"),
               @NamedQuery(name=FIND_COMPTE, query="SELECT c FROM Compte c WHERE c.nom = :name"),
               @NamedQuery(name=FIND_COMPTE_BY_ID, query="SELECT c FROM Compte c WHERE c.id = :id")})

public class Compte implements Serializable {
    /**
     * Définie les requêtes
    */
    public static interface QN {
        /**
         * Chercher les comptes
        */
        String ALL_COMPTE = "Compte.allCompte";

        /**
         * Chercher un compte
        */
        String FIND_COMPTE = "Compte.findCompte";

        /**
         * Chercher un compte par son id
        */
        String FIND_COMPTE_BY_ID = "Compte.findCompteById";
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * Clé primaire (généré automatiquement).
     */
    private long id;

    /**
     * Nom du compte
     */
    private String nom = null;

    /**
     * Solde du compte
     */
    private float solde = 0;

    /**
     * Liste des opérations du compte
     */
    private Collection<Operation> operations;

    /**
     * Constructeur par défaut
     */
    public Compte() {
        operations = new ArrayList<Operation>();
    }

    /**
     * Constructor avec un solde donné
     * @param solde - le solde du compte
     */
    public Compte(final String nom, final float solde) {
        this();
        setNom(nom);
        setSolde(solde);
    }

    /**
     * @return les opérations de ce compte
     */
    @OneToMany(mappedBy="compte", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    public Collection<Operation> getOperations() {
        return operations;
    }

    /**
     * Ajoute une opération au compte
     * @param typeOperation - le type d'opération
     * @param montant - le montant
     */
    public void addOperation(final String typeOperation, float montant) {
        Operation operation = new Operation();
        operation.setTypeOperation(typeOperation);
        operation.setMontant(montant);
        operation.setCompte(this);
        getOperations().add(operation);
    }

    /**
     * Sets les opérations fait par le compte
     * @param operations la liste des opérations
     */
    public void setOperations(final Collection<Operation> operations) {
        this.operations = operations;
    }


    /**
     * @return nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * remplacer le nom du compte
     * @param nom - le nom du compte
     */
    public void setNom(final String nom) {
        this.nom = nom;
    }

    /**
     * @return solde
     */
    public float getSolde() {
        return solde;
    }

    /**
     * remplacer le solde du compte
     * @param solde - le solde du compte
     */
    public void setSolde(final float solde) {
        this.solde = solde;
    }

    /**
     * @return an id for this object (incremented automatically)
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * Sets l'id du compte
     * @param id l'id du compte
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Retirer de l'argent du compte
     * @param montant
     * @throws Exception
     */
    public void  retrait(float montant) throws Exception {
        if(montant > this.solde){
            throw new Exception("Le montant que vous voulez rétirer est supérieur au solde du compte");
        }
        else{
            this.solde = this.solde - montant;
        }
    }

    /**
     * Déposer de l'argent sur le compte
     * @param montant
     * @throws Exception
     */

    public void  depot(float montant) throws Exception {
        if(montant < 0){
            throw new Exception("Le montant que vous voulez déposer est négatif");
        }
        else{
            this.solde = this.solde + montant;
        }
    }




    /**
     * @return String representation of this entity object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append("Compte numéro ");
        sb.append(getId());
        sb.append(" nommé : ");
        sb.append(getNom());
        sb.append(", a : ");
        sb.append(getSolde());
        sb.append(" euros]");
        return sb.toString();
    }
}
