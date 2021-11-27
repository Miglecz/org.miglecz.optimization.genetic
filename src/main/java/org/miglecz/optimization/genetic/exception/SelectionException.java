package org.miglecz.optimization.genetic.exception;

import java.util.List;
import lombok.Getter;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.MultiSelection;

@Getter
public class SelectionException extends GeneticException {
    private final MultiSelection<?> selection;
    private final List<Solution<?>> previous;

    public SelectionException(MultiSelection<?> selection, List<Solution<?>> previous, Exception e) {
        super(e);
        this.selection = selection;
        this.previous = previous;
    }
}
