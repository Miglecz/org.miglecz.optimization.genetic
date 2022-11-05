package org.miglecz.optimization.stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.miglecz.optimization.Iteration.newIteration;
import static org.miglecz.optimization.Solution.newSolution;
import static org.miglecz.optimization.stream.Collectors.toBestIteration;
import static org.miglecz.optimization.stream.Collectors.toBestSolution;
import java.util.List;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Solution;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CollectorsTest {
    @DataProvider
    Object[][] toBestIterationData() {
        return new Object[][]{
                new Object[]{List.of(), null},
                new Object[]{List.of(newIteration(0, List.of())), newIteration(0, List.of())},
                new Object[]{List.of(
                        newIteration(0, List.of())
                        , newIteration(1, List.of(newSolution(7, 0)))
                        , newIteration(2, List.of(newSolution(7, 1), newSolution(8, 2)))
                        , newIteration(3, List.of())
                ),
                        newIteration(2, List.of(newSolution(7, 1), newSolution(8, 2)))
                }
        };
    }

    @Test(dataProvider = "toBestIterationData")
    void collectShouldReturnBestIteration(List<Iteration<Integer>> iterations, Iteration<Integer> expected) {
        // Given
        // When
        final Iteration<Integer> result = iterations.stream()
                .collect(toBestIteration())
                .orElse(null);
        // Then
        assertThat(result, equalTo(expected));
    }

    @DataProvider
    Object[][] bestSolutionData() {
        return new Object[][]{
                new Object[]{List.of(), null},
                new Object[]{List.of(newIteration(0, List.of())), null},
                new Object[]{List.of(
                        newIteration(0, List.of())
                        , newIteration(1, List.of(newSolution(7, 0)))
                        , newIteration(2, List.of(newSolution(7, 1), newSolution(8, 2)))
                        , newIteration(3, List.of())
                ), newSolution(8, 2)}
        };
    }

    @Test(dataProvider = "bestSolutionData")
    void collectShouldReturnBestSolution(List<Iteration<Integer>> iterations, Solution<Integer> expected) {
        // Given
        // When
        final Solution<Integer> result = iterations.stream()
                .collect(toBestSolution());
        // Then
        assertThat(result, equalTo(expected));
    }
}
