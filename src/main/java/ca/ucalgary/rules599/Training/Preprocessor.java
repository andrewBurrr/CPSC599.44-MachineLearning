package ca.ucalgary.rules599.Training;

import ca.ucalgary.rules599.model.AccidentData;
import ca.ucalgary.rules599.model.AccidentFilter;
import ca.ucalgary.rules599.model.Person;
import ca.ucalgary.rules599.model.Vehicle;
import ca.ucalgary.rules599.util.CsvWriter;
import ca.ucalgary.rules599.util.FileUtil;
import ca.ucalgary.rules599.util.Logger599;
import org.apache.commons.collections.CollectionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;


/**
 * Data PreProcessor for Input file
 *
 *
 *
 *
 *
 */
public class Preprocessor {
    private static Logger599 LOG = new Logger599(Preprocessor.class.getName());
    private float injuryWeight;
    public List<AccidentData> processInitialData(File file, String outfile, float injuryWeight){
            this.injuryWeight=injuryWeight;
        List<AccidentData> inputList = new LinkedList<>();
            try{
             InputStream inputFS = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
                // skip the header of the csv
                inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
                br.close();
            } catch (IOException e) {

            }
            return getUpdatedDriverList(inputList, outfile);
        }

    private Function<String, AccidentData> mapToItem = (line) -> {
        AccidentData item = new AccidentData().getAccidentDatafromCSV(line);
        return item;
    };


    private List<AccidentData> getUpdatedDriverList(List<AccidentData> list, String outfile) {
        if (list.size() < 2) { // obvious case.
            return list;
        }
        List<AccidentData> accidentDataList1 = new ArrayList<>();

        List<AccidentData> driverList = new ArrayList<>(list).stream().filter(d -> (d.getPerson().getP_PSN().equals("11"))).collect(Collectors.toList());
        Map<AccidentFilter, Long> filteredList = list.stream().collect(Collectors.groupingBy(AccidentData::getfilter,counting()));
        List<AccidentData> accidentDataList = driverList.stream()
                .map(c -> extendAccidentData.apply(c,list))
                .collect(Collectors.toList());
        try {

            //call Ben's function
            accidentDataList1 = accidentDataList.stream().map(d -> d.getRushHour()).collect(Collectors.toList());
            List<String> stringList = accidentDataList1.stream().map(x ->  x.getString()).collect(Collectors.toList());
            FileUtil.createFile(outfile,stringList);

            CsvWriter.OpenCSVWriter(accidentDataList1,outfile);
            //CsvWriter.OpenCSVWriter(new ArrayList<>(filteredList.keySet()),"src/test/resources/filtered.csv");

            LOG.info("PreProcessing Completed");

        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return accidentDataList1;
    }


    BiFunction<AccidentData,List<AccidentData>,AccidentData> extendAccidentData = (c,b) ->{
      List<AccidentData> newList = b.stream()
                .map(currValue -> {
                   Person newPerson = currValue.getPerson();
                   newPerson.setP_ID("0");
                   Vehicle newVehicle = currValue.getVehicle();
                   newVehicle.setV_ID("0");
                    return new AccidentData().builder().aggregateData(null).person(newPerson).vehicle(newVehicle)
                            .collision(currValue.getCollision()).build();

                }).collect(Collectors.toList());

        Person newPerson = c.getPerson();
        newPerson.setP_ID("0");
        Vehicle newVehicle = c.getVehicle();
        newVehicle.setV_ID("0");
        AccidentData newFilter = AccidentData.builder().person(newPerson).vehicle(newVehicle).collision(c.getCollision()).build();
        List<AccidentData> personCount = newList.stream().filter(newFilter::applyFilter).collect(Collectors.toList());

       if(!CollectionUtils.isEmpty(personCount)) {
            // # of injuries + k (# of deaths)/ total passengers(incl. driver) s.t. k >= 2
            // parameterize the weighting of deaths in the severity score to change rules created
           int noInjuries  = personCount.stream().filter(e -> e.getCollision().getC_SEV().equalsIgnoreCase("2")).collect(Collectors.toList()).size();
           int noDeaths  = personCount.stream().filter(e -> e.getCollision().getC_SEV().equalsIgnoreCase("1")).collect(Collectors.toList()).size();
           int noofPassenger = personCount.size();

           String crashSeverity = getCrashSeverity((injuryWeight*noInjuries +noDeaths)/noofPassenger);
           return new AccidentData(c,personCount.size(),crashSeverity);  //add the driver

        }else {
            return null; //Should never happen

        }

    };

private String getCrashSeverity(float computedValue){

    if (isBetween(computedValue, 0, 0.29f)) {
        return "low";
    } else if (isBetween(computedValue, 0.3f, 0.59f)) {
        return "medium";
    }else if (isBetween(computedValue, 0.6f, 1)) {
        return "high";
    }

return "undefined";
}


    public static boolean isBetween(float x, float lower, float upper) {
        return lower <= x && x <= upper;
    }

}


