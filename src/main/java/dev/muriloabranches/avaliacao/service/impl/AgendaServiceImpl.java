package dev.muriloabranches.avaliacao.service.impl;

import dev.muriloabranches.avaliacao.controller.request.CreateAgendaRequest;
import dev.muriloabranches.avaliacao.entity.Agenda;
import dev.muriloabranches.avaliacao.entity.enums.Status;
import dev.muriloabranches.avaliacao.exception.AgendaAlreadyOpenedException;
import dev.muriloabranches.avaliacao.exception.ResourceAlreadyExistsException;
import dev.muriloabranches.avaliacao.exception.ResourceNotFoundException;
import dev.muriloabranches.avaliacao.repository.AgendaRepository;
import dev.muriloabranches.avaliacao.service.AgendaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AgendaServiceImpl implements AgendaService {

    private AgendaRepository agendaRepository;

    @Override
    public Agenda create(CreateAgendaRequest request) {
        if (agendaRepository.findByName(request.name()).isPresent()) {
            throw new ResourceAlreadyExistsException("agenda");
        }

        Agenda agenda = new Agenda();
        agenda.setName(request.name());
        agenda.setDescription(request.description());
        agenda.setStatus(Status.CREATED);

        return agendaRepository.save(agenda);
    }

    @Override
    public void open(Long id, Integer minutes) {
        Optional<Agenda> agenda = findById(id);

        if (agenda.isEmpty()) {
            throw new ResourceNotFoundException("agenda");
        }

        if (agenda.get().getStatus() != Status.CREATED) {
            throw new AgendaAlreadyOpenedException(agenda.get().getName());
        }

        agenda.get().setStatus(Status.OPENED);
        agenda.get().setEndDateTime(LocalDateTime.now().plusMinutes(minutes));

        agendaRepository.save(agenda.get());
    }

    @Override
    public Optional<Agenda> findById(Long id) {
        return agendaRepository.findById(id);
    }

    @Override
    public void close(Long id) {
        Optional<Agenda> agenda = findById(id);

        if (agenda.isPresent()) {
            if (agenda.get().getStatus().equals(Status.CLOSED)) {
                return;
            }

            agenda.get().setStatus(Status.CLOSED);
            agendaRepository.save(agenda.get());
        }
    }
}
