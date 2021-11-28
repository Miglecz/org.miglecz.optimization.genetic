package org.miglecz.optimization.genetic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;

public class GeneticBuilderTest {
    @Test
    void buildShouldReturnGeneticInstance() {
        // Given
        final GeneticBuilder<Integer> builder = GeneticBuilder.builder(Integer.class)
                .withInitialize(Collections::emptyList)
                .withSelectionsList(List.of());
        // When
        final Genetic<Integer> result = builder.build();
        // Then
        assertThat(result, notNullValue());
    }

    @Test(expectedExceptions = NullPointerException.class)
    void buildShouldFailWhenNoInitialize() {
        // Given
        final GeneticBuilder<Integer> builder = GeneticBuilder.builder(Integer.class)
                //.withInitialize(Collections::emptyList)
                .withSelectionsList(List.of());
        // When
        builder.build();
        // Then
    }

    @Test(expectedExceptions = NullPointerException.class)
    void buildShouldFailWhenNoSelectionsList() {
        // Given
        final GeneticBuilder<Integer> builder = GeneticBuilder.builder(Integer.class)
                .withInitialize(Collections::emptyList)
                //.withSelectionsList(List.of())
                ;
        // When
        builder.build();
        // Then
    }
}
