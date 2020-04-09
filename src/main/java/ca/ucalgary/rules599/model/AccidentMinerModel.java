package ca.ucalgary.rules599.model;


import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccidentMinerModel implements IAccidentData{
    private String C_YEAR; //Year	19yy-20yy. yy = last two digits of the calendar year. (e.g. 90, 91, 92)
    private String C_MNTH; //Month	01-12. UU=Unknown, XX Jurisdiction does not provide the data
    private String C_WDAY; //Day of week	1-7.  U=Unknown, X Jurisdiction does not provide the data
    private String C_HOUR; // Collision hour	00 - 23. UU=Unknown, XX Jurisdiction does not provide the data
    private String C_SEV; //Collision severity	1-2. U=Unknown, X Jurisdiction does not provide the data
    private String C_VEHS; // Number of vehicles involved in Collision	01-98. 99= 99 or More vehicles involved. UU=Unknown, XX Jurisdiction does not provide the data
    private String C_CONF; // Collision configuration
    private String C_RCFG; //Roadway configuration	01-12. QQ= Choice is other than the preceding values. UU=Unknown, XX Jurisdiction does not provide the data
    private String C_WTHR; // Weather condition	1-7. Q= Choice is other than the preceding values. U=Unknown, X= Jurisdiction does not provide the data
    private String C_RSUR; //Road surface	1-9. Q= Choice is other than the preceding values. U=Unknown, X= Jurisdiction does not provide the data
    private String C_RALN; //Road alignment	1-6. Q= Choice is other than the preceding values. U=Unknown, X= Jurisdiction does not provide the data
    private String C_TRAF; // Traffic control	01-18. QQ= Choice is other than the preceding values. UU=Unknown, XX=Jurisdiction does not provide the data

    private String P_SEX; //Person sex	F=Female, M=Male, N= Data Element not applicable, U=Unknown, X=Jurisdiction does not provide this data
    private String P_AGE; //Person age
    private String P_PSN;  //Person position
    private String P_ISEV; //Medical treatment required
    private String P_SAFE; //Safety device used
    private String P_USER; //Road user class

    private String V_TYPE; //Vehicle type	01-23. NN=Data not Applicable. QQ= Choice is other than the preceding values. UU=Unknown, XX=Jurisdiction does not provide the data
    private String V_YEAR; //Vehicle model year	19yy-20yy. NNNN= Data Element not Applicable. UUUU=Unknown, XXXX=Jurisdiction does not provide the data

    private int noOfPerson;
    private int visibility;
    int trafficScore;
    int passengerPositioning;


    @Override
    public final int compareTo(@NotNull final Item o) {
        return toString().compareTo(o.toString());
    }


    @Override
    public IAccidentData getAccidentDatafromCSV(String csvString){
        String cvsSplitBy = ",";
        csvString= StringUtils.replaceEach(csvString.replaceAll("[\"()]",""),new String []{"Person","Collision","Vehicle","AggregateData"},new String []{"","","",""});
        String[] accdata = csvString.split(cvsSplitBy);
        return AccidentMinerModel.builder()
                .C_YEAR(accdata[4]).C_MNTH(accdata[5]).C_WDAY(accdata[6]).C_HOUR(accdata[7]).C_SEV(accdata[8]).C_VEHS(accdata[9])
                .C_CONF(accdata[10]).C_RCFG(accdata[11]).C_WTHR(accdata[12]).C_RSUR(accdata[13]).C_RALN(accdata[14]).C_TRAF(accdata[15])
                .P_SEX(accdata[17]).P_AGE(accdata[18]).P_PSN(accdata[19]).P_ISEV(accdata[20]).P_SAFE(accdata[21]).P_USER(accdata[22])
                .V_TYPE(accdata[24]).V_YEAR(accdata[25])
                .noOfPerson(Integer.valueOf(accdata[0])).trafficScore(Integer.valueOf(accdata[1])).visibility(Integer.valueOf(accdata[2])).passengerPositioning(Integer.valueOf(accdata[3]))
                .build();
    }


    public AccidentAttribute getItems(String csvString){
        return AccidentAttribute.builder().name(csvString).build();

    }




}
