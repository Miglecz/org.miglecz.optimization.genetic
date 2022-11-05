package org.miglecz.optimization.genetic.operator;

import java.util.function.ToDoubleFunction;

public interface Fitness<T> extends ToDoubleFunction<T> {
    double applyAsDouble(final T impl);
}
