package ca.ucalgary.rules599.model;


import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccidentData implements Item{
    Collision collision;
    Person person;
    Vehicle vehicle;

    @Override
    public final int compareTo(@NotNull final Item o) {
        return toString().compareTo(o.toString());
    }


    public AccidentData getAccidentDatafromCSV(String csvString){
        String cvsSplitBy = ",";
        String[] accdata = csvString.split(cvsSplitBy);
        Collision collision = Collision.builder().C_YEAR(accdata[0]).C_MNTH(accdata[1]).C_WDAY(accdata[2]).C_HOUR(accdata[3]).C_SEV(accdata[4]).C_VEHS(accdata[5])
                .C_CONF(accdata[6]).C_RCFG(accdata[7]).C_WTHR(accdata[8]).C_RSUR(accdata[9]).C_RALN(accdata[10]).C_TRAF(accdata[11]).build();
        Person person = Person.builder().P_ID(accdata[15]).P_SEX(accdata[16]).P_AGE(accdata[17]).P_PSN(accdata[18]).P_ISEV(accdata[19]).P_SAFE(accdata[20]).P_USER(accdata[21]).build();
        Vehicle vehicle = Vehicle.builder().V_ID(accdata[12]).V_TYPE(accdata[13]).V_YEAR(accdata[14]).build();
        return AccidentData.builder().collision(collision).person(person).vehicle(vehicle).build();

    }

}
