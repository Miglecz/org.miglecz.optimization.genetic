package org.miglecz.optimization;

import java.util.stream.Stream;

/**
 * Interface of a general optimization
 *
 * @param <T> type of implementation
 */
public interface Optimization<T> {
    /**
     * @return stream of iterations of solutions
     */
    Stream<Iteration<T>> stream();
}
