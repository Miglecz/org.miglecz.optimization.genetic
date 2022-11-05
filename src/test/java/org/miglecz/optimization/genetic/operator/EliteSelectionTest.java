package org.miglecz.optimization.genetic.operator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EliteSelectionTest extends TestBase {
    @DataProvider(name = "data")
    Object[][] data() {
        return new Object[][]{
            new Object[]{0, List.of(), List.of()}
            , new Object[]{0, List.of(newSolution(0, 1)), List.of()}
            , new Object[]{0, List.of(newSolution(0, 1), newSolution(1, 2)), List.of()}
            , new Object[]{1, List.of(), List.of()}
            , new Object[]{1, List.of(newSolution(0, 1)), List.of(newSolution(0, 1))}
            , new Object[]{1, List.of(newSolution(1, 1), newSolution(0, 2)), List.of(newSolution(1, 1))}
            , new Object[]{1, List.of(newSolution(0, 1), newSolution(0, 2)), List.of(newSolution(0, 1))}
            , new Object[]{1, List.of(newSolution(0, 1), newSolution(1, 2)), List.of(newSolution(1, 2))}
            , new Object[]{2, List.of(newSolution(2, 1), newSolution(1, 2), newSolution(0, 3)), List.of(newSolution(2, 1), newSolution(1, 2))}
            , new Object[]{2, List.of(newSolution(0, 1), newSolution(0, 2), newSolution(0, 3)), List.of(newSolution(0, 1), newSolution(0, 2))}
            , new Object[]{2, List.of(newSolution(0, 1), newSolution(1, 2), newSolution(2, 3)), List.of(newSolution(2, 3), newSolution(1, 2))}
        };
    }

    @Test(dataProvider = "data")
    void applyShouldReturnTopScoreSolutions(
        final int limit
        , final List<Solution<Integer>> previous
        , final List<Solution<Integer>> expected
    ) {
        // Given
        final EliteSelection<Integer> subject = new EliteSelection<>(limit, comparator(Integer.class));
        // When
        final List<Solution<Integer>> result = subject.apply(previous);
        // Then
        assertThat(result, equalTo(expected));
    }
}
