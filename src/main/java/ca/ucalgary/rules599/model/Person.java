package ca.ucalgary.rules599.model;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class Person  implements Item {
    private String P_ID; //Person sequence number	01-99. NN= Data Element not applicable. UU=Unknown
    private String P_SEX; //Person sex	F=Female, M=Male, N= Data Element not applicable, U=Unknown, X=Jurisdiction does not provide this data
    private String P_AGE; //Person age
    private String P_PSN;  //Person position
    private String P_ISEV; //Medical treatment required
    private String P_SAFE; //Safety device used
    private String P_USER; //Road user class

    @Override
    public final int compareTo(@NotNull final Item o) {
        return toString().compareTo(o.toString());
    }
}
