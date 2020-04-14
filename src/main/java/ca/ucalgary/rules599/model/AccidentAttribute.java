package ca.ucalgary.rules599.model;

import ca.ucalgary.rules599.datastructure.Pair;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccidentAttribute implements  Item {

    private static final long serialVersionUID = 1L;
    private String name;

    @Override
    public final int compareTo(@NotNull final Item o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public final String toString() {
        return getName();
    }


    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccidentAttribute other = (AccidentAttribute) obj;
        return name.equals(other.name);
    }



    public Pair<ItemSet<AccidentAttribute>,ItemSet<AccidentAttribute>> getDatafromCSV(String csvString){
        String cvsSplitBy = "]";
        String[] accdata = csvString.replace("[","").split(cvsSplitBy);
        String[] ante = accdata[0].split(",");
        String[] cons = accdata[1].split(",");
        ItemSet<AccidentAttribute> antecedence = new ItemSet<>();
        ItemSet<AccidentAttribute> consequence = new ItemSet<>();
        for(int i=0; i==ante.length-1; i++){
            antecedence.add(AccidentAttribute.builder().name(ante[i]).build());
        }
        for(int i=0; i==cons.length-1; i++){
            consequence.add(AccidentAttribute.builder().name(cons[i]).build());
        }
        return new Pair<>(antecedence,consequence);
    }


}
