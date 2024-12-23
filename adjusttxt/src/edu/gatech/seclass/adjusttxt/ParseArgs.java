package edu.gatech.seclass.adjusttxt;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ParseArgs {
    private Integer skip = null;
    private String spacing = null;
    private Boolean removeEmptyLines = false;
    private String reverseLine = null;
    private String prefix = null;
    private File file = null;

    private final Set<String> legalSpacingArgs = new HashSet<>(
            Arrays.asList("leading", "trailing", "all")
    );

    private final Set<String> legalTargetArgs = new HashSet<>(
            Arrays.asList("text", "words")
    );

    public ParseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            try {
                // Case: last arg should be the file
                if (i == args.length - 1) {
                    setFile(args[i]);
                    break;
                }

                // Cases for each of the options
                switch (args[i]) {
                    case "-s":
                        setOptionS(args[i + 1]);
                        i++;
                        break;
                    case "-w":
                        setOptionW(args[i + 1]);
                        i++;
                        break;
                    case "-x":
                        setOptionX();
                        break;
                    case "-r":
                        setOptionR(args[i + 1]);
                        i++;
                        break;
                    case "-p":
                        setOptionP(args[i + 1]);
                        i++;
                        break;
                    default:
                        // Invalid option
                        throw new IllegalArgumentException(
                                "Invalid option: " + args[i]);
                }
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }

        if (this.file == null) {
            throw new IllegalArgumentException("Last argument was not a file");
        }
    }

    /**
     * Validates argument after -s option. Updates this.skip. Description:
     *  Reverses all of the content in the line. The target parameter can be
     *  one of two case-sensitive string values: “words”, or “text”. The
     *  “words” string parameter reverses the order of the words within each
     *  line, whereas “text” reverses each line as a whole.
     *
     * @param arg argument passed into console after -r option
     */
    private void setOptionS(String arg) {
        int argAsInt;

        try {
            argAsInt = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    arg.toString() + "is not an Integer.");
        }

        if (argAsInt == 0 || argAsInt == 1) {
            this.skip = argAsInt;
            return;
        }

        throw new IllegalArgumentException("Invalid option: " + arg);
    }

    /**
     * Validates argument after -w option. Updates this.spacing. Description:
     *  Removes whitespace from lines of the input file based on the specified
     *  spacing. The spacing parameter can be one of three case-sensitive
     *  string values: "leading", "trailing", or "all". The "leading" string
     *  parameter removes whitespace at the beginning of each line, "trailing"
     *  removes whitespace at the end of each line, "all" removes all
     *  whitespace from the line (beginning, middle, and end).
     *
     * @param arg argument passed into console after -w option
     */
    private void setOptionW(String arg) {
        // Case: -x option toggled already
        if (this.removeEmptyLines) {
            throw new IllegalArgumentException(
                    "Cannot toggle both -x and -w options");
        }

        // Case: arg is one of the legal options
        if (legalSpacingArgs.contains(arg)) {
            this.spacing = arg;
            return;
        }

        // Case: arg is not one of the legal options
        throw new IllegalArgumentException("Invalid spacing option: " + arg);
    }

    /**
     * Sets removeEmptyLines to true if option -x is toggled. Description:
     *  Removes all empty lines from the input file. This option takes no
     *  parameters, and is mutually exclusive with the -w option.
     */
    private void setOptionX() {
        // Case: -w option toggled already
        if (this.spacing != null) {
            throw new IllegalArgumentException(
                    "Cannot toggle both -x and -w options");
        }

        // Default behavior: set removeEmptyLines to true
        this.removeEmptyLines = true;
    }

    /**
     * Validates argument after -r option. Updates this.reverseLine.
     * Description:
     *  Reverses all of the content in the line. The target parameter can be
     *  one of two case-sensitive string values: “words”, or “text”. The
     *  “words” string parameter reverses the order of the words within each
     *  line, whereas “text” reverses each line as a whole.
     *
     * @param arg argument passed into console after -r option
     */
    private void setOptionR(String arg) {
        // Case: arg is one of the legal options
        if (legalTargetArgs.contains(arg)) {
            this.reverseLine = arg;
            return;
        }

        // Case: argument is not "words" or "text"
        throw new IllegalArgumentException("Invalid target option: " + arg);
    }

    /**
     * Validates argument after -p option. Updates this.prefix. Description:
     *  Adds the string parameter prefix at the start of each line.
     *
     * @param arg argument passed into console after -p option
     */
    private void setOptionP(String arg) {
        if (arg == null || arg.isEmpty()) {
            throw new IllegalArgumentException("Invalid prefix option: " + arg);
        }
        this.prefix = arg;
    }


    /**
     * Validates that the last argument is a file. Sets this.file to new file.
     * 
     * @param arg argument passed into console after -p option
     *            
     * @throws IllegalArgumentException if file invalid
     */
    private void setFile(String arg) {
        File file = new File(arg);

        if (!file.exists() || !file.isFile() || this.file != null) {
            throw new IllegalArgumentException("Invalid file: " + file);
        }

        // Check that the file ends in a new line
        try (RandomAccessFile checkFile = new RandomAccessFile(file, "r")) {
            // Get file size. If empty, can move on
            long numLines = checkFile.length();

            if (numLines > 0) {
                // Compare to system's lineSeparator by checking last
                // length(lineSeparator) bytes and comparing to those in file
                byte[] lineSeparator = System.lineSeparator().getBytes();
                int lineSeparatorLength = lineSeparator.length;
                checkFile.seek(numLines - lineSeparatorLength);

                byte[] endingBytes = new byte[lineSeparatorLength];
                checkFile.readFully(endingBytes);

                // Throw error if last length(lineSeparator) bytes do not match
                // lineSeparator
                if (!Arrays.equals(lineSeparator, endingBytes)) {
                    throw new IllegalArgumentException("File does not end with newline");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.file = file;
    }

    /**
     * Getter for saved option values
     */
    public HashMap<String, Object> getOptions() {
        HashMap<String, Object> options = new HashMap<>();
        if (this.skip != null) {
            options.put("skip", this.skip);
        }

        if (this.spacing != null) {
            options.put("spacing", this.spacing);
        }

        options.put("removeEmptyLines", this.removeEmptyLines);

        if (this.reverseLine != null) {
            options.put("reverseLine", this.reverseLine);
        }

        if (this.prefix != null) {
            options.put("prefix", this.prefix);
        }

        if (this.file != null) {
            options.put("file", this.file);
        }

        return options;
    }
}
