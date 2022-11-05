package org.miglecz.optimization;

import static java.util.Collections.unmodifiableList;
import static lombok.AccessLevel.PRIVATE;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * Container for holding solutions of an optimization iteration (generation of a population)
 *
 * @param <T> type of implementation
 */
@Value
@RequiredArgsConstructor(access = PRIVATE)
public class Iteration<T> {
    int index;
    List<Solution<T>> solutions;
    @NonFinal
    transient Solution<T> best = null;

    /**
     * Factory method
     *
     * @param index     index of iteration
     * @param solutions list of solutions
     * @param <T>       type of implementation
     * @return iteration holding input @params
     */
    public static <T> Iteration<T> newIteration(final int index, final List<Solution<T>> solutions) {
        return new Iteration<>(index, unmodifiableList(solutions));
    }

    /**
     * @return solution in the iteration by best score
     */
    public Optional<Solution<T>> getBest() {
        if (best == null && !solutions.isEmpty()) {
            best = solutions.stream().max(Comparator.comparingDouble(Solution::getScore)).orElse(null);
        }
        return Optional.ofNullable(best);
    }
}
