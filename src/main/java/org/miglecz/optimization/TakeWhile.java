package org.miglecz.optimization;

import java.util.Optional;
import java.util.function.Predicate;

public class TakeWhile {
    public static <T> Predicate<Iteration<T>> aboveFitnessThreshold(final double epsilon) {
        return new AboveThreshold<>(epsilon);
    }

    public static <T> Predicate<Iteration<T>> aboveFitnessThreshold(final double epsilon, final int steadyIterations) {
        return new AboveThreshold<>(epsilon, steadyIterations);
    }

    public static <T> Predicate<Iteration<T>> progressingIteration(final int steadyIterations) {
        return new AboveThreshold<>(0, steadyIterations);
    }

    private static class AboveThreshold<T> implements Predicate<Iteration<T>> {
        private final double epsilon;
        private final int sequenceThreshold;
        private Double previous = null;
        private int sequence;

        public AboveThreshold(final double epsilon) {
            this(epsilon, 1);
        }

        public AboveThreshold(final double epsilon, final int sequenceThreshold) {
            this.epsilon = epsilon;
            this.sequenceThreshold = sequenceThreshold;
        }

        @Override
        public boolean test(final Iteration<T> iteration) {
            final Optional<Solution<T>> solution = iteration.getBest();
            if (solution.isEmpty()) {
                return false;
            }
            final double best = solution.get().getScore();
            if (previous == null) {
                previous = best;
                sequence = sequenceThreshold;
                return true;
            }
            final double delta = best - previous;
            if (delta <= epsilon) {
                return --sequence > 0;
            }
            previous = best;
            sequence = sequenceThreshold;
            return true;
        }
    }
}
