package org.miglecz.optimization.genetic.exception;

public class GeneticException extends RuntimeException {
    public GeneticException(final String message) {
        super(message);
    }

    public GeneticException(final Exception e) {
        super(e);
    }
}
