package org.miglecz.optimization.stream;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.nullsFirst;
import static java.util.function.BinaryOperator.maxBy;
import java.util.Optional;
import java.util.stream.Collector;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Optimization;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.GeneticOptimizationBuilder;

/**
 * Helper class for {@link java.util.stream.Stream#collect(Collector)} in {@link Optimization#stream()}
 */
public class Collectors {
    /**
     * TODO replace maxBy and MIN_VALUE to something related to {@link GeneticOptimizationBuilder#comparator} in {@link Iteration} object
     *
     * @param <T> type of implementation
     * @return collector for getting the iteration holding the best score
     */
    public static <T> Collector<Iteration<T>, ?, Optional<Iteration<T>>> toBestIteration() {
        return java.util.stream.Collectors.maxBy(nullsFirst(comparingDouble((Iteration<T> i) -> i.getBest().map(Solution::getScore).orElse(Double.MIN_VALUE))));
    }

    /**
     * @param <T> type of implementation
     * @return collector for getting the solution with the best score
     */
    public static <T> Collector<Iteration<T>, ?, Solution<T>> toBestSolution() {
        return java.util.stream.Collectors.reducing(null, i -> i.getBest().orElse(null), maxBy(nullsFirst(comparingDouble(Solution::getScore))));
    }
}
