package ca.ucalgary.rules599;

import ca.ucalgary.rules599.Training.EvolutionaryAlgorithm;
import ca.ucalgary.rules599.config.TrainerConfig;
import ca.ucalgary.rules599.util.Logger599;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class Rules599Application implements CommandLineRunner {

    private static final String PARSER = "\\s*(?<verbose>(?:-v|--verbose)\\s(?:[0-7]))\\s(?:(?<config>-c|--config)|(?:(?<input>(?:-i|--input)\\s(?:\\S+\\.txt))\\s(?<output>(?:-o|--output)\\s(?:\\S+\\.txt))\\s(?<weights>(?:-w|--weights)(?:\\s+(?:[1-9][0-9]*))+)))\\s*";
    /** {@link String} regular expression to match verbose flag and capture groups.*/
    private static final String VERBOSE = "(?<verbose>(?:-v|--verbose)\\s(?:[0-7]))";
    /** {@link String} regular expression to match config flag.*/
    private static final String CONFIG = "(?<config>-c|--config)";
    /** {@link String} regular expression to match input flag and capture filename group*/
    private static final String INPUT = "(?<input>(?:-i|--input)\\s(?:\\S+\\.txt))";
    /** {@link String} regular expression to match output flag and capture filename group*/
    private static final String OUTPUT = "(?<output>(?:-o|--output)\\s(?:\\S+\\.txt))";
    /** {@link String} regular expression to match weights and capture number list group*/
    private static final String WEIGHTS = "(?<weights>(?:-w|--weights)(?:\\s+(?:[1-9][0-9]*))+)";
    private static Logger599 LOG = new Logger599(Rules599Application.class.getName());

    @Autowired
    TrainerConfig trainerConfig;


    public static void main(String[] args) {
        LOG.info("STARTING Rules599 APPLICATION");
        SpringApplication.run(Rules599Application.class, args);
        LOG.info("Rules599 APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : Rules599Application");
        LinkedList<?> taskList;
        if (args.length < 3) {
            LOG.debug("Usage: java -jar AppName -v level [-c | -i input -o output -w weights]");
            System.exit(0);
        } else {
            HashMap<String,String> argParse = parseArgs(args);
            taskList = (argParse.get("config") == null) ? runSettings(argParse.get("input"), argParse.get("output"), argParse.get("weights")) : runManySettings();
            LOG.info("Begin execution flow of tasks:");
            for (Object task : taskList) {
                LOG.info("Training Instance ["+task.toString()+"]");
                boolean result = new EvolutionaryAlgorithm(trainerConfig).processTrainer(task);
            }
        }
    }


    /**
     * Estimated difficulty/time: Easy / 0.5 Hours
     * TODO load single configuration
     * @return a list of one element containing the configuration settings for a single training model
     */
    private static LinkedList<?> runSettings(String input, String output, String weights) {
        LOG.info("Entering runSettings()");
        LinkedList<String> result = new LinkedList<>();
        result.add("test string");
        LOG.info("Exiting runSettings() with " +  result);
        return result;
    }

    /**
     * Estimated difficulty/time: Easy / 0.5 Hours
     * TODO load many simulation configurations from config file
     * Note: will likely need to do a similar parsing step as parseArgs function to configure parameters
     * @return a list of many configurations for a training model
     */
    private static LinkedList<?> runManySettings() {
        // TODO setup file reader
        LOG.info("Entering runManySettings()");
        LinkedList<?> result = new LinkedList<>();
        LOG.info(" Exiting runManySettings() with " + result);
        return result;
    }



    private static HashMap<String,String> parseArgs(String[] args) {
        Pattern pattern = Pattern.compile("^(?:\\s*"+VERBOSE+"\\s(?:"+CONFIG+"|(?:"+INPUT+"\\s"+OUTPUT+"\\s"+WEIGHTS+"))\\s*)$");
        Matcher matcher = pattern.matcher(String.join(" ", args));
        HashMap<String,String> matches = new HashMap<>();
        String[] group;
        String key = "";
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) == null) continue;
                group = matcher.group(i).split("\\s");
                switch (group[0]) {
                    case "-c": case "--config"  : key = "config"; break;
                    case "-i": case "--input"   : key = "input" ; break;
                    case "-o": case "--output"  : key = "output"; break;
                    case "-v": case "--verbose" : key = "verbose"; break;
                    case "-w": case "--weights" : key = "weights"; break;
                }
                if (group.length < 2)
                    matches.put(key, "true");
                else if (group.length > 2)
                    matches.put(key, String.join(" ", (String[]) Arrays.copyOfRange(group,1,group.length-1)));
                else
                    matches.put(key, group[1]);
            }
        }
        for (String mapKey : matches.keySet()) LOG.info("Arg[" + mapKey + "]: " + matches.get(mapKey));
        LOG.info("Arguments Parsed");
        return matches;
    }
}
