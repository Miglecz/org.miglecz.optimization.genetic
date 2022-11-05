package org.miglecz.optimization.genetic.exception;

import java.util.List;
import lombok.Getter;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.MultiSelection;

@Getter
public class SelectionException extends GeneticException {
    private final MultiSelection<?> selection;
    private final List<Solution<?>> previous;

    public SelectionException(final MultiSelection<?> selection, final List<Solution<?>> previous, final Exception e) {
        super(e);
        this.selection = selection;
        this.previous = previous;
    }
}
