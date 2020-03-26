package ca.ucalgary.rules599.rules;

import ca.ucalgary.rules599.datastructure.Filterable;
import ca.ucalgary.rules599.datastructure.Sortable;
import ca.ucalgary.rules599.datastructure.SortedArraySet;
import ca.ucalgary.rules599.metrics.Confidence;
import ca.ucalgary.rules599.metrics.Leverage;
import ca.ucalgary.rules599.metrics.Lift;
import ca.ucalgary.rules599.metrics.Support;
import ca.ucalgary.rules599.model.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;



public class RuleSet<ItemType extends Item> extends SortedArraySet<AssociationRule<ItemType>> implements
        Sortable<RuleSet<ItemType>, AssociationRule>,
        Filterable<RuleSet<ItemType>, AssociationRule>, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;


    public RuleSet(@Nullable final Comparator<? super AssociationRule<ItemType>> comparator) {
        super(comparator);
    }


    protected RuleSet(@NotNull final Collection<AssociationRule<ItemType>> rules,
                      @Nullable final Comparator<? super AssociationRule<ItemType>> comparator) {
        super(rules, comparator);
    }

    @NotNull
    public final RuleSet<ItemType> sort(@Nullable final Comparator<AssociationRule> comparator) {
        return new RuleSet<>(this, comparator);
    }

    @NotNull
    public final RuleSet<ItemType> filter(@NotNull final Predicate<AssociationRule> predicate) {
        ensureNotNull(predicate, "The predicate may not be null");
        RuleSet<ItemType> filteredRuleSet = new RuleSet<>(comparator());

        for (AssociationRule<ItemType> item : this) {
            if (predicate.test(item)) {
                filteredRuleSet.add(item);
            }
        }

        return filteredRuleSet;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public final RuleSet<ItemType> clone() {
        return new RuleSet<>(this, comparator());
    }

    @Override
    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(1);
        decimalFormat.setMaximumFractionDigits(2);
        Iterator<AssociationRule<ItemType>> iterator = iterator();
        stringBuilder.append("[");

        while (iterator.hasNext()) {
            AssociationRule<ItemType> rule = iterator.next();
            stringBuilder.append(rule.toString());
            stringBuilder.append(" (support = ");
            stringBuilder.append(decimalFormat.format(new Support().evaluate(rule)));
            stringBuilder.append(", confidence = ");
            stringBuilder.append(decimalFormat.format(new Confidence().evaluate(rule)));
            stringBuilder.append(", lift = ");
            stringBuilder.append(decimalFormat.format(new Lift().evaluate(rule)));
            stringBuilder.append(", leverage = ");
            stringBuilder.append(decimalFormat.format(new Leverage().evaluate(rule)));
            stringBuilder.append(")");

            if (iterator.hasNext()) {
                stringBuilder.append(",\n");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}