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

public class AggregateData implements Item {
    int noOfPerson;
    int visibility;
    int trafficScore;
    String crashSevirity;

    @Override
    public final int compareTo(@NotNull final Item o) {
        return toString().compareTo(o.toString());
    }

}
