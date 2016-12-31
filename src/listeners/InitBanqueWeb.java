package listeners;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class InitBanqueWeb implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			Context envContext = new InitialContext();
			String repo = "java:comp/env"+arg0.getServletContext().getInitParameter("nomDataSource");
			DataSource ds = (DataSource)envContext.lookup(repo);
			arg0.getServletContext().setAttribute("dataSource", ds);
		} catch (NamingException e) {
			System.err.println("Erreur lors de la récupération des informations du DataSource.");
			e.printStackTrace();
		}
	}

}
