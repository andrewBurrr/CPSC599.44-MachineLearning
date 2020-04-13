package ca.ucalgary.rules599.Training;

import ca.ucalgary.rules599.model.AccidentData;
import ca.ucalgary.rules599.model.AccidentFilter;
import ca.ucalgary.rules599.util.FileUtil;
import ca.ucalgary.rules599.util.Logger599;

import java.io.*;
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
    public List<AccidentData> processInitialData(File file, String outfile){

            List<AccidentData> inputList = new ArrayList<>();
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

        List<AccidentData> driverList = new ArrayList<>(list).stream().filter(d -> (d.getPerson().getP_PSN().equals("11"))).collect(Collectors.toList());
        Map<AccidentFilter, Long> filteredList = list.stream().collect(Collectors.groupingBy(AccidentData::getfilter,counting()));
        List<AccidentData> accidentDataList = driverList.stream()
                .map(c -> extendAccidentData.apply(c,filteredList))
                .collect(Collectors.toList());
        try {

            //call Ben's function
            List<AccidentData> accidentDataList1 = accidentDataList.stream().map(d -> d.getRushHour()).collect(Collectors.toList());
            List<String> stringList = accidentDataList1.stream().map(x ->  x.getString()).collect(Collectors.toList());
            FileUtil.createFile(outfile,stringList);

            //CsvWriter.OpenCSVWriter(accidentDataList,"src/test/resources/drivers.csv");
            //CsvWriter.OpenCSVWriter(new ArrayList<>(filteredList.keySet()),"src/test/resources/filtered.csv");

            LOG.info("PreProcessing Completed");

        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return accidentDataList;
    }


    BiFunction<AccidentData,Map<AccidentFilter,Long>,AccidentData> extendAccidentData = (c,b) ->{
        Optional<AccidentFilter> personCount = b.keySet().stream()
                .filter(c::applyFilter)
                .findFirst();
        if(personCount.isPresent()) {
            // # of injuries + k (# of deaths)/ total passengers(incl. driver) s.t. k >= 2
            // parameterize the weighting of deaths in the severity score to change rules created
            return new AccidentData(c,b.get(personCount.get())+1L);  //add the driver
        }else {
            return new AccidentData(c,1L); //only the driver in the car
        }

    };

}


