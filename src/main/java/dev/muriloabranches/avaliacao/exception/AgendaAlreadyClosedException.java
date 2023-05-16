package dev.muriloabranches.avaliacao.exception;

public class AgendaAlreadyClosedException extends RuntimeException {
    public AgendaAlreadyClosedException(String name) {
        super(String.format("%s already closed", name));
    }
}
