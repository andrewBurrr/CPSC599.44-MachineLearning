package ca.ucalgary.rules599.Training;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 * TODO replace all print statements with log calls
 * Class containing the implementation for visibility PreProcessing.
 * Visibility evaluation is split into three parts:
 * - weather visibilty
 * - road configuration visibility
 * - sunlight visibility
 * Most of the code in this class are helper methods for processing sunrise, sunset, solarnoon data used
 * for evaluating sunlight visibility
 * @author Benedict Mendoza
 * @since 2020-04-07
 */
public class Visibility_PreProcessing {


//    public static void main(String[] args) {
//        //String source_directory = "C:\\Users\\bille\\Desktop\\sunlight data";
//
//        Sunlight_Data sd = new Sunlight_Data();
//        LocalTime average_daytime_jan_monday_1999 = sd.get_daylength("99", "1", "1");
//        System.out.printf("Average daylength of mondays on january 1999: %s\n",
//                            average_daytime_jan_monday_1999.toString());
//
//        LocalTime average_solarnoon_jan_monday_1999 = sd.get_solarNoon("99", "1", "1");
//        System.out.printf("Average solarnoon of mondays on january 1999: %s\n",
//                            average_solarnoon_jan_monday_1999.toString());
//
//        LocalTime average_sunset_jan_monday_1999 = sd.get_sunset("99", "1", "1");
//        System.out.printf("Average sunset of mondays on january 1999: %s\n",
//                            average_sunset_jan_monday_1999.toString());
//
//        LocalTime average_sunrise_jan_monday_1999 = sd.get_sunrise("99", "1", "1");
//        System.out.printf("Average sunrise of mondays on january 1999: %s\n",
//                            average_sunrise_jan_monday_1999.toString());
//
//    }

    private static Float[] weather_visibility_values = null;
    private static Float[] road_alignment_penalties = null;
    private static Float weather_weight = 0.6f;
    private static Float road_alignment_penalty = 0.25f;
    private static Float sunlight_weight = 0.4f;

    /**
     * TODO enumerate output
     * evaluates road visibility based on weather, date, time and road configuration
     * outputs three 3 values representing
     * good visibility = 2
     * fair visibility = 1
     * bad visibility = 0
     * @param C_YEAR: year value from the dataset
     * @param C_MNTH: month value from the dataset
     * @param C_WDAY: day value from the dataset
     * @param C_HOUR: hour value from the dataset
     * @param C_WTHR: weather value from the dataset
     * @param C_RALN: road alignment value from the dataset
     * @return: integers 0, 1, 2, representing good, fair and bad visibility respectively
     */
    public static int evaluate_visibility(String C_YEAR, String C_MNTH, String C_WDAY, String C_HOUR,
                                          String C_WTHR, String C_RALN) {

        // if poor weather visibility then it's poor visibility straightaway
        if (evaluate_weather_visibility(C_WTHR) == 0.0f)
            return 0;

        Float visibility_value = get_visibility_value(C_YEAR, C_MNTH, C_WDAY, C_HOUR,
                C_WTHR, C_RALN);

        if (visibility_value < 0.4f) {      // poor visibility evaluation
            return 0;
        } else if (visibility_value < 0.7f) {  // fair visibility evaluation
            return 1;
        } else {
            return 2;                           // good visibility evaluation
        }
    }

    /**
     * evaluates road visibility based on weather, date, time and road configuration
     * 60% of visibility is based on weather
     * 40% of visibility is based on sunlight
     * There is a 20% visibility bonus if road alignment is at the bottom of the hill
     * @param C_YEAR: year value from the dataset
     * @param C_MNTH: month value from the dataset
     * @param C_WDAY: day value from the dataset
     * @param C_HOUR: hour value from the dataset
     * @param C_WTHR: weather value from the dataset
     * @param C_RALN: road alignment value from the dataset
     * @return precise visibility value between 0 to 1
     */
    public static Float get_visibility_value(String C_YEAR, String C_MNTH, String C_WDAY, String C_HOUR,
                                             String C_WTHR, String C_RALN) {

        return (weather_weight * evaluate_weather_visibility(C_WTHR)
                + sunlight_weight * evaluate_sunlight(C_YEAR, C_MNTH, C_WDAY, C_HOUR))
                - (road_alignment_penalty * (evaluate_road_alignment_penalty(C_RALN)) );
    }

    /**
     * Evaluates weather conditions and quantifies them
     * Contains the attribute to value mapping for weather conditions
     * @param C_WTHR: weather value from the dataset
     * @return number equivalent to C_WTHR between 0 to 1, 0 being the worst to 1 being the best
     */
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

    /**
     * Evaluates road alignment and quantifies it
     * Contains the attribute to value mapping for road alignment
     * at the moment, only 'bottom of hill' road alignment maps to a value
     * @param C_RALN: road alignment value from the dataset
     * @return number equivalent to C_RALN between 0 to 1, 0 being the worst to 1 being the best
     */
    public static Float evaluate_road_alignment_penalty(String C_RALN) {
        if (road_alignment_penalties == null) {
            road_alignment_penalties = new Float[1];
            road_alignment_penalties[0] = 1.0f;   // value for Bottom of Hill, only value defined so far
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

        return road_index==6? road_alignment_penalties[0] : 0.0f;
    }

    /**
     * Evaluates amount of natural light outside and quantifies it
     * Makes use of sunset, sunrise, solarnoon averages across multiple cities in Canada
     * @param C_YEAR: year value from the dataset
     * @param C_MNTH: month value from the dataset
     * @param C_WDAY: day value from the dataset
     * @param C_HOUR: hour value from the dataset
     * @return value representing natural light present at the time, 0 being the worst to 1 being the best
     */
    public static Float evaluate_sunlight(String C_YEAR, String C_MNTH, String C_WDAY, String C_HOUR) {
        int hour = -1;
        try {
            hour = Integer.parseInt(C_HOUR);
        } catch (NumberFormatException nfe) {
            System.out.printf("Unable to convert C_HOUR to int since it has the value '%s'\n", C_HOUR);
            return 1.0f;    // assume best case sunlight value
        }
        // range check
        if (hour > 23 || hour < 0) {
            return 1.0f; // if hour is unknown assume best case sunlight value
        }
        // check if hour is between sunset and sunrise
        // hence hour happened at night
        LocalTime sunset = Visibility_PreProcessing.get_sunset(C_YEAR, C_MNTH, C_WDAY);
        LocalTime sunrise = Visibility_PreProcessing.get_sunrise(C_YEAR, C_MNTH, C_WDAY);
        if (hour>sunset.getHour() || hour<sunrise.getHour() ) {
            return 0.0f;
        }

        // compute sunlight value relative to how close it is to solarnoon
        LocalTime solarnoon = Visibility_PreProcessing.get_solarnoon(C_YEAR, C_MNTH, C_WDAY);
        LocalTime daylength = Visibility_PreProcessing.get_daylength(C_YEAR, C_MNTH, C_WDAY);
        Float penalty = Math.abs((float) (solarnoon.getHour() - hour));
        penalty /= daylength.getHour();
        Float sunlight_value = 1.0f - penalty;

        return sunlight_value;
    }


    // ** EVERYTHING BELOW HERE ARE HELPERS NEEDED TO CALCULATE SUNLIGHT VISIBILITY **
    // ** TODO Maybe turn everything below here into an inner class                 **
    // **                                                                           **
    // *******************************************************************************

    //@Value("${sunrise.data}") String source_directory;
    public static String source_directory = "src/main/java/ca/ucalgary/rules599/Training/sunlightData";
    // [0] = sunrise
    // [1] = sunset
    // [2] = solarNoon
    // [3] = daytime duration
    private static int SUNRISE_DATA_INDEX = 0;
    private static int SUNSET_DATA_INDEX = 1;
    private static int SOLARNOON_DATA_INDEX = 2;
    private static int DAYLENGTH_DATA_INDEX = 3;

    private static HashMap<String, HashMap<Integer, LocalDateTime[][][]> > all_city_data = null;
    private static HashMap<Integer, LocalTime[][]> all_average_daylength = null;
    private static HashMap<Integer, LocalTime[][]> all_average_solarnoon = null;
    private static HashMap<Integer, LocalTime[][]> all_average_sunset = null;
    private static HashMap<Integer, LocalTime[][]> all_average_sunrise = null;


    /**
     * averages the sunrise across a few cities in canada for that year, month and day
     * @param C_YEAR
     * @param C_MNTH
     * @param C_WDAY
     * @return LocalTime object representing the sunrise time
     */
    public static LocalTime get_sunrise(String C_YEAR, String C_MNTH, String C_WDAY) {
        if (all_average_sunrise == null) {
            all_average_sunrise = new HashMap<>();
        }

        Integer C_MNTH_int = convert_C_MNTH(C_MNTH);
        Integer C_WDAY_int = convert_C_WDAY(C_WDAY);
        Integer C_YEAR_int = convert_C_YEAR(C_YEAR);
        if (C_MNTH_int==null || C_WDAY_int==null || C_YEAR_int==null)
            return null;

        LocalTime[][] year_data = all_average_sunrise.get(C_YEAR_int);
        if (year_data == null) {
            // one for each month and for each day such as monday, tuesday....
            year_data = new LocalTime[12][7];
            all_average_sunrise.put(C_YEAR_int, year_data);
        }

        LocalTime avg_sunrise = year_data[C_MNTH_int-1] // since C_MNTH starts at 1
                [C_WDAY_int%7]; // since sunday=7 but we want it to be sunday=0

        if (avg_sunrise == null) {
            // calculate average daylength for this day, month and year
            year_data[C_MNTH_int-1][C_WDAY_int%7] = calculate_average(SUNRISE_DATA_INDEX,
                    C_YEAR_int, C_MNTH_int, C_WDAY_int);
        }

        System.out.printf("average sunrise at %s-%s-%s = %s\n",
                C_YEAR, C_MNTH_int, C_WDAY, year_data[C_MNTH_int-1][C_WDAY_int%7].toString());
        return year_data[C_MNTH_int-1][C_WDAY_int%7];
    }

    /**
     * averages the sunset across a few cities in canada for that year, month and day
     * @param C_YEAR
     * @param C_MNTH
     * @param C_WDAY
     * @return LocalTime object representing the sunset time
     */
    public static LocalTime get_sunset(String C_YEAR, String C_MNTH, String C_WDAY) {
        if (Visibility_PreProcessing.all_average_sunset == null) {
            Visibility_PreProcessing.all_average_sunset = new HashMap<>();
        }

        Integer C_MNTH_int = convert_C_MNTH(C_MNTH);
        Integer C_WDAY_int = convert_C_WDAY(C_WDAY);
        Integer C_YEAR_int = convert_C_YEAR(C_YEAR);
        if (C_MNTH_int==null || C_WDAY_int==null || C_YEAR_int==null)
            return null;

        LocalTime[][] year_data = Visibility_PreProcessing.all_average_sunset.get(C_YEAR_int);
        if (year_data == null) {
            // one for each month and for each day such as monday, tuesday....
            year_data = new LocalTime[12][7];
            Visibility_PreProcessing.all_average_sunset.put(C_YEAR_int, year_data);
        }

        LocalTime avg_sunset = year_data[C_MNTH_int-1] // since C_MNTH starts at 1
                [C_WDAY_int%7]; // since sunday=7 but we want it to be sunday=0


        if (avg_sunset == null) {
            // calculate average daylength for this day, month and year
            year_data[C_MNTH_int-1][C_WDAY_int%7] = calculate_average(Visibility_PreProcessing.SUNSET_DATA_INDEX,
                    C_YEAR_int, C_MNTH_int, C_WDAY_int);
        }

        System.out.printf("average sunset at %s-%s-%s = %s\n",
                C_YEAR, C_MNTH_int, C_WDAY, year_data[C_MNTH_int-1][C_WDAY_int%7].toString());
        return year_data[C_MNTH_int-1][C_WDAY_int%7];
    }

    /**
     * averages the solarNoon across a few cities in canada for that year, month and day
     * @param C_YEAR
     * @param C_MNTH
     * @param C_WDAY
     * @return LocalTime object representing the solarNoon time
     */
    public static LocalTime get_solarnoon(String C_YEAR, String C_MNTH, String C_WDAY) {
        if (all_average_solarnoon == null) {
            all_average_solarnoon = new HashMap<>();
        }

        Integer C_MNTH_int = convert_C_MNTH(C_MNTH);
        Integer C_WDAY_int = convert_C_WDAY(C_WDAY);
        Integer C_YEAR_int = convert_C_YEAR(C_YEAR);
        if (C_MNTH_int==null || C_WDAY_int==null || C_YEAR_int==null)
            return null;

        LocalTime[][] year_data = all_average_solarnoon.get(C_YEAR_int);
        if (year_data == null) {
            // one for each month and for each day such as monday, tuesday....
            year_data = new LocalTime[12][7];
            all_average_solarnoon.put(C_YEAR_int, year_data);
        }

        LocalTime avg_solarnoon = year_data[C_MNTH_int-1] // since C_MNTH starts at 1
                [C_WDAY_int%7]; // since sunday=7 but we want it to be sunday=0

        if (avg_solarnoon == null) {
            // calculate average daylength for this day, month and year
            year_data[C_MNTH_int-1][C_WDAY_int%7] = calculate_average(SOLARNOON_DATA_INDEX,
                    C_YEAR_int, C_MNTH_int, C_WDAY_int);
        }

        System.out.printf("average solarnoon at %s-%s-%s = %s\n",
                C_YEAR, C_MNTH_int, C_WDAY, year_data[C_MNTH_int-1][C_WDAY_int%7].toString());
        return year_data[C_MNTH_int-1][C_WDAY_int%7];
    }

    /**
     * averages the daylength across a few cities in canada for that year, month and day
     * @param C_YEAR
     * @param C_MNTH
     * @param C_WDAY
     * @return LocalTime object representing the duration of daylength
     */
    public static LocalTime get_daylength(String C_YEAR, String C_MNTH, String C_WDAY) {
        if (all_average_daylength == null) {
            all_average_daylength = new HashMap<>();
        }

        Integer C_MNTH_int = convert_C_MNTH(C_MNTH);
        Integer C_WDAY_int = convert_C_WDAY(C_WDAY);
        Integer C_YEAR_int = convert_C_YEAR(C_YEAR);
        if (C_MNTH_int==null || C_WDAY_int==null || C_YEAR_int==null)
            return null;

        LocalTime[][] year_data = all_average_daylength.get(C_YEAR_int);
        if (year_data == null) {
            // one for each month and for each day such as monday, tuesday....
            year_data = new LocalTime[12][7];
            all_average_daylength.put(C_YEAR_int, year_data);
        }

        LocalTime avg_daylength = year_data[C_MNTH_int-1] // since C_MNTH starts at 1
                [C_WDAY_int%7]; // since sunday=7 but we want it to be sunday=0

        if (avg_daylength == null) {
            // calculate average daylength for this day, month and year
            year_data[C_MNTH_int-1][C_WDAY_int%7] = calculate_average(DAYLENGTH_DATA_INDEX,
                    C_YEAR_int, C_MNTH_int, C_WDAY_int);
        }

        System.out.printf("average daylength at %s-%s-%s = %s\n",
                C_YEAR, C_MNTH_int, C_WDAY, year_data[C_MNTH_int-1][C_WDAY_int%7].toString());
        return year_data[C_MNTH_int-1][C_WDAY_int%7];
    }


    private static LocalTime calculate_average(int data_index, int year, int month, int day) {
        if (all_city_data == null) {
            all_city_data = get_all_city_data(source_directory);
        }

        Iterator<String> cities = all_city_data.keySet().iterator();
        long total_nanoseconds = 0;
        int total_days = 0;
        DayOfWeek specified_day = DayOfWeek.of(day);
        while (cities.hasNext()) {
            String curr_city = cities.next();
            //System.out.printf("Processing city: %s\n", curr_city);
            LocalDateTime[][][] city_data = all_city_data.get(curr_city)
                    .get(year);

            LocalDateTime[][] specified_month = city_data[month-1];
            for (int i=0; i<specified_month.length; i++) {
                LocalDateTime[] current_day = specified_month[i];
                LocalDateTime specified_data = current_day[data_index];

                if (specified_data.getDayOfWeek() == specified_day) {
                    //System.out.printf("%s\n", specified_data.toLocalTime().toString());
                    total_nanoseconds += specified_data.toLocalTime().toNanoOfDay();
                    total_days++;
                }
            }
        }

        return LocalTime.ofNanoOfDay(total_nanoseconds/total_days);
    }

    private static Integer convert_C_MNTH(String C_MNTH) {
        try {
            return Integer.parseInt(C_MNTH);

        } catch (Exception e) {
            System.out.printf("Failed to convert C_MNTH=%s into integer\n", C_MNTH);
            return null;
        }
    }

    public static Integer convert_C_YEAR(String C_YEAR) {
        try {
            Integer C_YEAR_int = Integer.parseInt(C_YEAR);
            if (C_YEAR_int <= 20) {
                C_YEAR_int += 2000;
            } else {
                C_YEAR_int += 1900;
            }
            return C_YEAR_int;

        } catch (Exception e) {
            System.out.printf("Failed to convert C_YEAR=%s into integer\n", C_YEAR);
            return null;
        }
    }

    private static Integer convert_C_WDAY(String C_WDAY) {
        try {
            return Integer.parseInt(C_WDAY);

        } catch (Exception e) {
            System.out.printf("Failed to convert C_WDAY=%s into integer\n", C_WDAY);
            return null;
        }
    }


    private static HashMap<String, HashMap<Integer, LocalDateTime[][][]> >
    get_all_city_data(String source_directory) {

        File[] directories = new File(source_directory).listFiles();
        HashMap<String, HashMap<Integer, LocalDateTime[][][]> > cities_sunlight_data = new HashMap<>();
        //Sunlight_Data sd = new Sunlight_Data();

        for (File dir : directories) {
            HashMap<Integer, LocalDateTime[][][]> city_sunlight_data = new HashMap<>();

            if (dir.isDirectory()) {
                for (File txt_file : dir.listFiles()) {
                    if (!txt_file.isFile())
                        continue;

                    LocalDateTime[][][] city_data_for_one_year = Visibility_PreProcessing.parse_annual_city_data(txt_file);
                    if (city_data_for_one_year != null) {
                        Integer year = city_data_for_one_year[0][0][0].getYear();
                        city_sunlight_data.put(year, city_data_for_one_year);
//                        System.out.printf("Processed file: %s\n", txt_file.getName());

                    } else {
                        System.out.printf("Unable to parse file: %s\n", txt_file.getAbsolutePath());
                    }
                }
            }
            String city_name = dir.getName();
            cities_sunlight_data.put(city_name, city_sunlight_data);
            System.out.printf("Processed city: %s\n\n", city_name);

//            Iterator<Integer> years_processed = city_sunlight_data.keySet().iterator();
////            while (years_processed.hasNext()) {
////                Integer current_year = years_processed.next();
////                System.out.printf("Year %d\n", current_year);
////                LocalDateTime[][][] year_data = city_sunlight_data.get(current_year);
////                System.out.printf("%d has %d months\n", current_year, year_data.length);
////
////                // check that each months contain valid days
////                for (int i=0; i<year_data.length; i++) {
////                    System.out.printf("Month %d has %d days\n", (i+1), year_data[i].length);
////                    System.out.printf("first day of month: %s\n", year_data[i][0][0].toString());
////                    System.out.printf("last day of month: %s\n", year_data[i][year_data[i].length-1][0].toString());
////                }
////            }
        }
        return cities_sunlight_data;
    }

    private static LocalDateTime[][][] parse_annual_city_data(File file_obj) {
        if (!file_obj.isFile())
            return null;

        String file_path = file_obj.getAbsolutePath();
        List<String> entries = Visibility_PreProcessing.read_file(file_path);
        if (entries == null)
            return null;

        List<String[]> entries_tokenized = tokenizer(entries);
        if (entries_tokenized == null)
            return null;

        String year = entries_tokenized.get(0)[1];
        // create an array for each month
        LocalDateTime[][][] annual_data = new LocalDateTime[12][][];
        int current_month = Visibility_PreProcessing.parse_tokens(year, entries_tokenized.get(1))[0]
                .getMonthValue();
        ArrayList<LocalDateTime[]> days_in_month = new ArrayList<LocalDateTime[]>();
        for (int i=1; i<entries_tokenized.size(); i++) {
            LocalDateTime[] converted_entry = Visibility_PreProcessing.parse_tokens(year, entries_tokenized.get(i));

            if (converted_entry[0].getMonthValue() != current_month) {
                // convert ArrayList object into array
                LocalDateTime[][] days_in_month_array = new LocalDateTime[days_in_month.size()][4];
                days_in_month_array = days_in_month.toArray(days_in_month_array);
                // store Array of LocalDateTime representing one month
                annual_data[current_month-1] = days_in_month_array;
                // move on to the next month
                current_month = converted_entry[0].getMonthValue();
                days_in_month = new ArrayList<>();
            }
            days_in_month.add(converted_entry);
        }
        LocalDateTime[][] days_in_month_array = new LocalDateTime[days_in_month.size()][4];
        days_in_month_array = days_in_month.toArray(days_in_month_array);
        // store Array of LocalDateTime representing one month
        annual_data[current_month-1] = days_in_month_array;

        return annual_data;
    }


    private static final int SUNRISE_INDEX = 4;
    private static final int SUNSET_INDEX = 6;
    private static final int SOLARNOON_INDEX = 5;
    private static final int DAYTIME_INDEX = 11;
    private static final int MONTH_INDEX = 0;
    private static final int DAY_INDEX = 1;

    // returns LocalTime object representing the sunrise, sunset, daytime and solarNoon of an entry
    // [0] = sunrise
    // [1] = sunset
    // [2] = solarNoon
    // [3] = daytime duration
    private static LocalDateTime[] parse_tokens(String year, String[] tokens) {

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MMM-dd[ HH:mm:ss]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter().withLocale(Locale.ENGLISH);

        try {
            String date = year + "-" + tokens[MONTH_INDEX] + "-"
                    + Visibility_PreProcessing.append_trailing_zeroes(tokens[DAY_INDEX], 2);
            LocalDateTime sunrise = LocalDateTime.parse(date + " "
                            + Visibility_PreProcessing.format_time_string(tokens[SUNRISE_INDEX]) + ":00",
                    formatter);

            LocalDateTime sunset = LocalDateTime.parse( date + " "
                            + Visibility_PreProcessing.format_time_string(tokens[SUNSET_INDEX]) + ":00",
                    formatter);

            LocalDateTime solarNoon = LocalDateTime.parse( date + " "
                            + Visibility_PreProcessing.format_time_string(tokens[SOLARNOON_INDEX]) + ":00",
                    formatter);

            int[] daytime_duration = Visibility_PreProcessing.convert_to_time(tokens[DAYTIME_INDEX]);
            LocalDateTime daytime = LocalDateTime.parse( date + " "
                            + String.format("%02d:%02d:%02d",
                    daytime_duration[0], daytime_duration[1], daytime_duration[2]),
                    formatter);

            return new LocalDateTime[]{sunrise, sunset, solarNoon, daytime};

        } catch (Exception e) {
            System.out.printf("Exception occured while parsing tokens: ");
            for (int i=0; i<tokens.length; i++) {
                System.out.printf("%s, ", tokens[i]);
            }
            System.out.println("");
            e.printStackTrace();
            return null;
        }

    }

    private static String append_trailing_zeroes(String num_str, int string_length) {
        char[] chars = new char[string_length];
        Arrays.fill(chars, '0');
        String pad = new String(chars);

        return (pad + num_str).substring(num_str.length());
    }


    private static String format_time_string(String time_str) {
        String[] time_str_tokens = time_str.split(":");

        return ( ("00" + time_str_tokens[0]).substring(time_str_tokens[0].length()) + ":" +
                ("00" + time_str_tokens[1]).substring(time_str_tokens[1].length()));
    }

    private static int[] convert_to_time(String hour_string) {
        double hour_magnitude = Float.parseFloat(hour_string);

        int hour =  (int) hour_magnitude;
        int minutes = (int) (hour_magnitude * 60) % 60;
        int seconds = (int) (hour_magnitude * 60 * 60) % 60;

        return new int[] {hour, minutes, seconds};
    }


    // returns a list of string where each element represents a column entry
    // format: Month Day  Nautical Civil  Rise  Noon   Set   Civil Nautical   Day Sky Total  hh:mm:ss
    private static List<String[]> tokenizer(List<String> records) {
        List<String[]> tokens = new ArrayList<String[]>();

        // get records' year and city
        String[] record_source_info = null;
        for (int i=3; i<6; i++) {
            String[] record_tokens = records.get(i).trim().split("\\s+");
            if (Visibility_PreProcessing.is_year( record_tokens[record_tokens.length-1] )) {
                record_source_info = record_tokens;
                break;
            }
        }

        if (record_source_info == null) {
            System.out.printf("Unable to retrieve year\n");
            return null;
        }
        tokens.add(new String[]{ record_source_info[1].replace(",", ""),
                record_source_info[record_source_info.length-1].trim()});

        for (int i=0; i<records.size(); i++) {
            String a_record = records.get(i).trim();
            if (a_record.length()>=3 && Visibility_PreProcessing.is_date(a_record)) {
                String[] record_tokenized = a_record.split("\\s+");
                /*
                //System.out.printf("Number of tokens %d\n", record_tokenized.length);
                for (int j=0; j<record_tokenized.length; j++) {
                    System.out.printf("%s\n", record_tokenized[j]);
                }
                break;
                */
                tokens.add(record_tokenized);

            } else {
                //System.out.printf("Skipping entry: %s\n", a_record);
            }
        }

        return tokens;
    }

    private static boolean is_year(@NotNull String year_string) {
        if (year_string.length() != 4) {
            return false;
        }
        try {
            Integer.parseInt(year_string);
            return true;

        } catch (Exception e) {
            System.out.printf("%s is not a year\n", year_string);
            return false;
        }
    }

    private static boolean is_date(String entry) {
        String month_data = entry.substring(0, 3).toLowerCase();

        switch(month_data) {
            case "jan":
            case "feb":
            case "mar":
            case "apr":
            case "may":
            case "jun":
            case "jul":
            case "aug":
            case "sep":
            case "oct":
            case "nov":
            case "dec":
                return true;
            default:
                return false;
        }
    }

    /**
     * Open and read a file, and return the lines in the file as a list
     * of Strings.
     * (Demonstrates Java FileReader, BufferedReader, and Java5.)
     */
    private static List<String> read_file(String filename)
    {
        List<String> records = new ArrayList<String>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                records.add(line);
            }
            reader.close();
            return records;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }
}
