package org.miglecz.optimization.stream;

import static com.google.common.truth.Truth.assertThat;
import static java.util.stream.Collectors.toList;
import static org.miglecz.optimization.Iteration.newIteration;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import org.miglecz.optimization.Iteration;
import org.miglecz.optimization.Solution;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TakeWhilesTest {
    @DataProvider
    Object[][] data() {
        return new Object[][]{
            new Object[]{1, List.of(
                newIteration(0, List.<Solution<Integer>>of())
                , newIteration(1, List.of(newSolution(8, 0)))
            ), List.of()},
            new Object[]{1, List.of(
                newIteration(0, List.of(newSolution(7, 0)))
                , newIteration(1, List.of(newSolution(8, 0)))
            ), List.of(
                newIteration(0, List.of(newSolution(7, 0)))
                , newIteration(1, List.of(newSolution(8, 0)))
            )},
            new Object[]{1, List.of(
                newIteration(0, List.of(newSolution(7, 0)))
                , newIteration(1, List.of(newSolution(7, 1)))
            ), List.of(
                newIteration(0, List.of(newSolution(7, 0)))
            )},
            new Object[]{2, List.of(
                newIteration(0, List.of(newSolution(7, 0)))
                , newIteration(1, List.of(newSolution(7, 1)))
                , newIteration(2, List.of(newSolution(8, 2)))
            ), List.of(
                newIteration(0, List.of(newSolution(7, 0)))
                , newIteration(1, List.of(newSolution(7, 1)))
                , newIteration(2, List.of(newSolution(8, 2)))
            )},
            new Object[]{2, List.of(
                newIteration(0, List.of(newSolution(7, 0)))
                , newIteration(1, List.of(newSolution(7, 1)))
                , newIteration(2, List.of(newSolution(7, 2)))
            ), List.of(
                newIteration(0, List.of(newSolution(7, 0)))
                , newIteration(1, List.of(newSolution(7, 1)))
            )},
            new Object[]{2, List.of(
                newIteration(0, List.of(newSolution(7, 0)))
                , newIteration(1, List.of(newSolution(7, 1)))
                , newIteration(2, List.of(newSolution(8, 2)))
                , newIteration(3, List.of(newSolution(8, 3)))
                , newIteration(4, List.of(newSolution(9, 4)))
                , newIteration(5, List.of(newSolution(9, 5)))
                , newIteration(6, List.of(newSolution(9, 6)))
            ), List.of(
                newIteration(0, List.of(newSolution(7, 0)))
                , newIteration(1, List.of(newSolution(7, 1)))
                , newIteration(2, List.of(newSolution(8, 2)))
                , newIteration(3, List.of(newSolution(8, 3)))
                , newIteration(4, List.of(newSolution(9, 4)))
                , newIteration(5, List.of(newSolution(9, 5)))
            )}
        };
    }

    @Test(dataProvider = "data")
    void aboveFitnessThresholdShouldReturnList(final int generations, final List<Iteration<Integer>> iterations, final List<Iteration<Integer>> expected) {
        // Given
        // When
        final List<Iteration<Integer>> result = iterations.stream()
            .takeWhile(TakeWhiles.aboveFitnessThreshold(0, generations))
            .collect(toList());
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void belowScoreShouldReturnList() {
        // Given
        final List<Iteration<Integer>> iterations = List.of(
            newIteration(0, List.of(newSolution(-1, 0)))
            , newIteration(1, List.of(newSolution(0, 1)))
            , newIteration(2, List.of(newSolution(1, 2)))
        );
        // When
        final List<Iteration<Integer>> result = iterations.stream()
            .takeWhile(TakeWhiles.belowScore(0))
            .collect(toList());
        // Then
        assertThat(result).isEqualTo(List.of(
            newIteration(0, List.of(newSolution(-1, 0)))
            , newIteration(1, List.of(newSolution(0, 1)))
        ));
    }
}
