package org.miglecz.optimization.genetic.facade.operator;

import java.util.function.ToDoubleFunction;

public interface Fitness<T> extends ToDoubleFunction<T> {
    double applyAsDouble(T impl);
}
