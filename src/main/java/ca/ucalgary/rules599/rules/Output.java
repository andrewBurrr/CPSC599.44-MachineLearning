package ca.ucalgary.rules599.rules;

import ca.ucalgary.rules599.model.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

import static ca.ucalgary.rules599.util.Condition.ensureAtLeast;
import static ca.ucalgary.rules599.util.Condition.ensureNotNull;


public class Output<ItemType extends Item> implements Serializable, Cloneable {


    private static final long serialVersionUID = 1L;


    private final Apriori.Configuration configuration;


    private final long startTime;


    private final long endTime;


    private final FrequentItemSets<ItemType> frequentItemSets;


    private final RuleSet<ItemType> ruleSet;


    public Output(@NotNull final Apriori.Configuration configuration, final long startTime,
                  final long endTime,
                  @NotNull final FrequentItemSets<ItemType> frequentItemSets,
                  @Nullable final RuleSet<ItemType> ruleSet) {
        ensureNotNull(configuration, "The configuration may not be null");
        ensureAtLeast(startTime, 0, "The start time must be at least 0");
        ensureAtLeast(endTime, startTime, "The end time must be at least " + startTime);
        ensureNotNull(frequentItemSets, "The frequent item sets may not be null");
        this.configuration = configuration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequentItemSets = frequentItemSets;
        this.ruleSet = ruleSet;
    }


    @NotNull
    public final Apriori.Configuration getConfiguration() {
        return configuration;
    }


    public final long getStartTime() {
        return startTime;
    }


    public final long getEndTime() {
        return endTime;
    }


    public final long getRuntime() {
        return endTime - startTime;
    }


    @NotNull
    public final FrequentItemSets<ItemType> getFrequentItemSets() {
        return frequentItemSets;
    }


    @Nullable
    public final RuleSet<ItemType> getRuleSet() {
        return ruleSet;
    }

    @Override
    public final Output<ItemType> clone() {
        return new Output<>(configuration.clone(), startTime, endTime, frequentItemSets.clone(),
                ruleSet.clone());
    }

    @Override
    public final String toString() {
        return "configuration=" + configuration.toString() + ",\nstartTime=" + startTime +
                ",\nendTime=" + endTime + ",\nruntime=" + getRuntime() + ",\nfrequentItemSets=" +
                frequentItemSets.toString() + ",\nruleSet=" + ruleSet.toString();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + configuration.hashCode();
        result = prime * result + (int) (startTime ^ (startTime >>> 32));
        result = prime * result + (int) (endTime ^ (endTime >>> 32));
        result = prime * result + frequentItemSets.hashCode();
        result = prime * result + (ruleSet == null ? 0 : ruleSet.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Output<?> other = (Output<?>) obj;
        if (!configuration.equals(other.configuration))
            return false;
        if (startTime != other.startTime)
            return false;
        if (endTime != other.endTime)
            return false;
        if (!frequentItemSets.equals(other.frequentItemSets))
            return false;
        if (ruleSet == null) {
            if (other.ruleSet != null)
                return false;
        } else if (!ruleSet.equals(other.ruleSet))
            return false;
        return true;
    }

}
