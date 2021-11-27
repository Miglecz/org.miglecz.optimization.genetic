package org.miglecz.optimization.genetic;

import java.util.List;
import java.util.function.Supplier;
import org.miglecz.optimization.Solution;

public interface InitSelection<T> extends Supplier<List<Solution<T>>> {
    //List<Solution<T>> get();
}
