package ca.ucalgary.rules599.model;

public class PreProcessing {


    public static int rush_hour_threshold = 20;
    private static Integer[][] rush_hour_averages = null;
    //private static HashSet<String> holidays = null;

    /**
     * calculates whether a given entry happened during a rush hour
     * based on date and time
     * @param C_YEAR: year value of an entry
     * @param C_MNTH: month value of an entry
     * @param C_WDAY: day value of an entry
     * @param C_HOUR: hour value of an entry
     * @return boolean where true means entry happened during a rush hour, otherwise false
     */
    public static boolean process_rush_hour(String C_YEAR, String C_MNTH, String C_WDAY, String C_HOUR) {
        if (rush_hour_averages == null) {
            rush_hour_averages = RushHour_Data.get_rush_hour_averages();
        }
        // sanity check
        // could be faster with ascii values
        if (C_WDAY.equals("U") || C_WDAY.equals("X") ||
            C_HOUR.equals("UU") || C_HOUR.equals("XX")) {
            return false;
        }

        int hour = Integer.parseInt(C_HOUR);
        int wday = Integer.parseInt(C_WDAY) % 7;  // sunday=7 needs to be 0

        return rush_hour_averages[hour][wday] >= rush_hour_threshold;
    }

}
