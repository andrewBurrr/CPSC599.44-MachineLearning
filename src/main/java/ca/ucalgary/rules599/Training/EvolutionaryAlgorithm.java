package ca.ucalgary.rules599.Training;

import ca.ucalgary.rules599.config.TrainerConfig;
import ca.ucalgary.rules599.datastructure.TransactionalItemSet;
import ca.ucalgary.rules599.model.AccidentAttribute;
import ca.ucalgary.rules599.model.AccidentData;
import ca.ucalgary.rules599.model.ItemSet;
import ca.ucalgary.rules599.model.Population;
import ca.ucalgary.rules599.rules.*;
import org.springframework.beans.factory.annotation.Autowired;


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

    public  Boolean processor(String fileName, String outFile, Map<Integer, TransactionalItemSet<AccidentAttribute>> frequentItemSet){
        try {
            Output<AccidentAttribute> output = new Processor().generateApriori(fileName,frequentItemSet, configuration);
            FileOutputStream f = new FileOutputStream(new File(outFile));
            ObjectOutputStream o = new ObjectOutputStream(f);

            GAImplementation gaImplementation;
            Population population =  new Population();
            population.initializePopulation(new Processor().findnItemFrequentItemSets(fileName, configuration.getMinSupport(),1, 5),5);
            gaImplementation = new GAImplementation(population);
            Output<AccidentAttribute> actualData =gaImplementation.outputSortedRules(fileName, outFile, population.individuals, configuration,0.8f);
            


            for(AssociationRule ruleSet : output.getRuleSet()) {
                o.writeObject(ruleSet.toString());
            }
            // Write objects to file

            for(ItemSet itemSet : output.getFrequentItemSets()) {
                o.writeObject(itemSet.toString());
            }

            o.close();
            f.close();

        } catch (
                FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
        return true;
    }



}
