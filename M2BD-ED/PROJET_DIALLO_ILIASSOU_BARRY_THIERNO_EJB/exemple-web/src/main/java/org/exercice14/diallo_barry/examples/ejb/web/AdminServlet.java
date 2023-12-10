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
public class AdminServlet extends HttpServlet {

    /**
     * Serializable class uid.
     */
    private static final long serialVersionUID = 7724116000656853982L;

    /**
     * Local writer bean.
     */
    @EJB
    private LocalWriter writerBean;

    /**
     * Local reader bean.
     */
    @EJB
    private LocalReader readerBean;

    /**
     * Initializer bean.
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

        String title = "Ajout d'un compte";

        printHTMLHeader(out, title);

        out.println("<div class=\"contenneurAjoutEtAffich\">");

        out.println("  <div class=\"contennueAjout\">");

        // initialise la base de données si c'est le premier lancement de l'appli
        initCompte(out);

        out.println("<br />");

        // Affiche le formulaire d'ajout d'un compte
        printAddCompte(out);

        out.println("  </div>");


        out.println("  <div class=\"contennueAffich\">");

        // Essaye d'ajouter le nouveau compte
        addCompte(out, request.getParameter("nom"), request.getParameter("solde"));

        // Affiche les comptes mis à jour
        printComptes(out);
        out.println("  </div>");
        out.println("</div>");

        out.close();
    }


    /**
     * Print the page Header.
     * @param out Servlet {@link PrintWriter}
     * @param title page's title
     */
    private void printHTMLHeader(final PrintWriter out, final String title) {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
        out.println("  <head>");
        out.println("    <link type=\"text/css\" href=\"../AdminServlet.css\" rel=\"stylesheet\" id=\"stylesheet\"/>");
        out.println("    <title>" + title + "</title>");

        // Ajout du script JavaScript
        out.println("    <script type='text/javascript'>");
        out.println("    function validateForm() {");
        out.println("        var nom = document.forms['addCompteForm']['nom'].value;");
        out.println("        var solde = document.forms['addCompteForm']['solde'].value;");
        out.println("        if (nom == '' || solde == '') {");
        out.println("            alert('Le nom du compte et le solde doivent être renseignés.');");
        out.println("            return false;");
        out.println("        }");
        out.println("        return true;");
        out.println("    }");
        out.println("    </script>");

        out.println("  </head>");
        out.println("<body style=\"background : white; color : black;\">");

        out.println("<form action=\"\\exemple-web\" method=\"get\">");
        out.println("<div><input type=\"submit\" value=\"Accueil\"/></div>");
        out.println("</form>");

        out.println("<h1 class=\"title\">" + title + "</h1>");
    }


    /**
     * Initialise la liste des comptes
     * @param out the given writer
     */
    private void initCompte(final PrintWriter out) {
        out.println("Initialisation des comptes<br/>");

        try {
            initializerBean.initializeEntities();
        } catch (Exception e) {
            printException(out, "Impossible d'initialiser la liste des comptes", e);
            return;
        }
    }

    /**
     * Afficher compte.
     * @param out the given writer
     */
    private void printComptes(final PrintWriter out) {
        out.println("Les comptes");
        out.println("<br /><br />");

        // Récupérer la liste des comptes
        List<Compte> comptes = null;
        try {
            comptes = readerBean.listAllComptes();
        } catch (Exception e) {
            printException(out, "Impossible d'appeler listAllCompte sur le bean", e);
            return;
        }

        // Liste pour chaque compte, le solde
        if(comptes != null){
            out.println("Liste des comptes avec leurs solde");
            out.println("<ul>");
            for (Compte compte : comptes) {
                out.println("<li>");
                out.println("<strong>"+compte.getNom() + " a "+ compte.getSolde() + " euros.</strong>");
                Collection<Operation> lesOperationsDuCompte = compte.getOperations();

                if (lesOperationsDuCompte != null && lesOperationsDuCompte.size() != 0) {
                    out.println("<br/> &nbsp;&nbsp;Liste de ses opérations :");
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
     * Affiche les comptes
     * @param out the given writer
     */
    private void printAddCompte(final PrintWriter out) {

        out.println("<p>Ajout d'un nouveau compte : </p>");
        out.println("<form name='addCompteForm' action='add-compte' method='get' onsubmit='return validateForm()'>");
        out.println("  <div>");
        out.println("    <input name=\"nom\" type=\"text\" value=\"\" placeholder=\"Nom du compte\"/>");
        out.println("    <br/><br/>");
        out.println("    <input name=\"solde\" type=\"number\" value=\"\" placeholder=\"Solde du compte\"/>");
        out.println("    <br/><br/>");
        out.println("    <input type=\"submit\" value=\"Ajouter\"/>");
        out.println("  </div>");
        out.println("</form>");
    }


    /**
     * Ajouter un nom et solde {@link Compte} dans le model
     * @param out {@link PrintWriter} utilisé pour les affichages d'exceptions
     * @param nom nom du compte
     * @param solde solde du compte
     */
    private void addCompte(final PrintWriter out, final String nom, final String solde) {

        // Si le solde ou le nom n'est pas null
        if (nom != null && solde != null) {

            float soldeFloat = Float.parseFloat(solde);

            try {
                // Persistance d'un nouveau compte
                Compte compte = new Compte(nom, soldeFloat);
                writerBean.addCompte(compte);
            } catch (Exception e) {
                printException(out, "impossible d'ajouter un nouveau compte avec pour solde (" + solde + ")", e);
                return;
            }
        }

    }

    /**
     * If there is an exception, print the exception.
     * @param out the given writer
     * @param errMsg the error message
     * @param e the content of the exception
     */
    private void printException(final PrintWriter out, final String errMsg, final Exception e) {
        out.println("<p>Exception : " + errMsg);
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre></p>");
    }

}
