package ca.ucalgary.rules599.datastructure;

public class Triple<F, S, T> {
    public final F first;
    public final S second;
    public final T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <F, S, T> Triple<F, S, T> create(F first, S second, T third) {
        return new Triple<>(first, second, third);
    }

    public final int hashCode() {
        boolean prime = true;
        int result = 1;
        result = 31 * result + (this.first == null ? 0 : this.first.hashCode());
        result = 31 * result + (this.second == null ? 0 : this.second.hashCode());
        result = 31 * result + (this.third == null ? 0 : this.third.hashCode());
        return result;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Triple<?, ?, ?> other = (Triple)obj;
            if (this.first == null) {
                if (other.first != null) {
                    return false;
                }
            } else if (!this.first.equals(other.first)) {
                return false;
            }

            if (this.second == null) {
                if (other.second != null) {
                    return false;
                }
            } else if (!this.second.equals(other.second)) {
                return false;
            }

            if (this.third == null) {
                if (other.third != null) {
                    return false;
                }
            } else if (!this.third.equals(other.third)) {
                return false;
            }

            return true;
        }
    }
}

