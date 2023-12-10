package org.exercice14.diallo_barry.examples.ejb.web;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.Queue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ejb.EJB;


import org.exercice14.diallo_barry.examples.ejb.entity.Compte;
import org.exercice14.diallo_barry.examples.ejb.reader.LocalReader;
import org.exercice14.diallo_barry.examples.ejb.init.Initializer;



/**
 * Definition of the two JMS destinations used by the quickstart
 * (one queue and one topic).
 */
@JMSDestinationDefinitions(
    value = {
        @JMSDestinationDefinition(
            name = "java:/queue/SampleQueue",
            interfaceName = "javax.jms.Queue",
            destinationName = "EJBExempleQueue"
        )
    }
)

@WebServlet("/MdbServlet")
public class MdbServlet extends HttpServlet {

    private static final long serialVersionUID = -8314035702649252239L;

    /**
     * Nombre d'opérations qui vont être crée
    */
    private static final int ITERATION_NUMBER = 10;
    
    /**
     * Link to the initializer bean.
    */
    @EJB
    private Initializer initializerBean;

    /**
     * Link to the Local Reader bean. Bean will be injected by JOnAS.
     */
    @EJB
    private LocalReader readerBean;

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/queue/SampleQueue")
    private Queue queue;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<form action=\"\\exemple-web\" method=\"get\">");
        out.println("<div><input type=\"submit\" value=\"Accueil\"/></div>");
        out.println("</form>");
        out.write("<h1>Ajout des opérations avec JMS</h1>");

        //Initialisation des comptes
        initCompte(out);

        // Récupérer la liste des comptes
        List<Compte> comptes = null;
        try {
            comptes = readerBean.listAllComptes();
        } catch (Exception e) {
            displayException(out, "Impossible d'appeler listAllCompte sur le bean", e);
            return;
        }

        try {
            final Destination destination = queue;
            out.write("<h3>Les opérations suivant vont être envoyé à la file : </h3>");
            for (int i = 0; i < ITERATION_NUMBER; i++) {

                Random rand = new Random();
                
                //Obtenir un numéro de compte aléatoire parmis tous les comptes
                int nbComptes = comptes.size();
                int nombreAleatoire = (int)Math.round(rand.nextInt(nbComptes));
                long idCompteAleatoire = comptes.get(nombreAleatoire).getId();

                //Si typeOperationAleatoire = 1 c'est un dépot sinon c'est un retrait
                int typeOperationAleatoire = (int)Math.round(rand.nextInt(2));

                //Montant compris entre 1 et 100
                int montantAleatoire = (int)Math.round(rand.nextInt(100) + 1);

                String typeOperation = "Depot";
                int montant = montantAleatoire;

                if(typeOperationAleatoire == 1){
                    typeOperation = "Retrait";
                }

                //message qui va être envoyé
                String title = idCompteAleatoire + "_" + typeOperation + "_" + montantAleatoire; 

                context.createProducer().send(destination, title);
                out.write("Message (" + i + "): " + typeOperation +" de "+ montantAleatoire + " euros du compte numéro "+ idCompteAleatoire+  "</br>");
            }

            out.println("<br/><br/>");
            out.println("<form action=\"\\exemple-web\\display\" method=\"get\">");
            out.println("<div><input type=\"submit\" value=\"Voir le résultat\"/></div>");
            out.println("</form>");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    /**
     * Initialiser la liste des comptes.
     * @param out the given writer
     */
    private void initCompte(final PrintWriter out) {
        try {
            initializerBean.initializeEntities();
        } catch (Exception e) {
            displayException(out, "Impossible d'initialiser la liste des comptes", e);
            return;
        }
    }

     /**
     * If there is an exception, print the exception.
     * @param out the given writer
     * @param errMsg the error message
     * @param e the content of the exception
     */
    private void displayException(final PrintWriter out, final String errMsg, final Exception e) {
        out.println("<p>Exception : " + errMsg);
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre></p>");
    }
}
