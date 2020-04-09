package ca.ucalgary.rules599.util;

import ca.ucalgary.rules599.model.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import static ca.ucalgary.rules599.util.Condition.ensureNotEmpty;
import static ca.ucalgary.rules599.util.Condition.ensureNotNull;

public class DataIterator implements Iterator<Transaction<AccidentAttribute>> {

    /**
     * An implementation of the interface {@link Transaction}. Each transaction corresponds to a
     * single line of a text file.
     */
    public static class TransactionImplementation implements Transaction<AccidentAttribute> {

        /**
         * The line, the transaction corresponds to.
         */
        private final String line;

        /**
         * The type of file to be read
         */
        private final int type;


        /**
         * Creates a new implementation of the interface {@link Transaction}.
         *
         * @param line The line, the transaction corresponds to, as a {@link String}. The line may
         *             neither be null, nor empty
         */
        public TransactionImplementation(@NotNull final String line, int type) {
            ensureNotNull(line, "The line may not be null");
            ensureNotEmpty(line, "The line may not be empty");
            ensureNotNull(type, "The line may not be null");
            this.line = line;
            this.type = type;
        }

        @NotNull
        @Override
        public Iterator<AccidentAttribute> iterator() {
            return new LineIterator(line, this.type);
        }

    }

    /**
     * An iterator, which allows to iterate the items, which are contained by a single line of a
     * text file.
     */
    private static class LineIterator implements Iterator<AccidentAttribute> {

        /**
         * The tokenizer, which is used by the iterator.
         */
        private final StringTokenizer tokenizer;

        /**
         * Creates a new iterator, which allows to iterate the items, which are contained by a
         * single line of a text file.
         *
         * @param line The line, which should be tokenized by the iterator, as a {@link String}. The
         *             line may neither be null, nor empty
         */
        LineIterator(@NotNull final String line, int type) {
            ensureNotNull(line, "The line may not be null");
            ensureNotEmpty(line, "The line may not be empty");
            ensureNotNull(type, "The line may not be null");
            this.tokenizer = new StringTokenizer(line, ",");
        }

        @Override
        public boolean hasNext() {
            return tokenizer.hasMoreTokens();
        }

        @Override
        public AccidentAttribute next() {
            String token = tokenizer.nextToken();
               return new AccidentMinerModel().getItems(token);


        }

    }

    /**
     * The text file, which is read by the iterator.
     */
    private final File file;
    /**
     * The type of file iterating through, 1, Original input file, 2 Augmented Drivers file.
     */
    private final int type;

    /**
     * The read, which is used the text file.
     */
    private BufferedReader reader;

    /**
     * The next line, which is read by the iterator.
     */
    private String nextLine;

    /**
     * True, if the end of the text file has been reached.
     */
    private boolean reachedEnd;

    /**
     * True, if the first line is a header.
     */
    private boolean skipFirstLine;

    /**
     * Opens the reader, which is used to read the text file, if it is not opened yet.
     */
    private void openReader() {
        if (reader == null) {
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } catch (IOException e) {
                String message = "Failed to open file " + file;
                LOGGER.error(message, e);
                throw new RuntimeException(message, e);
            }
        }
    }

    /**
     * Closes the reader, which is used to read the text file, if it is opened.
     */
    private void closeReader() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                // No need to handle
            } finally {
                reader = null;
            }
        }
    }

    /**
     * Reads the next line from the text file.
     *
     * @return The next line as a {@link String} or null, if the end of the file is reached
     */
    private String readNextLine() {
        try {

            if(this.skipFirstLine){
                reader.readLine();
                this.skipFirstLine=false;
            }
            String nextLine = reader.readLine();

            if (nextLine != null) {
                if (!nextLine.matches("\\s*") && !nextLine.startsWith("#")) {
                    return nextLine.replace(" ","");
                } else {
                    return readNextLine();
                }
            }

            closeReader();
            reachedEnd = true;
            return null;
        } catch (IOException e) {
            String message = "Failed to read file " + file;
            LOGGER.error(message, e);
            throw new RuntimeException(message);
        }
    }

    /**
     * The SL4J logger, which is used by the iterator.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataIterator.class);

    /**
     * Creates a new iterator, which allows to iterate the transactions, which are contained by a
     * text file.
     *
     * @param file The text file, which should be read by the iterator, as an instance of the class
     *             {@link File}. The file may not be null
     */
    public DataIterator(@NotNull final File file,int type, boolean skipFirstLine) {
        ensureNotNull(file, "The file may not be null");
        this.file = file;
        this.reader = null;
        this.nextLine = null;
        this.reachedEnd = false;
        this.skipFirstLine = skipFirstLine;
        this.type=type;
    }

    @Override
    public final boolean hasNext() {
        openReader();

        if (nextLine == null && !reachedEnd) {
            nextLine = readNextLine();
        }

        return nextLine != null && !reachedEnd;
    }

    @Override
    public final Transaction<AccidentAttribute> next() {
        if (hasNext()) {
            Transaction<AccidentAttribute> transaction = new TransactionImplementation(nextLine,this.type);
            nextLine = null;
            return transaction;
        }

        throw new NoSuchElementException();
    }

}