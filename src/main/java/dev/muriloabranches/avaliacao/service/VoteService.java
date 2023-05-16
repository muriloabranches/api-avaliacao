package dev.muriloabranches.avaliacao.service;

import dev.muriloabranches.avaliacao.controller.request.CreateVoteRequest;
import dev.muriloabranches.avaliacao.controller.response.ResultResponse;

public interface VoteService {

    void create(Long agendaId, CreateVoteRequest request);

    ResultResponse result(Long agendaId);
}
