package ca.ucalgary.rules599.modules;

import ca.ucalgary.rules599.metrics.Confidence;
import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.rules.AssociationRule;
import ca.ucalgary.rules599.rules.RuleSet;
import ca.ucalgary.rules599.rules.Sorting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static ca.ucalgary.rules599.util.Condition.ensureAtLeast;
import static ca.ucalgary.rules599.util.Condition.ensureAtMaximum;
import static ca.ucalgary.rules599.util.Condition.ensureNotNull;

public class AssociationRuleGeneratorModule<ItemType extends Item> implements AssociationRuleGenerator<ItemType> {


    private static final Logger LOGGER = LoggerFactory
            .getLogger(AssociationRuleGeneratorModule.class);

    private void generateRules(@NotNull final ItemSet<ItemType> itemSet,
                               @NotNull final Map<Integer, ? extends ItemSet<ItemType>> frequentItemSets,
                               @NotNull final RuleSet<ItemType> ruleSet,
                               final double minConfidence) {
        generateRules(itemSet, frequentItemSets, ruleSet, itemSet, null, minConfidence);
    }

    /**
     * Generates association rules from a specific item set by moving items from a rule's body to
     * its head. This method is executed recursively until the resulting rule does not reach the
     * minimum confidence anymore.
     *
     * @param itemSet          The item set, the association rules should be created from, as an
     *                         instance of the class {@link ItemSet}. The item set may not be null
     * @param frequentItemSets A map, which contains all available frequent item sets, as an
     *                         instance of the type {@link Map} or an empty map, if no frequent item
     *                         sets are available. The map must store the frequent item sets as
     *                         values and their hash codes as the corresponding keys
     * @param ruleSet          The rule set, the generated rules should be added to, as an instance
     *                         of the class {@link RuleSet}. The rule set may not be null
     * @param body             The body, the items, which should be moved to the head, should be
     *                         taken from, as an instance of the class {@link ItemSet}. The body may
     *                         not be null
     * @param head             The head, the items, which are taken from the given body, should be
     *                         moved to, as an instance of the class {@link ItemSet} or null, if an
     *                         empty head should be created
     * @param minConfidence    The minimum confidence, which must at least be reached by association
     *                         rules, as a {@link Double} value. The confidence must be at least 0
     *                         and at maximum 1
     */
    private void generateRules(@NotNull final ItemSet<ItemType> itemSet,
                               @NotNull final Map<Integer, ? extends ItemSet<ItemType>> frequentItemSets,
                               @NotNull final RuleSet<ItemType> ruleSet,
                               @NotNull final ItemSet<ItemType> body,
                               @Nullable final ItemSet<ItemType> head, final double minConfidence) {
        for (ItemType item : body) {
            ItemSet<ItemType> headItemSet = head != null ? head.clone() : new ItemSet();
            headItemSet.add(item);
            ItemSet<ItemType> bodyItemSet = body.clone();
            bodyItemSet.remove(item);
            bodyItemSet.setSupport(frequentItemSets.get(bodyItemSet.hashCode()).getSupport());
            headItemSet.setSupport(frequentItemSets.get(headItemSet.hashCode()).getSupport());
            double support = itemSet.getSupport();
            AssociationRule<ItemType> rule = new AssociationRule<>(bodyItemSet,headItemSet, support);
            double confidence = new Confidence().evaluate(rule);

            if (confidence >= minConfidence) {
                ruleSet.add(rule);

                if (bodyItemSet.size() > 1) {
                    generateRules(itemSet, frequentItemSets, ruleSet, bodyItemSet, headItemSet,
                            minConfidence);
                }
            }
        }
    }

    @NotNull
    public final RuleSet<ItemType> generateAssociationRules(
            @NotNull final Map<Integer, ? extends ItemSet<ItemType>> frequentItemSets,
            final double minConfidence) {
        ensureNotNull(frequentItemSets, "The frequent item sets may not be null");
        ensureAtLeast(minConfidence, 0, "The minimum confidence must be at least 0");
        ensureAtMaximum(minConfidence, 1, "The minimum confidence must be at maximum 1");
        LOGGER.debug("Generating association rules");
        RuleSet<ItemType> ruleSet = new RuleSet<>(Sorting.forAssociationRules());

        LOGGER.debug("Total Number of items" + frequentItemSets.size());
        int count=1;
        for (ItemSet<ItemType> itemSet : frequentItemSets.values()) {
            LOGGER.debug("Running rules for " + count);
            if (itemSet.size() > 1) {
                LOGGER.debug("rules for item" + itemSet.hashCode());
                generateRules(itemSet, frequentItemSets, ruleSet, minConfidence);
            }
            count++;
        }

        LOGGER.debug("Generated {} association rules", ruleSet.size());
        LOGGER.debug("Rule set = {}", ruleSet);
        return ruleSet;
    }

}