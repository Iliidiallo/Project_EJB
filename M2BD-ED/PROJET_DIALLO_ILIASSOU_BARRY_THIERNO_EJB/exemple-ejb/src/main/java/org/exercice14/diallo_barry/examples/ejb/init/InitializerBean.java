package org.exercice14.diallo_barry.examples.ejb.init;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.exercice14.diallo_barry.examples.ejb.reader.LocalReader;
import org.exercice14.diallo_barry.examples.ejb.reader.Reader;
import org.exercice14.diallo_barry.examples.ejb.writer.LocalWriter;
import org.exercice14.diallo_barry.examples.ejb.writer.Writer;
import org.exercice14.diallo_barry.examples.ejb.entity.Compte;
import org.exercice14.diallo_barry.examples.ejb.entity.Operation;

/**
 * The {@link InitializerBean} EJB is here to initialize only once
 * the Database/Entities. It simply checks if there is some {@link Compte}s
 * already persisted; if none are found, we will inject defaults values.
 */

@Stateless(mappedName="myInitializerBean")
@Remote(Initializer.class)
public class InitializerBean implements Initializer {

    /**
     * Injected reference to the {@link Writer} EJB.
     */
    @EJB
    private LocalWriter writer;

    /**
     * Injected reference to the {@link Reader} EJB.
     */
    @EJB
    private LocalReader reader;

    /**
     * Initialize the minimal set of entities needed by the sample.
     *
     * @see Initializer#initializeEntities()
     */
    public void initializeEntities() throws Exception {

        if (reader.findCompte("Compte Iliassou") == null) {

            //Création d'un premier compte bancaire
            Compte compte = new Compte("Compte Iliassou", 50);

            Operation operation = new Operation("Retrait", 15, compte);
            compte.retrait(15);
            compte.getOperations().add(operation);

            //Création d'une 2 ème action pour ce compte
            Operation operation2 = new Operation("Depot", 50, compte);
            compte.depot(50);
            compte.getOperations().add(operation2);

            // Ajout du compte Iliassou dans la base de données
            writer.addCompte(compte);
        }

        if (reader.findCompte("Compte Thierno") == null) {

            //Création du deuxième compte bancaire
            Compte compte = new Compte("Compte Thierno", 1000);

            //Création d'une action pour ce compte
            Operation operationRetrait = new Operation("Retrait", 600, compte);
            compte.retrait(600);
            compte.getOperations().add(operationRetrait);

            //Création d'une operationDepot1 pour ce compte
            Operation operationDepot1 = new Operation("Depot", 100, compte);
            compte.depot(100);
            compte.getOperations().add(operationDepot1);


            //Création d'une operationDepot2 pour ce compte
            Operation operation3 = new Operation("Depot", 130, compte);
            compte.depot(130);
            compte.getOperations().add(operation3);

            writer.addCompte(compte);
        }

    }
}
