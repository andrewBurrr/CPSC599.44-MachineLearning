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
public class Vehicle  implements Item {
    private String V_ID; //Vehicle sequence number	01-98. 99= vehicles sequence Number assigned to pedestrians. UU=Unknown
    private String V_TYPE; //Vehicle type	01-23. NN=Data not Applicable. QQ= Choice is other than the preceding values. UU=Unknown, XX=Jurisdiction does not provide the data
    private String V_YEAR; //Vehicle model year	19yy-20yy. NNNN= Data Element not Applicable. UUUU=Unknown, XXXX=Jurisdiction does not provide the data


    @Override
    public final int compareTo(@NotNull final Item o) {
        return toString().compareTo(o.toString());
    }

}
