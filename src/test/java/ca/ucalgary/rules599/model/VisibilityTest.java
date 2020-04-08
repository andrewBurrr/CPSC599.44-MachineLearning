package ca.ucalgary.rules599.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class VisibilityTest {


    @Test
    public void test_correct_weather_input() {
        String C_YEAR = "16";
        String C_MNTH = "01";
        String C_WDAY = "01";
        String C_HOUR = "12"; //
        String C_WTHR = "01";
        String C_RALN = "1";
        Float test1 = Visibility_PreProcessing.get_visibility_value(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        Float actual1 = 1.0f;
        assertEquals(test1, actual1, 0.001f);

        C_WTHR = "02";
        Float test2 = Visibility_PreProcessing.get_visibility_value(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        Float actual2 = 0.88f;
        assertEquals(actual2, test2, 0.001f);

        C_WTHR = "03";
        Float test3 = Visibility_PreProcessing.get_visibility_value(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        Float actual3 = 0.76f;
        assertEquals(actual3, test3, 0.001f);

        C_WTHR = "04"; // snow
        Float test4 = Visibility_PreProcessing.get_visibility_value(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        Float actual4 = 0.64f;
        assertEquals(actual4, test4, 0.001f);

        C_WTHR = "05";
        Float test5 = Visibility_PreProcessing.get_visibility_value(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        Float actual5 = 0.70f;
        assertEquals(actual5, test5, 0.001f);

        C_WTHR = "06";
        Float test6 = Visibility_PreProcessing.get_visibility_value(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        Float actual6 = 0.40f;
        assertEquals(actual6, test6, 0.001f);

        C_WTHR = "07";
        Float test7 = Visibility_PreProcessing.get_visibility_value(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        Float actual7 = 1.0f;
        assertEquals(actual7, test7, 0.001f);

    }

    @Test
    public void test_good_visibility() {
        // good visibility must have an overall visibility greater than equal 0.7
        // night time can only be fair visibility at best
        String C_YEAR = "16";
        String C_MNTH = "01";
        String C_WDAY = "01";
        String C_HOUR = "12";   // noon
        String C_WTHR = "01";   // clear and sunny
        String C_RALN = "1";

        int test1 = Visibility_PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        assertEquals(test1, 2);

        C_HOUR = "09";   // morning-ish
        int test2 = Visibility_PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        assertEquals(test2, 2);

        C_HOUR = "16";   // afternoon
        int test3 = Visibility_PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        assertEquals(test3, 2);

    }

    @Test
    public void test_fair_visibility() {
        // good visibility must have an overall visibility greater than equal 0.4 and less than 0.7
        String C_YEAR = "16";
        String C_MNTH = "01";
        String C_WDAY = "01";
        String C_HOUR = "19";   // 7 pm
        String C_WTHR = "2";   // overcast
        String C_RALN = "1";    // straight road

        // actual value must be 0.48
        int test1 = Visibility_PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        assertEquals(1, test1);

        // snowing at day on a straight road
        C_HOUR = "12";   // 12 pm
        C_WTHR = "4";   // snowing
        C_RALN = "1";    // straight road
        // actual value = 0.64
        int test2 = Visibility_PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        assertEquals(1, test2);
    }

    @Test
    public void test_poor_visibility() {
        // poor visibility must have an overall visibility greater less than 0.4
        // or weather value is "visibility limitation"
        String C_YEAR = "16";
        String C_MNTH = "01";
        String C_WDAY = "01";
        String C_HOUR = "12";   // noon
        String C_WTHR = "6";   // visibility limitation
        String C_RALN = "1";    // straight road

        int test1 = Visibility_PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        assertEquals(0, test1);

        // snowing at night on a straight road
        C_WTHR = "4";   // snow
        C_HOUR = "19";   // 7 pm
        C_RALN = "1";    // straight road
        int test2 = Visibility_PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        assertEquals(0, test2);

        // driving at night at the bottom of the hill
        // actual value = 0.35
        C_WTHR = "Q";   // unspecified weather
        C_HOUR = "19";   // 7 pm
        C_RALN = "6";    // bottom of hill
        int test3 = Visibility_PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        assertEquals(0, test3);

    }

    @Test
    public void test_sunlight_at_solarnoon() {
        String C_YEAR = "99";
        String C_MNTH = "01";   // january
        String C_WDAY = "05";   // friday
        String C_HOUR = "12";

        Float test1 = Visibility_PreProcessing.evaluate_sunlight(C_YEAR, C_MNTH, C_WDAY, C_HOUR);
        Float actual1 = 1.0f;
        assertEquals(actual1, test1, 0.001f);
    }

    @Test
    public void test_sunlight_at_night() {
        String C_YEAR = "99";
        String C_MNTH = "01";   // january
        String C_WDAY = "05";   // friday
        //String C_HOUR = "12";

        for (int i=17; i<32; i++) {
            String current_hour = String.format("%02d", i%24);
            //System.out.printf("checking hour: %s\n", current_hour);
            Float test1 = Visibility_PreProcessing.evaluate_sunlight(C_YEAR, C_MNTH, C_WDAY, current_hour);
            assertEquals(0.0f, test1, 0.001f);
        }
    }

    @Test
    public void test_sunlight_during_the_day() {
        String C_YEAR = "99";   // year = 1999
        String C_MNTH = "01";   // january
        String C_WDAY = "05";   // friday
        //String C_HOUR = "12";

        for (int i=8; i<17; i++) {
            String current_hour = String.format("%02d", i%24);
            Float test = Visibility_PreProcessing.evaluate_sunlight(C_YEAR, C_MNTH, C_WDAY, current_hour);
            //System.out.printf("Sunlight value at %s=%.2f\n", current_hour, test.floatValue());

            assertTrue(test > 0.4f);
        }
    }

}