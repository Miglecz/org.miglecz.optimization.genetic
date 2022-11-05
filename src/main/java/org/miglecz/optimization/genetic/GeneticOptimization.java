package org.miglecz.optimization.genetic;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.miglecz.optimization.Iteration.newIteration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Optimization;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.exception.InitializationException;
import org.miglecz.optimization.genetic.exception.SelectionException;

@RequiredArgsConstructor
class GeneticOptimization<T> implements Optimization<T> {
    private final InitSelection<T> initialize;
    private final List<List<MultiSelection<T>>> selectionsList;

    @Override
    public Stream<Iteration<T>> stream() {
        try {
            return Stream.iterate(newIteration(0, initialize.get()), this::derive);
        } catch (Exception e) {
            throw new InitializationException(initialize, e);
        }
    }

    private Iteration<T> derive(final Iteration<T> previousGeneration) {
        List<Solution<T>> previous = previousGeneration.getSolutions();
        List<Solution<T>> result = emptyList();
        for (List<MultiSelection<T>> selections : selectionsList) {
            result = new ArrayList<>();
            for (MultiSelection<T> selection : selections) {
                try {
                    result.addAll(selection.apply(previous));
                } catch (Exception e) {
                    throw new SelectionException(selection, unmodifiableList(previous), e);
                }
            }
            previous = result;
        }
        return newIteration(previousGeneration.getIndex() + 1, result);
    }
}
