package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gestionErreurs.ErrorMessage;
import gestionErreurs.TraitementException;
import javaBeans.BOperations;

/**
 * Servlet implementation class SOperations
 */
@WebServlet("/SOperations")
public class SOperations extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/SaisieNoDeCompte").forward(request, response);;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append(request.getParameter("NoDeCompte"));
		switch (request.getParameter("Demande")) {
		case "Consulter":
			try {
				consulter(request, response);
			} catch (TraitementException e) {
				request.setAttribute("NoDeCompte", request.getParameter("NoDeCompte"));
				request.setAttribute("CodeAffichage", e.getMessage());
				request.setAttribute("ErrorMessage", ErrorMessage.getErrorMessage(e.getMessage()));
				request.getServletContext().getRequestDispatcher("/SaisieNoDeCompte").forward(request, response);
			}
			break;
		case "Traiter" :
			try {
				traitement(request, response);
			} catch (TraitementException e) {
				request.setAttribute("NoDeCompte", request.getParameter("NoDeCompte"));
				request.setAttribute("CodeAffichage", e.getMessage());
				request.setAttribute("ErrorMessage", ErrorMessage.getErrorMessage(e.getMessage()));
				request.getServletContext().getRequestDispatcher("/Operations").forward(request, response);
			}
			break;
		default:
			break;
		}
	}
	
	private static void consulter(HttpServletRequest request, HttpServletResponse response) throws TraitementException, ServletException, IOException {
		BOperations op = new BOperations();
		verifNoDeCompte(request.getParameter("NoDeCompte"));
		op.openConnection();
		op.setNoDeCompte(request.getParameter("NoDeCompte"));
		op.consult();
		op.closeConnection();
		setConsultAttribute(request, op);
		request.setAttribute("CodeAffichage", "10");
		request.getServletContext().getRequestDispatcher("/Operations").forward(request, response);
		
	}

	private static void traitement(HttpServletRequest request, HttpServletResponse response) throws TraitementException, ServletException, IOException {
		BOperations op = new BOperations();
		op.openConnection();
		op.setNoDeCompte((String)request.getSession().getAttribute("NoDeCompte"));
		op.setOp(request.getParameter("Op"));
		op.setValue(request.getParameter("valeurEntiere").concat("."+request.getParameter("valeurDecimale")));
		op.traiter();
		op.closeConnection();
		setConsultAttribute(request, op);
		request.setAttribute("CodeAffichage", "11");
		request.setAttribute("Message", "Opération réalisée : " + op.getOp() + op.getValue() + " Ancien solde : " + op.getOldSolde() +".");
		request.getServletContext().getRequestDispatcher("/Operations").forward(request, response);
	}
	
	private static void setConsultAttribute(HttpServletRequest request, BOperations op) {
		HttpSession session = request.getSession();
		session.setAttribute("NoDeCompte", op.getNoDeCompte());
		session.setAttribute("Nom", op.getNom());
		session.setAttribute("Prenom", op.getPrenom());
		session.setAttribute("Solde", op.getSolde());
	}

	private static void verifNoDeCompte(String parameter) throws TraitementException {		
		if(parameter.length()!=4) throw new TraitementException("5");
		try {
			Integer.valueOf(parameter);
		} catch(Exception e) {
			throw new TraitementException("4");
		}
	}
	
	private static String convert(String parameter, String parameter2) {
		// TODO Auto-generated method stub
		return null;
	}

}
