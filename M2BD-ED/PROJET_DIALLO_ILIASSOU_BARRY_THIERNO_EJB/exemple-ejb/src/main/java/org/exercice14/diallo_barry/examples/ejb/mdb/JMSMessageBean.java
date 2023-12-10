package org.exercice14.diallo_barry.examples.ejb.mdb;

import javax.annotation.security.RunAs;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.exercice14.diallo_barry.examples.ejb.reader.LocalReader;
import org.exercice14.diallo_barry.examples.ejb.writer.LocalWriter;
import org.exercice14.diallo_barry.examples.ejb.entity.Compte;
import org.exercice14.diallo_barry.examples.ejb.entity.Operation;

/**
 * The {@link JMSMessageBean} is a message driven bean activated when a JMS
 * {@link Message} comes to a given Destination.
 * For each new {@link Message}, this bean will create and persists a new
 * {@link Operation} instance.
 * This MDB is annotated with {@link RunAs} because it uses a secured
 * business interface.
 */
@MessageDriven(name = "EJBExempleQueue", activationConfig={
        @ActivationConfigProperty(propertyName = "destinationLookup",
                                  propertyValue = "queue/SampleQueue"),
        @ActivationConfigProperty(propertyName = "destinationType",
                                  propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
        						  propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName="maxPoolSize", 
        						  propertyValue="1"),
        @ActivationConfigProperty(propertyName="maxSession", 
        						  propertyValue="1")
        })
public class JMSMessageBean implements MessageListener {

    /**
     * Secured business interface.
     */
    @EJB
    private LocalWriter writer;

    /**
     * Unsecured {@link LocalReader} business interface.
     */
    @EJB
    private LocalReader reader;

    /**
     * Called when a new JMS {@link Message} is received on the destination.
     * This method will use the {@link LocalWriter} Bean interface to add
     * Operation d'un compte
     * @param message {@link Message} containing {@link Operation} title.
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public synchronized void onMessage(final Message message) {

        System.out.println("Reçoit les messages JMS: " + message);

        // Extract Message's text value
        String text = null;
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                text = textMessage.getText();
            } catch (JMSException e) {
                System.err.println("Unexpected Exception: " + e.getMessage());
                e.printStackTrace(System.err);
                return;
            }
        } else {
            return;
        }
        

        //Partitionnement du texte 
        String[] textePartitionner = text.split("_");

        //id du compte 
        String idCompteEnChar = textePartitionner[0];
        long idCompte = (long) Integer.parseInt(idCompteEnChar);

        //Type d'opération
        String typeOperation = textePartitionner[1];

        //Montant
        String montantEnChar = textePartitionner[2];
        float montant = (float) Integer.parseInt(montantEnChar);

        //Montant pour la mise à jour solde
        float montantMiseAJourSolde = montant; 
        if(typeOperation == "Retrait"){
            montantMiseAJourSolde = -montant;
        }

        //Récupération du compte 
        Compte compte = reader.findCompteById(idCompte);

        //Mise à jour du solde du compte

        try {
            compte.depot(montantMiseAJourSolde);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        writer.updateCompte(compte);

        //Création de l'opération
        Operation operation = new Operation(typeOperation, montant, compte);
        writer.addOperation(operation);

    }

}
