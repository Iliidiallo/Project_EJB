package org.exercice14.diallo_barry.examples.ejb.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exercice14.diallo_barry.examples.ejb.entity.Compte;
import org.exercice14.diallo_barry.examples.ejb.entity.Operation;
import org.exercice14.diallo_barry.examples.ejb.init.Initializer;
import org.exercice14.diallo_barry.examples.ejb.reader.LocalReader;
import org.exercice14.diallo_barry.examples.ejb.writer.LocalWriter;

/**
 * Defines a servlet that is accessing the two entities through a local session
 * bean.
 */
public class ListeCompteServlet extends HttpServlet {

    /**
     * Serializable class uid.
     */
    private static final long serialVersionUID = -3172627111841538912L;


    /**
     * Link to the Local Reader bean. Bean will be injected by JOnAS.
     */
    @EJB
    private LocalReader readerBean;

    /**
     * Link to the Local Reader bean. Bean will be injected by JOnAS.
     */
    @EJB
    private LocalWriter writerBean;

    /**
     * Link to the initializer bean.
     */
    @EJB
    private Initializer initializerBean;


    /**
     * Called by the server (via the service method) to allow a servlet to
     * handle a GET request.
     * @param request an HttpServletRequest object that contains the request the
     *        client has made of the servlet
     * @param response an HttpServletResponse object that contains the response
     *        the servlet sends to the client
     * @throws IOException if an input or output error is detected when the
     *         servlet handles the GET request
     * @throws ServletException if the request for the GET could not be handled
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
        out.println("  <head>");
        out.println("    <link type=\"text/css\" href=\"ListeCompteServlet.css\" rel=\"stylesheet\" id=\"stylesheet\" />");
        out.println("    <title>Affichage des comptes</title>");

        // Ajouter le script JavaScript ici
        out.println("    <script>");
        out.println("    function showSoldePopup(solde) {");
        out.println("        alert('Le solde du compte est de ' + solde + ' euros.');");
        out.println("    }");
        out.println("    </script>");

        out.println("  </head>");
        out.println("<body style=\"background : white; color : black;\">");

        out.println("<form action=\"\\exemple-web\" method=\"get\">");
        out.println("<div><input type=\"submit\" value=\"Accueil\"/></div>");
        out.println("</form>");


        out.println("<h1 class=\"title\">Affichage des comptes</h1>");


        out.println("  <div class=\"contenneurAffichage\">");
        out.println("   <div class=\"affichage\">");

        initCompte(out);
        

        displayCompte(out);
        
        out.println("   </div>");
        out.println("</div>");

        out.println("<br />");

        out.println("  <div class=\"links\">");
        out.println("    <form action=\"secured/Admin\" method=\"get\">");
        out.println("      <div><input type=\"submit\" value=\"Ajouter un compte\"/></div>");
        out.println("    </form>");
        out.println("  </div>");

        out.println("</body>");
        out.println("</html>");
        out.close();
    }


    /**
     * Initialiser la liste des comptes.
     * @param out the given writer
     */
    private void initCompte(final PrintWriter out) {
        out.println("<br />");
        try {
            initializerBean.initializeEntities();
        } catch (Exception e) {
            displayException(out, "Impossible d'initialiser la liste des comptes", e);
            return;
        }
    }

    /**
     * Afficher compte.
     * @param out the given writer
     */
    private void displayCompte(final PrintWriter out) {
        out.println("<br />");

        // Récupérer la liste des comptes
        List<Compte> comptes = null;
        try {
            comptes = readerBean.listAllComptes();
        } catch (Exception e) {
            displayException(out, "Impossible d'appeler listAllCompte sur le bean", e);
            return;
        }

        // Liste pour chaque compte, le solde
        if(comptes != null){
            out.println("Liste des comptes:");
            out.println("<ul>");
            for (Compte compte : comptes) {
                out.println("<li>");
                out.println("<strong>" + compte.getNom() + ":</strong> " +
                        "<button onclick='showSoldePopup(" + compte.getSolde() + ")'>Afficher Solde</button>");
              Collection<Operation> lesOperationsDuCompte = compte.getOperations();

                if (lesOperationsDuCompte != null && lesOperationsDuCompte.size() != 0) {
                    out.println("<br/> &nbsp;&nbsp;Historique des transactions:");
                    out.println("<ul>");
                    for(Operation operation : lesOperationsDuCompte){
                        out.println("<li>"+operation.getTypeOperation() + " de " + operation.getMontant() + " euros</li>");
                    }
                    out.println("</ul>");
                }
                out.println("</li>");
            }
            out.println("</ul>");
        } else {
            out.println("Aucun compte trouvé !");
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
