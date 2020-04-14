package ca.ucalgary.rules599;

import ca.ucalgary.rules599.Training.EvolutionaryAlgorithm;
import ca.ucalgary.rules599.config.TrainerConfig;
import ca.ucalgary.rules599.rules.Apriori;
import ca.ucalgary.rules599.util.Logger599;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static boolean runPreProcessor;
    private static boolean runProcessor;
    private static boolean runPostProcessor;

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
        HashMap<String,String> argParse = parseArgs(args);
        if (args.length < 3) {
            LOG.debug("Usage: java -jar AppName -v level [-c | -i input -o output -w weights]");
            System.exit(0);
        }
                LOG.info("Begin execution flow of tasks:");
                LOG.info("Training Instance ");
                String inputFile = argParse.get("input") == null ? trainerConfig.getInputFile(): argParse.get("input");
                String outFile = argParse.get("output") == null ? trainerConfig.getOutputFile(): argParse.get("output");
                String configFile = argParse.get("config") == null ? trainerConfig.getConfigFile(): argParse.get("config");
                float injuryWeight = argParse.get("weights") == null ? 1: Float.parseFloat(argParse.get("weights"));
                Apriori.Configuration configuration = getConfigurationFromFile(configFile);
                if(runPreProcessor){
                    new EvolutionaryAlgorithm(configuration).Preprocessor(inputFile, outFile,injuryWeight);
                }else if (runProcessor){
                    new EvolutionaryAlgorithm(configuration).processor(inputFile, outFile, null); //Need to change this to pass in Existing Knowledge
                }else if (runPostProcessor){
                    //boolean result = new EvolutionaryAlgorithm(configuration).Preprocessor(inputFile, outFile);
                }
                else{
                    LOG.error("Usage: java -jar AppName [-pre -pro -post] -v level [-c config -i input -o output -w weights]");
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
        HashMap<String,String> matches = new HashMap<>();

            for (int i = 0; i <= args.length-1; i++) {
                switch (args[i].toLowerCase()) {
                    case "-c": case "--config"  : matches.put("config",args[i+1].toLowerCase()); break;
                    case "-i": case "--input"   : matches.put("input",args[i+1].toLowerCase()); break;
                    case "-o": case "--output"  : matches.put("output",args[i+1].toLowerCase()); break;
                    case "-v": case "--verbose" : matches.put("verbose",""); break;
                    case "-w": case "--weights" : matches.put("weights",args[i+1].toLowerCase()); break;
                    case "-pre": case "preprocessor": runPreProcessor=true; break;
                    case "-pro": case "processor":  runProcessor=true; break;
                    case "-post": case "postprocessor": runPostProcessor=true; break;
                    case "-all": runPreProcessor=true; runProcessor=true; runPostProcessor=true; break;
                    case "-help":
                        System.out.println();
                        System.out.println("rules599");
                        System.out.println();
                        System.out.println("-c   --config           Path to the Configuration for the system");
                        System.out.println("-i   --input            Path to the input file");
                        System.out.println("-o   --output           Path to the Output file");
                        System.out.println("-w   --output           Path to the Output file");
                        System.out.println("-pre --weights          Weight assigned.");
                        System.out.println("-pro --processor        Operation is Processing only.");
                        System.out.println("-post --postprocessor   Operation is Postprocessing only.");
                        System.out.println("-all                    Operation Perform all Pre-Processing, processing and Post Processing.");
                        System.out.println("-help                   Show this help.");
                        System.out.println();
                        System.exit(0);
                }
            }

        for (String mapKey : matches.keySet()) LOG.info("Arg[" + mapKey + "]: " + matches.get(mapKey));
        LOG.info("Arguments Parsed");
        return matches;
    }


    private Apriori.Configuration getConfigurationFromFile(String fileName){
        Yaml yaml = new Yaml();
        Apriori.Configuration config = new Apriori.Configuration();
        try( InputStream in = Files.newInputStream(Paths.get(fileName))) {
            config = yaml.loadAs( in, Apriori.Configuration.class );
            System.out.println( config.toString() );
        }catch (IOException e){
            LOG.error(e.getMessage());
        }
        return config;
    }

}
