package org.miglecz.optimization.genetic.operator;

import java.util.function.UnaryOperator;

public interface Mutation<T> extends UnaryOperator<T> {
    T apply(final T impl);
}
