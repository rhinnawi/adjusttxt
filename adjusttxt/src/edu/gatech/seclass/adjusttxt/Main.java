package edu.gatech.seclass.adjusttxt;

import java.io.*;
import java.util.HashMap;

public class Main {
    // Empty Main class for compiling Individual Project
    // During Deliverable 1 and Deliverable 2, DO NOT ALTER THIS CLASS or implement it
    private static Integer skip;
    private static String spacing;
    private static boolean removeEmptyLines = false;
    private static String reverseLine;
    private static String prefix = "";
    private static File file;

    public static void main(String[] args) {
        ParseArgs parseArgs;
        String output;
        HashMap<String, Object> options;

        try {
            parseArgs = new ParseArgs(args);
            options = parseArgs.getOptions();

            // Set all options
            skip = (Integer) options.getOrDefault("skip", null);
            spacing = (String) options.getOrDefault("spacing", null);
            removeEmptyLines = (Boolean) options.getOrDefault(
                    "removeEmptyLines", false);
            reverseLine = (String) options.getOrDefault("reverseLine", null);
            prefix = (String) options.getOrDefault("prefix", "");
            file = (File) options.get("file");

            // Generate output and print to stdout
            output = generateOutput();
            System.out.print(output);
        } catch (Exception e) {
//            System.out.println(e.toString());
            usage();
        }
    }

    // If tests fail, convert back to private and copy into ParseArgs
    private static void usage() {
        System.err.println(
                "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
    }

    /**
     * Processes input file to generate output String
     *
     * @return String to be outputted to console
     * @throws IllegalArgumentException when file doesn't end in newline
     */
    private static String generateOutput() {
        StringBuilder out = new StringBuilder();

        // Parse through all lines. Update in order -s -> -w / -x -> -r -> -p
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                // Handle -s and -x options
                if (skipLine(lineNum, line)) {
                    lineNum++;
                    continue;
                }

                // Handle -w option
                String processedLine = processSpacing(line);

                // Handle -r option
                processedLine = processReversal(processedLine);

                // Handle -p option (add prefix)
                processedLine = prefix + processedLine;

                // Add line to output
                out.append(processedLine).append(System.lineSeparator());
                lineNum++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out.toString();
    }

    /**
     * Choose whether to skip a line based on skip value
     *
     * @param lineNumber current line number in file
     * @param line current line
     * @return True to skip line, False to process it
     */
    private static Boolean skipLine(int lineNumber, String line) {
        boolean skipLine = false;

        // Case: skip = 0 or 1. Skip line if even or odd respectively
        if (skip != null) {
            skipLine = (lineNumber % 2) == skip;
        }

        // Case: -x toggled. Skip empty lines
        if (removeEmptyLines && isEmptyLine(line)) {
            skipLine = true;
        }

        return skipLine;
    }

    // Checks if current line is empty
    private static Boolean isEmptyLine(String line) {
        return line.trim().isEmpty();
    }

    /**
     * Removes leading, trailing, or all white spaces depending on this.spacing
     * @param line line in the file being processed
     * @return processed line
     */
    private static String processSpacing(String line) {
        String processedLine = line;

        // Case: -w option not toggled
        if (spacing == null) {
            return processedLine;
        }

        // Remove whitespaces based on spacing argument and return updated line
        if (spacing.equals("leading")){
            processedLine = line.replaceAll("^\\s+", "");
        }
        if (spacing.equals("trailing")) {
            processedLine = line.replaceAll("\\s+$", "");
        }
        if (spacing.equals("all")) {
            processedLine = line.replaceAll("\\s+", "");
        }

        return processedLine;
    }

    /**
     * Reverses line based on "text" and "words" options stored in
     * this.reverseLine
     * @param line line in the file being processed
     * @return processed line
     */
    private static String processReversal(String line) {
        String processedLine = line;
        StringBuilder reversedLine;

        // Case: -r option not toggled
        if (reverseLine == null) {
            return processedLine;
        }

        // Handle reversing text
        if (reverseLine.equals("text")){
            reversedLine = new StringBuilder(line);
            processedLine = reversedLine.reverse().toString();
        }

        // Handle reversing word order. Preserve original whitespaces.
        if (reverseLine.equals("words")) {
            // Get all words in String array. Reverse them and store as String
            reversedLine = new StringBuilder();

            // (?<=\\s)|(?=\\s) is a regex string that allows for inclusion of
            // all whitespaces in the String array
            String[] words = line.split("(?<=\\s)|(?=\\s)");

            for (int i = words.length - 1; i >= 0; i--) {
                reversedLine.append(words[i]);
            }
            processedLine = reversedLine.toString();
        }

        return processedLine;
    }
}
