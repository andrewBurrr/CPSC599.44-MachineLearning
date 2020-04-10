package ca.ucalgary.rules599.model;


import ca.ucalgary.rules599.Training.Visibility_PreProcessing;
import ca.ucalgary.rules599.model.RushHour_PreProcessing;
import ca.ucalgary.rules599.util.CsvWriter;
import ca.ucalgary.rules599.util.Logger599;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccidentData implements IAccidentData{
    Collision collision;
    Person person;
    Vehicle vehicle;
    AggregateData aggregateData;


    @Override
    public final int compareTo(@NotNull final Item o) {
        return toString().compareTo(o.toString());
    }
    public AccidentData(AccidentData accidentData, Long kount){
        Logger599 LOG = new Logger599(AccidentData.class.getName());
     try{
        this.collision=accidentData.getCollision();
        this.person=accidentData.getPerson();
        this.vehicle=accidentData.getVehicle();
        this.aggregateData = AggregateData.builder().visibility(0).trafficScore(0).passengerPositioning(0).noOfPerson(Long.valueOf(kount).intValue()).build();
    } catch (Exception e) {
     LOG.info("Error Was " + e);
    }
    }

    @Override
    public AccidentData getAccidentDatafromCSV(String csvString){
        String cvsSplitBy = ",";
        String[] accdata = csvString.split(cvsSplitBy);
        Collision collision = Collision.builder().C_YEAR(accdata[0]).C_MNTH(accdata[1]).C_WDAY(accdata[2]).C_HOUR(accdata[3]).C_SEV(accdata[4]).C_VEHS(accdata[5])
                .C_CONF(accdata[6]).C_RCFG(accdata[7]).C_WTHR(accdata[8]).C_RSUR(accdata[9]).C_RALN(accdata[10]).C_TRAF(accdata[11]).build();
        Person person = Person.builder().P_ID(accdata[15]).P_SEX(accdata[16]).P_AGE(accdata[17]).P_PSN(accdata[18]).P_ISEV(accdata[19]).P_SAFE(accdata[20]).P_USER(accdata[21]).build();
        Vehicle vehicle = Vehicle.builder().V_ID(accdata[12]).V_TYPE(accdata[13]).V_YEAR(accdata[14]).build();
        return AccidentData.builder().collision(collision).person(person).vehicle(vehicle).build();
    }



    public AccidentFilter getfilter(){
        return AccidentFilter.builder().collision(getCollision()).vehicle(getVehicle()).build();
    }

    public boolean applyFilter(AccidentFilter accidentFilter){
        return getCollision() == accidentFilter.getCollision() && getVehicle() == accidentFilter.getVehicle();
    }

    public Boolean findPassengers(AccidentFilter accidentFilter){
        return getCollision() == accidentFilter.getCollision() && getVehicle() == accidentFilter.getVehicle() && (getPerson().getP_USER().equals ("1") || getPerson().getP_USER().equals("2"));
    }

    public String getString(){
            return "NoOfPerson(" + Integer.toString(aggregateData.getNoOfPerson())+ "),Visibility(" + Integer.toString(aggregateData.getVisibility())+ "),TrafficScore(" +
                    Integer.toString(aggregateData.getTrafficScore()) + "),PassengerPositioning(" + Integer.toString(aggregateData.getPassengerPositioning())+ "),C_YEAR(" +collision.getC_YEAR()+ "),C_MNTH(" + collision.getC_MNTH()+ "),C_WDAY(" +collision.getC_WDAY() + "),C_HOUR(" + collision.getC_HOUR() + "),C_SEV(" + collision.getC_SEV() + "),C_VEHS(" + collision.getC_VEHS()
                            + "),C_CONF(" + collision.getC_CONF() + "),C_RCFG(" + collision.getC_RCFG() + "),C_WTHR(" + collision.getC_WTHR() + "),C_RSUR(" + collision.getC_RSUR() + "),C_RALN(" + collision.getC_RALN()
                            + "),C_TRAF(" + collision.getC_TRAF()+ "),P_SEX(" + person.getP_SEX() + "),P_AGE(" + person.getP_AGE() + "),P_PSN(" + person.getP_PSN() + "),P_ISEV(" + person.getP_ISEV()
                    + "),P_SAFE(" + person.getP_SAFE() + "),P_USER(" +person.getP_USER() + "),V_TYPE(" + vehicle.getV_TYPE() + "),V_YEAR(" +vehicle.getV_YEAR();

    }


    public AccidentData getRushHour(){

        AggregateData aggregateData = getAggregateData();
        int visibility = Visibility_PreProcessing.evaluate_visibility(getCollision().getC_YEAR(),getCollision().getC_MNTH(),getCollision().getC_WDAY(),getCollision().getC_HOUR(),getCollision().getC_WTHR(),getCollision().getC_RALN());
        boolean rush_hour =RushHour_PreProcessing.evaluate_rush_hour(getCollision().getC_YEAR(),getCollision().getC_MNTH(),getCollision().getC_WDAY(),getCollision().getC_HOUR());
        aggregateData.setVisibility(visibility);
        aggregateData.setTrafficScore(Boolean.compare(rush_hour,false));
        setAggregateData(aggregateData);

        return AccidentData.builder().aggregateData(aggregateData).collision(getCollision()).vehicle(getVehicle()).person(getPerson()).build();
    }
}
