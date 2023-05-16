package dev.muriloabranches.avaliacao.service;

import dev.muriloabranches.avaliacao.controller.request.CreateAgendaRequest;
import dev.muriloabranches.avaliacao.entity.Agenda;

import java.util.Optional;

public interface AgendaService {

    Agenda create(CreateAgendaRequest request);

    void open(Long id, Integer minutes);

    Optional<Agenda> findById(Long id);

    void close(Long id);
}
