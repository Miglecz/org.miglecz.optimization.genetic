package org.miglecz.optimization.genetic.operator;

import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Crossover helper
 */
public class Crossovers {
    public static <T> List<T> crossover(final List<T> parent1, final List<T> parent2) {
        return crossover(new Random(), parent1, parent2);
    }

    /**
     * Create list from parent lists by randomly chosen crossover point
     *
     * @param random  generator to get crossover point
     * @param parent1 list of a parent
     * @param parent2 list of another parent
     * @param <T>     type of implementation
     * @return list of offspring
     */
    public static <T> List<T> crossover(final Random random, final List<T> parent1, final List<T> parent2) {
        return Stream.concat(
                parent1.stream().limit(random.nextInt(parent1.size()) + 1)
                , parent2.stream().skip(random.nextInt(parent2.size()))
            )
            .collect(toList());
    }
}
