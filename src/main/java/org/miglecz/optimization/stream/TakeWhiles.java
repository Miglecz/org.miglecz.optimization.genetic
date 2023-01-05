package org.miglecz.optimization.stream;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Predicate;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Optimization;
import org.miglecz.optimization.Solution;

/**
 * Helper class for {@link java.util.stream.Stream#takeWhile(Predicate)} in {@link Optimization#stream()}
 */
public class TakeWhiles {
    /**
     * Take while the iterations best score is lower than the expected
     *
     * @param score expected final score of the solution
     * @param <T>   type of implementation
     * @return predicate of iteration
     */
    public static <T> Predicate<Iteration<T>> belowScore(final double score) {
        return BelowScore.of(score);
    }

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

    /**
     * Take while time duration is not exceeded
     * and stop if duration have passed
     *
     * @param duration seconds
     * @param <T>      type of implementation
     * @return predicate of iteration
     */
    public static <T> Predicate<Iteration<T>> duration(final Duration duration) {
        return InTime.of(duration);
    }

    private static class BelowScore<T> implements Predicate<Iteration<T>> {
        private final double threshold;
        private double previous = -Double.MAX_VALUE;

        private BelowScore(final double threshold) {
            this.threshold = threshold;
        }

        static <T> BelowScore<T> of(final double threshold) {
            return new BelowScore<>(threshold);
        }

        @Override
        public boolean test(final Iteration<T> iteration) {
            if (previous >= threshold) {
                return false;
            }
            previous = iteration.getBest().map(Solution::getScore).orElse(-Double.MAX_VALUE);
            return true;
        }
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

    private static class InTime<T> implements Predicate<Iteration<T>> {
        private final long thresholdMillis;
        private Long endMillis = null;

        private InTime(final Duration duration) {
            thresholdMillis = duration.get(ChronoUnit.SECONDS) * 1000;
        }

        static <T> InTime<T> of(final Duration duration) {
            return new InTime<>(duration);
        }

        @Override
        public boolean test(final Iteration<T> iteration) {
            final var currentTimeMillis = System.currentTimeMillis();
            if (endMillis == null) {
                endMillis = currentTimeMillis + thresholdMillis;
            }
            return currentTimeMillis < endMillis;
        }
    }
}
