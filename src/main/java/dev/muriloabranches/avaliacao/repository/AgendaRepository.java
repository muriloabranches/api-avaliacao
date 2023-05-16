package dev.muriloabranches.avaliacao.repository;

import dev.muriloabranches.avaliacao.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    Optional<Agenda> findByName(String name);
}
