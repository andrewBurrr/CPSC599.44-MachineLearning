package ca.ucalgary.rules599.Training;

import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.AccidentAttribute;
import ca.ucalgary.rules599.model.AccidentData;
import ca.ucalgary.rules599.model.Population;
import ca.ucalgary.rules599.rules.*;



import java.io.*;
import java.util.List;
import java.util.Map;

public class EvolutionaryAlgorithm {

private Apriori.Configuration configuration;



    public EvolutionaryAlgorithm(Apriori.Configuration trainerConfig){
        this.configuration=trainerConfig;
    }

    public  Boolean Preprocessor(String inputFile, String outfile, float injuryWeight){
     try {
         List<AccidentData> preprocessor = new Preprocessor().processInitialData(new File(inputFile),outfile,injuryWeight);
        } catch (Exception e) {
            System.out.println("Error initializing stream");
        }
        return true;
    }

    public  Boolean processor(String fileName, String outFile, String knownFile,Map<Integer, TransactionalItemSet<AccidentAttribute>> frequentItemSet, int popSize, float injuryWeight){
        try {

            // run preProcessing first
            if(Preprocessor(fileName, outFile, injuryWeight)){

                GAImplementation gaImplementation;
                Population population = new Population();
                population.initializePopulation(new Processor().findnItemFrequentItemSets(fileName, configuration.getMinSupport(), configuration.getFrequentItemSetCount(), popSize), popSize);
                gaImplementation = new GAImplementation(population);
                String absolutePath = new File(outFile).getAbsolutePath();
                String reportOutput = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator)) + "report.txt";
                Output<AccidentAttribute> output = gaImplementation.outputSortedRules(outFile, reportOutput, knownFile, population.individuals, configuration, configuration.getMinSupport());
            }
        } catch (Exception e) {
            System.out.println("Error initializing stream");
        }
        return true;
    }



}
