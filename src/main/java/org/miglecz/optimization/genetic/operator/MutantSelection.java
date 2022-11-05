package org.miglecz.optimization.genetic.operator;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.MultiSelection;

@RequiredArgsConstructor
public class MutantSelection<T> implements MultiSelection<T> {
    private final int limit;
    private final SingleSelection<T> selection;
    private final Mutation<T> mutation;
    private final Fitness<T> fitness;

    @Override
    public List<Solution<T>> apply(final List<Solution<T>> solutions) {
        return IntStream.range(0, limit)
            .mapToObj(i -> selection.apply(solutions))
            .map(Solution::getImpl)
            .map(mutation)
            .map(impl -> newSolution(fitness.applyAsDouble(impl), impl))
            .collect(toUnmodifiableList());
    }
}
