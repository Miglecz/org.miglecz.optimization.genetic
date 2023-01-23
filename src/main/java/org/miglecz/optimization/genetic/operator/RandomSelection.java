package org.miglecz.optimization.genetic.operator;

import java.util.List;
import java.util.random.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.miglecz.optimization.Solution;

@RequiredArgsConstructor
public class RandomSelection<T> implements SingleSelection<T> {
    private final RandomGenerator random;

    @Override
    public Solution<T> apply(final List<Solution<T>> solutions) {
        final int size = solutions.size();
        if (size > 1) {
            return solutions.get(random.nextInt(solutions.size()));
        } else if (size == 1) {
            return solutions.get(0);
        } else {
            throw new UnsupportedOperationException("population should not be empty");
        }
    }
}
