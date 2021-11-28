package org.miglecz.optimization.genetic.facade;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.miglecz.optimization.Iteration.newIteration;
import static org.miglecz.optimization.Solution.newSolution;
import static org.miglecz.optimization.genetic.facade.GeneticBuilderFacade.builder;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.genetic.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GeneticBuilderFacadeTest extends TestBase {
    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "population should not be null")
    void buildShouldFailWithPopulationNull() {
        // Given
        // When
        builder(Integer.class)
                //.withPopulation(null)
                .build();
        // Then
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "fitness should not be null")
    void buildShouldFailWithFitnessNull() {
        // Given
        // When
        builder(Integer.class)
                .withPopulation(0)
                //.withFitness(null)
                .build();
        // Then
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "factory should not be null")
    void buildShouldFailWithFactoryNull() {
        // Given
        // When
        builder(Integer.class)
                .withPopulation(0)
                .withFitness(impl -> 0)
                //.withFactory(null)
                .build();
        // Then
    }

    @DataProvider
    Object[][] data() {
        final Random random = new Random();
        return new Object[][]{
                new Object[]{List.of()}
                , new Object[]{List.of(1)}
                , new Object[]{List.of(1, 2)}
                , new Object[]{List.of(1, 2, 3)}
                , new Object[]{IntStream.range(0, random.nextInt(97) + 3).mapToObj(i -> random.nextInt(100)).collect(toUnmodifiableList())}
        };
    }

    @Test(dataProvider = "data")
    void streamShouldReturnFactoryGeneratedInitialSolutions(final List<Integer> impls) {
        // Given
        final AtomicInteger index = new AtomicInteger();
        final var subject = builder(Integer.class)
                .withPopulation(impls.size())
                .withFitness(impl -> 0)
                .withFactory(() -> impls.get(index.getAndIncrement()))
                .build();
        // When
        final List<Iteration<Integer>> result = subject.stream()
                .limit(1)
                .collect(toList());
        // Then
        assertThat(result, equalTo(List.of(
                newIteration(0, impls.stream().map(impl -> newSolution(0, impl)).collect(toList()))
        )));
    }
}
