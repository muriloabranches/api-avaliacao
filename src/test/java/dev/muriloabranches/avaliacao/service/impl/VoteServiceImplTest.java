package dev.muriloabranches.avaliacao.service.impl;

import dev.muriloabranches.avaliacao.controller.request.CreateVoteRequest;
import dev.muriloabranches.avaliacao.controller.response.ResultResponse;
import dev.muriloabranches.avaliacao.entity.Agenda;
import dev.muriloabranches.avaliacao.entity.User;
import dev.muriloabranches.avaliacao.entity.Vote;
import dev.muriloabranches.avaliacao.entity.enums.Status;
import dev.muriloabranches.avaliacao.entity.enums.Value;
import dev.muriloabranches.avaliacao.exception.AgendaAlreadyClosedException;
import dev.muriloabranches.avaliacao.exception.ResourceNotFoundException;
import dev.muriloabranches.avaliacao.repository.VoteRepository;
import dev.muriloabranches.avaliacao.service.AgendaService;
import dev.muriloabranches.avaliacao.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceImplTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private AgendaService agendaService;

    @Mock
    private UserService userService;

    @InjectMocks
    private VoteServiceImpl voteService;

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAgendaIsNotFound() {
        Long agendaId = 1L;
        CreateVoteRequest request = new CreateVoteRequest("YES", 2L);

        when(agendaService.findById(agendaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> voteService.create(agendaId, request));

        verify(agendaService).findById(agendaId);
        verifyNoMoreInteractions(agendaService, userService, voteRepository);
    }

    @Test
    void shouldThrowAgendaAlreadyClosedExceptionWhenAgendaIsAlreadyClosed() {
        Long agendaId = 1L;
        CreateVoteRequest request = new CreateVoteRequest("YES", 2L);
        Agenda agenda = new Agenda(agendaId, "agenda name", "agenda description", Status.OPENED, LocalDateTime.now().minusMinutes(1), Set.of());

        when(agendaService.findById(agendaId)).thenReturn(Optional.of(agenda));

        assertThrows(AgendaAlreadyClosedException.class, () -> voteService.create(agendaId, request));

        verify(agendaService).findById(agendaId);
        verify(agendaService).close(agendaId);
        verifyNoMoreInteractions(agendaService, userService, voteRepository);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUserIsNotFound() {
        Long agendaId = 1L;
        CreateVoteRequest request = new CreateVoteRequest("YES", 10L);
        Agenda agenda = new Agenda(agendaId, "agenda name", "agenda description", Status.OPENED, LocalDateTime.now().plusMinutes(1), Set.of());

        when(agendaService.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(userService.findById(request.userId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> voteService.create(agendaId, request));

        verify(agendaService).findById(agendaId);
        verify(userService).findById(request.userId());
        verifyNoMoreInteractions(agendaService, userService, voteRepository);
    }

    @Test
    void shouldReturnResult() {
        Long agendaId = 1L;
        Agenda agenda = new Agenda();
        agenda.setId(agendaId);
        agenda.setName("Test Agenda");
        agenda.setEndDateTime(LocalDateTime.now().plusMinutes(10));

        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");

        Vote vote1 = new Vote();
        vote1.setId(1L);
        vote1.setAgenda(agenda);
        vote1.setUser(user1);
        vote1.setValue(Value.YES);

        Vote vote2 = new Vote();
        vote2.setId(2L);
        vote2.setAgenda(agenda);
        vote2.setUser(user2);
        vote2.setValue(Value.YES);

        Vote vote3 = new Vote();
        vote3.setId(3L);
        vote3.setAgenda(agenda);
        vote3.setUser(user1);
        vote3.setValue(Value.NO);

        List<Vote> votes = List.of(vote1, vote2, vote3);

        when(agendaService.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(voteRepository.findByAgenda(agenda)).thenReturn(votes);

        ResultResponse resultResponse = voteService.result(agendaId);

        assertNotNull(resultResponse);
        assertEquals(resultResponse.total(), votes.size());
        assertEquals(resultResponse.result(), Value.YES.toString());
    }
}