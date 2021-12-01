package org.miglecz.optimization.genetic.facade;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.Genetic;
import org.miglecz.optimization.genetic.MultiSelection;
import org.miglecz.optimization.genetic.facade.operator.Crossover;
import org.miglecz.optimization.genetic.facade.operator.EliteSelection;
import org.miglecz.optimization.genetic.facade.operator.Factory;
import org.miglecz.optimization.genetic.facade.operator.Fitness;
import org.miglecz.optimization.genetic.facade.operator.ImmigrantSelection;
import org.miglecz.optimization.genetic.facade.operator.InitialSelection;
import org.miglecz.optimization.genetic.facade.operator.MutantSelection;
import org.miglecz.optimization.genetic.facade.operator.Mutation;
import org.miglecz.optimization.genetic.facade.operator.OffspringSelection;
import org.miglecz.optimization.genetic.facade.operator.RandomSelection;
import org.miglecz.optimization.genetic.facade.operator.TournamentSelection;

public class GeneticBuilderFacade<T> {
    private final Class<T> klass;
    private Random random = new Random(1);
    private Integer population;
    private Factory<T> factory;
    private Fitness<T> fitness;
    private Integer elite;
    private final Comparator<Solution<T>> comparator = Comparator.<Solution<T>>comparingDouble(Solution::getScore).reversed();
    private Integer offspring;
    private Integer mutant;
    private Mutation<T> mutation;
    private Integer immigrant;
    private Crossover<T> crossover;

    private GeneticBuilderFacade(final Class<T> klass) {
        this.klass = klass;
    }

    public static <T> GeneticBuilderFacade<T> builder(final Class<T> klass) {
        return new GeneticBuilderFacade<>(klass);
    }

    private static void notNull(Object obj, String message) {
        if (Objects.isNull(obj)) {
            throw new NullPointerException(format("%s should not be null", message));
        }
    }

    public GeneticBuilderFacade<T> withRandom(final Random random) {
        this.random = random;
        return this;
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

    public GeneticBuilderFacade<T> withElite(final Integer elite) {
        this.elite = elite;
        return this;
    }

    public GeneticBuilderFacade<T> withOffspring(final Integer offspring, final Crossover<T> crossover) { //TODO change Integer to int everywhere
        this.offspring = offspring;
        this.crossover = crossover;
        return this;
    }

    public GeneticBuilderFacade<T> withMutant(final Integer mutant, final Mutation<T> mutation) {
        this.mutant = mutant;
        this.mutation = mutation;
        return this;
    }

    public GeneticBuilderFacade<T> withImmigrant(final Integer immigrant) {
        this.immigrant = immigrant;
        return this;
    }

    public Genetic<T> build() {
        notNull(random, "random");
        notNull(population, "population");
        notNull(fitness, "fitness");
        notNull(factory, "factory");
        notNull(comparator, "comparator");
        final List<MultiSelection<T>> mainSelection = new ArrayList<>();
        if (elite != null) {
            mainSelection.add(new EliteSelection<>(elite, comparator));
        }
        if (offspring != null || crossover != null) {
            notNull(offspring, "offspring");
            notNull(crossover, "crossover");
            mainSelection.add(new OffspringSelection<>(offspring, new TournamentSelection<>(random, comparator), crossover, fitness));
        }
        if (mutant != null || mutation != null) {
            notNull(mutant, "mutant");
            notNull(mutation, "mutation");
            mainSelection.add(new MutantSelection<>(mutant, new RandomSelection<>(random), mutation, fitness));
        }
        if (immigrant != null) {
            mainSelection.add(new ImmigrantSelection<>(immigrant, fitness, factory));
        }
        return new Genetic<>(
                new InitialSelection<>(population, fitness, factory)
                , List.of( //@formatter:off
                        unmodifiableList(mainSelection)
                        , List.of(new EliteSelection<>(population, comparator))
                ) //@formatter:on
        );
    }
}
