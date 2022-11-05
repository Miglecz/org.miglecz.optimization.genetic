package org.miglecz.optimization.genetic.operator;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.miglecz.optimization.Solution;

@RequiredArgsConstructor
public class InitialSelection<T> implements org.miglecz.optimization.genetic.InitSelection<T> {
    private final int limit;
    private final Fitness<T> fitness;
    private final Factory<T> factory;

    @Override
    public List<Solution<T>> get() {
        return IntStream.range(0, limit)
            .mapToObj(i -> factory.get())
            .map(impl -> newSolution(fitness.applyAsDouble(impl), impl))
            .collect(toUnmodifiableList());
    }
}
