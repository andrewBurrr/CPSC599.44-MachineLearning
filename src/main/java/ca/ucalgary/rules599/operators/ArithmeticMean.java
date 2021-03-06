package ca.ucalgary.rules599.operators;

import ca.ucalgary.rules599.datastructure.Pair;
import ca.ucalgary.rules599.model.Metric;
import ca.ucalgary.rules599.model.Operator;
import ca.ucalgary.rules599.rules.AssociationRule;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

import static ca.ucalgary.rules599.util.Condition.ensureGreater;
import static ca.ucalgary.rules599.util.Condition.ensureNotEmpty;
import static ca.ucalgary.rules599.util.Condition.ensureNotNull;

public class ArithmeticMean implements Operator {


    private final Collection<Pair<Metric, Double>> metrics;


    public ArithmeticMean() {
        this.metrics = new LinkedList<>();
    }


    @NotNull
    public final ArithmeticMean add(@NotNull final Metric metric) {
        return add(metric, 1);
    }


    @NotNull
    public final ArithmeticMean add(@NotNull final Metric metric, final double weight) {
        ensureNotNull(metric, "The metric may not be null");
        ensureGreater(weight, 0, "The weight must be greater than 0");
        metrics.add(Pair.create(metric, weight));
        return this;
    }

    @Override
    public final double evaluate(@NotNull final AssociationRule rule) {
        ensureNotNull(rule, "The rule may not be null");
        ensureNotEmpty(metrics, "No metrics added", IllegalStateException.class);
        double result = 0;
        double sumOfWeights = metrics.stream().mapToDouble(x -> x.second).sum();

        for (Pair<Metric, Double> pair : metrics) {
            Metric metric = pair.first;
            double heuristicValue = metric.evaluate(rule);
            double weight = pair.second;
            result += heuristicValue * (weight / sumOfWeights);
        }

        return result;
    }

}
