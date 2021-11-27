package org.miglecz.optimization;

import static java.util.Collections.unmodifiableList;
import static lombok.AccessLevel.PRIVATE;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = PRIVATE)
public class Iteration<T> {
    int index;
    List<Solution<T>> solutions;

    public static <T> Iteration<T> newIteration(final int index, final List<Solution<T>> solutions) {
        return new Iteration<>(index, unmodifiableList(solutions));
    }
}
