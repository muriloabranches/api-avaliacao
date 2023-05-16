package dev.muriloabranches.avaliacao.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String name) {
        super(String.format("%s already exists", name));
    }
}
