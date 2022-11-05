package org.miglecz.optimization.genetic;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.miglecz.optimization.Optimization;
import org.miglecz.optimization.Solution;
import org.miglecz.optimization.genetic.operator.Crossover;
import org.miglecz.optimization.genetic.operator.EliteSelection;
import org.miglecz.optimization.genetic.operator.Factory;
import org.miglecz.optimization.genetic.operator.Fitness;
import org.miglecz.optimization.genetic.operator.ImmigrantSelection;
import org.miglecz.optimization.genetic.operator.InitialSelection;
import org.miglecz.optimization.genetic.operator.MutantSelection;
import org.miglecz.optimization.genetic.operator.Mutation;
import org.miglecz.optimization.genetic.operator.OffspringSelection;
import org.miglecz.optimization.genetic.operator.RandomSelection;
import org.miglecz.optimization.genetic.operator.TournamentSelection;

public class GeneticOptimizationBuilder<T> {
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

    private GeneticOptimizationBuilder(final Class<T> klass) {
        this.klass = klass;
    }

    public static <T> GeneticOptimizationBuilder<T> builder(final Class<T> klass) {
        return new GeneticOptimizationBuilder<>(klass);
    }

    private static void notNull(final Object obj, final String message) {
        if (Objects.isNull(obj)) {
            throw new NullPointerException(format("%s should not be null", message));
        }
    }

    public GeneticOptimizationBuilder<T> withRandom(final Random random) {
        this.random = random;
        return this;
    }

    public GeneticOptimizationBuilder<T> withPopulation(final int population) {
        this.population = population;
        return this;
    }

    public GeneticOptimizationBuilder<T> withFactory(final Factory<T> factory) {
        this.factory = factory;
        return this;
    }

    public GeneticOptimizationBuilder<T> withFitness(final Fitness<T> fitness) {
        this.fitness = fitness;
        return this;
    }

    public GeneticOptimizationBuilder<T> withElite(final int elite) {
        this.elite = elite;
        return this;
    }

    public GeneticOptimizationBuilder<T> withOffspring(final int offspring, final Crossover<T> crossover) {
        this.offspring = offspring;
        this.crossover = crossover;
        return this;
    }

    public GeneticOptimizationBuilder<T> withMutant(final int mutant, final Mutation<T> mutation) {
        this.mutant = mutant;
        this.mutation = mutation;
        return this;
    }

    public GeneticOptimizationBuilder<T> withImmigrant(final int immigrant) {
        this.immigrant = immigrant;
        return this;
    }

    public Optimization<T> build() {
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
        return new GeneticOptimization<>(
            new InitialSelection<>(population, fitness, factory)
            , List.of( //@formatter:off
                unmodifiableList(mainSelection)
                , List.of(new EliteSelection<>(population, comparator))
            ) //@formatter:on
        );
    }
}
