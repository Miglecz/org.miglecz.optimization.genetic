package org.miglecz.optimization.genetic;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toCollection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
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
    private RandomGenerator random = new Random(1);
    private Integer population;
    private Factory<T> factory;
    private Fitness<T> fitness;
    private Integer elite;
    private final Comparator<Solution<T>> comparator = descending();
    private Integer offspring;
    private Integer mutant;
    private Mutation<T> mutation;
    private Integer immigrant;
    private Crossover<T> crossover;
    private Boolean parallel = false;

    private static <T> Comparator<Solution<T>> ascending() {
        return Comparator.comparingDouble(Solution::getScore);
    }

    private static <T> Comparator<Solution<T>> descending() {
        return Comparator.<Solution<T>>comparingDouble(Solution::getScore).reversed();
    }

    private GeneticOptimizationBuilder(final Class<T> klass) {
        this.klass = klass;
    }

    public static <T> GeneticOptimizationBuilder<T> builder(final Class<T> klass) {
        return new GeneticOptimizationBuilder<>(klass);
    }

    private static void notNull(final Object obj, final String message) {
        Objects.requireNonNull(obj, () -> format("%s %s should not be null"
            , GeneticOptimizationBuilder.class.getSimpleName()
            , message
        ));
    }

    public GeneticOptimizationBuilder<T> withRandom(final RandomGenerator random) {
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

    public GeneticOptimizationBuilder<T> withParallel(final boolean parallel) {
        this.parallel = parallel;
        return this;
    }

    public Optimization<T> build() {
        notNull(random, "random");
        notNull(fitness, "fitness");
        notNull(factory, "factory");
        notNull(comparator, "comparator");
        if (population == null) {
            population = Stream.of(elite, offspring, mutant, immigrant)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
        }
        final List<MultiSelection<T>> mainSelection = new ArrayList<>();
        if (elite != null) {
            mainSelection.add(new EliteSelection<>(elite, comparator));
        }
        if (offspring != null) {
            notNull(crossover, "crossover");
            IntStream.range(0, offspring)
                .mapToObj(i -> new OffspringSelection<>(1, new TournamentSelection<>(random, comparator), crossover, fitness))
                .collect(toCollection(() -> mainSelection));
        }
        if (mutant != null) {
            notNull(mutant, "mutant");
            notNull(mutation, "mutation");
            IntStream.range(0, mutant)
                .mapToObj(i -> new MutantSelection<>(1, new RandomSelection<>(random), mutation, fitness))
                .collect(toCollection(() -> mainSelection));
        }
        if (immigrant != null) {
            IntStream.range(0, immigrant)
                .mapToObj(i -> new ImmigrantSelection<>(1, fitness, factory))
                .collect(toCollection(() -> mainSelection));
        }
        return new GeneticOptimization<>(
            new InitialSelection<>(population, fitness, factory)
            , List.of( //@formatter:off
                unmodifiableList(mainSelection)
                , List.of(new EliteSelection<>(population, comparator))
            ) //@formatter:on
            , parallel
        );
    }
}
