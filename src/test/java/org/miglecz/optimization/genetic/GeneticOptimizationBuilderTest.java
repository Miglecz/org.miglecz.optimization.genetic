package org.miglecz.optimization.genetic;

import static com.google.common.truth.Truth.assertThat;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.miglecz.optimization.Iteration.newIteration;
import static org.miglecz.optimization.Solution.newSolution;
import static org.miglecz.optimization.genetic.GeneticOptimizationBuilder.builder;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.genetic.operator.Crossover;
import org.miglecz.optimization.genetic.operator.Mutation;
import org.miglecz.optimization.stream.Collectors;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GeneticOptimizationBuilderTest extends TestBase {
    @Test
    void buildShouldNotFailWithDefaultRandom() {
        // Given
        // When
        builder(Integer.class)
            //.withRandom(null)
            .withPopulation(0)
            .withFitness(impl -> 0)
            .withFactory(() -> 0)
            .build();
        // Then
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "GeneticOptimizationBuilder random should not be null")
    void buildShouldFailWithRandomNull() {
        // Given
        // When
        builder(Integer.class)
            .withRandom(null)
            .build();
        // Then
    }

    @DataProvider
    Object[][] population() {
        return new Object[][]{
            new Object[]{null, null, null, null, null, 0}
            , new Object[]{null, null, 3, 4, 5, 12}
            , new Object[]{null, 2, null, 4, 5, 11}
            , new Object[]{null, 2, 3, null, 5, 10}
            , new Object[]{null, 2, 3, 4, null, 9}
            , new Object[]{null, 2, 3, 4, 5, 14}
            , new Object[]{1, 2, 3, 4, 5, 1}
        };
    }

    @Test(dataProvider = "population")
    void buildShouldFailWithDefaultPopulation(final Integer population, final Integer elite, final Integer offspring, final Integer mutant, final Integer immigrant, final Integer expected) {
        // Given
        var builder = builder(Integer.class);
        if (population != null) {
            builder = builder.withPopulation(population);
        }
        if (elite != null) {
            builder = builder.withElite(elite);
        }
        if (offspring != null) {
            builder = builder.withOffspring(offspring, (i, j) -> i);
        }
        if (mutant != null) {
            builder = builder.withMutant(mutant, impl -> impl);
        }
        if (immigrant != null) {
            builder = builder.withImmigrant(immigrant);
        }
        // When
        final var result = builder
            .withFitness(impl -> 1)
            .withFactory(() -> 2)
            .build()
            .stream()
            .peek(System.out::println)
            .skip(1)
            .limit(2)
            .collect(Collectors.toBestIteration())
            .get()
            .getSolutions()
            .size();
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "GeneticOptimizationBuilder fitness should not be null")
    void buildShouldFailWithFitnessNull() {
        // Given
        // When
        builder(Integer.class)
            .withPopulation(0)
            .withFitness(null)
            .withFactory(() -> 0)
            .build();
        // Then
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "GeneticOptimizationBuilder factory should not be null")
    void buildShouldFailWithDefaultFactory() {
        // Given
        // When
        builder(Integer.class)
            .withPopulation(0)
            .withFitness(impl -> 0)
            //.withFactory(null)
            .build();
        // Then
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "GeneticOptimizationBuilder factory should not be null")
    void buildShouldFailWithFactoryNull() {
        // Given
        // When
        builder(Integer.class)
            .withPopulation(0)
            .withFitness(impl -> 0)
            .withFactory(null)
            .build();
        // Then
    }

    @DataProvider
    Object[][] data() {
        final RandomGenerator random = new Random();
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
        final var optimization = builder(Integer.class)
            .withPopulation(impls.size())
            .withFitness(impl -> 0)
            .withFactory(() -> impls.get(index.getAndIncrement()))
            .build();
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(1)
            .collect(toList());
        // Then
        assertThat(result).isEqualTo(List.of(
            newIteration(0, impls.stream().map(impl -> newSolution(0, impl)).collect(toList()))
        ));
    }

    @DataProvider
    Object[][] immigrantData() {
        return new Object[][]{
            new Object[]{0, List.of(1, 2), 1, 3, List.of(
                newIteration(0, List.of())
                , newIteration(1, List.of())
                , newIteration(2, List.of())
            )}
            , new Object[]{1, List.of(1, 2, 3), 1, 3, List.of( //@formatter:off
                newIteration(0, List.of(newSolution(0, 1)))
                , newIteration(1, List.of(newSolution(0, 2)))
                , newIteration(2, List.of(newSolution(0, 3)))
            )} //@formatter:on
            , new Object[]{1, List.of(1, 2, 3, 4, 5), 2, 3, List.of( //@formatter:off
                newIteration(0, List.of(newSolution(0, 1)))
                , newIteration(1, List.of(newSolution(0, 2)))
                , newIteration(2, List.of(newSolution(0, 4)))
            )} //@formatter:on
            , new Object[]{2, List.of(1, 2, 3, 4), 1, 3, List.of( //@formatter:off
                newIteration(0, List.of(newSolution(0, 1), newSolution(0, 2)))
                , newIteration(1, List.of(newSolution(0, 3)))
                , newIteration(2, List.of(newSolution(0, 4)))
            )} //@formatter:on
        };
    }

    @Test(dataProvider = "immigrantData")
    void streamShouldReturnImmigrants(final Integer population, final List<Integer> impls, final Integer immigrant, final int generation, final List<Iteration<Integer>> expected) {
        // Given
        final AtomicInteger index = new AtomicInteger();
        final var optimization = builder(Integer.class)
            .withPopulation(population)
            .withFitness(impl -> 0)
            .withFactory(() -> impls.get(index.getAndIncrement()))
            .withImmigrant(immigrant)
            .build();
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(generation)
            .collect(toList());
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @DataProvider
    Object[][] eliteData() {
        return new Object[][]{
            new Object[]{0, List.of(), 1, 3, List.of(
                newIteration(0, List.of())
                , newIteration(1, List.of())
                , newIteration(2, List.of())
            )}
            , new Object[]{1, List.of(1), 1, 3, List.of( //@formatter:off
                newIteration(0, List.of(newSolution(0, 1)))
                , newIteration(1, List.of(newSolution(0, 1)))
                , newIteration(2, List.of(newSolution(0, 1)))
            )} //@formatter:on
            , new Object[]{1, List.of(1), 2, 3, List.of( //@formatter:off
                newIteration(0, List.of(newSolution(0, 1)))
                , newIteration(1, List.of(newSolution(0, 1)))
                , newIteration(2, List.of(newSolution(0, 1)))
            )} //@formatter:on
            , new Object[]{2, List.of(1, 2), 2, 3, List.of( //@formatter:off
                newIteration(0, List.of(newSolution(0, 1), newSolution(0, 2)))
                , newIteration(1, List.of(newSolution(0, 1), newSolution(0, 2)))
                , newIteration(2, List.of(newSolution(0, 1), newSolution(0, 2)))
            )} //@formatter:on
        };
    }

    @Test(dataProvider = "eliteData")
    void streamShouldReturnElites(final Integer population, final List<Integer> impls, final Integer elite, final int generation, final List<Iteration<Integer>> expected) {
        // Given
        final AtomicInteger index = new AtomicInteger();
        final var optimization = builder(Integer.class)
            .withPopulation(population)
            .withFitness(impl -> 0)
            .withFactory(() -> impls.get(index.getAndIncrement()))
            .withElite(elite)
            .build();
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(generation)
            .collect(toList());
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void streamShouldReturnFixedIterationSizes() {
        // Given
        final AtomicInteger index = new AtomicInteger(1);
        final var optimization = builder(Integer.class)
            .withPopulation(1)
            .withFitness(impl -> impl)
            .withFactory(index::getAndIncrement)
            .withElite(999)
            .withImmigrant(1)
            .build();
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(4)
            .collect(toList());
        // Then
        assertThat(result).isEqualTo(List.of(
            newIteration(0, List.of(newSolution(1, 1)))
            , newIteration(1, List.of(newSolution(2, 2)))
            , newIteration(2, List.of(newSolution(3, 3)))
            , newIteration(3, List.of(newSolution(4, 4)))
        ));
    }

    @DataProvider
    Object[][] mutantsData() {
        return new Object[][]{
            new Object[]{0, List.of(), 0, (Mutation<Integer>) impl -> impl, 1, List.of(newIteration(0, List.of()))}
            , new Object[]{0, List.of(), 1, (Mutation<Integer>) impl -> null, 1, List.of(newIteration(0, List.of()))}
            , new Object[]{2, List.of(1, 2), 2, (Mutation<Integer>) impl -> impl + 1, 3, List.of( //@formatter:off
                newIteration(0, List.of(newSolution(0, 1), newSolution(1, 2)))
                , newIteration(1, List.of(newSolution(2, 3), newSolution(1, 2)))
                , newIteration(2, List.of(newSolution(3, 4), newSolution(3, 4)))
            )} //@formatter:on
        };
    }

    @Test(dataProvider = "mutantsData")
    void streamShouldReturnMutants(
        final Integer population
        , final List<Integer> impls
        , final Integer mutant
        , final Mutation<Integer> mutation
        , final Integer limit
        , final List<Iteration<Integer>> expected
    ) {
        // Given
        final AtomicInteger index = new AtomicInteger(0);
        final var optimization = builder(Integer.class)
            .withPopulation(population)
            .withFitness(impl -> impl - 1)
            .withFactory(() -> impls.get(index.getAndIncrement()))
            .withRandom(new Random(1))
            .withMutant(mutant, mutation)
            .build();
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(limit)
            .collect(toList());
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "GeneticOptimizationBuilder mutation should not be null")
    void builderShouldFailWhenMutationNull() {
        // Given
        // When
        builder(Integer.class)
            .withPopulation(0)
            .withFitness(impl -> 0)
            .withFactory(() -> 0)
            .withMutant(0, null)
            .build();
        // Then
    }

    @DataProvider
    Object[][] offspringData() {
        return new Object[][]{
            new Object[]{0, List.of(), 0, (Crossover<Integer>) (a, b) -> null, 1, List.of(newIteration(0, List.of()))}
            , new Object[]{0, List.of(), 0, (Crossover<Integer>) (a, b) -> a * b, 2, List.of(newIteration(0, List.of()), newIteration(1, List.of()))}
            , new Object[]{2, List.of(2, 3), 20, (Crossover<Integer>) Integer::sum, 3, List.of( //@formatter:off
                newIteration(0, List.of(newSolution(1, 2), newSolution(2, 3)))
                , newIteration(1, List.of(newSolution(5, 6), newSolution(5, 6)))
                , newIteration(2, List.of(newSolution(11, 12), newSolution(11, 12)))
            )} //@formatter:on
        };
    }

    @Test(dataProvider = "offspringData")
    void streamShouldReturnOffsprings(
        final Integer population
        , final List<Integer> initials
        , final Integer offspring
        , final Crossover<Integer> crossover
        , final Integer limit
        , final List<Iteration<Integer>> expected
    ) {
        // Given
        final AtomicInteger index = new AtomicInteger(0);
        final var optimization = builder(Integer.class)
            .withPopulation(population)
            .withFitness(impl -> impl - 1)
            .withFactory(() -> initials.get(index.getAndIncrement()))
            .withRandom(new Random(1))
            .withOffspring(offspring, crossover)
            .build();
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(limit)
            .collect(toList());
        // Then
        assertThat(result).isEqualTo(expected);
    }
}
