package ca.ucalgary.rules599.metrics;

import ca.ucalgary.rules599.model.Metric;
import ca.ucalgary.rules599.rules.AssociationRule;
import org.jetbrains.annotations.NotNull;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class Leverage implements Metric {


    public final double evaluate(@NotNull final AssociationRule rule) {
        ensureNotNull(rule, "The rule may not be null");
        double bodySupport = rule.getBody().getSupport();
        double headSupport = rule.getConsequence().getSupport();
        return rule.getSupport() - (bodySupport * headSupport);
    }


    public final double minValue() {
        return -Double.MAX_VALUE;
    }


    public final double maxValue() {
        return 1;
    }

}