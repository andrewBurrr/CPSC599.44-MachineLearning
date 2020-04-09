package ca.ucalgary.rules599.task;

import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.modules.AssociationRuleGenerator;
import ca.ucalgary.rules599.modules.AssociationRuleGeneratorModule;
import ca.ucalgary.rules599.rules.Apriori;
import ca.ucalgary.rules599.rules.RuleSet;
import ca.ucalgary.rules599.rules.Sorting;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class AssociationRuleGeneratorTask<ItemType extends Item> extends AbstractTask<ItemType> {


    private final AssociationRuleGenerator<ItemType> associationRuleGenerator;


    public AssociationRuleGeneratorTask(@NotNull final Apriori.Configuration configuration) {
        this(configuration, new AssociationRuleGeneratorModule<>());
    }


    public AssociationRuleGeneratorTask(@NotNull final Apriori.Configuration configuration,
                                        @NotNull final AssociationRuleGenerator<ItemType> associationRuleGenerator) {
        super(configuration);
        ensureNotNull(associationRuleGenerator, "The association rule generator may not be null");
        this.associationRuleGenerator = associationRuleGenerator;
    }


    @NotNull
    public final RuleSet<ItemType> generateAssociationRules(@NotNull final Map<Integer, ? extends ItemSet<ItemType>> frequentItemSets) {
        if (getConfiguration().getRuleCount() > 0) {
            RuleSet<ItemType> result = null;
            double currentMinConfidence = getConfiguration().getMaxConfidence();

            while (currentMinConfidence >= getConfiguration().getMinConfidence() &&
                    (result == null || result.size() < getConfiguration().getRuleCount())) {
                RuleSet<ItemType> ruleSet = associationRuleGenerator
                        .generateAssociationRules(frequentItemSets, currentMinConfidence);

                if (result == null || ruleSet.size() >= result.size()) {
                    result = ruleSet;
                }

                currentMinConfidence -= getConfiguration().getConfidenceDelta();
            }

            return result != null ? result : new RuleSet<>(Sorting.forAssociationRules());
        } else {
            return associationRuleGenerator.generateAssociationRules(frequentItemSets,
                    getConfiguration().getMinConfidence());
        }
    }

}
