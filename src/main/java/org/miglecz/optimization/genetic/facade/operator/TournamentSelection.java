package org.miglecz.optimization.genetic.facade.operator;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.miglecz.optimization.Solution;

@RequiredArgsConstructor
public class TournamentSelection<T> implements SingleSelection<T> {
    private final Random random;
    private final Comparator<Solution<T>> comparator;

    @Override
    public Solution<T> apply(final List<Solution<T>> solutions) {
        final int size = solutions.size();
        return IntStream.range(0, 2)
                .mapToObj(i -> random.nextInt(size))
                .map(solutions::get)
                .sorted(comparator)
                .limit(1)
                .findAny()
                .get();
    }
}
