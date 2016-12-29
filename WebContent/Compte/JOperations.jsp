<%@page import="java.util.Locale"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	Calendar c = Calendar.getInstance();
	int jInit, mInit, aInit, jFinal, mFinal, aFinal;
	switch((String)request.getAttribute("CodeAffichage")) {
	case "10" :
		jInit = c.get(Calendar.DAY_OF_MONTH); mInit = c.get(Calendar.MONTH)+1; aInit = c.get(Calendar.YEAR);
		jFinal = jInit; mFinal = mInit; aFinal = aInit;
		break;
	case "11" :
		jInit = Integer.parseInt((String) request.getSession().getAttribute("jInit"));
		mInit = Integer.parseInt((String) request.getSession().getAttribute("mInit"));
		aInit = Integer.parseInt((String) request.getSession().getAttribute("aInit"));
		jFinal = Integer.parseInt((String) request.getSession().getAttribute("jFinal")); 
		mFinal = Integer.parseInt((String) request.getSession().getAttribute("mFinal"));
		aFinal = Integer.parseInt((String) request.getSession().getAttribute("aFinal"));
		break;
	case "12" :
		jInit = Integer.parseInt((String) request.getSession().getAttribute("jInit"));
		mInit = Integer.parseInt((String) request.getSession().getAttribute("mInit"));
		aInit = Integer.parseInt((String) request.getSession().getAttribute("aInit"));
		jFinal = Integer.parseInt((String) request.getSession().getAttribute("jFinal")); 
		mFinal = Integer.parseInt((String) request.getSession().getAttribute("mFinal"));
		aFinal = Integer.parseInt((String) request.getSession().getAttribute("aFinal"));
		break;
	default : 
		jInit = Integer.parseInt((String) request.getSession().getAttribute("jInit"));
		mInit = Integer.parseInt((String) request.getSession().getAttribute("mInit"));
		aInit = Integer.parseInt((String) request.getSession().getAttribute("aInit"));
		jFinal = Integer.parseInt((String) request.getSession().getAttribute("jFinal")); 
		mFinal = Integer.parseInt((String) request.getSession().getAttribute("mFinal"));
		aFinal = Integer.parseInt((String) request.getSession().getAttribute("aFinal"));
		break;
	}

	DecimalFormat formater = new DecimalFormat("00");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<table>
		<tr style="background-color: grey; border: 1 px solid black">

			<td align="center">No de Compte</td>
			<td align="center">Nom</td>
			<td align="center">Prenom</td>
			<td align="center">Soldes</td>
		</tr>
		<tr>
			<td align="center"><%=request.getSession().getAttribute("NoDeCompte")%></td>
			<td align="center"><%=request.getSession().getAttribute("Nom")%></td>
			<td align="center"><%=request.getSession().getAttribute("Prenom")%></td>
			<td align="center"><%=request.getSession().getAttribute("Solde")%></td>
		</tr>
	</table>
	<br />

	<form action="/BanqueWeb/gestionCompte" method="post">

		<!-- OPERATION & VALEUR -->
		<table>
			<tr style="background-color: grey; border: 1 px solid black">
				<td align="center" colspan="2">Opérations à effectuer</td>
				<td align="center" colspan="2">Valeur</td>
			</tr>
			<tr>
				<td>(+) : <input type="radio" name="Op" value="+" checked>
				</td>
				<%
					if ((session.getAttribute("Op") != null) && (session.getAttribute("Op").equals("-"))) {
				%>
				<td>(-) : <input type="radio" name="Op" value="-" checked>
				</td>
				<%
					} else {
				%>
				<td>(-) : <input type="radio" name="Op" value="-">
				</td>
				<%
					}
				%>
				<td><input type="text" name="valeurEntiere" maxLength=10
					value=<%=session.getAttribute("valeurEntiere") == null ? "" : session.getAttribute("valeurEntiere")%>>,</td>
				<td><input type="text" name="valeurDecimale" maxLength=2
					value=<%=session.getAttribute("valeurDecimale") == null ? "" : session.getAttribute("valeurDecimale")%>></td>
				<td><input type="submit" value="Traiter" name="Demande" /></td>
			</tr>
		</table>

		<!-- DATES -->
		<table>
			<tr style="background-color: grey; border: 1 px solid black">
				<td colspan="7" align="center">Listes des opérations réalisées


				
			</tr>
			<tr>
				<td>Du :</td>
				<td><select name="jInit">
						<%
							for (int i = 1; i <= 31; i++) {
						%>	<option <%=(i == jInit) ? "selected" : ""%> value="<%=i%>"><%=formater.format(i)%></option>
						<% 	} %>
				</select></td>
				<td><select name="mInit">
						<%
							for (int i = 1; i <= 12; i++) {
								%>
						<option <%=(i == mInit) ? "selected" : ""%> value="<%=i%>"><%=formater.format(i)%></option>
						<%
									}
								%>
				</select></td>
				<td><select name="aInit">
						<%
						for (int i = 2016; i > 2016-5 ; i--) {
								%>
						<option <%=(i == aInit) ? "selected" : ""%> value="<%=i%>"><%=i%></option>
						<%
									}
								%>
				</select></td>
				<td><select name="jFinal">
						<%
							for (int i = 1; i <= 31; i++) {
						%>
						<option <%=(i == jFinal) ? "selected" : ""%> value="<%=i%>"><%=formater.format(i)%></option>
						<%
							}
						%>
				</select></td>
				<td><select name="mFinal"> 
				<%
							for (int i = 1; i <= 12; i++) {
								%>
						<option <%=(i == mFinal) ? "selected" : ""%> value="<%=i%>"><%=formater.format(i)%></option>
						<%
							}
						%>
				</select></td>
				<td><select name="aFinal">
						<%
							for (int i = 2016; i > 2016-5 ; i--) {
								%>
						<option <%=(i == aFinal) ? "selected" : ""%> value="<%=i%>"><%=i%></option>
						<%
									}
								%>
				</select></td>
				<td><input type="submit" value="Extraire la liste"
					name="Demande" /></td>
			</tr>
		</table>
		
		<input type="submit" value="Fin de traitement"
					name="Demande" /></td>
	</form>

	<%
		switch((String)request.getAttribute("CodeAffichage")) {
		case "10" :
			break;
		case "11" :
			%>
			<p style="color: blue">${Message}</p>
			<%
			break;
		case "12" :
			break;
		default : 
			%>
			<p style="color: red">${ErrorMessage}</p>
			<%
			break;
		}
	%>
</body>
</html>