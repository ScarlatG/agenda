package it.prova.agenda.web.api.exception;

public class UtenteLoggatoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UtenteLoggatoNotFoundException(String message) {
		super(message);
	}

}