package ca.ucalgary.rules599.Training;

import ca.ucalgary.rules599.Rules599Application;
import ca.ucalgary.rules599.config.TrainerConfig;
import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.AccidentAttributeHolder;
import ca.ucalgary.rules599.model.IAccidentData;
import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.model.AccidentAttribute;
import ca.ucalgary.rules599.modules.AssociationRuleGeneratorModule;
import ca.ucalgary.rules599.modules.FrequentItemSetMinerModule;
import ca.ucalgary.rules599.rules.Apriori;
import ca.ucalgary.rules599.rules.Output;
import ca.ucalgary.rules599.rules.RuleSet;
import ca.ucalgary.rules599.task.AssociationRuleGeneratorTask;
import ca.ucalgary.rules599.task.FrequentItemSetMinerTask;
import ca.ucalgary.rules599.util.DataIterator;
import ca.ucalgary.rules599.util.Logger599;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Processor {
    Apriori.Configuration configuration;

    @Autowired
    TrainerConfig trainerConfig;

    private static Logger599 LOG = new Logger599(Processor.class.getName());
    public RuleSet generateRules(String fileName, double support, @NotNull final double minConfidence){

        AssociationRuleGeneratorModule<AccidentAttribute> associationRuleGenerator = new AssociationRuleGeneratorModule<>();
        Map<Integer, TransactionalItemSet<AccidentAttribute>> map = findFrequentItemSets(fileName, support);
        RuleSet<AccidentAttribute> ruleSet = associationRuleGenerator.generateAssociationRules(map, minConfidence);
        return ruleSet;
    }

    public Output<AccidentAttribute> generateApriori(String fileName, Map<Integer, TransactionalItemSet<AccidentAttribute>> frequentItemSet, Apriori.Configuration configuration){
        FrequentItemSetMinerTask<AccidentAttribute> frequentItemSetMinerTask = new FrequentItemSetMinerTask<>(configuration);
        AssociationRuleGeneratorTask<AccidentAttribute> associationRuleGeneratorTask = new AssociationRuleGeneratorTask<>(configuration);

        Apriori<AccidentAttribute> apriori = new Apriori<>(configuration, frequentItemSetMinerTask,associationRuleGeneratorTask);
        Output<AccidentAttribute> output = apriori.execute(() -> new DataIterator(new File(fileName),2,true));
        return output;
    }


    public Map<Integer, TransactionalItemSet<AccidentAttribute>> findFrequentItemSets(@NotNull final String fileName,
                                                                                       final double minSupport) {
        File inputFile = new  File(fileName);
        FrequentItemSetMinerModule<AccidentAttribute> frequentItemSetMiner = new FrequentItemSetMinerModule<>();
        Map<Integer, TransactionalItemSet<AccidentAttribute>> frequentItemSets = frequentItemSetMiner
                .findFrequentItemSets(() -> new DataIterator(inputFile,2,true), minSupport);
        return frequentItemSets;
    }



    public List<Map<String, int[]>> findGeneticFrequentItemSets(@NotNull final String fileName,final double minSupport,
                                                                                             @NotNull final String attributeFileName) {

        AccidentAttributeHolder holder = readAttributeValues(attributeFileName);

        File inputFile = new  File(fileName);
        FrequentItemSetMinerModule<AccidentAttribute> frequentItemSetMiner = new FrequentItemSetMinerModule<>();
        Map<Integer, TransactionalItemSet<AccidentAttribute>> frequentItemSets = frequentItemSetMiner
                .findFrequentItemSets(() -> new DataIterator(inputFile,2,true), minSupport);

        return holder.createList(frequentItemSets);

    }

    private Map<Integer, ItemSet<IAccidentData>> createFrequentItemSets(
            @NotNull final List<IAccidentData> frequentItemSet, @NotNull final double[] supports) {
        Map<Integer, ItemSet<IAccidentData>> map = new HashMap<>();
        int index = 0;

        for (IAccidentData frequentItem : frequentItemSet) {
            ItemSet<IAccidentData> itemSet = new ItemSet<>();
            double support = supports[index];
            itemSet.setSupport(support);
            itemSet.add(frequentItem);
            map.put(itemSet.hashCode(), itemSet);
            index++;
        }

        return map;
    }

    private AccidentAttributeHolder readAttributeValues(String fileName){
        Yaml yaml = new Yaml();
        final AccidentAttributeHolder holder =new AccidentAttributeHolder();
        try ( InputStream in = Files.newInputStream(Paths.get(fileName))) {
            Iterable<Object> itr = yaml.loadAll(in);
            for (Object o : itr) {
              System.out.println("Loaded object type:" + o.getClass());
                Map<String, List<String>> map1 = (Map<String, List<String>>) o;
                map1.entrySet().forEach((e) -> holder.updateFields(e.getKey(),e.getValue()));
            }
       } catch (IOException e){
        LOG.error(e.getMessage());
    }
        return holder;
    }





}
