package org.miglecz.optimization.genetic.facade.operator;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toUnmodifiableList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.MultiSelection;

public class RandomSelection<T> implements MultiSelection<T> {
    private final int limit;
    private final Random random;

    public RandomSelection(final int limit, final Random random) {
        this.limit = limit;
        this.random = random;
    }

    @Override
    public List<Solution<T>> apply(final List<Solution<T>> solutions) {
        if (solutions.isEmpty()) {
            return emptyList();
        }
        return IntStream.range(0, limit)
                .map(i -> random.nextInt(solutions.size()))
                .mapToObj(solutions::get)
                .collect(toUnmodifiableList());
    }
}
