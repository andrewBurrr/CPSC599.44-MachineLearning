package ca.ucalgary.rules599.rules;

import ca.ucalgary.rules599.metrics.Confidence;
import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.model.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;

public abstract class Sorting<T> implements Comparator<T> {


    private static abstract class AbstractSorting<T, SortingType extends Sorting<T>> extends
            Sorting<T> {

        protected Order order;


        protected Comparator<T> tieBreaker;


        @SuppressWarnings("unchecked")
        protected SortingType self() {
            return (SortingType) this;
        }

        private AbstractSorting() {
            withOrder(Order.DESCENDING);
            withTieBreaking(null);
        }


        @NotNull
        public SortingType withOrder(@NotNull final Order order) {
            ensureNotNull(order, "The order may not be null");
            this.order = order;
            return self();
        }


        @NotNull
        public SortingType withTieBreaking(@Nullable final Comparator<T> comparator) {
            this.tieBreaker = comparator;
            return self();
        }

    }


    public static class ItemSetSorting extends AbstractSorting<ItemSet, ItemSetSorting> {


        public final int compare(final ItemSet o1, final ItemSet o2) {
            int result = Double.compare(o1.getSupport(), o2.getSupport());

            if (result == 0 && tieBreaker != null) {
                result = tieBreaker.compare(o1, o2);
            }

            return order == Order.ASCENDING ? result : result * -1;
        }

    }


    public static class AssociationRuleSorting extends
            AbstractSorting<AssociationRule, AssociationRuleSorting> {


        private Operator operator;


        private AssociationRuleSorting() {
            byOperator(new Confidence());
        }


        @NotNull
        public AssociationRuleSorting byOperator(@Nullable final Operator operator) {
            this.operator = operator;
            return self();
        }


        public final int compare(final AssociationRule o1, final AssociationRule o2) {
            int result = operator != null ?
                    Double.compare(operator.evaluate(o1), operator.evaluate(o2)) : o1.compareTo(o2);

            if (result == 0 && tieBreaker != null) {
                result = tieBreaker.compare(o1, o2);
            }

            return order == Order.ASCENDING ? result : result * -1;
        }

    }


    public enum Order {
        ASCENDING,
        DESCENDING

    }


    @NotNull
    public static ItemSetSorting forItemSets() {
        return new ItemSetSorting();
    }


    @NotNull
    public static AssociationRuleSorting forAssociationRules() {
        return new AssociationRuleSorting();
    }

}