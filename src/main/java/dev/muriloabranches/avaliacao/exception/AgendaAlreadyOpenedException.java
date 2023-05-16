package dev.muriloabranches.avaliacao.exception;

public class AgendaAlreadyOpenedException extends RuntimeException {
    public AgendaAlreadyOpenedException(String name) {
        super(String.format("%s already opened", name));
    }
}
