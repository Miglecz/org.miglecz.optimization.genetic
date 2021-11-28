package org.miglecz.optimization.genetic.facade.operator;

import java.util.List;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.MultiSelection;

public class ImmigrantSelection<T> extends InitialSelection<T> implements MultiSelection<T> {
    public ImmigrantSelection(
            final int population
            , final Fitness<T> fitness
            , final Factory<T> factory
    ) {
        super(population, fitness, factory);
    }

    @Override
    public List<Solution<T>> apply(final List<Solution<T>> ignore) {
        return super.get();
    }
}
