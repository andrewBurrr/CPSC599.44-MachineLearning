package ca.ucalgary.rules599.metrics;

import ca.ucalgary.rules599.model.Metric;
import ca.ucalgary.rules599.rules.AssociationRule;
import org.jetbrains.annotations.NotNull;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class Support implements Metric {


    public final double evaluate(@NotNull final AssociationRule rule) {
        ensureNotNull(rule, "The rule may not be null");
        return rule.getSupport();
    }


    public final double minValue() {
        return 0;
    }


    public final double maxValue() {
        return 1;
    }

}