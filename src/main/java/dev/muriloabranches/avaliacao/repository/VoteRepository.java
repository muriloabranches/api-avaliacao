package dev.muriloabranches.avaliacao.repository;

import dev.muriloabranches.avaliacao.entity.Agenda;
import dev.muriloabranches.avaliacao.entity.User;
import dev.muriloabranches.avaliacao.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByAgendaAndUser(Agenda agenda, User user);

    List<Vote> findByAgenda(Agenda agenda);
}
