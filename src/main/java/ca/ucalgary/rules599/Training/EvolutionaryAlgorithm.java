package ca.ucalgary.rules599.Training;

import ca.ucalgary.rules599.config.TrainerConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class EvolutionaryAlgorithm {
    private TrainerConfig trainerConfig;

    @Autowired
    public EvolutionaryAlgorithm(TrainerConfig trainerConfig){
        this.trainerConfig=trainerConfig;
    }

    public static Boolean processTrainer(Object tasklist){
        return true;
    }


}
