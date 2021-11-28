package org.miglecz.optimization.genetic.facade.operator;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.MultiSelection;
import org.miglecz.optimization.genetic.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MutantSelectionTest extends TestBase {
    @DataProvider
    Object[][] data() {
        return new Object[][]{
                new Object[]{(MultiSelection<Integer>) solutions -> emptyList(), List.of(), List.of()}
                , new Object[]{(MultiSelection<Integer>) solutions -> solutions, List.of(newSolution(0, 1), newSolution(1, 2)), List.of(newSolution(2, 3), newSolution(5, 6))}
        };
    }

    @Test(dataProvider = "data")
    void applyShouldReturnMutants(final MultiSelection<Integer> selection, final List<Solution<Integer>> previous, final List<Solution<Integer>> expected) {
        // Given
        final var subject = new MutantSelection<>(selection, impl -> impl - 1, impl -> impl * 3);
        // When
        final List<Solution<Integer>> result = subject.apply(previous);
        // Then
        assertThat(result, equalTo(expected));
    }

    @Test(expectedExceptions = NullPointerException.class)
    void applyShouldThrowNpe() {
        // Given
        final MutantSelection<Object> selection = new MutantSelection<>(solutions -> null, impl -> 0, impl -> impl);
        // When
        selection.apply(emptyList());
        // Then
    }

}
