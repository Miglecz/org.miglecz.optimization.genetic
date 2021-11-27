package org.miglecz.optimization.genetic;

import static lombok.AccessLevel.PRIVATE;
import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class GeneticBuilder<T> {
    private InitSelection<T> initialize;
    private List<List<MultiSelection<T>>> selectionsList;

    public static <T> GeneticBuilder<T> builder() {
        return new GeneticBuilder<>();
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
