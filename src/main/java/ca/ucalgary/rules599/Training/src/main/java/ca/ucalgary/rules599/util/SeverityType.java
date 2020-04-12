package ca.ucalgary.rules599.util;

public enum SeverityType {
        NO_INJURY(1), INJURY(2), FATALITY(3), NOT_APPLICABLE(4), UNKNOWN(5), NOT_PROVIDED(6);

        private final int id;
        SeverityType(int id) { this.id = id; }
        public int getValue() { return id; }

    }


