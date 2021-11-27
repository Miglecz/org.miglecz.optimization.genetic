package org.miglecz.optimization.genetic.exception;

import lombok.Getter;
import org.miglecz.optimization.genetic.InitSelection;

@Getter
public class InitializationException extends GeneticException {
    private final InitSelection<?> init;

    public InitializationException(InitSelection<?> initialize, Exception e) {
        super(e);
        this.init = initialize;
    }
}
