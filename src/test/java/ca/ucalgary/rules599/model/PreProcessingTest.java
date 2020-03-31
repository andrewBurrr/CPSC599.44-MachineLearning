package ca.ucalgary.rules599.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PreProcessingTest {
    /** values in the array
     * 4 2 1 2 2 2 4
     * 2 1 1 2 1 1 2
     * 2 1 1 2 2 1 2
     * 1 1 1 2 1 1 1
     * 0 0 1 1 1 1 0
     * 0 1 1 1 1 1 0
     * 0 11 13 13 12 9 0
     * 0 30 35 34 33 24 1
     * 2 34 41 40 39 29 5
     * 5 19 24 23 23 19 9
     * 9 14 18 18 18 18 14
     * 12 16 19 19 20 22 18
     * 16 18 21 21 23 26 21
     * 17 17 20 20 22 25 22
     * 17 20 23 23 24 30 22
     * 16 28 34 34 36 40 22
     * 15 39 48 48 51 49 20
     * 13 36 46 47 50 42 18
     * 10 18 24 25 27 25 16
     * 8 10 13 14 15 16 11
     * 7 8 10 10 11 11 9
     * 6 7 8 9 9 10 9
     * 5 4 6 6 7 9 8
     * 3 2 3 4 4 6 7
     */
    Integer[][] rush_hour_averages = RushHour_Data.get_rush_hour_averages();


    @Before
    // ensure that threshold is always set to default value
    public void setUp() throws Exception {
        PreProcessing.rush_hour_threshold = 20;
    }

    @Test
    public void check_threshold_default_value() {
        assertEquals(PreProcessing.rush_hour_threshold, 20);
    }

    @Test
    public void check_false_values() {
        int current_threhold = PreProcessing.rush_hour_threshold;

        // check sunday at 12:00 am
        String wday = "07";
        String hour = "00";
        boolean test1 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        boolean actual1 = rush_hour_averages[Integer.parseInt(hour)][Integer.parseInt(wday) % 7] >= current_threhold;
        assertEquals(test1, actual1);

        // check sunday at 11:00 pm
        wday = "07";
        hour = "23";
        boolean test2 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        boolean actual2 = rush_hour_averages[Integer.parseInt(hour)][Integer.parseInt(wday) % 7] >= current_threhold;
        assertEquals(test2, actual2);

        // check saturday at 12:00 am
        wday = "06";
        hour = "00";
        boolean test3 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        boolean actual3 = rush_hour_averages[Integer.parseInt(hour)][Integer.parseInt(wday) % 7] >= current_threhold;
        assertEquals(test3, actual3);

        // check saturday at 11:00 pm
        wday = "06";
        hour = "23";
        boolean test4 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        boolean actual4 = rush_hour_averages[Integer.parseInt(hour)][Integer.parseInt(wday) % 7] >= current_threhold;
        assertEquals(test4, actual4);
    }

    @Test
    public void check_sanity_checks() {
        // return false on hour = UU
        String wday = "06";
        String hour = "UU";
        boolean test1 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test1, false);

        // return false on hour = XX
        wday = "06";
        hour = "XX";
        boolean test2 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test2, false);

        // return false on wday = U
        wday = "U";
        hour = "00";
        boolean test3 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test3, false);

        // return false on wday = X
        wday = "X";
        hour = "00";
        boolean test4 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test4, false);

        // return false on both incorrect wday and hour
        wday = "X";
        hour = "UU";
        boolean test5 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test5, false);
    }

    @Test
    public void check_rush_hours() {
        int current_threhold = PreProcessing.rush_hour_threshold;

        // check monday at 7:00 am
        String wday = "01";
        String hour = "07";
        boolean test1 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        boolean actual1 = rush_hour_averages[Integer.parseInt(hour)][Integer.parseInt(wday) % 7] >= current_threhold;
        assertEquals(test1, true);

        // check monday at 4:00 pm
        wday = "01";
        hour = "16";
        boolean test2 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        boolean actual2 = rush_hour_averages[Integer.parseInt(hour)][Integer.parseInt(wday) % 7] >= current_threhold;
        assertEquals(test2, true);

        // check friday at 8:00 am
        wday = "05";
        hour = "08";
        boolean test3 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        boolean actual3 = rush_hour_averages[Integer.parseInt(hour)][Integer.parseInt(wday) % 7] >= current_threhold;
        assertEquals(test3, true);

        // check friday at 5:00 pm
        wday = "05";
        hour = "17";
        boolean test4 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        boolean actual4 = rush_hour_averages[Integer.parseInt(hour)][Integer.parseInt(wday) % 7] >= current_threhold;
        assertEquals(test4, true);
    }

    @Test
    public void check_that_threhold_changes() {

        // check hour less than default threshold of 20
        // sunday at 12:00 am
        String wday = "07";
        String hour = "00";
        boolean test1 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        //boolean actual1 = rush_hour_averages[Integer.parseInt(hour)][Integer.parseInt(wday) % 7] >= current_threhold;
        assertEquals(test1, false);

        // set threshold to 0 so its always true
        PreProcessing.rush_hour_threshold = 0;
        boolean test2 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test2, true);

    }

    @Test
    public void check_that_congestion_rate_is_accessed_correctly() {
        // sunday at 12:00 am
        // average congestion rate = 4
        String wday = "7";
        String hour = "00";
        boolean test1 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test1, false);

        // sunday at 12:00 am
        // check again but set congestion rate to 4
        PreProcessing.rush_hour_threshold = 4;
        boolean test2 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test2, true);

        // saturday at 11:00 pm
        // average congestion rate = 7
        PreProcessing.rush_hour_threshold = 20;     // reset to default
        wday = "06";
        hour = "23";
        boolean test3 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test3, false);

        // saturday at 11:00 pm
        // check again but set congestion rate to 7
        PreProcessing.rush_hour_threshold = 7;
        boolean test4 = PreProcessing.process_rush_hour("15", "12", wday, hour);
        assertEquals(test4, true);
    }
}