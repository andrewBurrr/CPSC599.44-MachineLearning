package ca.ucalgary.rules599.util;

import ca.ucalgary.rules599.model.Item;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.opencsv.CSVWriter.DEFAULT_ESCAPE_CHARACTER;

public class CsvWriter {
        public static <T extends Item> void OpenCSVWriter(List<T> ObjectToWrite, String fileName) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
            try (
                    Writer writer = Files.newBufferedWriter(Paths.get(fileName));
            ) {
                StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withEscapechar(DEFAULT_ESCAPE_CHARACTER)
                        .build();
                beanToCsv.write(ObjectToWrite);
            }

        }


    public static void OpenCSVStringWriter(List<List<String>> ObjectToWrite, String fileName) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(fileName));
        ) {
            StatefulBeanToCsv<List<List<String>>> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withEscapechar(DEFAULT_ESCAPE_CHARACTER)
                    .build();
            beanToCsv.write(ObjectToWrite);
        }

    }


}

