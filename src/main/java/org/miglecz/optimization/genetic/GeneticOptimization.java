package org.miglecz.optimization.genetic;

import static com.google.common.flogger.LazyArgs.lazy;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.miglecz.optimization.Iteration.newIteration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.extern.flogger.Flogger;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Optimization;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.exception.InitializationException;
import org.miglecz.optimization.genetic.exception.SelectionException;

@Flogger
class GeneticOptimization<T> implements Optimization<T> {
    private final List<InitSelection<T>> initializeList;
    private final List<List<MultiSelection<T>>> selectionsList;
    private boolean parallel = false;

    public GeneticOptimization(final List<InitSelection<T>> initializeList, final List<List<MultiSelection<T>>> selectionsList) {
        this.initializeList = initializeList;
        this.selectionsList = selectionsList;
    }

    public GeneticOptimization(final List<InitSelection<T>> initializeList, final List<List<MultiSelection<T>>> selectionsList, final boolean parallel) {
        this.initializeList = initializeList;
        this.selectionsList = selectionsList;
        this.parallel = parallel;
    }

    @Override
    public Stream<Iteration<T>> stream() {
        try {
            log.atConfig().log("initializing=%s:%s"
                , initializeList.size()
                , lazy(() -> initializeList)
            );
            final var initial = (parallel ? initializeList.parallelStream() : initializeList.stream())
                .map(Supplier::get)
                .flatMap(Collection::stream)
                .toList();
            return Stream.iterate(newIteration(0, initial), this::derive);
        } catch (final Exception e) {
            throw new InitializationException(e);
        }
    }

    private Iteration<T> derive(final Iteration<T> previousGeneration) {
        List<Solution<T>> previous = previousGeneration.getSolutions();
        List<Solution<T>> result = emptyList();
        for (final List<MultiSelection<T>> selections : selectionsList) {
            result = Collections.synchronizedList(new ArrayList<>());
            final List<Solution<T>> finalResult = result;
            final List<Solution<T>> finalPrevious = previous;
            log.atConfig().log("selections=%s:%s"
                , selections.size()
                , lazy(() -> selections)
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
