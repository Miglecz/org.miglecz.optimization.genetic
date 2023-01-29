package org.miglecz.optimization.genetic;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.miglecz.optimization.Iteration.newIteration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Optimization;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.exception.InitializationException;
import org.miglecz.optimization.genetic.exception.SelectionException;

@Slf4j
class GeneticOptimization<T> implements Optimization<T> {
    private final InitSelection<T> initialize;
    private final List<List<MultiSelection<T>>> selectionsList;
    private boolean parallel = false;

    public GeneticOptimization(final InitSelection<T> initialize, final List<List<MultiSelection<T>>> selectionsList) {
        this.initialize = initialize;
        this.selectionsList = selectionsList;
    }

    public GeneticOptimization(final InitSelection<T> initialize, final List<List<MultiSelection<T>>> selectionsList, final boolean parallel) {
        this.initialize = initialize;
        this.selectionsList = selectionsList;
        this.parallel = parallel;
    }

    @Override
    public Stream<Iteration<T>> stream() {
        try {
            return Stream.iterate(newIteration(0, initialize.get()), this::derive);
        } catch (final Exception e) {
            throw new InitializationException(initialize, e);
        }
    }

    private Iteration<T> derive(final Iteration<T> previousGeneration) {
        List<Solution<T>> previous = previousGeneration.getSolutions();
        List<Solution<T>> result = emptyList();
        for (final List<MultiSelection<T>> selections : selectionsList) {
            result = Collections.synchronizedList(new ArrayList<>());
            final List<Solution<T>> finalResult = result;
            final List<Solution<T>> finalPrevious = previous;
            log.debug("parallel={} selections={}:{}"
                , parallel
                , selections.size()
                , selections
            );
            (parallel ? selections.parallelStream() : selections.stream())
                .forEach(selection -> collect(finalResult, finalPrevious, selection));
            previous = result;
        }
        return newIteration(previousGeneration.getIndex() + 1, result);
    }

    private static <T> void collect(final List<Solution<T>> result, final List<Solution<T>> previous, final MultiSelection<T> selection) {
        try {
            result.addAll(selection.apply(previous));
        } catch (final Exception e) {
            throw new SelectionException(selection, unmodifiableList(previous), e);
        }
    }
}
