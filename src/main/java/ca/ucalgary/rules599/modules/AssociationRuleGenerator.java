package ca.ucalgary.rules599.modules;

import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.rules.RuleSet;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface AssociationRuleGenerator<ItemType extends Item> {


    @NotNull
    RuleSet<ItemType> generateAssociationRules(
            @NotNull Map<Integer, ? extends ItemSet<ItemType>> frequentItemSets,
            double minConfidence);

}