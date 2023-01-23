package org.miglecz.optimization.genetic.operator;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.emptyList;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RandomSelectionTest extends TestBase {
    @DataProvider
    Object[][] data() {
        return new Object[][]{
            new Object[]{List.of(newSolution(1, 1)), newSolution(1, 1), new Random(0)}
            , new Object[]{List.of(newSolution(1, 1), newSolution(2, 2)), newSolution(2, 2), new Random(0)}
        };
    }

    @Test(dataProvider = "data")
    void applyShouldReturnRandomItems(final List<Solution<Integer>> previous, final Solution<Integer> expected, final RandomGenerator random) {
        // Given
        final RandomSelection<Integer> subject = new RandomSelection<>(random);
        // When
        final Solution<Integer> result = subject.apply(previous);
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class, expectedExceptionsMessageRegExp = "population should not be empty")
    void applyShouldReturnRandomItems() {
        // Given
        final RandomSelection<Integer> subject = new RandomSelection<>(new Random());
        // When
        subject.apply(emptyList());
        // Then
    }
}
