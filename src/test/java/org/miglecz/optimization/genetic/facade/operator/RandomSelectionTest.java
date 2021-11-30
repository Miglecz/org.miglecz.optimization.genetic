package org.miglecz.optimization.genetic.facade.operator;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import java.util.Random;
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
    void applyShouldReturnRandomItems(final List<Solution<Integer>> previous, final Solution<Integer> expected, final Random random) {
        // Given
        final RandomSelection<Integer> subject = new RandomSelection<>(random);
        // When
        final Solution<Integer> result = subject.apply(previous);
        // Then
        assertThat(result, equalTo(expected));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "bound must be positive")
    void applyShouldReturnRandomItems() {
        // Given
        final RandomSelection<Integer> subject = new RandomSelection<>(new Random());
        // When
        subject.apply(emptyList());
        // Then
    }
}
