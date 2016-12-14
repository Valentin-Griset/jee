<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table><tr style="background-color:grey;border:1 px solid black">

<td align="center">No de Compte</td>
<td align="center">Nom</td>
<td align="center">Prenom</td>
<td align="center">Soldes</td>
</tr>
<tr>
<td align="center"><%= request.getSession().getAttribute("NoDeCompte") %></td>
<td align="center"><%= request.getSession().getAttribute("Nom") %></td>
<td align="center"><%= request.getSession().getAttribute("Prenom") %></td>
<td align="center"><%= request.getSession().getAttribute("Solde") %></td>
</tr>
</table>

<p>Opérations à effectuer : </p>
<form action="/BanqueWeb/gestionCompte" method="post">
<table>
<tr>
<td>+ : <input type="radio" name="Op" value="+"> </td>
<td>- : <input type="radio" name="Op" value="-"> </td>
<td><input type="text" name="valeurEntiere" maxLength=10>,</td>
<td><input type="text" name="valeurDecimale" maxLength=2></td>
<td><input type="submit" value="Traiter" name="Demande"/></td>
</tr>
</table>
</form>

<% if((request.getAttribute("CodeAffichage") != "10") && (request.getAttribute("CodeAffichage") != "11" )) {%>
<p style="color:red">${ErrorMessage}</p>
<% } else if(request.getAttribute("CodeAffichage") == "11" ) {%>
<p style="color:blue" >${Message}</p>
<% } %>
</body>
</html>