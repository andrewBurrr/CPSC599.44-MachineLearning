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



}
