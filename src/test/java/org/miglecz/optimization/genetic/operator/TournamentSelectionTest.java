package org.miglecz.optimization.genetic.operator;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.emptyList;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import java.util.Random;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TournamentSelectionTest extends TestBase {
    @DataProvider
    Object[][] data() {
        return new Object[][]{
            new Object[]{List.of(newSolution(0, 0)), newSolution(0, 0)}
            , new Object[]{List.of(newSolution(0, 0), newSolution(1, 1)), newSolution(1, 1)}
            , new Object[]{List.of(newSolution(0, 0), newSolution(0, 1)), newSolution(0, 1)}
            , new Object[]{List.of(newSolution(1, 1), newSolution(0, 0)), newSolution(1, 1)}
            , new Object[]{List.of(newSolution(0, 0), newSolution(1, 1), newSolution(2, 2), newSolution(3, 3)), newSolution(2, 2)}
        };
    }

    @Test(dataProvider = "data")
    void applyShouldReturnBetterScoreSolution(final List<Solution<Integer>> solutions, final Solution<Integer> expected) {
        // Given
        final TournamentSelection<Integer> subject = new TournamentSelection<>(new Random(1), comparator(Integer.class));
        // When
        final Solution<Integer> result = subject.apply(solutions);
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "bound must be positive")
    void applyShouldFail() {
        // Given
        final TournamentSelection<Integer> subject = new TournamentSelection<>(new Random(0), comparator(Integer.class));
        // When
        subject.apply(emptyList());
        // Then
    }
}
