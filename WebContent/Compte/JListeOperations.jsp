<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% 
 
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Liste d'opérations</title>
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
	
	
	<table>	
		<% 
		ArrayList<String[]> operations = (ArrayList)request.getSession().getAttribute("listeOperation");
			%> 
		<tr style="background-color: grey; border: 1 px solid black">
			<td align="center">Date</td>
			<td align="center">Opération</td>
			<td align="center">Valeur</td>
		</tr>
	
	<%
		for(String[] operation : operations) {
			
			%>
			<tr>
				<td align="center"><%=operation[0] %></td>
				<td align="center"><%=operation[1] %></td>
				<td align="center"><%=operation[2] %></td>
			</tr>
			<% } 
		%>
	</table>
	
	<form action="/BanqueWeb/gestionCompte" method="post">
	<input type="submit" value="Retour" name="Demande" />
	</form>
	
</body>
</html>