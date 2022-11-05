package org.miglecz.optimization.genetic.operator;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.MultiSelection;

@RequiredArgsConstructor
public class EliteSelection<T> implements MultiSelection<T> {
    private final int limit;
    private final Comparator<Solution<T>> comparator;

    @Override
    public List<Solution<T>> apply(final List<Solution<T>> solutions) {
        if (limit <= 0) {
            return emptyList();
        } else if (limit >= solutions.size()) {
            return unmodifiableList(solutions);
        } else {
            return solutions.stream()
                .sorted(comparator)
                .limit(limit)
                .collect(toUnmodifiableList());
        }
    }
}
