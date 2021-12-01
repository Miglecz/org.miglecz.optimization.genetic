package org.miglecz.optimization;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.miglecz.optimization.Iteration.newIteration;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TakeWhileTest {
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
    void collectShouldReturnList(int generations, List<Iteration<Integer>> iterations, List<Iteration<Integer>> expected) {
        // Given
        // When
        final List<Iteration<Integer>> result = iterations.stream()
                .takeWhile(TakeWhile.aboveFitnessThreshold(0, generations))
                .collect(toList());
        // Then
        assertThat(result, equalTo(expected));
    }
}
