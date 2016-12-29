package servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import gestionErreurs.ErrorMessage;
import gestionErreurs.TraitementException;
import javaBeans.BOperations;

/**
 * Servlet implementation class SOperations
 */
@WebServlet("/SOperations")
public class SOperations extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static DataSource ds;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init();
		try {
			Context envContext = new InitialContext();
			String repo = "java:comp/env"+config.getInitParameter("nomDataSource");
			ds = (DataSource)envContext.lookup(repo);
			System.out.println(repo);
			
		} catch (NamingException e) {
			System.err.println("Erreur lors de la récupération des informations du DataSource.");
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("CodeAffichage", "0");
		request.getRequestDispatcher("/SaisieNoDeCompte").forward(request, response);
		
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
				addValueAttribute(request);
				addExtractAttribute(request, request.getSession());
				request.setAttribute("CodeAffichage", e.getMessage());
				request.setAttribute("ErrorMessage", ErrorMessage.getErrorMessage(e.getMessage()));
				request.getServletContext().getRequestDispatcher("/Operations").forward(request, response);
			}
			break;
		case "Extraire la liste" :
			try {
				listeOperations(request, response);
			} catch (TraitementException e) {
				request.setAttribute("CodeAffichage", e.getMessage());
				request.setAttribute("ErrorMessage", ErrorMessage.getErrorMessage(e.getMessage()));
				request.getServletContext().getRequestDispatcher("/Operations").forward(request, response);
			}
			break;
		case "Retour" :
				backToOperations(request, response);
			break;
		case "Fin de traitement" : 
				removeExtractAttribute(request.getSession());
				removeValueAttribute(request.getSession());
				removeClientAttribute(request.getSession());
				request.getServletContext().getRequestDispatcher("/SaisieNoDeCompte").forward(request, response);
			break;
		default:
			break;
		}
	}

	private static void removeClientAttribute(HttpSession session) {
		session.removeAttribute("NoDeCompte");
		session.removeAttribute("Nom");
		session.removeAttribute("Prenom");
		session.removeAttribute("Solde");
	}

	private static void backToOperations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Retour de Liste
		request.setAttribute("CodeAffichage", "12");
		request.getServletContext().getRequestDispatcher("/Operations").forward(request, response);
	}

	private static void addValueAttribute(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("Op", request.getParameter("Op"));
		session.setAttribute("valeurEntiere", request.getParameter("valeurEntiere"));
		session.setAttribute("valeurDecimale", request.getParameter("valeurDecimale"));
	}
	
	private static void consulter(HttpServletRequest request, HttpServletResponse response) throws TraitementException, ServletException, IOException {
		BOperations op = new BOperations();
		verifNoDeCompte(request.getParameter("NoDeCompte"));
		op.openConnection(ds);
		op.setNoDeCompte(request.getParameter("NoDeCompte"));
		op.consult();
		op.closeConnection();
		setConsultAttribute(request, op);
		
		//1 er Appel
		removeValueAttribute(request.getSession());
		removeExtractAttribute(request.getSession());
		request.setAttribute("CodeAffichage", "10");
		
		request.getServletContext().getRequestDispatcher("/Operations").forward(request, response);
		
	}

	private static void traitement(HttpServletRequest request, HttpServletResponse response) throws TraitementException, ServletException, IOException {
		verifValeur(request.getParameter("valeurEntiere").concat("."+request.getParameter("valeurDecimale")));
		BOperations op = new BOperations();
		op.openConnection(ds);
		op.setNoDeCompte((String)request.getSession().getAttribute("NoDeCompte"));
		op.setOp(request.getParameter("Op"));
		op.setValue(request.getParameter("valeurEntiere").concat("."+request.getParameter("valeurDecimale")));
		op.traiter();
		op.closeConnection();
		setConsultAttribute(request, op);
		
		
		//Traitement de l'opération réussi
		removeValueAttribute(request.getSession());
		addExtractAttribute(request, request.getSession());
		request.setAttribute("CodeAffichage", "11");
		
		request.setAttribute("Message", "Opération réalisée : " + op.getOp() + op.getValue() + " Ancien solde : " + op.getOldSolde() +".\nNouveau solde : " + op.getNewSolde());
		request.getServletContext().getRequestDispatcher("/Operations").forward(request, response);
	}
	
	private static void listeOperations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TraitementException {
		addExtractAttribute(request, request.getSession());
		addValueAttribute(request);
		
		String dateInitiale = request.getParameter("aInit")+"-"+request.getParameter("mInit")+"-"+request.getParameter("jInit");
		String dateFinale = request.getParameter("aFinal")+"-"+request.getParameter("mFinal")+"-"+request.getParameter("jFinal");
		verifDates(dateInitiale, dateFinale);
		BOperations op = new BOperations();
		op.openConnection(ds);
		op.setNoDeCompte((String)request.getSession().getAttribute("NoDeCompte"));
		op.setDateInf(dateInitiale);
		op.setDateSup(dateFinale);
		List<String[]> operations = op.sortByDates();
		op.closeConnection();
		if(operations.isEmpty()) {
			throw new TraitementException("32");
		}
		
		
		request.getSession().setAttribute("listeOperation", operations);
		request.getServletContext().getRequestDispatcher("/ListeOperation").forward(request, response);
	}


	
	private static void verifValeur(String value) throws TraitementException {
		if(value.equals(".")) throw new TraitementException("26");
		try {
			Double.parseDouble(value);
		} catch(NumberFormatException e) {
			throw new TraitementException("25");
		}	
	}

	private static void verifNoDeCompte(String parameter) throws TraitementException {		
		if(parameter.length()!=4) throw new TraitementException("5");
		try {
			Integer.valueOf(parameter);
		} catch(Exception e) {
			throw new TraitementException("4");
		}
	}
	
	private static void verifDates(String dateInitiale, String dateFinale) throws TraitementException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dInit = null;
		Date dFinal = null;
		try {
			dInit = df.parse(dateInitiale);
			dFinal = df.parse(dateFinale);
		} catch (ParseException e) {
			throw new TraitementException("60");
		}
		if(dInit.compareTo(dFinal)==1) {
			throw new TraitementException("31");
		}
	}
	
	private static void setConsultAttribute(HttpServletRequest request, BOperations op) {
		HttpSession session = request.getSession();
		session.setAttribute("NoDeCompte", op.getNoDeCompte());
		session.setAttribute("Nom", op.getNom());
		session.setAttribute("Prenom", op.getPrenom());
		session.setAttribute("Solde", op.getSolde());
	}
	
	private static void addExtractAttribute(HttpServletRequest request, HttpSession session) {
		session.setAttribute("jInit", request.getParameter("jInit"));
		session.setAttribute("mInit", request.getParameter("mInit"));
		session.setAttribute("aInit", request.getParameter("aInit"));
		session.setAttribute("jFinal", request.getParameter("jFinal"));
		session.setAttribute("mFinal", request.getParameter("mFinal"));
		session.setAttribute("aFinal", request.getParameter("aFinal"));
	}
	
	private static void removeValueAttribute(HttpSession session) {
		session.removeAttribute("Op");
		session.removeAttribute("valeurEntiere");
		session.removeAttribute("valeurDecimale");
	}
	
	private static void removeExtractAttribute(HttpSession session) {
		session.removeAttribute("jInit");
		session.removeAttribute("mInit");
		session.removeAttribute("aInit");
		session.removeAttribute("jFinal");
		session.removeAttribute("mFinal");
		session.removeAttribute("aFinal");
	}
}
