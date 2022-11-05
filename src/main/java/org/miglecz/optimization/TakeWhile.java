package org.miglecz.optimization;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Helper class for {@link java.util.stream.Stream#takeWhile(Predicate)} in {@link Optimization#stream()}
 */
public class TakeWhile {
    /**
     * Take while the iterations delta score is not less than the given threshold
     *
     * @param epsilon threshold inclusive
     * @param <T>     type of implementation
     * @return predicate of iteration
     */
    public static <T> Predicate<Iteration<T>> aboveFitnessThreshold(final double epsilon) {//TODO rename fitness to score?
        return AboveThreshold.of(epsilon);
    }

    /**
     * Take while the iterations are progressing by score
     * and the delta score is not less than the given epsilon threshold
     * and stop if the non-progressing number of sequential iterations reach the given steadyIterations threshold
     *
     * @param epsilon          delta score threshold
     * @param steadyIterations non-progressing number of sequential iterations threshold
     * @param <T>              type of implementation
     * @return predicate of iteration
     */
    public static <T> Predicate<Iteration<T>> aboveFitnessThreshold(final double epsilon, final int steadyIterations) {
        return AboveThreshold.of(epsilon, steadyIterations);
    }

    /**
     * Take while the iterations are progressing by score
     * and stop if the non-progressing number of sequential iterations reach the given threshold
     *
     * @param steadyIterations threshold inclusive
     * @param <T>              type of implementation
     * @return predicate of iteration
     */
    public static <T> Predicate<Iteration<T>> progressingIteration(final int steadyIterations) {
        return AboveThreshold.of(0, steadyIterations);
    }

    private static class AboveThreshold<T> implements Predicate<Iteration<T>> {
        private final double epsilon;
        private final int sequenceThreshold;
        private Double previous = null;
        private int sequence;

        private AboveThreshold(final double epsilon) {
            this(epsilon, 1);
        }

        private AboveThreshold(final double epsilon, final int sequenceThreshold) {
            this.epsilon = epsilon;
            this.sequenceThreshold = sequenceThreshold;
        }

        static <T> AboveThreshold<T> of(final double epsilon) {
            return new AboveThreshold<>(epsilon);
        }

        static <T> AboveThreshold<T> of(final double epsilon, final int sequenceThreshold) {
            return new AboveThreshold<>(epsilon, sequenceThreshold);
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
