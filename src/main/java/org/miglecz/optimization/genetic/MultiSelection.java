package org.miglecz.optimization.genetic;

import java.util.List;
import java.util.function.UnaryOperator;
import org.miglecz.optimization.Solution;

public interface MultiSelection<T> extends UnaryOperator<List<Solution<T>>> {
    List<Solution<T>> apply(final List<Solution<T>> solutions);
}
