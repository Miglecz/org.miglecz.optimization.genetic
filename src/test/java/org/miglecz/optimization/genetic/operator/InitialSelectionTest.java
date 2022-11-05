package org.miglecz.optimization.genetic.operator;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.miglecz.optimization.Solution.newSolution;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class InitialSelectionTest extends TestBase {
    @Test(dataProvider = "data")
    void getShouldReturnInitialPopulation(
        final int population
        , final Fitness<Integer> fitness
        , final Factory<Integer> factory
        , final List<Solution<Integer>> expected
    ) {
        // Given
        final InitialSelection<Integer> subject = new InitialSelection<>(population, fitness, factory);
        // When
        final List<Solution<Integer>> result = subject.get();
        // Then
        assertThat(result, equalTo(expected));
    }

    @DataProvider(name = "data")
    Object[][] data() {
        final Random random = new Random();
        final List<Integer> impls = IntStream.range(0, random.nextInt(20))
            .mapToObj(i -> random.nextInt(100) + 2)
            .collect(toList());
        final int multiple = random.nextInt(9) + 1;
        return new Object[][]{
            new Object[]{0, (Fitness<Integer>) (impl) -> 0, (Factory<Integer>) () -> 0, List.of()}
            , new Object[]{1, (Fitness<Integer>) (impl) -> 2, (Factory<Integer>) () -> 1, List.of(newSolution(2, 1))}
            , new Object[]{ //@formatter:off
                impls.size()
                , (Fitness<Integer>) impl -> impl * multiple
                , new Factory<Integer>() {
                    private int index = 0;
                    @Override
                    public Integer get() {return impls.get(index++);}
                }
                , impls.stream().map(i -> newSolution(i * multiple, i)).collect(toList())
            } //@formatter:on
        };
    }
}
