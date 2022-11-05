package org.miglecz.optimization;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.nullsFirst;
import static java.util.function.BinaryOperator.maxBy;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.miglecz.optimization.genetic.facade.GeneticBuilderFacade;

/**
 * Helper class for {@link java.util.stream.Stream#collect(Collector)} in {@link Optimization#stream()}
 */
public class Collect {
    /**
     * TODO replace maxBy and MIN_VALUE to something related to {@link GeneticBuilderFacade#comparator} in {@link Iteration} object
     *
     * @param <T> type of implementation
     * @return collector for getting the iteration holding the best score
     */
    public static <T> Collector<Iteration<T>, ?, Optional<Iteration<T>>> toBestIteration() {
        return Collectors.maxBy(nullsFirst(comparingDouble((Iteration<T> i) -> i.getBest().map(Solution::getScore).orElse(Double.MIN_VALUE))));
    }

    /**
     * @param <T> type of implementation
     * @return collector for getting the solution with the best score
     */
    public static <T> Collector<Iteration<T>, ?, Solution<T>> toBestSolution() {
        return Collectors.reducing(null, i -> i.getBest().orElse(null), maxBy(nullsFirst(comparingDouble(Solution::getScore))));
    }
}
