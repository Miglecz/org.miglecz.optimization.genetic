package org.miglecz.optimization.genetic;

import java.util.List;
import java.util.Objects;

public class GeneticBuilder<T> {
    private final Class<T> klass;
    private InitSelection<T> initialize;
    private List<List<MultiSelection<T>>> selectionsList;

    private GeneticBuilder(final Class<T> klass) {
        this.klass = klass;
    }

    public static <T> GeneticBuilder<T> builder(final Class<T> klass) {
        return new GeneticBuilder<>(klass);
    }

    public GeneticBuilder<T> withInitialize(final InitSelection<T> initialize) {
        this.initialize = initialize;
        return this;
    }

    public GeneticBuilder<T> withSelectionsList(final List<List<MultiSelection<T>>> selectionsList) {
        this.selectionsList = selectionsList;
        return this;
    }

    public Genetic<T> build() {
        if (Objects.isNull(initialize)) {
            throw new NullPointerException();
        }
        if (Objects.isNull(selectionsList)) {
            throw new NullPointerException();
        }
        return new Genetic<>(initialize, selectionsList);
    }
}
