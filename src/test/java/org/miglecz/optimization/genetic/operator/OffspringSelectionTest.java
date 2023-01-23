package org.miglecz.optimization.genetic.operator;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.miglecz.optimization.Solution.newSolution;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OffspringSelectionTest extends TestBase {
    @Test
    void applyShouldDoNothingOnEmpty() {
        // Given
        final SingleSelection<Integer> selection = mock(SingleSelection.class);
        final Crossover<Integer> crossover = mock(Crossover.class);
        final Fitness<Integer> fitness = mock(Fitness.class);
        final OffspringSelection<Integer> subject = new OffspringSelection<>(1, selection, crossover, fitness);
        // When
        final List<Solution<Integer>> result = subject.apply(emptyList());
        // Then
        assertThat(result).isEqualTo(emptyList());
        verifyNoMoreInteractions(selection, crossover, fitness);
    }

    @Test
    void applyShouldNotCallCrossoverOnSingleSolution() {
        // Given
        final int limit = 10;
        final SingleSelection<Integer> selection = mock(SingleSelection.class);
        final Crossover<Integer> crossover = mock(Crossover.class);
        final Fitness<Integer> fitness = mock(Fitness.class);
        final OffspringSelection<Integer> subject = new OffspringSelection<>(limit, selection, crossover, fitness);
        final Solution<Integer> solution = newSolution(1, 2);
        final List<Solution<Integer>> solutions = List.of(solution);
        final List<Solution<Integer>> expected = IntStream.range(0, limit).mapToObj(i -> solution).collect(toUnmodifiableList());
        // When
        final List<Solution<Integer>> result = subject.apply(solutions);
        // Then
        assertThat(result).isEqualTo(expected);
        verifyNoMoreInteractions(selection, crossover, fitness);
    }

    @DataProvider
    Object[][] data() {
        final RandomGenerator random = new Random();
        return new Object[][]{
            new Object[]{0}
            , new Object[]{1}
            , new Object[]{2}
            , new Object[]{3}
            , new Object[]{random.nextInt(98) + 2}
        };
    }

    @Test(dataProvider = "data")
    void applyShouldCallMocksSpecificTimes(final int limit) {
        // Given
        final SingleSelection<Integer> selection = mock(SingleSelection.class);
        final Crossover<Integer> crossover = mock(Crossover.class);
        final Solution<Integer> solution = newSolution(8, 7);
        final Fitness<Integer> fitness = mock(Fitness.class);
        final List<Solution<Integer>> solutions = List.of(newSolution(0, 1), newSolution(1, 2));
        final List<Solution<Integer>> expected = IntStream.range(0, limit).mapToObj(i -> solution).collect(toUnmodifiableList());
        final OffspringSelection<Integer> subject = new OffspringSelection<>(limit, selection, crossover, fitness);
        given(selection.apply(solutions)).willReturn(solution);
        given(crossover.apply(any(Integer.class), any(Integer.class))).willReturn(7);
        given(fitness.applyAsDouble(any(Integer.class))).willReturn(8.0);
        // When
        final List<Solution<Integer>> result = subject.apply(solutions);
        // Then
        assertThat(result).isEqualTo(expected);
        verify(selection, times(limit * 2)).apply(solutions);
        verify(crossover, times(limit)).apply(any(Integer.class), any(Integer.class));
        verify(fitness, times(limit)).applyAsDouble(any(Integer.class));
        verifyNoMoreInteractions(selection, crossover, fitness);
    }
}
