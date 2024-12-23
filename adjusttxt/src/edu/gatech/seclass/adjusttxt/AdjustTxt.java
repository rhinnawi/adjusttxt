package edu.gatech.seclass.adjusttxt;

import java.io.*;
import java.util.Arrays;

public class AdjustTxt implements AdjustTxtInterface{

    private String filePath = null;
    private LineToSkip lineToSkip = null;
    private RemoveSpaces removeSpaces = null;
    private boolean removeEmptyLines = false;
    private ReverseLine reverseLine = null;
    private String prefix = null;
    private File file = null;

    /**
     * Reset the AdjustTxt object to its initial state, for reuse.
     */
    @Override
    public void reset() {
        this.filePath = null;
        this.lineToSkip = null;
        this.removeSpaces = null;
        this.removeEmptyLines = false;
        this.reverseLine = null;
        this.prefix = null;
        this.file = null;
    }

    /**
     * Sets the path of the input file. This method has to be called before invoking the {@link
     * #adjusttxt()} methods.
     *
     * @param filepath The file path to be set.
     */
    @Override
    public void setFilepath(String filepath) {
        this.filePath = filepath;
    }

    /**
     * Set to apply the skipping of lines based upon the supplied parameter, lineToSkip. 0 is
     * considered even and 1 is odd. All files start with line 1. This method has to be called
     * before invoking the {@link #adjusttxt()} method.
     *
     * @param lineToSkip Determine which lines to skip
     */
    @Override
    public void setLineToSkip(LineToSkip lineToSkip) {
        this.lineToSkip = lineToSkip;
    }

    /**
     * Set to remove either leading, trailing, or all spaces from the input file. This method has to
     * be called before invoking the {@link #adjusttxt()} method.
     *
     * @param removeSpaces Determine which whitespace to remove
     */
    @Override
    public void setRemoveSpaces(RemoveSpaces removeSpaces) {
        this.removeSpaces = removeSpaces;
    }

    /**
     * Set to remove all empty lines from the input file. This method has to be called before
     * invoking the {@link #adjusttxt()} method.
     *
     * @param removeEmptyLines Flag to toggle functionality
     */
    @Override
    public void setRemoveEmptyLines(boolean removeEmptyLines) {
        this.removeEmptyLines = removeEmptyLines;
    }

    /**
     * Set to reverse the lines of a file. This method has to be called before invoking the {@link
     * #adjusttxt()} method.
     *
     * @param reverseLine Determine how to reverse a line
     */
    @Override
    public void setReverseLine(ReverseLine reverseLine) {
        this.reverseLine = reverseLine;
    }

    /**
     * Adds prefix at the start of each line. This method has to be called before invoking the
     * {@link #adjusttxt()} method.
     *
     * @param prefix The prefix to be set
     */
    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Outputs a System.lineSeparator() delimited string that contains selected parts of the lines
     * in the file specified using {@link #setFilepath} and according to the current configuration,
     * which is set through calls to the other methods in the interface.
     *
     * <p>It throws a {@link AdjustTxtException} if an error condition occurs (e.g., when the
     * specified file does not exist).
     *
     * @throws AdjustTxtException thrown if an error condition occurs
     */
    @Override
    public void adjusttxt() throws AdjustTxtException {
        // Set up file
        try {
            setUpFile();
        } catch (Exception e) {
            throw new AdjustTxtException(e.toString());
        }

        // Make sure that both -x and -w options are not toggled
        if (this.removeEmptyLines && this.removeSpaces != null) {
            throw new AdjustTxtException(
                    "Cannot toggle both -x and -w options");
        }

        // -p cannot be an empty string if toggled
        if (this.prefix != null && this.prefix.isEmpty()) {
            throw new AdjustTxtException("Prefix cannot be empty");
        }

        // Output adjusted text or throw error
        try {
            String output = generateOutput();
            System.out.print(output);
        } catch (Exception e) {
            throw new AdjustTxtException(e.toString());
        }
    }

    /**
     * Validates the stored file path and stores File type as this.file
     *
     * @throws IllegalArgumentException if file invalid
     * @throws RuntimeException if RandomAccessFile fails
     */
    private void setUpFile() {
        // Case: no file path provided
        if (this.filePath == null) {
            throw new IllegalArgumentException("filepath is null");
        }

        // Create new file. Validate it
        File newFile = new File(this.filePath);

        if (!newFile.exists() || !newFile.isFile()) {
            throw new IllegalArgumentException("Invalid file: " + newFile);
        }

        // Check that the file ends in a new line
        try (RandomAccessFile checkFile = new RandomAccessFile(newFile, "r")) {
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

        // Store new file
        this.file = newFile;
    }

    /**
     * Processes input file to generate output String
     *
     * @return String to be outputted to console
     * @throws IllegalArgumentException when file doesn't end in newline
     */
    private String generateOutput() {
        StringBuilder out = new StringBuilder();
        File file = this.file;
        String prefix = (this.prefix == null) ? "" : this.prefix;

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
     * Choose whether to skip a line based on this.lineToSkip and
     * this.removeEmptyLines values
     *
     * @param lineNumber current line number in file
     * @param line current line
     * @return True to skip line, False to process it
     */
    private Boolean skipLine(int lineNumber, String line) {
        boolean skipLine = false;

        // Case: lineToSkip = 0 (even) or 1 (odd). Skip even / odd line
        if (this.lineToSkip != null) {
            int skip = lineToSkip == LineToSkip.odd ? 1 : 0;
            skipLine = (lineNumber % 2) == skip;
        }

        // Case: -x toggled. Skip empty lines
        if (this.removeEmptyLines && isEmptyLine(line)) {
            skipLine = true;
        }

        return skipLine;
    }

    // Checks if current line is empty
    private static Boolean isEmptyLine(String line) {
        return line.trim().isEmpty();
    }

    /**
     * Removes leading, trailing, or all white spaces depending on
     * this.removeSpaces value.
     *
     * @param line line in the file being processed
     * @return processed line
     */
    private String processSpacing(String line) {
        String processedLine = line;

        // Case: -w option not toggled
        if (this.removeSpaces == null) {
            return processedLine;
        }

        // Remove whitespaces based on spacing argument and return updated line
        if (this.removeSpaces == RemoveSpaces.leading) {
            processedLine = line.replaceAll("^\\s+", "");
        }
        if (this.removeSpaces == RemoveSpaces.trailing) {
            processedLine = line.replaceAll("\\s+$", "");
        }
        if (this.removeSpaces == RemoveSpaces.all) {
            processedLine = line.replaceAll("\\s+", "");
        }

        return processedLine;
    }

    /**
     * Reverses line based on "text" and "words" options stored in
     * this.reverseLine
     *
     * @param line line in the file being processed
     * @return processed line
     */
    private String processReversal(String line) {
        String processedLine = line;
        StringBuilder reversedLine;

        // Case: -r option not toggled
        if (this.reverseLine == null) {
            return processedLine;
        }

        // Handle reversing text
        if (this.reverseLine == ReverseLine.text){
            reversedLine = new StringBuilder(line);
            processedLine = reversedLine.reverse().toString();
        }

        // Handle reversing word order. Preserve original whitespaces.
        if (this.reverseLine == ReverseLine.words) {
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
