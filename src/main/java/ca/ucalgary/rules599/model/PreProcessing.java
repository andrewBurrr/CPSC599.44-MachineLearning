package ca.ucalgary.rules599.model;

public class PreProcessing {

    public static int rush_hour_threshold = 20;
    private static Integer[][] rush_hour_averages = null;
    private static Float[] weather_visibility_values = null;
    private static Float[] road_visibility_values = null;
    private static Float weather_weight = 0.5f;
    private static Float road_weight = 0.25f;
    private static Float sunlight_weight = 0.25f;

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

    public static Float evaluate_visibility(String C_YEAR, String C_MNTH, String C_WDAY, String C_HOUR,
                                            String C_WTHR, String C_RALN) {

        return weather_weight*evaluate_weather_visibility(C_WTHR) + road_weight*evaluate_road_visibility(C_RALN) +
                sunlight_weight*evaluate_sunlight(C_YEAR, C_MNTH, C_WDAY, C_HOUR);
    }

    private static Float evaluate_weather_visibility(String C_WTHR) {
        if (weather_visibility_values == null) {
            weather_visibility_values = new Float[7];
            weather_visibility_values[0] = 1.0f;   // value for clear and sunny
            weather_visibility_values[1] = 0.8f;   // value for overcast
            weather_visibility_values[2] = 0.6f;   // value for rain
            weather_visibility_values[3] = 0.4f;   // value for snow
            weather_visibility_values[4] = 0.5f;   // value for freezing rain, sleet, hail
            weather_visibility_values[5] = 0.0f;   // value for visibility limitation
            weather_visibility_values[6] = 1.0f;   // value for strong wind
        }

        int weather_index = 0;
        try {
            weather_index = Integer.parseInt(C_WTHR);
        } catch (NumberFormatException nfe) {
            System.out.printf("Unable to convert C_WTHR to int since it has the value '%s'\n", C_WTHR);
            // assume best case scenario
            return 1.0f;
        }

        // range check
        if (weather_index > 6 || weather_index <= 0) {
            return 1.0f;
        }

        return weather_visibility_values[weather_index-1];
    }

    public static Float[] get_weather_visibility_values() {
        Float[] weather_visibility_clone = new Float[weather_visibility_values.length];
        for (int i=0; i<weather_visibility_values.length; i++) {
            weather_visibility_clone[i] = weather_visibility_values[i].floatValue();
        }

        return weather_visibility_clone;
    }

    private static Float evaluate_road_visibility(String C_RALN) {
        if (road_visibility_values == null) {
            road_visibility_values = new Float[1];
            road_visibility_values[0] = 0.5f;   // value for Bottom of Hill, only value defined so far
        }

        int road_index = 0;
        try {
            road_index = Integer.parseInt(C_RALN);
        } catch (NumberFormatException nfe) {
            System.out.printf("Unable to convert C_RALN to int since it has the value '%s'\n", C_RALN);
            // assume best case scenario
            return 1.0f;
        }

        // range check
        if (road_index > 6 || road_index <= 0) {
            return 1.0f;
        }

        return road_index==6? road_visibility_values[0] : 1.0f;
    }

    // TODO: implement some way to determine sunlight and sunset and solarnoon
    // NOTE: as it stands this function only does a VERY rough calculation of sunlight
    private static Sunlight_Data sunlight_data = new Sunlight_Data();
    private static Float evaluate_sunlight(String C_YEAR, String C_MNTH, String C_WDAY, String C_HOUR) {
        int hour = -1;
        try {
            hour = Integer.parseInt(C_HOUR);
        } catch (NumberFormatException nfe) {
            System.out.printf("Unable to convert C_HOUR to int since it has the value '%s'\n", C_HOUR);
            return 1.0f;
        }

        // range check
        if (hour > 23 || hour < 0) {
            return 1.0f;
        }

        Float sunlight_value = 0.0f;
        if (hour < 12) {
            sunlight_value = (float)hour/11.0f;  // the closer to 11 the higher the sunlight
        } else {
            sunlight_value = 1.0f - (float)(hour-12)/23.0f; // the farther from 12 the lower the sunlight
        }
        return sunlight_value;
    }







}
