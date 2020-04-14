package ca.ucalgary.rules599.Training;

import ca.ucalgary.rules599.datastructure.Pair;
import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.metrics.Confidence;
import ca.ucalgary.rules599.metrics.Fitness;
import ca.ucalgary.rules599.model.*;
import ca.ucalgary.rules599.modules.FrequentItemSetMinerModule;
import ca.ucalgary.rules599.rules.Apriori;
import ca.ucalgary.rules599.rules.AssociationRule;
import ca.ucalgary.rules599.rules.Output;
import ca.ucalgary.rules599.task.AssociationRuleGeneratorTask;
import ca.ucalgary.rules599.task.FrequentItemSetMinerTask;
import ca.ucalgary.rules599.util.DataIterator;
import ca.ucalgary.rules599.util.MyLinkedMap;
import ca.ucalgary.rules599.util.RuleDataIterator;


import java.io.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GAImplementation {
    Population breedingPopulation;
    Population population = new Population();
    ItemSet<AccidentAttribute> fittest;
    ItemSet<AccidentAttribute> secondFittest;
    int generationCount = 0;
    int maxPop;
    double minSupport=0;
    MyLinkedMap<Integer, TransactionalItemSet<AccidentAttribute>> breedingPopulationMap = new MyLinkedMap<>();

    public GAImplementation(Population breedingPopulation){
        this.breedingPopulation=breedingPopulation;
        this.maxPop = breedingPopulation.maxPopulation;
        breedingPopulationMap.putAll(this.breedingPopulation.individuals);
    }


    public Population runGA() {

        while (this.population.maxPopulation < maxPop) {
            ++this.generationCount;
            //Do crossover
            this.crossover();

        }

        //get the least fit 20%, Sort the Population in descending order of support and get the size*.8

        TreeMap<Integer, TransactionalItemSet<AccidentAttribute>> sortedTree = new TreeMap<>(population.individuals);
        TreeMap<Integer, TransactionalItemSet<AccidentAttribute>> lowestPopulation  =  sortedTree.entrySet().stream()
                .limit(new Double(population.individuals.size()*.2).longValue())
                .collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);

        //set minimum Support
        this.minSupport = this.population.getMinSupport();

        //Do mutation
        this.mutation(lowestPopulation);

        System.out.println("\nSolution found in generation " + this.generationCount);
        return population;

    }


    //Crossover
    void crossover() {
        Random rn = new Random();

        //Select 2 random crossover parent from the breeding Population
        int crossOverPoint1 = rn.nextInt(breedingPopulation.individuals.size());
        int crossOverPoint2 = rn.nextInt(breedingPopulation.individuals.size());



        TransactionalItemSet<AccidentAttribute> parent1 = breedingPopulationMap.getValue(crossOverPoint1);
        TransactionalItemSet<AccidentAttribute> parent2 = breedingPopulationMap.getValue(crossOverPoint2);

        TransactionalItemSet<AccidentAttribute> child1 = parent1.cloneMe();
        TransactionalItemSet<AccidentAttribute> child2 = parent2.cloneMe();

        //Swap values among parents
        Iterator<AccidentAttribute> parent1Iterator = parent1.iterator();
        Iterator<AccidentAttribute> parent2Iterator = parent1.iterator();

        while (parent2Iterator.hasNext()) {
            AccidentAttribute predicate = parent2Iterator.next();
            while (parent1Iterator.hasNext()) {
                if (!predicate.getName().equalsIgnoreCase(parent1Iterator.next().getName())){
                    //add the predicate to child 1
                    child1.add(predicate);
                }
            }
        }

        while (parent1Iterator.hasNext()) {
            AccidentAttribute predicate = parent1Iterator.next();
            while (parent2Iterator.hasNext()) {
                if (!predicate.getName().equalsIgnoreCase(parent2Iterator.next().getName())){
                    //add the predicate to child 2
                    child2.add(predicate);
                }
            }
        }


        child1.setSupport(calculateSupport(this.maxPop, child1.size()));
        child2.setSupport(calculateSupport(this.maxPop, child2.size()));

        //add child1 and child2 to the crossPopulation
        addOffspringPopulation(child1);
        addOffspringPopulation(child2);

    }


    //Mutation
    void mutation(Map<Integer, TransactionalItemSet<AccidentAttribute>>  lowestitemsets) {
        Random rn = new Random();

        Iterator<? extends ItemSet<AccidentAttribute>> iterator = lowestitemsets.values().iterator();
        while (iterator.hasNext()) {
            ItemSet<AccidentAttribute> itemSet = iterator.next();
            Iterator<AccidentAttribute> itemSetIterator = itemSet.iterator();
            while (itemSetIterator.hasNext()) {
                AccidentAttribute predicate = itemSetIterator.next();
                //String predicateValue= predicate.getName().replace(")", "").split("[(]")[1];
                //if (Integer.valueOf(predicateValue)== 0 || Integer.valueOf(predicateValue)== 1){
                    //delete value from the predicate from the ItemSet
                    itemSet.remove(predicate);

                    int pointIndex = rn.nextInt(breedingPopulation.individuals.size());
                    ItemSet<AccidentAttribute> randomAttribute = breedingPopulation.individuals.get((breedingPopulation.individuals.keySet().toArray())[pointIndex]);
                    AccidentAttribute newAttribute = randomAttribute.first(); //It is a One item set, first gets the only attribute
                    int occurrences = itemSet.size();
                    itemSet.setSupport(calculateSupport(population.individuals.size(), occurrences));
                    itemSet.add(newAttribute);
                    if (itemSet.getSupport() >= this.minSupport){
                       break; //stop if the coverage is above min coverage
                    }
            }
        }

    }


    //Replace least fittest individual from most fittest offspring
    void addOffspringPopulation(TransactionalItemSet<AccidentAttribute> itemSet) {

        if (population.maxPopulation<this.maxPop)
        {
            population.individuals.put(itemSet.hashCode(), itemSet);
            ++this.population.maxPopulation;
        }else{
           //Replace least fittest individual from most fittest offspring
            population.individuals.replace(population.getLeastFittestIndex().hashCode(),itemSet);
            ++this.population.maxPopulation;
        }

    }



    private double calculateSupport(final int transactions, final int occurrences) {
        return transactions > 0 ? (double) occurrences / (double) transactions : 0;
    }

    public  List<Pair<ItemSet<AccidentAttribute>,ItemSet<AccidentAttribute>>> importExistingRules(String knownItemFileName){
        List<Pair<ItemSet<AccidentAttribute>,ItemSet<AccidentAttribute>>> inputList = new ArrayList<>();
        try{
            InputStream inputFS = new FileInputStream(knownItemFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            inputList = br.lines().skip(1).map(mapToAttributes).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {

        }
        return inputList;
    }


    public  Map<Integer,TransactionalItemSet<AccidentAttribute>> updatedFrequentItemSets(String knownItemFileName){
        File inputFile = new  File(knownItemFileName);
        FrequentItemSetMinerModule<AccidentAttribute> frequentItemSetMiner = new FrequentItemSetMinerModule<>();
        Map<Integer, TransactionalItemSet<AccidentAttribute>> frequentItemSets = frequentItemSetMiner
                .findFrequentItemSets(() -> new RuleDataIterator(inputFile,2,true), minSupport);


        //merge the Population FrequentItems with the input Ruleset.
        return Stream.concat(frequentItemSets.entrySet().stream(),population.individuals.entrySet().stream()).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public Output<AccidentAttribute> generateApriori(String fileName, String knownItemFileName, Apriori.Configuration configuration){

        FrequentItemSetMinerTask<AccidentAttribute> frequentItemSetMinerTask = new FrequentItemSetMinerTask<>(
                configuration, (iterator, minSupport) -> updatedFrequentItemSets(knownItemFileName));
        AssociationRuleGeneratorTask<AccidentAttribute> associationRuleGeneratorTask = new AssociationRuleGeneratorTask<>(configuration);

        Apriori<AccidentAttribute> apriori = new Apriori<>(configuration, frequentItemSetMinerTask,associationRuleGeneratorTask);
        Output<AccidentAttribute> output = apriori.execute(() -> new DataIterator(new File(fileName),2,true));
        return output;
    }


    public Output<AccidentAttribute> outputSortedRules(String fileName, String knownItemFileName, Apriori.Configuration configuration) {
        Output<AccidentAttribute> output = generateApriori(fileName, knownItemFileName, configuration);
        long countLow = output.getRuleSet().stream().filter(e -> e.getBody().contains(AccidentAttribute.builder().name("low"))).count();
        long countMedium = output.getRuleSet().stream().filter(e -> e.getBody().contains(AccidentAttribute.builder().name("medium"))).count();
        long countHigh = output.getRuleSet().stream().filter(e -> e.getBody().contains(AccidentAttribute.builder().name("high"))).count();
        long countGeneral = output.getRuleSet().stream().filter(e -> !e.getBody().contains(AccidentAttribute.builder().name("high")) && !e.getBody().contains(AccidentAttribute.builder().name("low")) && !e.getBody().contains(AccidentAttribute.builder().name("medium"))).count();

        double overAllConfidence = 0;
        int totalRules = output.getRuleSet().size();

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("Writing Rule configuration File");
        printWriter.printf(output.getConfiguration().toString());
        printWriter.print("\n\n");


        printWriter.print("Writing External rules added");
        printWriter.printf(updatedFrequentItemSets(knownItemFileName).toString());
        printWriter.print("\n\n");


        printWriter.print("Writing Rule Generation Statistics");
        printWriter.print("|Low  | medium  | high | General |");
        printWriter.print(String.valueOf(countLow) + " | " + String.valueOf(countMedium) + " | " + String.valueOf(countHigh) + " | " + String.valueOf(countGeneral) + " | ");
        printWriter.print("\n\n");


        MyLinkedMap<Integer, AssociationRule<AccidentAttribute>> ruleWithFitness = new MyLinkedMap<>();
        MyLinkedMap<Integer, Double> ruleFitness = new MyLinkedMap<>();

        for (AssociationRule<AccidentAttribute> rule : output.getRuleSet()) {
            double confidence = new Confidence().evaluate(rule);
            overAllConfidence = +confidence;
            ruleWithFitness.put(rule.hashCode(), rule);
            ruleFitness.put(rule.hashCode(), new Fitness().evaluate(rule));

        }


        printWriter.printf(updatedFrequentItemSets(knownItemFileName).toString());
        printWriter.print("\n\n");

        //sort the list of fitness and use it for average, min and max
        //breedingPopulationMap.getValue

        TreeMap<Integer, Double> sortedTree = new TreeMap<>(ruleFitness);
        double highestFitness = (double) sortedTree.values().toArray()[0]; //First result
        double lowestFitness = (double) sortedTree.values().toArray()[sortedTree.size() - 1];
        double averageFitness = sortedTree.values().stream().reduce(0d, Double::sum) / sortedTree.size();

        printWriter.print("Writing Rule Result Statistics");
        printWriter.print("|Average Fitness  | Lowest Fitness  | highestFitness | total no of Rules");
        printWriter.print(String.valueOf(averageFitness) + " | " + String.valueOf(lowestFitness) + " | " + String.valueOf(highestFitness) + " | " + String.valueOf(sortedTree.size()) + " | ");
        printWriter.print("\n\n");


        for (int key : sortedTree.keySet()) {
            ruleWithFitness.getValue(key);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append(ruleWithFitness.getValue(key).getBody().toString());
            stringBuilder.append(" (support = ");
            stringBuilder.append(ruleWithFitness.getValue(key).getSupport());
            stringBuilder.append(")");
            stringBuilder.append(",\n");
            stringBuilder.append("]");
            printWriter.print(stringBuilder);
        }

            printWriter.print("Writing Rule Created");
            printWriter.print("\n\n");


        overAllConfidence=overAllConfidence/totalRules;

        printWriter.close();
        return output;
    }



    private Function<String, Pair<ItemSet<AccidentAttribute>,ItemSet<AccidentAttribute>>> mapToAttributes = (line) -> {
        Pair<ItemSet<AccidentAttribute>,ItemSet<AccidentAttribute>> item = new AccidentAttribute().getDatafromCSV(line);
        return item;
    };


}

