package ca.ucalgary.rules599.Training;

import ca.ucalgary.rules599.config.TrainerConfig;
import ca.ucalgary.rules599.model.AccidentData;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class EvolutionaryAlgorithm {
    private TrainerConfig trainerConfig;

    @Autowired
    public EvolutionaryAlgorithm(TrainerConfig trainerConfig){
        this.trainerConfig=trainerConfig;
    }

    public  Boolean processTrainer(Object tasklist){
        try {
            List<AccidentData> preprocessor = new Preprocessor().processInitialData(trainerConfig.getDataFile().getFile());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


}
