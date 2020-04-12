package ca.ucalgary.rules599.rules;

import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.model.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Predicate;

import static ca.ucalgary.rules599.util.Condition.ensureAtLeast;
import static ca.ucalgary.rules599.util.Condition.ensureAtMaximum;
import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public abstract class Filter<T> implements Predicate<T> {


    private static abstract class AbstractFilter<T, FilterType extends Filter<T>> extends
            Filter<T> {


        protected final Predicate<T> parent;


        protected final Predicate<T> predicate;


        private AbstractFilter(@Nullable final FilterType parent,
                               @NotNull final Predicate<T> predicate) {
            ensureNotNull(predicate, "The predicate may not be null");
            this.parent = parent;
            this.predicate = predicate;
        }

    }


    public static class ItemSetFilter extends AbstractFilter<ItemSet, ItemSetFilter> {


        public ItemSetFilter(@Nullable final ItemSetFilter parent,
                             @NotNull final Predicate<ItemSet> predicate) {
            super(parent, predicate);
        }


        @NotNull
        public final ItemSetFilter bySupport(final double minSupport) {
            return bySupport(minSupport, 1);
        }


        @NotNull
        public final ItemSetFilter bySupport(final double minSupport, final double maxSupport) {
            ensureAtLeast(minSupport, 0, "The minimum support must be at least 0");
            ensureAtMaximum(minSupport, 1, "The minimum support must be at least 1");
            ensureAtMaximum(maxSupport, 1, "The maximum support must be at least 1");
            ensureAtLeast(maxSupport, minSupport,
                    "The maximum support must be at least the minimum support");
            return new ItemSetFilter(this,
                    x -> x.getSupport() >= minSupport && x.getSupport() <= maxSupport);
        }


        @NotNull
        public final ItemSetFilter bySize(final int minSize) {
            return bySize(minSize, Integer.MAX_VALUE);
        }


        @NotNull
        public final ItemSetFilter bySize(final int minSize, final int maxSize) {
            ensureAtLeast(minSize, 0, "The minimum size must be at least 0");
            ensureAtLeast(maxSize, minSize, "The maximum size must be at least the minimum size");
            return new ItemSetFilter(this, x -> x.size() >= minSize && x.size() <= maxSize);
        }

        @Override
        public final boolean test(@NotNull final ItemSet t) {
            ensureNotNull(t, "The item set may not be null");
            return predicate.test(t) && (parent == null || parent.test(t));
        }

    }


    public static class AssociationRuleFilter extends
            AbstractFilter<AssociationRule, AssociationRuleFilter> {


        private AssociationRuleFilter(@Nullable final AssociationRuleFilter parent,
                                      @NotNull final Predicate<AssociationRule> predicate) {
            super(parent, predicate);
        }


        @NotNull
        public final AssociationRuleFilter byOperator(@NotNull final Operator operator,
                                                      final double minPerformance) {
            return byOperator(operator, minPerformance, Double.MAX_VALUE);
        }


        @NotNull
        public final AssociationRuleFilter byOperator(@NotNull final Operator operator,
                                                      final double minPerformance,
                                                      final double maxPerformance) {
            ensureNotNull(operator, "The operator may not be null");
            ensureAtLeast(minPerformance, 0, "The minimum performance must be at least 0");
            ensureAtLeast(maxPerformance, minPerformance,
                    "The maximum performance must be at least the minimum performance");
            return new AssociationRuleFilter(this, x -> {
                double h = operator.evaluate(x);
                return h >= minPerformance && h <= maxPerformance;
            });
        }


        @NotNull
        public final AssociationRuleFilter bySize(final int minSize) {
            return bySize(minSize, Integer.MAX_VALUE);
        }


        @NotNull
        public final AssociationRuleFilter bySize(final int minSize, final int maxSize) {
            ensureAtLeast(minSize, 0, "The minimum size must be at least 0");
            ensureAtLeast(maxSize, minSize, "The maximum size must be at least the minimum size");
            return new AssociationRuleFilter(this, x -> {
                int size = x.getBody().size() + x.getConsequence().size();
                return size >= minSize && size <= maxSize;
            });
        }


        @NotNull
        public final AssociationRuleFilter byBodySize(final int minSize) {
            return byBodySize(minSize, Integer.MAX_VALUE);
        }


        @NotNull
        public final AssociationRuleFilter byBodySize(final int minSize, final int maxSize) {
            ensureAtLeast(minSize, 0, "The minimum size must be at least 0");
            ensureAtLeast(maxSize, minSize, "The minimum size must be at least the minimum size");
            return new AssociationRuleFilter(this,
                    x -> x.getBody().size() >= minSize && x.getBody().size() <= maxSize);
        }


        @NotNull
        public final AssociationRuleFilter byHeadSize(final int minSize) {
            return byHeadSize(minSize, Integer.MAX_VALUE);
        }


        @NotNull
        public final AssociationRuleFilter byHeadSize(final int minSize, final int maxSize) {
            ensureAtLeast(minSize, 0, "The minimum size must be at least 0");
            ensureAtLeast(maxSize, minSize, "The maximum size must be at least the minimum size");
            return new AssociationRuleFilter(this,
                    x -> x.getConsequence().size() >= minSize && x.getConsequence().size() <= maxSize);
        }

        @Override
        public boolean test(@NotNull final AssociationRule t) {
            ensureNotNull(t, "The association rule may not be null");
            return predicate.test(t) && (parent == null || parent.test(t));
        }

    }


    @NotNull
    public static ItemSetFilter forItemSets() {
        return new ItemSetFilter(null, x -> true);
    }


    @NotNull
    public static AssociationRuleFilter forAssociationRules() {
        return new AssociationRuleFilter(null, x -> true);
    }

}