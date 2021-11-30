package org.miglecz.optimization.genetic.facade.operator;

import java.util.function.BinaryOperator;

public interface Crossover<T> extends BinaryOperator<T> {
    // T apply(T parent1, T parent2)
}
