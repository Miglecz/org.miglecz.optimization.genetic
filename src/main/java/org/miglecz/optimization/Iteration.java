package org.miglecz.optimization;

import static java.util.Collections.unmodifiableList;
import static lombok.AccessLevel.PRIVATE;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@RequiredArgsConstructor(access = PRIVATE)
public class Iteration<T> {
    int index;
    List<Solution<T>> solutions;
    @NonFinal
    transient Solution<T> best = null;

    public static <T> Iteration<T> newIteration(final int index, final List<Solution<T>> solutions) {
        return new Iteration<>(index, unmodifiableList(solutions));
    }

    public Optional<Solution<T>> getBest() {
        if (best == null && !solutions.isEmpty()) {
            best = solutions.stream().max(Comparator.comparingDouble(Solution::getScore)).orElse(null);
        }
        return Optional.ofNullable(best);
    }
}
