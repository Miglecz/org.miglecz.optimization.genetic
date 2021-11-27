package org.miglecz.optimization;

import java.util.stream.Stream;

public interface Optimization<T> {
    Stream<Iteration<T>> stream();
}
