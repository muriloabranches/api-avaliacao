package dev.muriloabranches.avaliacao.service.impl;

import dev.muriloabranches.avaliacao.controller.request.CreateVoteRequest;
import dev.muriloabranches.avaliacao.controller.response.ResultResponse;
import dev.muriloabranches.avaliacao.entity.Agenda;
import dev.muriloabranches.avaliacao.entity.User;
import dev.muriloabranches.avaliacao.entity.Vote;
import dev.muriloabranches.avaliacao.entity.enums.Value;
import dev.muriloabranches.avaliacao.exception.AgendaAlreadyClosedException;
import dev.muriloabranches.avaliacao.exception.ResourceAlreadyExistsException;
import dev.muriloabranches.avaliacao.exception.ResourceNotFoundException;
import dev.muriloabranches.avaliacao.repository.VoteRepository;
import dev.muriloabranches.avaliacao.service.AgendaService;
import dev.muriloabranches.avaliacao.service.UserService;
import dev.muriloabranches.avaliacao.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {

    private VoteRepository voteRepository;
    private AgendaService agendaService;
    private UserService userService;

    @Override
    public void create(Long agendaId, CreateVoteRequest request) {
        Optional<Agenda> agenda = agendaService.findById(agendaId);

        if (agenda.isEmpty()) {
            throw new ResourceNotFoundException("agenda");
        }

        if (agenda.get().getEndDateTime().isBefore((LocalDateTime.now()))) {
            agendaService.close(agendaId);
            throw new AgendaAlreadyClosedException(agenda.get().getName());
        }

        Optional<User> user = userService.findById(request.userId());

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("user");
        }

        if (voteRepository.findByAgendaAndUser(agenda.get(), user.get()).isPresent()) {
            throw new ResourceAlreadyExistsException("vote");
        }

        Vote vote = new Vote();
        vote.setValue(Value.valueOf(request.value().toUpperCase()));
        vote.setAgenda(agenda.get());
        vote.setUser(user.get());

        voteRepository.save(vote);
    }

    @Override
    public ResultResponse result(Long agendaId) {
        Optional<Agenda> agenda = agendaService.findById(agendaId);

        if (agenda.isEmpty()) {
            throw new ResourceNotFoundException("agenda");
        }

        List<Vote> votes = voteRepository.findByAgenda(agenda.get());
        long totalVotes = votes.size();
        long yesVotes = votes.stream().filter(vote -> vote.getValue().equals(Value.YES)).count();
        long noVotes = votes.stream().filter(vote -> vote.getValue().equals(Value.NO)).count();

        return new ResultResponse(totalVotes, (yesVotes > noVotes ? Value.YES : Value.NO).toString());
    }
}
