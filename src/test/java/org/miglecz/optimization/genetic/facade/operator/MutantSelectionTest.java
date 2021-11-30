package org.miglecz.optimization.genetic.facade.operator;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MutantSelectionTest extends TestBase {
    @DataProvider
    Object[][] data() {
        return new Object[][]{
                new Object[]{1, (SingleSelection<Integer>) solutions -> newSolution(0, 1), List.of(newSolution(0, 1)), List.of(newSolution(2, 3))}
                , new Object[]{2, new SingleSelection<Integer>() { //@formatter:off
                    final AtomicInteger i = new AtomicInteger(1);
                    @Override
                    public Solution<Integer> apply(List<Solution<Integer>> solutions) {return solutions.get(i.getAndIncrement());}
                }, List.of(newSolution(0, 0), newSolution(0, 1), newSolution(1, 2)), List.of(newSolution(2, 3), newSolution(5, 6))} //@formatter:on
        };
    }

    @Test(dataProvider = "data")
    void applyShouldReturnMutants(final int mutate, final SingleSelection<Integer> selection, final List<Solution<Integer>> previous, final List<Solution<Integer>> expected) {
        // Given
        final var subject = new MutantSelection<>(mutate, selection, impl -> impl * 3, impl -> impl - 1);
        // When
        final List<Solution<Integer>> result = subject.apply(previous);
        // Then
        assertThat(result, equalTo(expected));
    }

    @Test(expectedExceptions = NullPointerException.class)
    void applyShouldThrowNpe() {
        // Given
        final MutantSelection<Integer> selection = new MutantSelection<>(1, solutions -> null, impl -> 0, impl -> impl);
        // When
        selection.apply(emptyList());
        // Then
    }
}
