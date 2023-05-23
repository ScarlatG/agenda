package it.prova.agenda.service.agenda;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.agenda.model.Agenda;
import it.prova.agenda.model.Utente;
import it.prova.agenda.repository.agenda.AgendaRepository;
import it.prova.agenda.service.utente.UtenteService;
import it.prova.agenda.web.api.exception.PermessoNegatoException;
import it.prova.agenda.web.api.exception.UtenteLoggatoNotFoundException;

@Service
@Transactional(readOnly = true)
public class AgendaServiceImpl implements AgendaService {

	@Autowired
	private AgendaRepository repository;

	@Autowired
	private UtenteService utenteService;

	@Override
	public List<Agenda> listAll() {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);

		if (utenteLoggato == null) {
			throw new UtenteLoggatoNotFoundException("Nessun utente loggato!");
		}

		return (List<Agenda>) repository.findAllByUtente(utenteLoggato);
	}

	@Override
	public List<Agenda> listAllByUtente(Utente utente) {
		// TODO Auto-generated method stub
		return (List<Agenda>) repository.findAllByUtente(utente);
	}

	@Override
	public Agenda caricaSingoloElemento(Long id) throws PermessoNegatoException {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);

		Agenda result = repository.findById(id).orElse(null);

		if (utenteLoggato.getRuoli().stream().map(r -> r.getCodice().equals("ROLE_ADMIN")).findAny()
				.orElse(null) != null || (result != null && utenteLoggato.getId() == result.getUtente().getId())) {
			return result;
		} else {
			throw new PermessoNegatoException("Non hai i permessi per visualizzare questo elemento!");
		}
	}

	@Override
	@Transactional
	public void aggiorna(Agenda agendaInstance) throws PermessoNegatoException {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);

		if (utenteLoggato.getRuoli().stream().map(r -> r.getCodice().equals("ROLE_ADMIN")).findAny()
				.orElse(null) != null
				|| (agendaInstance != null && utenteLoggato.getId() == agendaInstance.getUtente().getId())) {
			repository.save(agendaInstance);
		} else {
			throw new PermessoNegatoException("Non hai i permessi per modificare questo elemento!");
		}
	}

	@Override
	@Transactional
	public Agenda inserisciNuovo(Agenda agendaInstance) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);

		if (utenteLoggato == null) {
			throw new UtenteLoggatoNotFoundException("Nessun utente loggato!");
		}

		agendaInstance.setUtente(utenteLoggato);

		return repository.save(agendaInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idToRemove) throws PermessoNegatoException {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);

		Agenda result = repository.findById(idToRemove).orElse(null);

		if (utenteLoggato.getRuoli().stream().map(r -> r.getCodice().equals("ROLE_ADMIN")).findAny()
				.orElse(null) != null || (result != null && utenteLoggato.getId() == result.getUtente().getId())) {
			repository.deleteById(idToRemove);
		} else {
			throw new PermessoNegatoException("Non hai i permessi per rimuovere questo elemento!");
		}
	}

}