package org.miglecz.optimization.genetic.operator;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.MultiSelection;

@RequiredArgsConstructor
public class OffspringSelection<T> implements MultiSelection<T> {
    private final int limit;
    private final SingleSelection<T> selection;
    private final Crossover<T> crossover;
    private final Fitness<T> fitness;

    @Override
    public List<Solution<T>> apply(final List<Solution<T>> solutions) {
        final int size = solutions.size();
        if (size > 1) {
            return IntStream.range(0, limit)
                .mapToObj(i -> IntStream.range(0, 2).mapToObj(j -> selection.apply(solutions)).collect(toUnmodifiableList()))
                .map(list -> list.stream().map(Solution::getImpl).collect(toUnmodifiableList()))
                .map(list -> crossover.apply(list.get(0), list.get(1)))
                .map(impl -> newSolution(fitness.applyAsDouble(impl), impl))
                .collect(toUnmodifiableList());
        } else if (size == 1) {
            return IntStream.range(0, limit)
                .mapToObj(i -> solutions.get(0))
                .collect(toUnmodifiableList());
        } else {
            return emptyList();
        }
    }
}
