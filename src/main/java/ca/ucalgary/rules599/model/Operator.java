package ca.ucalgary.rules599.model;

import ca.ucalgary.rules599.rules.AssociationRule;
import org.jetbrains.annotations.NotNull;

public interface Operator {
    double evaluate(@NotNull AssociationRule rule);
}