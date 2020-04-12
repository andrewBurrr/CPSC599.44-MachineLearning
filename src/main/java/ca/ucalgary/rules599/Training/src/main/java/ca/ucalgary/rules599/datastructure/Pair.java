package ca.ucalgary.rules599.datastructure;

public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> create(F first, S second) {
        return new Pair<>(first, second);
    }

    public final int hashCode() {
        int result = 1;
        result = 31 * result + (this.first == null ? 0 : this.first.hashCode());
        result = 31 * result + (this.second == null ? 0 : this.second.hashCode());
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
            Pair<?, ?> other = (Pair)obj;
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

            return true;
        }
    }
}
