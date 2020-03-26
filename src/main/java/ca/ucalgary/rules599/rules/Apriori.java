package ca.ucalgary.rules599.rules;

import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.Item;
import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.model.Transaction;
import ca.ucalgary.rules599.task.AssociationRuleGeneratorTask;
import ca.ucalgary.rules599.task.FrequentItemSetMinerTask;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

import static ca.ucalgary.rules599.util.Condition.*;

public class Apriori<ItemType extends Item> {

    public static class Configuration implements Serializable, Cloneable {

        private static final long serialVersionUID = 1L;
        private double minSupport;
        private double maxSupport;
        private double supportDelta;
        private int frequentItemSetCount;
        private boolean generateRules;
        private double minConfidence;
        private double maxConfidence;
        private double confidenceDelta;
        private int ruleCount;

        public Configuration() {
            setMinSupport(0);
            setMaxSupport(1);
            setSupportDelta(0.1);
            setFrequentItemSetCount(0);
            setGenerateRules(false);
            setMinConfidence(0);
            setMaxConfidence(1);
            setConfidenceDelta(0.1);
            setRuleCount(0);
        }

        public double getMinSupport() {
            return minSupport;
        }
        public void setMinSupport(final double minSupport) {
            ensureAtLeast(minSupport, 0, "The minimum support must be at least 0");
            ensureAtMaximum(minSupport, maxSupport,
                    "The minimum support must be at maximum " + maxSupport);
            this.minSupport = minSupport;
        }

        public double getMaxSupport() {
            return maxSupport;
        }

        public void setMaxSupport(final double maxSupport) {
            ensureAtMaximum(maxSupport, 1, "The maximum support must be at maximum 1");
            ensureAtLeast(maxSupport, minSupport, "The maximum support must be at least" +
                    minSupport);
            this.maxSupport = maxSupport;
        }

        public double getSupportDelta() {
            return supportDelta;
        }

        public void setSupportDelta(final double supportDelta) {
            ensureGreater(supportDelta, 0, "The support delta must be greater than 0");
            this.supportDelta = supportDelta;
        }

        public int getFrequentItemSetCount() {
            return frequentItemSetCount;
        }
        public void setFrequentItemSetCount(final int frequentItemSetCount) {
            ensureAtLeast(frequentItemSetCount, 0,
                    "The number of frequent item sets must be at least 0");
            this.frequentItemSetCount = frequentItemSetCount;
        }
        public boolean isGeneratingRules() {
            return generateRules;
        }
        public void setGenerateRules(final boolean generateRules) {
            this.generateRules = generateRules;
        }
        public double getMinConfidence() {
            return minConfidence;
        }
        public void setMinConfidence(final double minConfidence) {
            ensureAtLeast(minConfidence, 0, "The minimum confidence must be at least 0");
            ensureAtMaximum(minConfidence, maxConfidence,
                    "The minimum confidence must be at maximum " + maxConfidence);
            this.minConfidence = minConfidence;
        }
        public double getMaxConfidence() {
            return maxConfidence;
        }

        public void setMaxConfidence(final double maxConfidence) {
            ensureAtMaximum(maxConfidence, 1, "The max confidence must be at maximum 1");
            ensureAtLeast(maxConfidence, minConfidence,
                    "The max confidence must be at least " + minConfidence);
            this.maxConfidence = maxConfidence;
        }
        public double getConfidenceDelta() {
            return confidenceDelta;
        }

        public void setConfidenceDelta(final double confidenceDelta) {
            ensureGreater(confidenceDelta, 0, "The confidence delta must be greater than 0");
            this.confidenceDelta = confidenceDelta;
        }

        public int getRuleCount() {
            return ruleCount;
        }

        public void setRuleCount(final int ruleCount) {
            ensureAtLeast(ruleCount, 0, "The rule count must be at least 0");
            this.ruleCount = ruleCount;
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @Override
        public final Configuration clone() {
            Configuration clone = new Configuration();
            clone.minSupport = minSupport;
            clone.maxSupport = maxSupport;
            clone.supportDelta = supportDelta;
            clone.frequentItemSetCount = frequentItemSetCount;
            clone.generateRules = generateRules;
            clone.minConfidence = minConfidence;
            clone.maxConfidence = maxConfidence;
            clone.confidenceDelta = confidenceDelta;
            clone.ruleCount = ruleCount;
            return clone;
        }

        @Override
        public final String toString() {
            return "[minSupport=" + minSupport + ", maxSupport=" + maxSupport +
                    ", supportDelta=" + supportDelta + ", frequentItemSetCount=" +
                    frequentItemSetCount + ", generateRules=" + generateRules + ", minConfidence=" +
                    minConfidence + ", maxConfidence=" + maxConfidence + ", confidenceDelta=" +
                    confidenceDelta + ", ruleCount=" + ruleCount + "]";
        }

        @Override
        public final int hashCode() {
            final int prime = 31;
            int result = 1;
            long tempMinSupport = Double.doubleToLongBits(minSupport);
            result = prime * result + (int) (tempMinSupport ^ (tempMinSupport >>> 32));
            long tempMaxSupport = Double.doubleToLongBits(maxSupport);
            result = prime * result + (int) (tempMaxSupport ^ (tempMaxSupport >>> 32));
            long tempSupportDelta = Double.doubleToLongBits(supportDelta);
            result = prime * result + (int) (tempSupportDelta ^ (tempSupportDelta >>> 32));
            result = prime * result + frequentItemSetCount;
            result = prime * result + (generateRules ? 1231 : 1237);
            long tempMinConfidence = Double.doubleToLongBits(minConfidence);
            result = prime * result + (int) (tempMinConfidence ^ (tempMinConfidence >>> 32));
            long tempMaxConfidence = Double.doubleToLongBits(maxConfidence);
            result = prime * result + (int) (tempMaxConfidence ^ (tempMaxConfidence >>> 32));
            long tempConfidenceDelta = Double.doubleToLongBits(confidenceDelta);
            result = prime * result + (int) (tempConfidenceDelta ^ (tempConfidenceDelta >>> 32));
            result = prime * result + ruleCount;
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
            Configuration other = (Configuration) obj;
            return minSupport == other.minSupport && maxSupport == other.maxSupport &&
                    supportDelta == other.supportDelta &&
                    frequentItemSetCount == other.frequentItemSetCount &&
                    generateRules == other.generateRules && minConfidence == other.minConfidence &&
                    maxConfidence == other.maxConfidence &&
                    confidenceDelta == other.confidenceDelta && ruleCount == other.ruleCount;
        }

    }

    private static abstract class AbstractBuilder<ItemType extends Item> {
        protected final Configuration configuration;

        private AbstractBuilder(final double minSupport) {
            configuration = new Configuration();
            configuration.setMinSupport(minSupport);
        }
        private AbstractBuilder(final int frequentItemSetCount) {
            configuration = new Configuration();
            configuration.setFrequentItemSetCount(frequentItemSetCount);
        }
        private AbstractBuilder(@NotNull final AbstractBuilder<ItemType> builder) {
            ensureNotNull(builder, "The builder may not be null");
            configuration = builder.configuration.clone();
        }

        @NotNull
        public final Apriori<ItemType> create() {
            return new Apriori<>(configuration);
        }

    }
    public static class Builder<ItemType extends Item> extends AbstractBuilder<ItemType> {
        public Builder(final double minSupport) {
            super(minSupport);
        }

        public Builder(final int frequentItemSetCount) {
            super(frequentItemSetCount);
        }

        @NotNull
        public final Builder<ItemType> minSupport(final double minSupport) {
            configuration.setMinSupport(minSupport);
            return this;
        }
        @NotNull
        public final Builder<ItemType> maxSupport(final double maxSupport) {
            configuration.setMaxSupport(maxSupport);
            return this;
        }

        @NotNull
        public final Builder<ItemType> supportDelta(final double supportDelta) {
            configuration.setSupportDelta(supportDelta);
            return this;
        }
        @NotNull
        public final Builder<ItemType> frequentItemSetCount(final int frequentItemSetCount) {
            configuration.setFrequentItemSetCount(frequentItemSetCount);
            return this;
        }
        @NotNull
        public final RuleGeneratorBuilder<ItemType> generateRules(final double minConfidence) {
            return new RuleGeneratorBuilder<>(this, minConfidence);
        }
        @NotNull
        public final RuleGeneratorBuilder<ItemType> generateRules(final int ruleCount) {
            return new RuleGeneratorBuilder<ItemType>(this, ruleCount);
        }

    }
    public static class RuleGeneratorBuilder<ItemType extends Item> extends
            AbstractBuilder<ItemType> {

        private RuleGeneratorBuilder(@NotNull final AbstractBuilder<ItemType> builder,
                                     final double minConfidence) {
            super(builder);
            configuration.setGenerateRules(true);
            minConfidence(minConfidence);
        }
        private RuleGeneratorBuilder(@NotNull final AbstractBuilder<ItemType> builder,
                                     final int ruleCount) {
            super(builder);
            configuration.setGenerateRules(true);
            ruleCount(ruleCount);
        }
        @NotNull
        public final RuleGeneratorBuilder<ItemType> minSupport(final double minSupport) {
            configuration.setMinSupport(minSupport);
            return this;
        }
        @NotNull
        public final RuleGeneratorBuilder<ItemType> maxSupport(final double maxSupport) {
            configuration.setMaxSupport(maxSupport);
            return this;
        }
        @NotNull
        public final RuleGeneratorBuilder<ItemType> supportDelta(final double supportDelta) {
            configuration.setSupportDelta(supportDelta);
            return this;
        }
        @NotNull
        public final RuleGeneratorBuilder<ItemType> frequentItemSetCount(
                final int frequentItemSetCount) {
            configuration.setFrequentItemSetCount(frequentItemSetCount);
            return this;
        }
        @NotNull
        public final RuleGeneratorBuilder<ItemType> minConfidence(final double minConfidence) {
            configuration.setMinConfidence(minConfidence);
            return this;
        }
        @NotNull
        public final RuleGeneratorBuilder<ItemType> maxConfidence(final double maxConfidence) {
            configuration.setMaxConfidence(maxConfidence);
            return this;
        }

        @NotNull
        public final RuleGeneratorBuilder<ItemType> confidenceDelta(final double confidenceDelta) {
            configuration.setConfidenceDelta(confidenceDelta);
            return this;
        }
        @NotNull
        public final RuleGeneratorBuilder<ItemType> ruleCount(final int ruleCount) {
            configuration.setRuleCount(ruleCount);
            return this;
        }

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Apriori.class);

    private final Configuration configuration;

    private final FrequentItemSetMinerTask<ItemType> frequentItemSetMinerTask;

    private final AssociationRuleGeneratorTask<ItemType> associationRuleGeneratorTask;


    public Apriori(@NotNull final Configuration configuration) {
        this(configuration, new FrequentItemSetMinerTask<>(configuration),
                new AssociationRuleGeneratorTask<>(configuration));
    }


    public Apriori(@NotNull final Configuration configuration,
                      @NotNull final FrequentItemSetMinerTask<ItemType> frequentItemSetMinerTask,
                      @NotNull final AssociationRuleGeneratorTask<ItemType> associationRuleGeneratorTask) {
        ensureNotNull(configuration, "The configuration may not be null");
        ensureNotNull(frequentItemSetMinerTask, "The frequent item set miner task may not be null");
        ensureNotNull(associationRuleGeneratorTask,
                "The association rule generator task may not be null");
        this.configuration = configuration;
        this.frequentItemSetMinerTask = frequentItemSetMinerTask;
        this.associationRuleGeneratorTask = associationRuleGeneratorTask;
    }


    @NotNull
    public final Configuration getConfiguration() {
        return configuration;
    }

    @NotNull
    public final Output<ItemType> execute(@NotNull final Iterable<Transaction<ItemType>> iterable) {
        ensureNotNull(iterable, "The iterable may not be null");
        LOGGER.info("Starting Apriori algorithm");
        long startTime = System.currentTimeMillis();
        Map<Integer, TransactionalItemSet<ItemType>> frequentItemSets = frequentItemSetMinerTask
                .findFrequentItemSets(iterable);
        RuleSet<ItemType> ruleSet = null;

        if (configuration.isGeneratingRules()) {
            ruleSet = associationRuleGeneratorTask.generateAssociationRules(frequentItemSets);
        }

        FrequentItemSets<ItemType> sortedItemSets = new FrequentItemSets<>(
                Comparator.reverseOrder());
        frequentItemSets.values().forEach(x -> sortedItemSets.add(new ItemSet<>(x)));
        long endTime = System.currentTimeMillis();
        Output<ItemType> output = new Output<>(configuration, startTime, endTime, sortedItemSets,
                ruleSet);
        LOGGER.info("Apriori algorithm completed after {} milliseconds", output.getRuntime());
        return output;
    }

}