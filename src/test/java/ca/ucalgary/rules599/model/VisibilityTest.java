package ca.ucalgary.rules599.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class VisibilityTest {


    @Test
    public void test_correct_weather_input() {
        String C_YEAR = "16";
        String C_MNTH = "01";
        String C_WDAY = "01";
        String C_HOUR = "11";
        String C_WTHR = "01";
        String C_RALN = "1";
        Float test1 = PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                                                        C_WTHR, C_RALN);
        Float actual1 = 1.0f;
        assertEquals(test1, actual1, 0.001);

        C_WTHR = "02";
        Float test2 = PreProcessing.evaluate_visibility(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);
        Float actual2 = 0.90f;
        assertEquals(test2, actual2, 0.001);
    }

    /*
    @Test
    public void evaluate_visibility() {
    }

    @Test
    public void test_test_correct_road_alignment_input() {

    }

    @Test
    public void test_correct_sunlight_input() {

    }

    @Test
    public void test_sunlight_values() {

    }
    */
}