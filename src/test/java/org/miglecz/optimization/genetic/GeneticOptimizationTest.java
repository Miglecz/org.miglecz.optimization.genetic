package org.miglecz.optimization.genetic;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.emptyList;
import static java.util.stream.IntStream.range;
import static org.miglecz.optimization.Iteration.newIteration;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.genetic.exception.InitializationException;
import org.miglecz.optimization.genetic.exception.SelectionException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GeneticOptimizationTest extends TestBase {
    @DataProvider(name = "data")
    Object[][] data() {
        final RandomGenerator random = new Random();
        return new Object[][]{
            new Object[]{0}
            , new Object[]{1}
            , new Object[]{2}
            , new Object[]{2 + random.nextInt(100)}
        };
    }

    @Test(dataProvider = "data")
    void streamShouldGenerateIterations(final int generations) {
        // Given
        final var optimization = new GeneticOptimization<Integer>(List.of(Collections::emptyList), emptyList());
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(generations)
            .toList();
        // Then
        assertThat(result).isEqualTo(range(0, generations)
            .mapToObj(i -> newIteration(i, emptyList()))
            .toList()
        );
    }

    @Test
    void selectionsShouldOverrideInitialize() {
        // Given
        final GeneticOptimization<Integer> optimization = new GeneticOptimization<>(
            List.of(() -> List.of(newSolution(0, 0))),
            List.of(
                List.of(
                    previousGeneration -> List.of(newSolution(1, 1))
                )
            )
        );
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(3)
            .toList();
        // Then
        assertThat(result).isEqualTo(List.of(
            newIteration(0, List.of(newSolution(0, 0)))
            , newIteration(1, List.of(newSolution(1, 1)))
            , newIteration(2, List.of(newSolution(1, 1)))
        ));
    }

    @Test
    void selectionShouldAggregate() {
        // Given
        final var optimization = new GeneticOptimization<Integer>(
            List.of(Collections::emptyList),
            List.of(
                List.of(
                    previousGeneration -> List.of(newSolution(1, 1))
                    , previousGeneration -> List.of(newSolution(2, 2))
                    , previousGeneration -> List.of(newSolution(3, 3))
                )
            )
        );
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(2)
            .toList();
        // Then
        assertThat(result).isEqualTo(List.of(
            newIteration(0, emptyList())
            , newIteration(1, List.of(newSolution(1, 1), newSolution(2, 2), newSolution(3, 3)))
        ));
    }

    @Test
    void selectionsShouldOverrideSelections() {
        // Given
        final var optimization = new GeneticOptimization<Integer>(
            List.of(Collections::emptyList),
            List.of(
                List.of(
                    previousGeneration -> List.of(newSolution(1, 1))
                    , previousGeneration -> List.of(newSolution(2, 2))
                )
                , List.of(
                    previousGeneration -> List.of(newSolution(3, 3))
                )
            )
        );
        // When
        final List<Iteration<Integer>> result = optimization.stream()
            .limit(2)
            .toList();
        // Then
        assertThat(result).isEqualTo(List.of(
            newIteration(0, List.of())
            , newIteration(1, List.of(newSolution(3, 3)))
        ));
    }

    @Test(expectedExceptions = InitializationException.class)
    void initErrorShouldThrowException() {
        // Given
        final var optimization = new GeneticOptimization<Integer>(
            List.of(() -> {
                throw new RuntimeException();
            }),
            List.of()
        );
        // When
        optimization.stream()
            .limit(1)
            .forEach(noop);
        // Then
    }

    @Test(expectedExceptions = InitializationException.class)
    void initNullResultShouldThrowException() {
        // Given
        final var optimization = new GeneticOptimization<Integer>(
            List.of(() -> null),
            List.of()
        );
        // When
        optimization.stream()
            .limit(1)
            .forEach(noop);
        // Then
    }

    @Test(expectedExceptions = SelectionException.class)
    void selectionErrorShouldThrowException() {
        // Given
        final var optimization = new GeneticOptimization<Integer>(
            List.of(Collections::emptyList),
            List.of(
                List.of(
                    previousGeneration -> {
                        throw new RuntimeException();
                    }
                )
            )
        );
        // When
        optimization.stream()
            .limit(2)
            .forEach(noop);
        // Then
    }

    @Test(expectedExceptions = SelectionException.class)
    void selectionNullResultShouldThrowException() {
        // Given
        final var optimization = new GeneticOptimization<Integer>(
            List.of(Collections::emptyList),
            List.of(
                List.of(
                    previousGeneration -> null
                )
            )
        );
        // When
        optimization.stream()
            .limit(2)
            .forEach(noop);
        // Then
    }
}
