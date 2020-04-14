package ca.ucalgary.rules599.model;

import ca.ucalgary.rules599.datastructure.TransactionalItemSet;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Population {

    public Map<Integer, TransactionalItemSet<AccidentAttribute>> individuals = new LinkedHashMap<>();
    public double firstSupport = 0;
    public TransactionalItemSet<AccidentAttribute> firstItemSet= new TransactionalItemSet<>();
    public double secondSupport = 0;
    public TransactionalItemSet<AccidentAttribute> secondItemSet= new TransactionalItemSet<>();
    public int maxPopulation =0;

    //Initialize population
    public void initializePopulation(Map<Integer, TransactionalItemSet<AccidentAttribute>> individuals, int maxPopulation) {
            this.individuals = individuals;
            this.maxPopulation =maxPopulation;
    }

    //Get the fittest individual
    public  TransactionalItemSet<AccidentAttribute> getFittest() {

        Iterator<? extends ItemSet<AccidentAttribute>> iterator = this.individuals.values().iterator();
        while (iterator.hasNext()) {
            TransactionalItemSet<AccidentAttribute> itemSet = (TransactionalItemSet<AccidentAttribute>)iterator.next();
            if (firstSupport < itemSet.getSupport()){
                firstSupport=itemSet.getSupport();
                firstItemSet =itemSet;
            }
        }

        return firstItemSet;
    }

    //Get the second most fittest individual
    public ItemSet<AccidentAttribute> getSecondFittest() {
        Iterator<? extends ItemSet<AccidentAttribute>> iterator = this.individuals.values().iterator();

        while (iterator.hasNext()) {
            TransactionalItemSet<AccidentAttribute> itemSet = (TransactionalItemSet<AccidentAttribute>)iterator.next();
            if (firstSupport > itemSet.getSupport() && itemSet.getSupport() > secondSupport){
                secondSupport=itemSet.getSupport();
                secondItemSet =itemSet;
            }
        }
        return secondItemSet;
    }

    //Get index of least fittest individual
    public TransactionalItemSet<AccidentAttribute> getLeastFittestIndex() {
        double minSupport = 0;
        Iterator<? extends ItemSet<AccidentAttribute>> iterator = this.individuals.values().iterator();
        TransactionalItemSet<AccidentAttribute> itemSet=null;
        while (iterator.hasNext()) {
            itemSet = (TransactionalItemSet<AccidentAttribute>)iterator.next();
            minSupport=itemSet.getSupport();
            if (minSupport < itemSet.getSupport()){
                minSupport=itemSet.getSupport();

            }
        }


        return itemSet ;
    }


    //Get index of least fittest individual
    public double getMinSupport() {
        double minSupport = 0;
        Iterator<? extends ItemSet<AccidentAttribute>> iterator = this.individuals.values().iterator();
        TransactionalItemSet<AccidentAttribute> itemSet=null;
        while (iterator.hasNext()) {
            itemSet = (TransactionalItemSet<AccidentAttribute>)iterator.next();
            minSupport=itemSet.getSupport();
            if (minSupport < itemSet.getSupport()){
                minSupport=itemSet.getSupport();

            }
        }


        return minSupport;
    }


    public void setMaxPopulation(int maxPop){
        this.maxPopulation=maxPop;
    }

}