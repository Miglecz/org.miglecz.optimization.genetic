package org.miglecz.optimization;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.nullsFirst;
import static java.util.function.BinaryOperator.maxBy;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.miglecz.optimization.genetic.facade.GeneticBuilderFacade;

public class Collect {
    /**
     * TODO replace maxBy and MIN_VALUE to something related to {@link GeneticBuilderFacade#comparator} in {@link Iteration} object
     */
    public static <T> Collector<Iteration<T>, ?, Optional<Iteration<T>>> toBestIteration() {
        return Collectors.maxBy(nullsFirst(comparingDouble((Iteration<T> i) -> i.getBest().map(Solution::getScore).orElse(Double.MIN_VALUE))));
    }

    public static <T> Collector<Iteration<T>, ?, Solution<T>> toBestSolution() {
        return Collectors.reducing(null, i -> i.getBest().orElse(null), maxBy(nullsFirst(comparingDouble(Solution::getScore))));
    }
}
