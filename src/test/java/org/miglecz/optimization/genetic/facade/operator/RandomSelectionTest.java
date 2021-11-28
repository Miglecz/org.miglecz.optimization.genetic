package org.miglecz.optimization.genetic.facade.operator;

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
        final Random random = new Random(0);
        return new Object[][]{
                new Object[]{0, List.of(), List.of(), null}
                , new Object[]{0, List.of(newSolution(1, 1)), List.of(), null}
                , new Object[]{1, List.of(), List.of(), random}
                , new Object[]{1, List.of(newSolution(1, 1)), List.of(newSolution(1, 1)), random}
                , new Object[]{2, List.of(), List.of(), null}
                , new Object[]{2, List.of(newSolution(1, 1)), List.of(newSolution(1, 1), newSolution(1, 1)), random}
                , new Object[]{1, List.of(newSolution(1, 1), newSolution(2, 2)), List.of(newSolution(2, 2)), random}
        };
    }

    @Test(dataProvider = "data")
    void applyShouldReturnRandomItems(final Integer limit, final List<Solution<Integer>> previous, final List<Solution<Integer>> expected, final Random random) {
        // Given
        final RandomSelection<Integer> subject = new RandomSelection<>(limit, random);
        // When
        final List<Solution<Integer>> result = subject.apply(previous);
        // Then
        assertThat(result, equalTo(expected));
    }
}
