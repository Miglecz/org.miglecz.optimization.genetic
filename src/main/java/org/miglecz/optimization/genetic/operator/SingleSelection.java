package org.miglecz.optimization.genetic.operator;

import java.util.List;
import java.util.function.Function;
import org.miglecz.optimization.Solution;

public interface SingleSelection<T> extends Function<List<Solution<T>>, Solution<T>> {
    Solution<T> apply(final List<Solution<T>> solutions);
}
