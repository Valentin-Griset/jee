package gestionErreurs;

public class TraitementException extends Exception{
	private static final long serialVersionUID = 1L;	
	
	public TraitementException(String error) {
		super(error);
	}

}
