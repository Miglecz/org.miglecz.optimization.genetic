package org.miglecz.optimization.genetic.facade;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.miglecz.optimization.genetic.facade.GeneticBuilderFacade.builder;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Optimization;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.TestBase;
import org.testng.annotations.Test;

public class UsabilityTest extends TestBase {
    @Test
    void geneticShouldBeAbleToCount() {
        // Given
        final Optimization<Integer> optimization = builder(Integer.class)
                .withPopulation(1)
                .withFitness(impl -> 0)
                .withFactory(() -> 10)
                .withMutant(1, impl -> impl - 1)
                .build();
        // When
        final List<Integer> result = optimization.stream()
                .limit(10)
                .map(Iteration::getSolutions)
                .flatMap(Collection::stream)
                .map(Solution::getImpl)
                .collect(toList());
        // Then
        assertThat(result, equalTo(of(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)));
    }

    @Test
    void geneticShouldBeAbleToEvolveToSquareRoot() {
        // Given
        final int square = 1024;
        final Random random = new Random(1);
        final Optimization<Integer> optimization = builder(Integer.class)
                .withPopulation(1)
                .withFactory(() -> 0)
                .withMutant(20, impl -> impl + (random.nextBoolean() ? 1 : -1))
                .withFitness(impl -> -Math.abs(square - impl * impl))
                .withElite(1)
                .build();
        // When
        final List<Integer> result = optimization.stream()
                .limit(50)
                .map(Iteration::getSolutions)
                .flatMap(Collection::stream)
                .map(Solution::getImpl)
                .collect(toList());
        // Then
        assertThat(result, equalTo(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32)));
    }

    @Test
    void geneticShouldBeAbleToQuickEvolveToSquareRoot() {
        // Given
        final int square = 1024;
        final Random random = new Random(1);
        final Optimization<Integer> optimization = builder(Integer.class)
                .withPopulation(1)
                .withFactory(() -> random.nextInt(10000))
                .withOffspring(20, (a, b) -> (a + b) / 2)
                .withMutant(20, impl -> impl + random.nextInt(100) - 50)
                .withImmigrant(20)
                .withFitness(impl -> -Math.abs(square - impl * impl))
                .withElite(1)
                .build();
        // When
        final List<Integer> result = optimization.stream()
                .limit(10)
                .map(Iteration::getSolutions)
                .flatMap(Collection::stream)
                .map(Solution::getImpl)
                .collect(toList()); //TODO stop when reached or when not converging anymore
        // Then
        assertThat(result, equalTo(List.of(8985, 153, 103, 54, 32, 32, 32, 32, 32, 32)));
    }
}
