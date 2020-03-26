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

        for (ItemSet<ItemType> itemSet : frequentItemSets.values()) {
            if (itemSet.size() > 1) {
                generateRules(itemSet, frequentItemSets, ruleSet, minConfidence);
            }
        }

        LOGGER.debug("Generated {} association rules", ruleSet.size());
        LOGGER.debug("Rule set = {}", ruleSet);
        return ruleSet;
    }

}