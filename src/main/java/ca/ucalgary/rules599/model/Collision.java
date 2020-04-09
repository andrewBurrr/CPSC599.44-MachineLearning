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
public class Collision  implements Item {

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


    @Override
    public final int compareTo(@NotNull final Item o) {
        return toString().compareTo(o.toString());
    }
}
