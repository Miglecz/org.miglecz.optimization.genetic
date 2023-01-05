package org.miglecz.optimization;

import static lombok.AccessLevel.PRIVATE;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Container for holding the implementation of a solution
 *
 * @param <T> type of implementation
 */
@Value
@RequiredArgsConstructor(access = PRIVATE)
public class Solution<T> {
    double score;
    T impl;

    /**
     * Factory method
     *
     * @param score of the implementation
     * @param impl  implementation
     * @param <T>   type of implementation
     * @return solution instance
     */
    public static <T> Solution<T> newSolution(final double score, final T impl) {
        return new Solution<>(score, impl);
    }

    public Solution<T> accept(final Consumer<Solution<T>> consumer) {
        consumer.accept(this);
        return this;
    }

    public <R> R apply(final Function<Solution<T>, R> consumer) {
        return consumer.apply(this);
    }
}
