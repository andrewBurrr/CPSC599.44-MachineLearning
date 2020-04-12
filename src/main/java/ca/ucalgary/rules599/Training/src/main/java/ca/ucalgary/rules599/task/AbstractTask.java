package ca.ucalgary.rules599.task;

import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.rules.Apriori;
import org.jetbrains.annotations.NotNull;

import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public abstract class AbstractTask<ItemType extends Item> {


    private final Apriori.Configuration configuration;


    public AbstractTask(@NotNull final Apriori.Configuration configuration) {
        ensureNotNull(configuration, "The configuration may not be null");
        this.configuration = configuration;
    }


    @NotNull
    protected final Apriori.Configuration getConfiguration() {
        return configuration;
    }

}