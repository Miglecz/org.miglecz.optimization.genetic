package org.miglecz.optimization;

import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = PRIVATE)
public class Solution<T> {
    double score;
    T impl;

    public static <T> Solution<T> newSolution(final double score, final T impl) {
        return new Solution<>(score, impl);
    }
}
