package it.prova.agenda.asfd;

public class AgendaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AgendaNotFoundException(String message) {
		super(message);
	}

}