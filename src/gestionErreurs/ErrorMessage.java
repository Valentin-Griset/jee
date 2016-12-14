package gestionErreurs;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {

	private static final Map<Integer,String> messages = new HashMap<>();
	static {
		messages.put(0, "Code réservé");
		messages.put(3, "Problème pour accéder à ce compte client, vérifiez qu'il est bien valide.");
		messages.put(4, "Le numéro de compte doit être numérique.");
		messages.put(5, "Le numéro de compte doit comporter quatre caractères.");
		messages.put(10, "Code réservé");
		messages.put(21, "Problème d'accès à la base de données, veuillez le signaler à l'utilisateur.");
		messages.put(22, "Problème après traitement. Le traitement a été effectué correctement mais il y a eu un problème à signaler à votre administrateur.");
		messages.put(24, "Opération refusée, débit demandé supérieur au crédit du compte.");
	}
	
	public static String getErrorMessage(String num) {
		return messages.get(Integer.valueOf(num));
	}
}
