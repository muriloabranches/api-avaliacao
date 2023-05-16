package dev.muriloabranches.avaliacao.service.impl;

import dev.muriloabranches.avaliacao.controller.request.CreateAgendaRequest;
import dev.muriloabranches.avaliacao.entity.Agenda;
import dev.muriloabranches.avaliacao.entity.enums.Status;
import dev.muriloabranches.avaliacao.exception.AgendaAlreadyOpenedException;
import dev.muriloabranches.avaliacao.exception.ResourceAlreadyExistsException;
import dev.muriloabranches.avaliacao.exception.ResourceNotFoundException;
import dev.muriloabranches.avaliacao.repository.AgendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendaServiceImplTest {

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private AgendaServiceImpl agendaService;

    @Test
    void shouldThrowResourceAlreadyExistsExceptionWhenNameAlreadyExists() {
        CreateAgendaRequest request = new CreateAgendaRequest("name", "description");

        when(agendaRepository.findByName(request.name())).thenReturn(Optional.of(new Agenda()));

        assertThrows(ResourceAlreadyExistsException.class, () -> agendaService.create(request));
    }

    @Test
    void shouldCreateAgendaWhenArgumentsAreValid() {
        CreateAgendaRequest dto = new CreateAgendaRequest("name", "description");

        agendaService.create(dto);

        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAgendaNotFound() {
        Long id = 1L;

        when(agendaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> agendaService.open(id, 1));
    }

    @Test
    void shouldThrowAgendaAlreadyOpenedExceptionWhenAgendaIsAlreadyOpened() {
        Agenda agenda = new Agenda();
        agenda.setStatus(Status.OPENED);

        when(agendaRepository.findById(anyLong())).thenReturn(Optional.of(agenda));

        assertThrows(AgendaAlreadyOpenedException.class, () -> agendaService.open(1L, 1));
    }

    @Test
    void shouldOpenAgendaWhenArgumentsAreValid() {
        Agenda agenda = new Agenda();
        agenda.setStatus(Status.CREATED);

        when(agendaRepository.findById(anyLong())).thenReturn(Optional.of(agenda));

        agendaService.open(1L, 1);

        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void shouldReturnAgendaWhenIdExists() {
        Agenda agenda = new Agenda();
        when(agendaRepository.findById(anyLong())).thenReturn(Optional.of(agenda));

        Optional<Agenda> result = agendaService.findById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalWhenIdDoesNotExist() {
        when(agendaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Agenda> result = agendaService.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCloseAgendaWhenAgendaExistsAndStatusIsNotClosed() {
        Long id = 1L;
        Agenda agenda = new Agenda();
        agenda.setStatus(Status.OPENED);

        when(agendaRepository.findById(id)).thenReturn(Optional.of(agenda));

        agendaService.close(id);

        verify(agendaRepository).save(agenda);
        assertEquals(Status.CLOSED, agenda.getStatus());
    }
}