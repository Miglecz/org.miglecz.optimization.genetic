package org.miglecz.optimization.genetic.facade.operator;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.MultiSelection;

@RequiredArgsConstructor
public class MutantSelection<T> implements MultiSelection<T> {
    private final MultiSelection<T> randomSelection;
    private final Fitness<T> fitness;
    private final Mutation<T> mutation;

    @Override
    public List<Solution<T>> apply(final List<Solution<T>> solutions) {
        return randomSelection.apply(solutions).stream()
                .map(Solution::getImpl)
                .map(mutation)
                .map(impl -> newSolution(fitness.applyAsDouble(impl), impl))
                .collect(toUnmodifiableList());
    }
}
