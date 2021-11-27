package org.miglecz.optimization.genetic.exception;

public class GeneticException extends RuntimeException {
    public GeneticException(String message) {
        super(message);
    }

    public GeneticException(Exception e) {
        super(e);
    }
}
