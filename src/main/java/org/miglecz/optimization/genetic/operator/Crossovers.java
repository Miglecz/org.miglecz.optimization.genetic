package org.miglecz.optimization.genetic.operator;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Crossover helper
 */
public class Crossovers {
    public static <T> T[] varyingCrossover(final T[] parent1, final T[] parent2) {
        return varyingCrossover(new Random(), parent1, parent2);
    }

    /**
     * Create list from parent lists by randomly chosen crossover point
     *
     * @param random  generator to get crossover point
     * @param parent1 list of a parent
     * @param parent2 list of another parent
     * @param <T>     type of implementation
     * @return list of offspring with any length
     */
    public static <T> T[] varyingCrossover(final Random random, final T[] parent1, final T[] parent2) {
        return (T[]) Stream.concat(
                Arrays.stream(parent1).limit(random.nextInt(parent1.length) + 1)
                , Arrays.stream(parent2).skip(random.nextInt(parent2.length))
            )
            .toArray();
    }

    public static <T> T[] uniformCrossover(final T[] parent1, final T[] parent2) {
        return uniformCrossover(new Random(), parent1, parent2);
    }

    public static <T> T[] uniformCrossover(final Random random, final T[] parent1, final T[] parent2) {
        if (parent1.length != parent2.length) {
            throw new IllegalArgumentException("parents lengths should be equal");
        }
        final T[] result = (T[]) Array.newInstance(parent1[0].getClass(), parent1.length);
        for (int i = 0; i < result.length; ++i) {
            result[i] = random.nextBoolean() ? parent1[i] : parent2[i];
        }
        return result;
    }

    public static float[] uniformCrossover(final float[] parent1, final float[] parent2) {
        return uniformCrossover(new Random(), parent1, parent2);
    }

    public static float[] uniformCrossover(final Random random, final float[] parent1, final float[] parent2) {
        if (parent1.length != parent2.length) {
            throw new IllegalArgumentException("parents lengths should be equal");
        }
        final float[] result = new float[parent1.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = random.nextBoolean() ? parent1[i] : parent2[i];
        }
        return result;
    }
}
