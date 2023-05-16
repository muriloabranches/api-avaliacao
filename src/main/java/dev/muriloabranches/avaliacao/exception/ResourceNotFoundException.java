package dev.muriloabranches.avaliacao.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String name) {
        super(String.format("%s not found", name));
    }
}
