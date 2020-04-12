package ca.ucalgary.rules599.rules;

import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.model.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public abstract class TieBreaker<T> implements Comparator<T> {

    private static abstract class AbstractTieBreaker<T, TieBreakerType extends TieBreaker<T>> extends
            TieBreaker<T> {


        private final TieBreakerType parent;

        private final Comparator<T> comparator;

        private AbstractTieBreaker(@Nullable final TieBreakerType parent,
                                   @NotNull final Comparator<T> comparator) {
            ensureNotNull(comparator, "The tie-breaking comparator may not be null");
            this.parent = parent;
            this.comparator = comparator;
        }

        @Override
        public final int compare(@NotNull final T o1, @NotNull final T o2) {
            if (parent != null) {
                int result = parent.compare(o1, o2);

                if (result != 0) {
                    return result;
                }
            }

            return comparator.compare(o1, o2);
        }

    }


    public static class ItemSetTieBreaker extends AbstractTieBreaker<ItemSet, ItemSetTieBreaker> {


        private ItemSetTieBreaker(@Nullable final ItemSetTieBreaker parent,
                                  @NotNull final Comparator<ItemSet> comparator) {
            super(parent, comparator);
        }


        @NotNull
        public final ItemSetTieBreaker preferSmall() {
            return new ItemSetTieBreaker(this, (o1, o2) -> Integer.compare(o2.size(), o1.size()));
        }


        @NotNull
        public final ItemSetTieBreaker preferLarge() {
            return new ItemSetTieBreaker(this, Comparator.comparingInt(ItemSet::size));
        }


        @NotNull
        public final ItemSetTieBreaker custom(
                @NotNull final Comparator<ItemSet> comparator) {
            return new ItemSetTieBreaker(this, comparator);
        }

    }


    public static class AssociationRuleTieBreaker extends
            AbstractTieBreaker<AssociationRule, AssociationRuleTieBreaker> {


        private AssociationRuleTieBreaker(@Nullable final AssociationRuleTieBreaker parent,
                                          @NotNull final Comparator<AssociationRule> comparator) {
            super(parent, comparator);
        }


        @NotNull
        public final AssociationRuleTieBreaker byOperator(@NotNull final Operator operator) {
            ensureNotNull(operator, "The metric may not be null");
            return new AssociationRuleTieBreaker(this, (o1, o2) -> {
                double h1 = operator.evaluate(o1);
                double h2 = operator.evaluate(o2);
                return Double.compare(h1, h2);
            });
        }


        @NotNull
        public final AssociationRuleTieBreaker preferSimple() {
            return new AssociationRuleTieBreaker(this, (o1, o2) -> {
                int size1 = o1.getBody().size() + o1.getConsequence().size();
                int size2 = o2.getBody().size() + o2.getConsequence().size();
                return Integer.compare(size2, size1);
            });
        }


        @NotNull
        public final AssociationRuleTieBreaker preferComplex() {
            return new AssociationRuleTieBreaker(this, (o1, o2) -> {
                int size1 = o1.getBody().size() + o1.getConsequence().size();
                int size2 = o2.getBody().size() + o2.getConsequence().size();
                return Integer.compare(size1, size2);
            });
        }


        @NotNull
        public final AssociationRuleTieBreaker preferSimpleBody() {
            return new AssociationRuleTieBreaker(this, (o1, o2) -> {
                int size1 = o1.getBody().size();
                int size2 = o2.getBody().size();
                return Integer.compare(size2, size1);
            });
        }

        @NotNull
        public final AssociationRuleTieBreaker preferComplexBody() {
            return new AssociationRuleTieBreaker(this, (o1, o2) -> {
                int size1 = o1.getBody().size();
                int size2 = o2.getBody().size();
                return Integer.compare(size1, size2);
            });
        }


        @NotNull
        public final AssociationRuleTieBreaker preferSimpleHead() {
            return new AssociationRuleTieBreaker(this, (o1, o2) -> {
                int size1 = o1.getConsequence().size();
                int size2 = o2.getConsequence().size();
                return Integer.compare(size2, size1);
            });
        }


        @NotNull
        public final AssociationRuleTieBreaker preferComplexHead() {
            return new AssociationRuleTieBreaker(this, (o1, o2) -> {
                int size1 = o1.getConsequence().size();
                int size2 = o2.getConsequence().size();
                return Integer.compare(size1, size2);
            });
        }


        @NotNull
        public final AssociationRuleTieBreaker custom(
                @NotNull final Comparator<AssociationRule> comparator) {
            return new AssociationRuleTieBreaker(this, comparator);
        }

    }


    @NotNull
    public static ItemSetTieBreaker forItemSets() {
        return new ItemSetTieBreaker(null, (o1, o2) -> 0);
    }


    @NotNull
    public static AssociationRuleTieBreaker forAssociationRules() {
        return new AssociationRuleTieBreaker(null, (o1, o2) -> 0);
    }

}