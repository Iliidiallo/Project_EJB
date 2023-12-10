package org.exercice14.diallo_barry.examples.ejb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Définie une opération
 */
@Entity
@NamedQueries({@NamedQuery(name=Operation.QN.ALL_OPERATIONS, query="select o FROM Operation o")})
public class Operation implements Serializable {

    /**
    * Définie les requêtes
     */
    public static interface QN {
       /**
        * Chercher les comptes
       */
        String ALL_OPERATIONS = "Operation.allOperation";
    }

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * Primary key.
     */
    private long id;

    /**
     * Compte
     */
    private Compte compte;

    /**
     * type d'opération
     */
    private String typeOperation;

    /**
     * montant opération
     */
    private float montant;

    /**
     * Constructeur par défaut
     */
    public Operation() {

    }

    /**
     * Constructeur. Crée un nouveau livre avec le type d'opération le montant et le compte
     * @param typeOperation le type d'opération
     * @param montant le montant
     * @param compte le compte
     */
    public Operation(final String typeOperation, final float montant, final Compte compte) {
        setTypeOperation(typeOperation);
        setMontant(montant);
        setCompte(compte);
    }

    /**
     * @return le compte de cette opération
     */
    @ManyToOne
    @JoinColumn(name="Compte_id")
    public Compte getCompte() {
        return compte;
    }

    /**
     * Sets le compte de cette opération
     * @param compte le compte donné
     */
    public void setCompte(final Compte compte) {
        this.compte = compte;
    }

    /**
     * @return le type d'opération
     */
    public String getTypeOperation() {
        return typeOperation;
    }

    /**
     * Set le type d'operation
     * @param typeOperation - le type d'operation
     */
    public void setTypeOperation(final String typeOperation) {
        this.typeOperation = typeOperation;
    }

    /**
     * @return le montant de l'opération
     */
    public float getMontant() {
        return montant;
    }

    /**
     * Set le montant de l'opération
     * @param montant - le montant de l'opération
     */
    public void setMontant(final float montant) {
        this.montant = montant;
    }

    /**
     * @return l'id de l'opération (incrémenter automatiquement)
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * Sets l'id de l'opération
     * @param id de l'opération
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return Représentation des opération
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append("[id=");
        sb.append(getId());
        sb.append(", typeOperation=");
        sb.append(getTypeOperation());
        sb.append(", montant=");
        sb.append(getMontant());
        sb.append("]");
        return sb.toString();
    }
}
