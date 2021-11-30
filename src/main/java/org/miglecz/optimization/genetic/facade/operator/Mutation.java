package org.miglecz.optimization.genetic.facade.operator;

import java.util.function.UnaryOperator;

public interface Mutation<T> extends UnaryOperator<T> {
    T apply(T impl);
}
