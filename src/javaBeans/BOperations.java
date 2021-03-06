package javaBeans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import gestionErreurs.TraitementException;

public class BOperations {

	// Consultation
	private String noDeCompte;
	private String nom;
	private String prenom;
	private BigDecimal solde;

	private Connection conn;

	// Traitement
	private BigDecimal oldSolde;
	private BigDecimal newSolde;
	private BigDecimal value;
	private String op;

	// Tri
	private String dateInf;
	private String dateSup;
	private List<String[]> operationsParDates;

	public void consult() throws TraitementException {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT nom,prenom,solde FROM compte where NOCOMPTE='" + noDeCompte + "'");
			if (rs.next()) {
				this.nom = rs.getString("NOM");
				this.prenom = rs.getString("PRENOM");
				this.solde = rs.getBigDecimal("SOLDE");
			} else {
				throw new TraitementException("3");
			}
		} catch (SQLException e) {
			throw new TraitementException("3");
		}
	}

	public void traiter() throws TraitementException {
		try {
			Statement st = null;
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT solde FROM compte where NOCOMPTE='" + noDeCompte + "'");
			if (rs.next()) {
				this.oldSolde = rs.getBigDecimal("SOLDE");
			} else {
				throw new TraitementException("3");
			}
			
			// Traitement
			newSolde = (op.equals("+")) ? oldSolde.add(value) : oldSolde.subtract(value);
			if (newSolde.compareTo(new BigDecimal(0)) == -1) {
				throw new TraitementException("24");
			} else {
				try {
					st.executeUpdate("UPDATE COMPTE SET SOLDE=" + newSolde + " where NOCOMPTE='" + noDeCompte + "'");

					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					StringBuilder sb = new StringBuilder();
					sb.append("insert into OPERATIONS values(");
					sb.append("'" + noDeCompte + "',");
					sb.append("'" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-"
							+ cal.get(Calendar.DAY_OF_MONTH) + "',");
					sb.append("'" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":"
							+ cal.get(Calendar.SECOND) + "',");
					sb.append("'" + op + "',");
					sb.append(this.value + ")");
					st.executeUpdate(sb.toString());
					conn.commit();
				} catch (SQLException e) {
					conn.rollback();
					throw e;
				}
				consult();
			}
		} catch(SQLException e) {
			throw new TraitementException("22");
		}
	}

	public List<String[]> sortByDates() throws TraitementException {
		operationsParDates = new ArrayList<String[]>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT date,heure,op,value FROM OPERATIONS where NOCOMPTE = '" + noDeCompte
					+ "' and date >= '" + dateInf + "' and date <= '" + dateSup + "' order by date desc,heure desc");
			while (rs.next()) {
				String[] operation = {rs.getDate("date")+"",rs.getString("op")+"",rs.getBigDecimal("value")+""};
				operationsParDates.add(operation);
			}
		} catch(SQLException e) {
			throw new TraitementException("21");
		}
		return operationsParDates;
	}

	public void openConnection(DataSource ds) throws TraitementException {
		if(ds == null) {
			System.err.println("Erreur lors de l'utilisation du DataSource.");
			throw new TraitementException("21");
		}
		try {
			this.conn = ds.getConnection();
			this.conn.setAutoCommit(false);
		} catch (SQLException e) {
			System.err.println("Erreur lors de la récupération de la connexion.");
			throw new TraitementException("21");
		}
	}

	public void closeConnection() throws TraitementException {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TraitementException("22");
		}
	}

	public String getNoDeCompte() {
		return noDeCompte;
	}

	public void setNoDeCompte(String noDeCompte) {
		this.noDeCompte = noDeCompte;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public BigDecimal getSolde() {
		return solde;
	}

	public String getValue() {
		return String.valueOf(value.floatValue());
	}

	public void setValue(String value) {
		this.value = new BigDecimal(value);
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public BigDecimal getOldSolde() {
		return oldSolde;
	}

	public BigDecimal getNewSolde() {
		return newSolde;
	}

	public String getDateInf() {
		return dateInf;
	}

	public void setDateInf(String dateInf) {
		this.dateInf = dateInf;
	}

	public String getDateSup() {
		return dateSup;
	}

	public void setDateSup(String dateSup) {
		this.dateSup = dateSup;
	}

	public List<String[]> getOperationsParDates() {
		return operationsParDates;
	}

}
