package org.miglecz.optimization.genetic.facade;

import java.util.List;
import java.util.Objects;
import org.miglecz.optimization.genetic.Genetic;
import org.miglecz.optimization.genetic.facade.operator.Factory;
import org.miglecz.optimization.genetic.facade.operator.Fitness;
import org.miglecz.optimization.genetic.facade.operator.InitialSelection;

public class GeneticBuilderFacade<T> {
    private final Class<T> klass;
    private Integer population;
    private Factory<T> factory;
    private Fitness<T> fitness;

    private GeneticBuilderFacade(final Class<T> klass) {
        this.klass = klass;
    }

    public static <T> GeneticBuilderFacade<T> builder(final Class<T> klass) {
        return new GeneticBuilderFacade<>(klass);
    }

    private static void notNull(Object obj, String message) {
        if (Objects.isNull(obj)) {
            throw new NullPointerException(message + " should not be null");
        }
    }

    public GeneticBuilderFacade<T> withPopulation(final Integer population) {
        this.population = population;
        return this;
    }

    public GeneticBuilderFacade<T> withFactory(final Factory<T> factory) {
        this.factory = factory;
        return this;
    }

    public GeneticBuilderFacade<T> withFitness(final Fitness<T> fitness) {
        this.fitness = fitness;
        return this;
    }

    public Genetic<T> build() {
        notNull(population, "population");
        notNull(fitness, "fitness");
        notNull(factory, "factory");
        return new Genetic<>(
                new InitialSelection<>(population, fitness, factory)
                , List.of( //@formatter:off
                        List.of()
                ) //@formatter:on
        );
    }
}
