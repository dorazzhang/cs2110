package cs2110;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Scanner;

/**
 * A collection of static methods for reading/writing gradebook tables in CSV format, transforming
 * their data, and calculating summary statistics.
 */
public class Gradebook {

    // --------------------------------------------------------------------------------------------
    // Section 1: Input / output

    /**
     * Read contents of a csv file and use this to construct a nested row-major sequence of strings.
     * Throws an IOException if `filename` cannot be accessed or read from.
     */
    public static Seq<Seq<String>> constructTable(String filename)
            throws IOException {
        // TODO 17: Implement this method according to its specification.
        // Remember to close any input sources you open (recommendation: use a try-with-resources
        //  statement).
        throw new UnsupportedOperationException();
    }

    /**
     * Convert the contents of `table` into Simplified CSV format and write this to the file located
     * at `filename`. Throws an IOException if `filename` cannot be accessed or written to.
     */
    public static <T> void outputCSV(String filename, Seq<Seq<T>> table) throws IOException {
        // TODO 18: Implement this method according to its specification.
        // Recommendation: Use a `PrintWriter` and a try-with-resources statement.
        throw new UnsupportedOperationException();
    }

    // --------------------------------------------------------------------------------------------
    // Section 2: Table manipulation


    /**
     * Return whether each element of `table` has the same size.
     */
    private static <T> boolean isRectangular(Seq<Seq<T>> table) {
        if (table.size() == 0) {
            return true;
        }
        int width = table.get(0).size();
        for (Seq<T> row : table) {
            if (row.size() != width) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return a new table that is the transpose of the original table. That is, the rows of the
     * original table become the columns of the new table (and vice versa), and the orders of the
     * entries in these rows / columns remains the same. Requires `original` is rectangular.
     */
    public static <T> Seq<Seq<T>> transpose(Seq<Seq<T>> original) {
        assert isRectangular(original);

        Seq<Seq<T>> transposed = new DLinkedSeq<>();

        // TODO 19: Complete the implementation of this method to its specification.
        // Implementation Constraint: The runtime of this method must be linearly proportional to
        //  the number of entries in the table.
        throw new UnsupportedOperationException();
    }

    // --------------------------------------------------------------------------------------------
    // Section 3: Summary statistics

    /**
     * Return the sum of the n'th powers of all numerical entries of `seq`. A numerical entry is any
     * entry that can be successfully parsed to a double by Double.parseDouble().
     */
    public static double powerSum(Seq<String> seq, int n) {
        // TODO 20: Implement this method according to its specification.
        throw new UnsupportedOperationException();
    }

    /**
     * Return the mean of the numerical entries of `seq`.  Returns NaN (aka 0.0/0.0) if `seq` does
     * not contain any numerical entries.
     */
    public static double mean(Seq<String> seq) {
        // TODO 21a: Implement this method according to its specification.
        // Implementation Constraint: Your implementation must use the powerSum() method.
        throw new UnsupportedOperationException();
    }

    /**
     * Return the standard deviation of the numerical entries of `seq`.  Returns NaN (aka 0.0/0.0)
     * if `seq` does not contain any numerical entries.
     */
    public static double stdDev(Seq<String> seq) {
        // TODO 21b: Implement this method according to its specification.
        // Implementation Constraint: Your implementation must use the powerSum() method.
        throw new UnsupportedOperationException();
    }

    /**
     * Append "mean" and "standard deviation" columns to the end of the table `table`. The first row
     * of the table (the column "headers") has "mean" and "standard deviation" appended (in that
     * order). In subsequent rows, the string representations of the mean and standard deviation (in
     * that order) of the numerical entries are appended. All numbers are represented with full
     * precision. Requires that `table` is rectangular with at least one row and that no rows alias
     * one another.
     */
    public static void addSummaryColumns(Seq<Seq<String>> table) {
        // TODO 22a: Implement this method according to its specification.
        throw new UnsupportedOperationException();
    }

    /**
     * Append "mean" and "standard deviation" rows to the bottom of the table `table`. The first
     * entries of these rows are "mean" and "standard deviation" (respectively). The subsequent
     * entries are the mean and standard deviation (respectively) of the numerical entries in that
     * column. All numbers are represented with full precision. Requires that `table` is rectangular
     * with at least one column (implying at least one row).
     */
    public static void addSummaryRows(Seq<Seq<String>> table) {
        // TODO 22b: Implement this method according to its specification.
        throw new UnsupportedOperationException();
    }

    /**
     * Rounds all numerical data within `table` to the specified number of decimal places.
     */
    public static Seq<Seq<String>> roundEntries(Seq<Seq<String>> table, int decimalPlaces) {
        assert decimalPlaces >= 0;
        DecimalFormat df = new DecimalFormat("#." + "#".repeat(decimalPlaces));

        Seq<Seq<String>> rounded = new DLinkedSeq<>();
        for (Seq<String> row : table) {
            Seq<String> roundedRow = new DLinkedSeq<>();
            for (String cell : row) {
                try { // see if cell holds numerical data
                    double d = Double.parseDouble(cell);
                    roundedRow.append(df.format(d));
                } catch (NumberFormatException e) {
                    roundedRow.append(cell);
                }
            }
            rounded.append(roundedRow);
        }

        return rounded;
    }

    // --------------------------------------------------------------------------------------------
    // Section 4: A grading statistics application

    /**
     * A basic application to add summary statistics to a gradebook spreadsheet table. Takes in two
     * program arguments: the filepath of the input csv and the filepath for the output csv.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: Gradebook <input filename> <output filename>");
            System.exit(1);
        }
        String inputFilename = args[0];
        String outputFilename = args[1];

        Seq<Seq<String>> gradebook = constructTable(inputFilename);

        // TODO (challenge extension): Uncomment this line after implementing `rectangularize()`,
        //  then test with a ragged input CSV.
//        rectangularize(gradebook);

        // Verify preconditions
        if (gradebook.size() == 0) {
            System.err.println("Gradebook must have at least one row");
            System.exit(1);
        }

        addSummaryColumns(gradebook);
        addSummaryRows(gradebook);
        gradebook = roundEntries(gradebook, 5);
        outputCSV(outputFilename, gradebook);
    }
}
