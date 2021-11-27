package org.miglecz.optimization.genetic;

import java.util.function.Consumer;
import org.miglecz.optimization.Iteration;

public class TestBase {
    protected final Consumer<? super Iteration<Integer>> noop = i -> {};
}
