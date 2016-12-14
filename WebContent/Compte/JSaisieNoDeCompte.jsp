<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Notre banque de la mort qui tue</title>
</head>
<body>
<h1>Saisie du numero de compte</h1>
<form action="/BanqueWeb/gestionCompte" method="post">
<% if(request.getAttribute("CodeAffichage") == "0") { %>
	<table><tr>
	<td>Entrez le numero de compte</td>
	<td><input type="text" name="NoDeCompte" value=""></td>
	</tr></table>
	<input type="submit" value="Consulter" name="Demande" />
<% } else {%>
	<table><tr>
	<td>Entrez le numero de compte</td>
	<td><input type="text" name="NoDeCompte" value=${NoDeCompte} ></td>
	</tr></table>
	<input type="submit" value="Consulter" name="Demande" />
	<p>${ErrorMessage}</p>
<%} %>
</form>


</body>
</html>