package org.miglecz.optimization.genetic;

import java.util.Comparator;
import java.util.function.Consumer;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Solution;

public class TestBase {
    protected final Consumer<? super Iteration<Integer>> noop = i -> {};

    protected <T> Comparator<Solution<T>> comparator(final Class<T> klass) {
        return Comparator.<Solution<T>>comparingDouble(Solution::getScore).reversed();
    }
}
