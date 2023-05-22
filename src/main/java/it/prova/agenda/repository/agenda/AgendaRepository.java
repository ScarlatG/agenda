package it.prova.agenda.repository.agenda;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.prova.agenda.model.Agenda;
import it.prova.agenda.model.Utente;

public interface AgendaRepository extends CrudRepository<Agenda, Long> {

	List<Agenda> findByUtente(Utente utente);

	List<Agenda> findAllWhereUtente(Utente utenteLoggato);
}