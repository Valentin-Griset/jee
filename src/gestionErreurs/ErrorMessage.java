package gestionErreurs;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {

	private static final Map<Integer,String> messages = new HashMap<>();
	static {
		messages.put(0, "Code r�serv�");
		messages.put(3, "Probl�me pour acc�der � ce compte client, v�rifiez qu'il est bien valide.");
		messages.put(4, "Le num�ro de compte doit �tre num�rique.");
		messages.put(5, "Le num�ro de compte doit comporter quatre caract�res.");
		messages.put(10, "Code r�serv�");
		messages.put(21, "Probl�me d'acc�s � la base de donn�es, veuillez le signaler � l'utilisateur.");
		messages.put(22, "Probl�me apr�s traitement. Le traitement a �t� effectu� correctement mais il y a eu un probl�me � signaler � votre administrateur.");
		messages.put(24, "Op�ration refus�e, d�bit demand� sup�rieur au cr�dit du compte.");
	}
	
	public static String getErrorMessage(String num) {
		return messages.get(Integer.valueOf(num));
	}
}
