import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreationTable {
	
	public static void createTableCompte(Connection conn) throws SQLException {
		Statement statement = conn.createStatement();
		statement.executeUpdate("CREATE TABLE COMPTE("
				+ "NOCOMPTE char(4) primary key,"
				+ "NOM varchar(20),"
				+ "PRENOM varchar(20),"
				+ "SOLDE decimal(10,2)"
				+ ")");
	}
	
	public static void createTableOperation(Connection conn) throws SQLException {
		Statement statement = conn.createStatement();
		statement.executeUpdate("CREATE TABLE OPERATIONS("
				+ "NOCOMPTE char(4) not null,"
				+ "DATE date not null,"
				+ "HEURE time not null,"
				+ "OP char(1) not null,"
				+ "VALUE decimal(10,2) not null"
				+ ")");
	}
	
	public static void populateTableCompte(Connection conn) throws SQLException {
		Statement statement = conn.createStatement();
		statement.executeUpdate("insert into COMPTE values('0001','Magnes','Charles','10.00')");
		statement.executeUpdate("insert into COMPTE values('0002','Legrand','Louis','205.00')");
		statement.executeUpdate("insert into COMPTE values('0003', 'Labelle','Katia','500.50')");
	}
	
	public static void populateTableOperation(Connection conn) throws SQLException {
		Statement statement = conn.createStatement();
		statement.executeUpdate("insert into OPERATIONS values('0001','2016-09-04','12:10:20','+', '30.00')");
		statement.executeUpdate("insert into OPERATIONS values('0002','2016-09-04','18:05:41','-', '10.00')");
		statement.executeUpdate("insert into OPERATIONS values('0003','2016-10-08','10:02:00','-', '80.00')");
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection(
					"jdbc:mysql://localhost/ig3_jee", 
					"necrosys", 
					"jt7ai2yyFc");
		
		createTableCompte(connect);
		createTableOperation(connect);
		populateTableCompte(connect);
		populateTableOperation(connect);
		
		connect.close();
	}
}
