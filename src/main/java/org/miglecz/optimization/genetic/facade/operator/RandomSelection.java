package org.miglecz.optimization.genetic.facade.operator;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.miglecz.optimization.Solution;

@RequiredArgsConstructor
public class RandomSelection<T> implements SingleSelection<T> {
    private final Random random;

    @Override
    public Solution<T> apply(final List<Solution<T>> solutions) {
        return solutions.get(random.nextInt(solutions.size()));
    }
}
