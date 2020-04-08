package ca.ucalgary.rules599.model;

import java.util.*;

/**
 * Contains traffic data from TomTom's website
 * Implements the pre-processing to determine traffic rate based on date and time
 * @author Benedict Mendoza
 * @since 2020-04-08
 */
public class RushHour_PreProcessing {

    // default congestion rate threshold
    // if greater than equal threshold then it is considered rush hour, false otherwise
    public static int rush_hour_threshold = 20;
    // array of averages index by time and day => averages[hour][day]
    private static Integer[][] rush_hour_averages = null;

    /**
     * calculates whether a given entry happened during a rush hour
     * based on date and time
     * @param C_YEAR: year value of an entry
     * @param C_MNTH: month value of an entry
     * @param C_WDAY: day value of an entry
     * @param C_HOUR: hour value of an entry
     * @return boolean where true means entry happened during a rush hour, otherwise false
     */
    public static boolean evaluate_rush_hour(String C_YEAR, String C_MNTH, String C_WDAY, String C_HOUR) {
        if (rush_hour_averages == null) {
            rush_hour_averages = RushHour_PreProcessing.get_rush_hour_averages();
        }
        int hour = -1;
        int wday = -1;
        try {
            hour = Integer.parseInt(C_HOUR);
            wday = Integer.parseInt(C_WDAY);
            if (hour<0 || hour>23 || wday<1 || wday>7) {
                // invalid values
                return false;
            }

        } catch (Exception e) {
            // input must have been unknown
            return false;
        }

        // wday has sunday=7 but needs to be 0
        return rush_hour_averages[hour][wday%7] >= rush_hour_threshold;
    }

    /**
     * averages all the rush hour data across multiple canadian cities
     * @return: 2d array of rush hour averages
     */
    public static Integer[][] get_rush_hour_averages() {
        if (rush_hour_averages != null)
            return rush_hour_averages;

        Integer[][] rush_hour_average = Arrays.stream(new Integer[HALIFAX_TOMTOM_STATS.length][HALIFAX_TOMTOM_STATS[0].length])
                                        .map(x -> new Integer[]{0, 0, 0, 0, 0, 0, 0})
                                        .toArray(Integer[][]::new);

        Collection<Integer[][]> all_rush_hour_stats = get_all_stats().values();
        for (Integer[][] a_chart : all_rush_hour_stats) {
            add_2d_array(rush_hour_average, a_chart);
        }

        // average the entries
        for (Integer[] an_array : rush_hour_average) {
            for (int i=0; i<an_array.length; i++) {
                an_array[i] /= all_rush_hour_stats.size();
            }
        }

        return rush_hour_average;
    }

    /**
     * stores all city rush hour data into a map
     * @return: a map of city_name->2d array of rush hours
     */
    public static Map<String, Integer[][]> get_all_stats() {
        Map<String, Integer[][]> all_stats = new HashMap<String, Integer[][]>();

        all_stats.put("halifax", HALIFAX_TOMTOM_STATS);
        all_stats.put("calgary", CALGARY_TOMTOM_STATS);
        all_stats.put("edmonton", EDMONTON_TOMTOM_STATS);
        all_stats.put("hamilton", HAMILTON_TOMTOM_STATS);
        all_stats.put("kitchener-waterloo", KITCHENER_WATERLOO_TOMTOM_STATS);
        all_stats.put("london-ontario", LONDON_ONTARIO_TOMTOM_STATS);
        all_stats.put("montreal", MONTREAL_TOMTOM_STATS);
        all_stats.put("ottawa", OTTAWA_TOMTOM_STATS);
        all_stats.put("quebec", QUEBEC_TOMTOM_STATS);
        all_stats.put("toronto", TORONTO_TOMTOM_STATS);
        all_stats.put("vancouver", VANCOUVER_TOMTOM_STATS);
        all_stats.put("winnipeg", WINNIPEG_TOMTOM_STATS);

        return all_stats;
    }

    /**
     * adds the values of two 2d arrays
     * @param dest: array use in addition and destination
     * @param src: array use in addition
     */
    private static void add_2d_array(Integer[][] dest, Integer[][] src) {
        if (dest.length != src.length || dest[0].length != src[0].length)
            return;

        for (int i=0; i<dest.length; i++) {
            for (int j = 0; j < dest[0].length; j++) {
                dest[i][j] += src[i][j];
            }
        }
    }


    //https://www.tomtom.com/en_gb/traffic-index/halifax-traffic
    // halifax's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] HALIFAX_TOMTOM_STATS = new Integer[][]{ { 3, 2, 2, 1, 2, 2, 3 },
                                                                            { 2, 2, 2, 1, 3, 2, 2 },
                                                                            { 2, 2, 3, 2, 4, 3, 2 },
                                                                            { 0, 2, 3, 3, 4, 2, 2 },
                                                                            { 0, 1, 2, 2, 2, 2, 2 },
                                                                            { 0, 0, 0, 0, 1, 1, 1 },
                                                                            { 0, 10, 12, 13, 12, 9, 0 },
                                                                            { 0, 34, 41, 40, 39, 27, 2 },
                                                                            { 2, 36, 46, 44, 43, 28, 5 },
                                                                            { 5, 17, 23, 22, 22, 19, 9 },
                                                                            { 7, 15, 18, 18, 18, 20, 14 },
                                                                            { 10, 17, 20, 20, 21, 23, 17 },
                                                                            { 14, 20, 23, 23, 25, 28, 20 },
                                                                            { 15, 18, 21, 21, 23, 26, 20 },
                                                                            { 14, 19, 21, 21, 22, 26, 18 },
                                                                            { 13, 27, 32, 33, 34, 35, 18 },
                                                                            { 11, 43, 54, 55, 56, 46, 16 },
                                                                            { 9, 34, 46, 49, 49, 35, 14 },
                                                                            { 8, 14, 18, 20, 21, 19, 13 },
                                                                            { 6, 9, 11, 12, 14, 13, 10 },
                                                                            { 5, 7, 9, 9, 10, 10, 8 },
                                                                            { 4, 6, 7, 8, 8, 9, 8 },
                                                                            { 3, 4, 4, 4, 5, 7, 7 },
                                                                            { 2, 2, 2, 3, 3, 4, 5 } };

    //https://www.tomtom.com/en_gb/traffic-index/calgary-traffic
    // calgary's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] CALGARY_TOMTOM_STATS = new Integer[][]{ { 4, 1, 0, 1, 2, 2, 3 },
                                                                            { 2, 0, 0, 1, 1, 1, 2 },
                                                                            { 1, 1, 1, 2, 1, 2, 2 },
                                                                            { 1, 0, 1, 2, 1, 2, 1 },
                                                                            { 0, 0, 0, 0, 0, 1, 0 },
                                                                            { 0, 0, 0, 0, 0, 0, 0 },
                                                                            { 0, 6, 6, 7, 7, 5, 0 },
                                                                            { 0, 25, 26, 29, 27, 20, 1 },
                                                                            { 2, 26, 29, 31, 31, 22, 4 },
                                                                            { 4, 13, 16, 16, 17, 15, 8 },
                                                                            { 7, 10, 12, 12, 13, 14, 11 },
                                                                            { 8, 11, 14, 14, 15, 16, 13 },
                                                                            { 11, 13, 15, 16, 17, 19, 15 },
                                                                            { 12, 12, 15, 15, 15, 20, 16 },
                                                                            { 11, 13, 16, 16, 17, 22, 16 },
                                                                            { 10, 21, 26, 26, 26, 29, 15 },
                                                                            { 10, 30, 39, 38, 39, 35, 14 },
                                                                            { 9, 28, 38, 36, 37, 28, 13 },
                                                                            { 8, 12, 18, 17, 19, 17, 12 },
                                                                            { 6, 7, 10, 10, 11, 11, 9 },
                                                                            { 5, 6, 8, 8, 9, 8, 8 },
                                                                            { 4, 5, 7, 7, 8, 9, 8 },
                                                                            { 4, 3, 4, 5, 6, 7, 8 },
                                                                            { 3, 1, 2, 3, 3, 5, 7 } };

    //https://www.tomtom.com/en_gb/traffic-index/edmonton-traffic
    // edmontons's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] EDMONTON_TOMTOM_STATS = new Integer[][]{    { 6, 1, 1, 2, 2, 2, 4 },
                                                                                { 3, 2, 2, 2, 2, 2, 3 },
                                                                                { 3, 2, 2, 2, 2, 2, 3 },
                                                                                { 2, 2, 2, 3, 2, 2, 2 },
                                                                                { 1, 0, 0, 1, 0, 0, 0 },
                                                                                { 0, 0, 0, 0, 0, 0, 0 },
                                                                                { 0, 4, 4, 5, 4, 4, 0 },
                                                                                { 0, 19, 21, 21, 20, 16, 1 },
                                                                                { 1, 22, 24, 25, 22, 19, 3 },
                                                                                { 4, 11, 12, 12, 11, 11, 7 },
                                                                                { 6, 9, 10, 10, 11, 11, 9 },
                                                                                { 8, 11, 12, 12, 13, 14, 12 },
                                                                                { 10, 13, 14, 14, 15, 16, 14 },
                                                                                { 10, 12, 13, 13, 14, 15, 14 },
                                                                                { 10, 13, 14, 14, 15, 17, 14 },
                                                                                { 10, 18, 21, 21, 20, 24, 14 },
                                                                                { 9, 25, 31, 31, 29, 31, 13 },
                                                                                { 8, 23, 29, 30, 29, 27, 12 },
                                                                                { 6, 11, 13, 15, 14, 15, 11 },
                                                                                { 5, 7, 8, 9, 9, 11, 9 },
                                                                                { 5, 6, 7, 8, 8, 8, 7 },
                                                                                { 5, 5, 6, 7, 7, 8, 8 },
                                                                                { 3, 4, 5, 5, 6, 8, 8 },
                                                                                { 2, 2, 3, 4, 3, 6, 7 } };

    //https://www.tomtom.com/en_gb/traffic-index/hamilton-traffic
    // hamiltons's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] HAMILTON_TOMTOM_STATS = new Integer[][]{    { 2, 5, 2, 2, 2, 3, 3 },
                                                                                { 2, 3, 3, 1, 2, 3, 2 },
                                                                                { 2, 2, 3, 2, 2, 2, 2 },
                                                                                { 2, 2, 3, 2, 3, 2, 1 },
                                                                                { 1, 0, 1, 0, 1, 0, 1 },
                                                                                { 0, 1, 2, 0, 2, 1, 0 },
                                                                                { 0, 14, 17, 14, 15, 8, 0 },
                                                                                { 0, 27, 30, 25, 28, 17, 0 },
                                                                                { 1, 27, 31, 29, 30, 20, 2 },
                                                                                { 2, 14, 15, 15, 17, 13, 5 },
                                                                                { 5, 11, 11, 11, 13, 12, 10 },
                                                                                { 9, 12, 13, 12, 14, 16, 15 },
                                                                                { 14, 13, 14, 14, 16, 19, 19 },
                                                                                { 15, 12, 14, 13, 15, 19, 19 },
                                                                                { 15, 13, 14, 15, 17, 24, 18 },
                                                                                { 13, 19, 21, 22, 25, 37, 16 },
                                                                                { 11, 28, 34, 34, 39, 50, 13 },
                                                                                { 9, 33, 40, 39, 46, 49, 10 },
                                                                                { 8, 16, 19, 20, 24, 27, 9 },
                                                                                { 7, 8, 8, 9, 10, 11, 6 },
                                                                                { 7, 7, 6, 7, 7, 7, 5 },
                                                                                { 8, 6, 5, 7, 6, 6, 6 },
                                                                                { 6, 4, 3, 4, 5, 5, 5 },
                                                                                { 3, 3, 2, 5, 6, 4, 4 } };

    //https://www.tomtom.com/en_gb/traffic-index/kitchener-waterloo-traffic
    // kitchener waterloo's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] KITCHENER_WATERLOO_TOMTOM_STATS = new Integer[][]{  { 5, 3, 3, 3, 3, 4, 5 },
                                                                                        { 5, 3, 3, 3, 3, 4, 4 },
                                                                                        { 4, 4, 4, 4, 4, 4, 4 },
                                                                                        { 3, 4, 4, 3, 3, 4, 3 },
                                                                                        { 3, 2, 3, 2, 3, 4, 4 },
                                                                                        { 2, 1, 2, 1, 2, 2, 2 },
                                                                                        { 2, 4, 5, 4, 4, 5, 1 },
                                                                                        { 1, 12, 14, 13, 13, 12, 2 },
                                                                                        { 2, 22, 25, 23, 24, 20, 5 },
                                                                                        { 4, 13, 15, 14, 16, 14, 8 },
                                                                                        { 6, 11, 12, 11, 12, 13, 11 },
                                                                                        { 8, 12, 14, 13, 14, 15, 13 },
                                                                                        { 10, 13, 15, 15, 16, 17, 14 },
                                                                                        { 10, 12, 14, 14, 15, 16, 14 },
                                                                                        { 10, 14, 16, 15, 16, 18, 14 },
                                                                                        { 9, 18, 19, 19, 21, 25, 13 },
                                                                                        { 9, 24, 27, 27, 30, 28, 11 },
                                                                                        { 8, 26, 31, 32, 35, 26, 10 },
                                                                                        { 6, 12, 14, 16, 16, 16, 9 },
                                                                                        { 6, 9, 10, 11, 11, 12, 7 },
                                                                                        { 6, 8, 9, 9, 9, 9, 7 },
                                                                                        { 6, 7, 8, 8, 9, 9, 8 },
                                                                                        { 5, 4, 5, 5, 6, 7, 6 },
                                                                                        { 4, 3, 3, 3, 4, 6, 6 } };

    //https://www.tomtom.com/en_gb/traffic-index/london-ontario-traffic
    // london ontario's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] LONDON_ONTARIO_TOMTOM_STATS = new Integer[][]{  { 7, 2, 2, 2, 3, 3, 5 },
                                                                                    { 5, 2, 2, 3, 3, 3, 5 },
                                                                                    { 4, 3, 3, 3, 3, 3, 4 },
                                                                                    { 2, 2, 2, 4, 2, 2, 2 },
                                                                                    { 0, 1, 2, 2, 2, 1, 1 },
                                                                                    { 0, 0, 1, 1, 2, 2, 1 },
                                                                                    { 0, 6, 8, 7, 7, 7, 2 },
                                                                                    { 1, 18, 21, 21, 21, 18, 5 },
                                                                                    { 4, 27, 30, 30, 29, 25, 8 },
                                                                                    { 7, 18, 21, 20, 21, 19, 12 },
                                                                                    { 11, 16, 19, 18, 19, 19, 16 },
                                                                                    { 13, 18, 21, 21, 23, 24, 20 },
                                                                                    { 19, 21, 25, 24, 26, 29, 24 },
                                                                                    { 19, 20, 23, 23, 25, 29, 24 },
                                                                                    { 18, 22, 25, 25, 27, 32, 24 },
                                                                                    { 17, 26, 30, 30, 33, 36, 24 },
                                                                                    { 14, 34, 40, 40, 43, 42, 21 },
                                                                                    { 12, 30, 37, 38, 40, 34, 17 },
                                                                                    { 10, 17, 21, 22, 24, 22, 16 },
                                                                                    { 9, 12, 14, 15, 16, 16, 13 },
                                                                                    { 8, 10, 12, 13, 13, 12, 11 },
                                                                                    { 8, 9, 10, 11, 12, 12, 11 },
                                                                                    { 5, 5, 6, 7, 7, 11, 10 },
                                                                                    { 3, 4, 4, 5, 5, 7, 8 } };

    //https://www.tomtom.com/en_gb/traffic-index/montreal-traffic
    // montreal's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] MONTREAL_TOMTOM_STATS = new Integer[][]{    { 6, 2, 2, 4, 3, 3, 6 },
                                                                                { 3, 1, 1, 3, 2, 2, 3 },
                                                                                { 2, 1, 1, 3, 1, 1, 2 },
                                                                                { 2, 1, 0, 2, 1, 0, 2 },
                                                                                { 0, 0, 0, 1, 0, 0, 0 },
                                                                                { 0, 9, 11, 12, 10, 5, 0 },
                                                                                { 0, 28, 34, 34, 30, 18, 0 },
                                                                                { 0, 44, 56, 54, 50, 33, 0 },
                                                                                { 0, 44, 57, 54, 51, 35, 4 },
                                                                                { 4, 23, 34, 32, 31, 21, 9 },
                                                                                { 8, 16, 23, 23, 24, 20, 15 },
                                                                                { 12, 17, 22, 22, 24, 24, 20 },
                                                                                { 15, 19, 22, 22, 24, 28, 23 },
                                                                                { 18, 19, 22, 22, 24, 28, 24 },
                                                                                { 19, 24, 29, 28, 32, 37, 25 },
                                                                                { 19, 35, 45, 42, 47, 52, 25 },
                                                                                { 18, 47, 60, 58, 65, 62, 25 },
                                                                                { 17, 46, 59, 59, 66, 54, 26 },
                                                                                { 11, 22, 32, 31, 36, 31, 18 },
                                                                                { 7, 9, 14, 14, 17, 19, 11 },
                                                                                { 7, 6, 9, 9, 10, 12, 9 },
                                                                                { 7, 6, 8, 9, 10, 10, 10 },
                                                                                { 5, 4, 7, 8, 9, 9, 11 },
                                                                                { 3, 3, 7, 7, 7, 8, 10 } };

    //https://www.tomtom.com/en_gb/traffic-index/ottawa-traffic
    // ottawa's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] OTTAWA_TOMTOM_STATS = new Integer[][]{  { 8, 2, 1, 3, 2, 2, 6 },
                                                                            { 5, 1, 1, 3, 1, 1, 4 },
                                                                            { 4, 0, 1, 3, 1, 1, 3 },
                                                                            { 1, 1, 1, 3, 1, 0, 1 },
                                                                            { 0, 0, 0, 1, 0, 0, 0 },
                                                                            { 0, 0, 1, 1, 0, 0, 0 },
                                                                            { 0, 15, 20, 20, 18, 12, 0 },
                                                                            { 1, 37, 48, 48, 46, 31, 2 },
                                                                            { 3, 42, 56, 54, 52, 36, 6 },
                                                                            { 7, 21, 33, 29, 28, 23, 10 },
                                                                            { 12, 15, 20, 19, 19, 19, 15 },
                                                                            { 15, 17, 20, 21, 21, 23, 18 },
                                                                            { 19, 20, 22, 23, 24, 28, 22 },
                                                                            { 20, 18, 20, 21, 22, 27, 23 },
                                                                            { 19, 20, 23, 23, 25, 32, 24 },
                                                                            { 18, 36, 46, 47, 51, 51, 23 },
                                                                            { 16, 50, 69, 69, 74, 62, 21 },
                                                                            { 13, 39, 59, 57, 62, 47, 19 },
                                                                            { 11, 18, 28, 26, 30, 26, 16 },
                                                                            { 8, 11, 15, 14, 16, 17, 12 },
                                                                            { 8, 9, 12, 12, 13, 13, 11 },
                                                                            { 7, 8, 12, 11, 11, 13, 11 },
                                                                            { 6, 5, 8, 7, 8, 11, 10 },
                                                                            { 4, 3, 6, 4, 5, 8, 9 } };

    //https://www.tomtom.com/en_gb/traffic-index/quebec-traffic
    // quebec's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] QUEBEC_TOMTOM_STATS = new Integer[][]{  { 1, 2, 1, 4, 3, 3, 2 },
                                                                            { 0, 1, 1, 5, 3, 2, 2 },
                                                                            { 0, 1, 3, 5, 4, 2, 1 },
                                                                            { 1, 0, 2, 6, 4, 2, 0 },
                                                                            { 0, 0, 2, 5, 3, 2, 0 },
                                                                            { 0, 0, 0, 2, 0, 1, 0 },
                                                                            { 0, 13, 14, 17, 14, 9, 0 },
                                                                            { 0, 44, 49, 57, 50, 34, 0 },
                                                                            { 1, 32, 37, 47, 40, 26, 2 },
                                                                            { 4, 11, 13, 17, 14, 12, 6 },
                                                                            { 8, 10, 12, 14, 14, 14, 11 },
                                                                            { 10, 11, 14, 14, 16, 18, 13 },
                                                                            { 12, 13, 15, 15, 17, 22, 12 },
                                                                            { 15, 14, 16, 15, 17, 23, 13 },
                                                                            { 16, 14, 16, 16, 17, 27, 15 },
                                                                            { 14, 24, 29, 30, 33, 41, 15 },
                                                                            { 12, 46, 59, 59, 64, 63, 14 },
                                                                            { 8, 33, 48, 48, 50, 43, 11 },
                                                                            { 4, 9, 15, 15, 17, 17, 6 },
                                                                            { 3, 5, 8, 9, 10, 9, 5 },
                                                                            { 4, 5, 8, 8, 8, 7, 5 },
                                                                            { 5, 5, 8, 9, 8, 7, 5 },
                                                                            { 4, 4, 7, 7, 6, 5, 5 },
                                                                            { 5, 3, 6, 7, 5, 4, 4 } };

    //https://www.tomtom.com/en_gb/traffic-index/toronto-traffic
    // toronto's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] TORONTO_TOMTOM_STATS = new Integer[][] {    { 8, 2, 1, 2, 1, 3, 8 },
                                                                                { 4, 1, 0, 0, 0, 1, 4 },
                                                                                { 3, 0, 0, 0, 0, 1, 2 },
                                                                                { 0, 0, 0, 0, 0, 0, 0 },
                                                                                { 0, 0, 0, 0, 0, 0, 0 },
                                                                                { 0, 1, 2, 1, 1, 1, 0 },
                                                                                { 0, 17, 20, 19, 18, 15, 0 },
                                                                                { 0, 37, 42, 39, 38, 31, 2 },
                                                                                { 2, 51, 58, 54, 53, 42, 6 },
                                                                                { 6, 33, 39, 36, 37, 29, 13 },
                                                                                { 11, 20, 26, 25, 26, 24, 19 },
                                                                                { 16, 20, 25, 25, 27, 27, 25 },
                                                                                { 21, 22, 27, 27, 30, 32, 31 },
                                                                                { 25, 22, 26, 26, 29, 33, 31 },
                                                                                { 25, 27, 32, 30, 34, 39, 32 },
                                                                                { 25, 38, 44, 44, 48, 51, 33 },
                                                                                { 24, 48, 55, 55, 60, 60, 32 },
                                                                                { 24, 59, 66, 67, 71, 63, 32 },
                                                                                { 20, 40, 47, 50, 53, 46, 30 },
                                                                                { 15, 19, 24, 26, 30, 31, 23 },
                                                                                { 12, 12, 14, 16, 18, 20, 16 },
                                                                                { 10, 9, 11, 12, 14, 17, 14 },
                                                                                { 8, 6, 8, 9, 11, 15, 14 },
                                                                                { 5, 3, 5, 5, 7, 12, 13 } };

    //https://www.tomtom.com/en_gb/traffic-index/vancouver-traffic
    // vancouver's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] VANCOUVER_TOMTOM_STATS = new Integer[][]{   { 4, 0, 0, 0, 0, 0, 3 },
                                                                                { 0, 0, 0, 0, 0, 0, 0 },
                                                                                { 0, 0, 0, 0, 0, 0, 0 },
                                                                                { 0, 0, 0, 0, 0, 0, 0 },
                                                                                { 0, 0, 0, 0, 0, 0, 0 },
                                                                                { 0, 0, 0, 0, 0, 0, 0 },
                                                                                { 0, 15, 17, 17, 17, 14, 0 },
                                                                                { 0, 37, 43, 42, 41, 33, 2 },
                                                                                { 3, 55, 64, 63, 62, 49, 9 },
                                                                                { 12, 38, 48, 49, 48, 41, 19 },
                                                                                { 20, 28, 37, 37, 37, 36, 27 },
                                                                                { 26, 30, 37, 37, 38, 40, 36 },
                                                                                { 33, 33, 38, 39, 40, 44, 43 },
                                                                                { 37, 32, 37, 39, 40, 45, 47 },
                                                                                { 37, 38, 44, 47, 49, 54, 46 },
                                                                                { 36, 52, 62, 64, 67, 68, 46 },
                                                                                { 33, 54, 68, 69, 73, 69, 42 },
                                                                                { 31, 56, 72, 75, 77, 67, 40 },
                                                                                { 24, 36, 48, 51, 53, 51, 36 },
                                                                                { 18, 20, 23, 25, 27, 30, 24 },
                                                                                { 15, 14, 16, 17, 18, 19, 17 },
                                                                                { 12, 12, 14, 15, 16, 18, 17 },
                                                                                { 8, 7, 9, 10, 11, 15, 15 },
                                                                                { 3, 2, 3, 4, 4, 9, 11 } };

    //https://www.tomtom.com/en_gb/traffic-index/winnipeg-traffic
    // winnipeg's congestion rate data from tomtom's traffic index
    // collected from the 2017 data
    public static final Integer[][] WINNIPEG_TOMTOM_STATS = new Integer[][]{    { 4, 2, 2, 2, 2, 3, 4 },
                                                                                { 2, 1, 2, 3, 2, 2, 2 },
                                                                                { 1, 1, 2, 2, 2, 2, 2 },
                                                                                { 1, 2, 2, 3, 2, 2, 1 },
                                                                                { 1, 1, 2, 3, 3, 3, 2 },
                                                                                { 0, 0, 1, 1, 1, 1, 1 },
                                                                                { 0, 8, 8, 8, 8, 7, 2 },
                                                                                { 2, 26, 29, 29, 27, 23, 4 },
                                                                                { 3, 32, 35, 36, 34, 28, 6 },
                                                                                { 6, 19, 21, 23, 21, 21, 10 },
                                                                                { 9, 17, 19, 20, 19, 21, 14 },
                                                                                { 11, 20, 23, 23, 23, 26, 18 },
                                                                                { 15, 23, 26, 26, 27, 31, 22 },
                                                                                { 17, 22, 25, 25, 26, 30, 23 },
                                                                                { 16, 23, 26, 27, 28, 32, 24 },
                                                                                { 16, 32, 36, 37, 38, 41, 23 },
                                                                                { 15, 40, 48, 48, 49, 48, 20 },
                                                                                { 12, 31, 38, 39, 39, 37, 18 },
                                                                                { 10, 16, 20, 21, 23, 23, 16 },
                                                                                { 8, 11, 13, 14, 16, 16, 12 },
                                                                                { 8, 10, 11, 11, 12, 13, 10 },
                                                                                { 7, 8, 9, 10, 10, 12, 10 },
                                                                                { 5, 6, 6, 7, 7, 10, 8 },
                                                                                { 3, 3, 3, 4, 5, 6, 6 } };



}
