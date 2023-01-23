package org.miglecz.optimization;

import static java.util.stream.Collectors.toUnmodifiableList;
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
        return new Iteration<>(index, solutions.stream()
            .sorted(Comparator.<Solution<T>>comparingDouble(Solution::getScore).reversed())
            .collect(toUnmodifiableList())
        );
    }

    /**
     * @return solution in the iteration by best score
     */
    public Optional<Solution<T>> getBest() {
        return solutions.stream().findFirst();
    }

    /**
     * @return solution in the iteration by median score
     */
    public Optional<Solution<T>> getMedian() {
        if (solutions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(solutions.get(solutions.size() / 2));
    }

    public Double getAverageScore() {
        return solutions.stream()
            .map(Solution::getScore)
            .reduce(Double::sum)
            .map(d -> d / solutions.size())
            .orElse(null);
    }

    @Override
    public String toString() {
        return String.format("%s{index=%d population=%d:%s}", getClass().getSimpleName(), getIndex(), getSolutions().size(), getSolutions());
    }

    public String toBestString() {
        return String.format("%s{index=%d population=%d best=%s}", getClass().getSimpleName(), getIndex(), getSolutions().size(), getBest().orElse(null));
    }
}
