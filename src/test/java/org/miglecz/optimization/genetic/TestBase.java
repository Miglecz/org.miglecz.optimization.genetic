package org.miglecz.optimization.genetic;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Solution;
import org.testng.annotations.BeforeMethod;

public class TestBase {
    protected final Consumer<? super Iteration<Integer>> noop = i -> {};
    protected Random random = null;

    protected <T> Comparator<Solution<T>> comparator(final Class<T> klass) {
        return Comparator.<Solution<T>>comparingDouble(Solution::getScore).reversed();
    }

    @BeforeMethod
    protected void beforeMethod() {
        random = new Random(0);
    }
}
