package dev.muriloabranches.avaliacao.controller;

import dev.muriloabranches.avaliacao.controller.request.CreateAgendaRequest;
import dev.muriloabranches.avaliacao.controller.request.CreateVoteRequest;
import dev.muriloabranches.avaliacao.controller.response.AgendaResponse;
import dev.muriloabranches.avaliacao.controller.response.ResultResponse;
import dev.muriloabranches.avaliacao.entity.Agenda;
import dev.muriloabranches.avaliacao.service.AgendaService;
import dev.muriloabranches.avaliacao.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/agendas")
@AllArgsConstructor
public class AgendaController {

    private AgendaService agendaService;
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<AgendaResponse> create(@RequestBody @Valid CreateAgendaRequest request) {
        Agenda agenda = agendaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AgendaResponse(agenda.getId(), agenda.getName(), agenda.getDescription()));
    }

    @PatchMapping("/{id}/open")
    public ResponseEntity<Object> open(@PathVariable("id") Long id, @RequestParam(required = false, defaultValue = "1") Integer minutes) {
        agendaService.open(id, minutes);
        return ResponseEntity.status(HttpStatus.OK).body("Agenda opened successfully");
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<Object> vote(@PathVariable("id") Long id, @RequestBody @Valid CreateVoteRequest request) {
        voteService.create(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vote created successfully");
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<ResultResponse> result(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(voteService.result(id));
    }
}
