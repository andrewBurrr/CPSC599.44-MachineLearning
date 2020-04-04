package ca.ucalgary.rules599.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.cglib.core.Local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.*;

public class Sunlight_Data {

    public static String source_directory = "C:\\Users\\bille\\Desktop\\sunlight data";
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


    /**
     * averages the sunrise across a few cities in canada for that year, month and day
     * @param C_YEAR
     * @param C_MNTH
     * @param C_WDAY
     * @return LocalTime object representing the sunrise time
     */
    public LocalTime get_sunrise(String C_YEAR, String C_MNTH, String C_WDAY) {
        if (all_average_sunrise == null) {
            all_average_sunrise = new HashMap<>();
        }

        int C_YEAR_int = convert_C_YEAR(C_YEAR);
        LocalTime[][] year_data = all_average_sunrise.get(C_YEAR);
        if (year_data == null) {
            // one for each month and for each day such as monday, tuesday....
            year_data = new LocalTime[12][7];
            all_average_sunrise.put(C_YEAR_int, year_data);
        }

        int C_MNTH_int = convert_C_MNTH(C_MNTH);
        int C_WDAY_int = convert_C_WDAY(C_WDAY);
        LocalTime avg_sunrise = year_data[C_MNTH_int-1] // since C_MNTH starts at 1
                                            [C_WDAY_int%7]; // since sunday=7 but we want it to be sunday=0

        if (avg_sunrise == null) {
            // calculate average daylength for this day, month and year
            year_data[C_MNTH_int-1][C_WDAY_int%7] = calculate_average(SUNRISE_DATA_INDEX,
                                                            C_YEAR_int, C_MNTH_int, C_WDAY_int);
        }

        return year_data[C_MNTH_int-1][C_WDAY_int%7];
    }

    /**
     * averages the sunset across a few cities in canada for that year, month and day
     * @param C_YEAR
     * @param C_MNTH
     * @param C_WDAY
     * @return LocalTime object representing the sunset time
     */
    public LocalTime get_sunset(String C_YEAR, String C_MNTH, String C_WDAY) {
        if (all_average_sunset == null) {
            all_average_sunset = new HashMap<>();
        }

        int C_YEAR_int = convert_C_YEAR(C_YEAR);
        LocalTime[][] year_data = all_average_sunset.get(C_YEAR);
        if (year_data == null) {
            // one for each month and for each day such as monday, tuesday....
            year_data = new LocalTime[12][7];
            all_average_sunset.put(C_YEAR_int, year_data);
        }

        int C_MNTH_int = convert_C_MNTH(C_MNTH);
        int C_WDAY_int = convert_C_WDAY(C_WDAY);
        LocalTime avg_sunset = year_data[C_MNTH_int-1] // since C_MNTH starts at 1
                                            [C_WDAY_int%7]; // since sunday=7 but we want it to be sunday=0


        if (avg_sunset == null) {
            // calculate average daylength for this day, month and year
            year_data[C_MNTH_int-1][C_WDAY_int%7] = calculate_average(SUNSET_DATA_INDEX,
                                                        C_YEAR_int, C_MNTH_int, C_WDAY_int);
        }

        return year_data[C_MNTH_int-1][C_WDAY_int%7];
    }

    /**
     * averages the solarNoon across a few cities in canada for that year, month and day
     * @param C_YEAR
     * @param C_MNTH
     * @param C_WDAY
     * @return LocalTime object representing the solarNoon time
     */
    public LocalTime get_solarNoon(String C_YEAR, String C_MNTH, String C_WDAY) {
        if (all_average_solarnoon == null) {
            all_average_solarnoon = new HashMap<>();
        }

        int C_YEAR_int = convert_C_YEAR(C_YEAR);
        LocalTime[][] year_data = all_average_solarnoon.get(C_YEAR);
        if (year_data == null) {
            // one for each month and for each day such as monday, tuesday....
            year_data = new LocalTime[12][7];
            all_average_solarnoon.put(C_YEAR_int, year_data);
        }

        int C_MNTH_int = convert_C_MNTH(C_MNTH);
        int C_WDAY_int = convert_C_WDAY(C_WDAY);
        LocalTime avg_solarnoon = year_data[C_MNTH_int-1] // since C_MNTH starts at 1
                                            [C_WDAY_int%7]; // since sunday=7 but we want it to be sunday=0

        if (avg_solarnoon == null) {
            // calculate average daylength for this day, month and year
            year_data[C_MNTH_int-1][C_WDAY_int%7] = calculate_average(SOLARNOON_DATA_INDEX,
                                                            C_YEAR_int, C_MNTH_int, C_WDAY_int);
        }

        return year_data[C_MNTH_int-1][C_WDAY_int%7];
    }

    /**
     * averages the daylength across a few cities in canada for that year, month and day
     * @param C_YEAR
     * @param C_MNTH
     * @param C_WDAY
     * @return LocalTime object representing the duration of daylength
     */
    public LocalTime get_daylength(String C_YEAR, String C_MNTH, String C_WDAY) {
        if (all_average_daylength == null) {
            all_average_daylength = new HashMap<>();
        }

        int C_YEAR_int = convert_C_YEAR(C_YEAR);
        LocalTime[][] year_data = all_average_daylength.get(C_YEAR);
        if (year_data == null) {
            // one for each month and for each day such as monday, tuesday....
            year_data = new LocalTime[12][7];
            all_average_daylength.put(C_YEAR_int, year_data);
        }

        int C_MNTH_int = convert_C_MNTH(C_MNTH);
        int C_WDAY_int = convert_C_WDAY(C_WDAY);
        LocalTime avg_daylength = year_data[C_MNTH_int-1] // since C_MNTH starts at 1
                                           [C_WDAY_int%7]; // since sunday=7 but we want it to be sunday=0

        if (avg_daylength == null) {
            // calculate average daylength for this day, month and year
            year_data[C_MNTH_int-1][C_WDAY_int%7] = calculate_average(DAYLENGTH_DATA_INDEX,
                                                                C_YEAR_int, C_MNTH_int, C_WDAY_int);
        }

        return year_data[C_MNTH_int-1][C_WDAY_int%7];
    }


    private LocalTime calculate_average(int data_index, int year, int month, int day) {
        if (all_city_data == null) {
            all_city_data = get_all_city_data(source_directory);
        }

        Iterator<String> cities = all_city_data.keySet().iterator();
        long total_nanoseconds = 0;
        int total_days = 0;
        DayOfWeek specified_day = DayOfWeek.of(day);
        while (cities.hasNext()) {
            String curr_city = cities.next();
            System.out.printf("Processing city: %s\n", curr_city);
            LocalDateTime[][][] city_data = all_city_data.get(curr_city)
                                                            .get(year);

            LocalDateTime[][] specified_month = city_data[month-1];
            for (int i=0; i<specified_month.length; i++) {
                LocalDateTime[] current_day = specified_month[i];
                LocalDateTime specified_data = current_day[data_index];

                if (specified_data.getDayOfWeek() == specified_day) {
                    System.out.printf("%s\n", specified_data.toLocalTime().toString());
                    total_nanoseconds += specified_data.toLocalTime().toNanoOfDay();
                    total_days++;
                }
            }
        }

        return LocalTime.ofNanoOfDay(total_nanoseconds/total_days);
    }

    public Integer convert_C_MNTH(String C_MNTH) {
        try {
            Integer C_MNTH_int = Integer.parseInt(C_MNTH);
            return C_MNTH_int;

        } catch (Exception e) {
            System.out.printf("Failed to convert C_MNTH=%s into integer\n", C_MNTH);
            return null;
        }
    }

    public Integer convert_C_YEAR(String C_YEAR) {
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

    public Integer convert_C_WDAY(String C_WDAY) {
        try {
            Integer C_WDAY_int = Integer.parseInt(C_WDAY);
            return C_WDAY_int;

        } catch (Exception e) {
            System.out.printf("Failed to convert C_WDAY=%s into integer\n", C_WDAY);
            return null;
        }
    }


    public HashMap<String, HashMap<Integer, LocalDateTime[][][]> >
            get_all_city_data(String source_directory) {

        File[] directories = new File(source_directory).listFiles();
        HashMap<String, HashMap<Integer, LocalDateTime[][][]> > cities_sunlight_data = new HashMap<>();
        Sunlight_Data sd = new Sunlight_Data();

        for (File dir : directories) {
            HashMap<Integer, LocalDateTime[][][]> city_sunlight_data = new HashMap<>();

            if (dir.isDirectory()) {
                for (File txt_file : dir.listFiles()) {
                    if (!txt_file.isFile())
                        continue;

                    LocalDateTime[][][] city_data_for_one_year = sd.parse_annual_city_data(txt_file);
                    if (city_data_for_one_year != null) {
                        Integer year = city_data_for_one_year[0][0][0].getYear();
                        city_sunlight_data.put(year, city_data_for_one_year);
                        System.out.printf("Processed file: %s\n", txt_file.getName());

                    } else {
                        System.out.printf("Unable to parse file: %s\n", txt_file.getAbsolutePath());
                    }
                }
            }
            String city_name = dir.getName();
            cities_sunlight_data.put(city_name, city_sunlight_data);
            System.out.printf("Processed city: %s\n\n", city_name);

            Iterator<Integer> years_processed = city_sunlight_data.keySet().iterator();
            while (years_processed.hasNext()) {
                Integer current_year = years_processed.next();
                System.out.printf("Year %d\n", current_year);
                LocalDateTime[][][] year_data = city_sunlight_data.get(current_year);
                System.out.printf("%d has %d months\n", current_year, year_data.length);

                // check that each months contain valid days
                for (int i=0; i<year_data.length; i++) {
                    System.out.printf("Month %d has %d days\n", (i+1), year_data[i].length);
                    System.out.printf("first day of month: %s\n", year_data[i][0][0].toString());
                    System.out.printf("last day of month: %s\n", year_data[i][year_data[i].length-1][0].toString());
                }
            }
        }
        return cities_sunlight_data;
    }

    public LocalDateTime[][][] parse_annual_city_data(File file_obj) {
        if (!file_obj.isFile())
            return null;

        String file_path = file_obj.getAbsolutePath();
        List<String> entries = read_file(file_path);
        if (entries == null)
            return null;

        List<String[]> entries_tokenized = tokenizer(entries);
        if (entries_tokenized == null)
            return null;

        String year = entries_tokenized.get(0)[1];
        // create an array for each month
        LocalDateTime[][][] annual_data = new LocalDateTime[12][][];
        int current_month = parse_tokens(year, entries_tokenized.get(1))[0].getMonthValue();
        ArrayList<LocalDateTime[]> days_in_month = new ArrayList<LocalDateTime[]>();
        for (int i=1; i<entries_tokenized.size(); i++) {
            LocalDateTime[] converted_entry = parse_tokens(year, entries_tokenized.get(i));

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


    private final int SUNRISE_INDEX = 4;
    private final int SUNSET_INDEX = 6;
    private final int SOLARNOON_INDEX = 5;
    private final int DAYTIME_INDEX = 11;
    private final int MONTH_INDEX = 0;
    private final int DAY_INDEX = 1;

    // returns LocalTime object representing the sunrise, sunset, daytime and solarNoon of an entry
    // [0] = sunrise
    // [1] = sunset
    // [2] = solarNoon
    // [3] = daytime duration
    public LocalDateTime[] parse_tokens(String year, String[] tokens) {

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                                            .appendPattern("yyyy-MMM-dd[ HH:mm:ss]")
                                            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                                            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                                            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                                            .toFormatter().withLocale(Locale.ENGLISH);

        try {
            String date = year + "-" + tokens[MONTH_INDEX] + "-" + append_trailing_zeroes(tokens[DAY_INDEX], 2);
            LocalDateTime sunrise = LocalDateTime.parse(date + " "
                                            + format_time_string(tokens[SUNRISE_INDEX]) + ":00",
                                            formatter);

            LocalDateTime sunset = LocalDateTime.parse( date + " "
                                            + format_time_string(tokens[SUNSET_INDEX]) + ":00",
                                            formatter);

            LocalDateTime solarNoon = LocalDateTime.parse( date + " "
                                            + format_time_string(tokens[SOLARNOON_INDEX]) + ":00",
                                            formatter);

            int[] daytime_duration = convert_to_time(tokens[DAYTIME_INDEX]);
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

    public String append_trailing_zeroes(String num_str, int string_length) {
        char[] chars = new char[string_length];
        Arrays.fill(chars, '0');
        String pad = new String(chars);

        return (pad + num_str).substring(num_str.length());
    }


    public String format_time_string(String time_str) {
        String[] time_str_tokens = time_str.split(":");

        return ( ("00" + time_str_tokens[0]).substring(time_str_tokens[0].length()) + ":" +
                ("00" + time_str_tokens[1]).substring(time_str_tokens[1].length()));
    }

    public int[] convert_to_time(String hour_string) {
        double hour_magnitude = Float.parseFloat(hour_string);

        int hour =  (int) hour_magnitude;
        int minutes = (int) (hour_magnitude * 60) % 60;
        int seconds = (int) (hour_magnitude * 60 * 60) % 60;

        return new int[] {hour, minutes, seconds};
    }



    // returns a list of string where each element represents a column entry
    // format: Month Day  Nautical Civil  Rise  Noon   Set   Civil Nautical   Day Sky Total  hh:mm:ss
    public List<String[]> tokenizer(List<String> records) {
        List<String[]> tokens = new ArrayList<String[]>();

        // get records' year and city
        String[] record_source_info = null;
        for (int i=3; i<6; i++) {
            String[] record_tokens = records.get(i).trim().split("\\s+");
            if (is_year( record_tokens[record_tokens.length-1] )) {
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
            if (a_record.length()>=3 && is_date(a_record)) {
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

    private boolean is_year(@NotNull String year_string) {
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

    private boolean is_date(String entry) {
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
    public List<String> read_file(String filename)
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
