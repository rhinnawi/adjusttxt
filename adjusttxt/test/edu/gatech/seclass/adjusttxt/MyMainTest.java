package edu.gatech.seclass.adjusttxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MyMainTest {
    // Place all of your tests in this class, optionally using MainTest.java as an example
    private final String usageStr =
            "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE"
                    + System.lineSeparator();

    @TempDir Path tempDirectory;

    @RegisterExtension OutputCapture capture = new OutputCapture();

    /* ----------------------------- Test Utilities ----------------------------- */

    /**
     * Returns path of a new "input.txt" file with specified contents written into it. The file will
     * be created using {@link TempDir TempDir}, so it is automatically deleted after test
     * execution.
     *
     * @param contents the text to include in the file
     * @return a Path to the newly written file, or null if there was an issue creating the file
     */
    private Path createFile(String contents) {
        return createFile(contents, "input.txt");
    }

    /**
     * Returns path to newly created file with specified contents written into it. The file will be
     * created using {@link TempDir TempDir}, so it is automatically deleted after test execution.
     *
     * @param contents the text to include in the file
     * @param fileName the desired name for the file to be created
     * @return a Path to the newly written file, or null if there was an issue creating the file
     */
    private Path createFile(String contents, String fileName) {
        Path file = tempDirectory.resolve(fileName);
        try {
            Files.writeString(file, contents);
        } catch (IOException e) {
            return null;
        }

        return file;
    }

    /**
     * Takes the path to some file and returns the contents within.
     *
     * @param file the path to some file
     * @return the contents of the file as a String, or null if there was an issue reading the file
     */
    private String getFileContent(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ------------------------------- Test Cases ------------------------------- */

    /** All code above this point is initialized by GA Tech staff. Code below is original */
    private final String zeroLines = System.lineSeparator();

    private final String singleLine = "  From the River to the Sea.  "
            + System.lineSeparator();

    private final String singleLineWAll = "FromtheRivertotheSea."
            + System.lineSeparator();

    private final String singleLineReversedText = "  .aeS eht ot reviR eht morF  "
            + System.lineSeparator();

    private final String singleLineReversedTextWAll = ".aeSehtotreviRehtmorF"
            + System.lineSeparator();

    private final String singleLineReversedWords = "  Sea. the to River the From  "
            + System.lineSeparator();

    private final String multipleLines = "  From the River to the Sea,  "
            + System.lineSeparator()
            + "Palestine will be free " + System.lineSeparator()
            + " for all its inhabitants" + System.lineSeparator()
            + " no matter their faith or background" + System.lineSeparator();

    private final String multipleLinesEven = "  From the River to the Sea,  "
            + System.lineSeparator()
            + " for all its inhabitants" + System.lineSeparator();

    private final String multipleLinesOdd = "Palestine will be free "
            + System.lineSeparator()
            + " no matter their faith or background"
            + System.lineSeparator();

    private final String multipleLinesReversedText = "  ,aeS eht ot reviR eht "
            + "morF  " + System.lineSeparator()
            + " eerf eb lliw enitselaP" + System.lineSeparator()
            + "stnatibahni sti lla rof " + System.lineSeparator()
            + "dnuorgkcab ro htiaf rieht rettam on " + System.lineSeparator();

    private final String prefix = "ceasefire_now$ ";

    /**
     * Custom Test Case 1
     *      Scenario: params in different order
     *
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void customTest01() {
        String input = multipleLines;
        String expected = prefix + "eerfeblliwenitselaP"
                + System.lineSeparator()
                + prefix + "dnuorgkcabrohtiafriehtrettamon"
                + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", "-s", "1", "-p", prefix, "-r", "text",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 2
     *
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void customTest02() {
        String input = multipleLines;
        String expected = prefix + "eerfeblliwenitselaP"
                + System.lineSeparator()
                + prefix + "dnuorgkcabrohtiafriehtrettamon"
                + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "1","-w", "all", "-r", "text", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 3
     *
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void customTest03() {
        String input = multipleLines;
        String expected = prefix + " eerf eb lliw enitselaP"
                + System.lineSeparator()
                + prefix + "dnuorgkcab ro htiaf rieht rettam on "
                + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "1", "-r", "text", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 4
     *
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : Not toggled
     * -x                            : Toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void customTest04() {
        String input = multipleLines + System.lineSeparator()
                + System.lineSeparator();
        String expected = prefix + " eerf eb lliw enitselaP"
                + System.lineSeparator()
                + prefix + "dnuorgkcab ro htiaf rieht rettam on "
                + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "1", "-x", "-r", "text", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 5
     *      Scenario: Empty lines at the beginning
     *
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : Not toggled
     * -x                            : Toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void customTest05() {
        String input = System.lineSeparator() + System.lineSeparator()
                + multipleLines;
        String expected = prefix + " eerf eb lliw enitselaP"
                + System.lineSeparator()
                + prefix + "dnuorgkcab ro htiaf rieht rettam on "
                + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "1", "-x", "-r", "text", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 6
     *      Scenario: empty file
     *
     * File included (as last param) : Yes
     * Number of lines               : None
     * Ends with newline             : No
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void customTest06() {
        String input = "";

        Path inputFile = createFile(input);
        String[] args = {
                "-x", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 7
     *    Scenario: file not last parameter
     */
    @Test
    public void customTest07() {
        String input = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {"-x", inputFile.toString(), "-p", "some_prefix"};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
    }

    /**
     * Custom Test Case 8
     *      Scenario: Remove white spaces from file with empty lines
     *
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : No
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void customTest08() {
        String input = System.lineSeparator() + System.lineSeparator()
                + System.lineSeparator() + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(input, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 9
     *      Scenario: repeated options (param -s)
     *
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : Not toggled
     * -x                            : Toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void customTest09() {
        String input = multipleLines + System.lineSeparator()
                + System.lineSeparator();
        String expected = prefix + " eerf eb lliw enitselaP"
                + System.lineSeparator()
                + prefix + "dnuorgkcab ro htiaf rieht rettam on "
                + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0", "-x", "-r", "text", "-s", "1", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 10		<error>
     *     Scenario: error for param followed by valid repeated option call
     *    -r :  Other string, then "text"
     */
    @Test
    public void customTest10() {
        String input = singleLine;

        Path inputFile = createFile(input);
        String[] args = {"-r", "wordss", "-r", "text", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 11
     *    Scenario: -w provided for all 3 allowed
     */
    @Test
    public void customTest11() {
        String input = "    The vibrant red roses bloomed in the garden" + System.lineSeparator()
                + "    She wore a beautiful blue dress to the party" + System.lineSeparator()
                + "    The sky turned into a brilliant shade of blue" + System.lineSeparator()
                + "    His favorite color is red, her favorite is blue" + System.lineSeparator();
        String expected = "The vibrant red roses bloomed in the garden" + System.lineSeparator()
                + "She wore a beautiful blue dress to the party" + System.lineSeparator()
                + "The sky turned into a brilliant shade of blue" + System.lineSeparator()
                + "His favorite color is red, her favorite is blue" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-w", "all", "-w", "trailing", "-w", "leading", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 12
     *    Scenario: -r provided for all 2 allowed
     */
    @Test
    public void customTest12() {
        String input = singleLine;
        String expected = singleLineReversedText;

        Path inputFile = createFile(input);
        String[] args = {"-r", "words", "-r", "text", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 13
     *    Scenario: -s provided for all 2 allowed
     */
    @Test
    public void customTest13() {
        String input = multipleLines;
        String expected = multipleLinesEven;

        Path inputFile = createFile(input);
        String[] args = {"-s", "1", "-s", "0", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 14  		<error>
     *    -r :  Empty
     */
    @Test
    public void customTest14() {
        String input = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {"-r", "", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 15
     *      Scenario: fake options
     */
    @Test
    public void customTest15() {
        String input = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {"-s", "1", "-x", "-r", "text", "-a", prefix,
                inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 16
     *      Scenario: Remove all lines from file with only lines
     *
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : No
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void customTest16() {
        String input = System.lineSeparator() + System.lineSeparator()
                + System.lineSeparator() + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-x", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 17 		<error>
     *    -x : Toggled with space
     */
    @Test
    public void customTest17() {
        String input = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {"-x", " ", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 18 		<error>
     *    Scenario: Preceded file with extra spaces
     */
    @Test
    public void customTest18() {
        String input = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {" ", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Custom Test Case 19 		<error>
     *    Scenario: Options with extra spaces between
     */
    @Test
    public void customTest19() {
        String input = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {"-s", "0", " ", "-x", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 1  		<error>
     *    File included (as last param) :  No
     */
    @Test
    public void test01() {
        String[] args = {"-x", "-p", "some_prefix"};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
    }

    /**
     * Test Case 2
     *    File ends with newline :  No
     */
    @Test
    public void test02() {
        String input = "test";

        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 3
     *    -s :  Zero
     */
    @Test
    public void test03() {
        String input = singleLine + singleLine;
        String expected = singleLine;

        Path inputFile = createFile(input);
        String[] args = {"-s", "0", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 4
     *    -s :  One, no lines in file
     */
    @Test
    public void test04() {
        String input = zeroLines;

        Path inputFile = createFile(input);
        String[] args = {"-s", "1", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 5
     *    -s :  One, 1+ lines in file
     */
    @Test
    public void test05() {
        String input = multipleLines;
        String expected = multipleLinesOdd;

        Path inputFile = createFile(input);
        String[] args = {"-s", "1", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 6  		<error>
     *    -s :  Non-integer
     */
    @Test
    public void test06() {
        String input = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {"-s", "1.0", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 7  		<error>
     *    -s :  Out of bounds
     */
    @Test
    public void test07() {
        String input = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {"-s", "3", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 8  		<error>
     *    -s :  Empty
     */
    @Test
    public void test08() {
        String input = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {"-s", "", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 9  		<single>
     *    -w :  leading, no lines in file
     */
    @Test
    public void test09() {
        String input = zeroLines;

        Path inputFile = createFile(input);
        String[] args = {"-w", "leading", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(zeroLines, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 10 		<single>
     *    -w :  leading, 1+ lines in file
     */
    @Test
    public void test10() {
        String input = multipleLines;
        String expected = "From the River to the Sea,  "
                + System.lineSeparator()
                + "Palestine will be free " + System.lineSeparator()
                + "for all its inhabitants" + System.lineSeparator()
                + "no matter their faith or background" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-w", "leading", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 11 		<single>
     *    -w :  trailing, no lines in file
     */
    @Test
    public void test11() {
        String input = zeroLines;

        Path inputFile = createFile(input);
        String[] args = {"-w", "trailing", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(zeroLines, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 12 		<single>
     *    -w :  trailing 1+ lines in file
     */
    @Test
    public void test12() {
        String input = multipleLines;
        String expected = "  From the River to the Sea,"
                + System.lineSeparator()
                + "Palestine will be free" + System.lineSeparator()
                + " for all its inhabitants" + System.lineSeparator()
                + " no matter their faith or background" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-w", "trailing", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 13 		<single>
     *    -w :  all, no lines in file
     */
    @Test
    public void test13() {
        String input = "";

        Path inputFile = createFile(input);
        String[] args = {"-w", "all", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 14 		<error>
     *    -w :  Other string
     */
    @Test
    public void test14() {
        String input = zeroLines;

        Path inputFile = createFile(input);
        String[] args = {"-w", "al", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 15 		<error>
     *    -w :  Empty
     */
    @Test
    public void test15() {
        String input = zeroLines;

        Path inputFile = createFile(input);
        String[] args = {"-w", "", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 16 		<single>
     *    -x :  Toggled
     */
    @Test
    public void test16() {
        String input = multipleLines + System.lineSeparator() + System.lineSeparator();
        String expected = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {"-x", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 17 		<error>
     *    -x :  Toggled with -w option
     */
    @Test
    public void test17() {
        String input = zeroLines;

        Path inputFile = createFile(input);
        String[] args = {"-w", "all", "-x", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 18 		<single>
     *    -r :  text
     */
    @Test
    public void test18() {
        String input = "Free Palestine" + System.lineSeparator();
        String expected = "enitselaP eerF" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r", "text", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 19 		<single>
     *    -r :  words, no lines in file
     */
    @Test
    public void test19() {
        String input = zeroLines;

        Path inputFile = createFile(input);
        String[] args = {"-r", "words", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(zeroLines, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 20 		<single>
     *    -r :  words, 1+ lines in file
     */
    @Test
    public void test20() {
        String input = singleLine;
        String expected = singleLineReversedWords;

        Path inputFile = createFile(input);
        String[] args = {"-r", "words", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 21 		<error>
     *    -r :  Other string
     */
    @Test
    public void test21() {
        String input = singleLine;

        Path inputFile = createFile(input);
        String[] args = {"-r", "wordss", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 22 		<single>
     *    -p :  Not empty
     */
    @Test
    public void test22() {
        String input = singleLine;
        String expected = prefix + ":" + singleLine;

        Path inputFile = createFile(input);
        String[] args = {"-p", prefix + ":", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 23 		<error>
     *    -p :  Empty
     */
    @Test
    public void test23() {
        String input = singleLine;

        Path inputFile = createFile(input);
        String[] args = {"-p", "", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 24 (Key = 1.1.1.7.6.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : None
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test24() {
        String input = zeroLines;

        Path inputFile = createFile(input);
        String[] args = {"-w", "all", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(zeroLines, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 25 (Key = 1.1.1.7.9.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : None
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test25() {
        String input = zeroLines;

        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(zeroLines, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 26 (Key = 1.2.1.1.6.3.1.1.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void test26() {
        String input = singleLine;
        String expected = prefix + ".aeSehtotreviRehtmorF"
                + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-w", "all", "-r", "text", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 27 (Key = 1.2.1.1.6.3.1.3.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not toggled
     */
    @Test
    public void test27() {
        String input = singleLine;
        String expected = ".aeSehtotreviRehtmorF" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-w", "all", "-r", "text", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 28 (Key = 1.2.1.1.6.3.5.1.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not empty
     */
    @Test
    public void test28() {
        String input = singleLine;
        String expected = prefix + singleLineWAll;

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-w", "all", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 29 (Key = 1.2.1.1.6.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test29() {
        String input = singleLine;
        String expected = "FromtheRivertotheSea." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-w", "all",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 30 (Key = 1.2.1.1.9.3.1.1.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void test30() {
        String input = singleLine;
        String expected = prefix + singleLineReversedText;

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0", "-r", "text", "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 31 (Key = 1.2.1.1.9.3.1.3.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not toggled
     */
    @Test
    public void test31() {
        String input = singleLine;
        String expected = singleLineReversedText;

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-r", "text",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 32 (Key = 1.2.1.1.9.3.5.1.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not empty
     */
    @Test
    public void test32() {
        String input = singleLine;
        String expected = prefix + singleLine;

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0", "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 33 (Key = 1.2.1.1.9.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test33() {
        String input = singleLine;
        String expected = singleLine;

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 34 (Key = 1.2.1.7.6.3.1.1.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void test34() {
        String input = singleLine;
        String expected = prefix + singleLineReversedTextWAll;

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", "-r", "text", "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 35 (Key = 1.2.1.7.6.3.1.3.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not toggled
     */
    @Test
    public void test35() {
        String input = singleLine;
        String expected = singleLineReversedTextWAll;

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", "-r", "text", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 36 (Key = 1.2.1.7.6.3.5.1.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not empty
     */
    @Test
    public void test36() {
        String input = singleLine;
        String expected = prefix + singleLineWAll;

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 37 (Key = 1.2.1.7.6.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test37() {
        String input = singleLine;
        String expected = singleLineWAll;

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 38 (Key = 1.2.1.7.9.3.1.1.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void test38() {
        String input = singleLine;
        String expected = prefix + singleLineReversedText;

        Path inputFile = createFile(input);
        String[] args = {
                "-r", "text", "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 39 (Key = 1.2.1.7.9.3.1.3.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not toggled
     */
    @Test
    public void test39() {
        String input = singleLine;
        String expected = singleLineReversedText;

        Path inputFile = createFile(input);
        String[] args = {
                "-r", "text", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


    /**
     * Test Case 40 (Key = 1.2.1.7.9.3.5.1.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not empty
     */
    @Test
    public void test40() {
        String input = singleLine;
        String expected = prefix + singleLine;

        Path inputFile = createFile(input);
        String[] args = {
                "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 41 (Key = 1.2.1.7.9.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : One
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test41() {
        String input = singleLine;
        String expected = singleLine;

        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 42 (Key = 1.3.1.1.6.3.1.1.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void test42() {
        String input = multipleLines;
        String expected = prefix + ",aeSehtotreviRehtmorF"
                + System.lineSeparator()
                + prefix + "stnatibahnistillarof" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-w", "all", "-r", "text", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 43 (Key = 1.3.1.1.6.3.1.3.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not toggled
     */
    @Test
    public void test43() {
        String input = multipleLines;
        String expected = ",aeSehtotreviRehtmorF"
                + System.lineSeparator()
                + "stnatibahnistillarof" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-w", "all", "-r", "text",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 44 (Key = 1.3.1.1.6.3.5.1.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not empty
     */
    @Test
    public void test44() {
        String input = multipleLines;
        String expected = prefix + "FromtheRivertotheSea,"
                + System.lineSeparator()
                + prefix + "forallitsinhabitants" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-w", "all", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 45 (Key = 1.3.1.1.6.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test45() {
        String input = multipleLines;
        String expected = "FromtheRivertotheSea," + System.lineSeparator()
                + "forallitsinhabitants" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-w", "all",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 46 (Key = 1.3.1.1.9.3.1.1.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void test46() {
        String input = multipleLines;
        String expected = prefix + "  ,aeS eht ot reviR eht morF  "
                + System.lineSeparator()
                + prefix + "stnatibahni sti lla rof " + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-r", "text", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 47 (Key = 1.3.1.1.9.3.1.3.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not toggled
     */
    @Test
    public void test47() {
        String input = multipleLines;
        String expected = "  ,aeS eht ot reviR eht morF  " + System.lineSeparator()
                + "stnatibahni sti lla rof " + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0","-r", "text", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 48 (Key = 1.3.1.1.9.3.5.1.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : One
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not empty
     */
    @Test
    public void test48() {
        String input = multipleLines;
        String expected = prefix + "  From the River to the Sea,  "
                + System.lineSeparator()
                + prefix + " for all its inhabitants" + System.lineSeparator();;

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0", "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 49 (Key = 1.3.1.1.9.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Zero
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test49() {
        String input = multipleLines;
        String expected = multipleLinesEven;

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 50 (Key = 1.3.1.7.6.3.1.1.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void test50() {
        String input = multipleLines;
        String expected = prefix + ",aeSehtotreviRehtmorF"
                + System.lineSeparator()
                + prefix + "eerfeblliwenitselaP" + System.lineSeparator()
                + prefix + "stnatibahnistillarof" + System.lineSeparator()
                + prefix + "dnuorgkcabrohtiafriehtrettamon"
                + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", "-r", "text", "-p", prefix,
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 51 (Key = 1.3.1.7.6.3.1.3.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not toggled
     */
    @Test
    public void test51() {
        String input = multipleLines;
        String expected = ",aeSehtotreviRehtmorF" + System.lineSeparator()
                + "eerfeblliwenitselaP" + System.lineSeparator()
                + "stnatibahnistillarof" + System.lineSeparator()
                + "dnuorgkcabrohtiafriehtrettamon" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", "-r", "text", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 52 (Key = 1.3.1.7.6.3.5.1.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not empty
     */
    @Test
    public void test52() {
        String input = multipleLines;
        String expected = prefix + "FromtheRivertotheSea,"
                + System.lineSeparator()
                + prefix + "Palestinewillbefree" + System.lineSeparator()
                + prefix + "forallitsinhabitants" + System.lineSeparator()
                + prefix + "nomattertheirfaithorbackground"
                + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 53 (Key = 1.3.1.7.6.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : all
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test53() {
        String input = multipleLines;
        String expected = "FromtheRivertotheSea," + System.lineSeparator()
                + "Palestinewillbefree" + System.lineSeparator()
                + "forallitsinhabitants" + System.lineSeparator()
                + "nomattertheirfaithorbackground" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-w", "all", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 54 (Key = 1.3.1.7.9.3.1.1.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not empty
     */
    @Test
    public void test54() {
        String input = multipleLines;
//        String expected = prefix + multipleLinesReversedText;
        String expected = prefix + "  ,aeS eht ot reviR eht "
                + "morF  " + System.lineSeparator()
                + prefix + " eerf eb lliw enitselaP" + System.lineSeparator()
                + prefix + "stnatibahni sti lla rof " + System.lineSeparator()
                + prefix + "dnuorgkcab ro htiaf rieht rettam on " + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-r", "text", "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 55 (Key = 1.3.1.7.9.3.1.3.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : text
     * -p                            : Not toggled
     */
    @Test
    public void test55() {
        String input = multipleLines;
        String expected = multipleLinesReversedText;

        Path inputFile = createFile(input);
        String[] args = {
                "-r", "text", inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 56 (Key = 1.3.1.7.9.3.5.1.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not empty
     */
    @Test
    public void test56() {
        String input = multipleLines;
//        String expected = prefix + multipleLines;
        String expected = prefix + "  From the River to the Sea,  "
                + System.lineSeparator()
                + prefix + "Palestine will be free " + System.lineSeparator()
                + prefix + " for all its inhabitants" + System.lineSeparator()
                + prefix + " no matter their faith or background" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-p", prefix, inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /**
     * Test Case 57 (Key = 1.3.1.7.9.3.5.3.)
     * File included (as last param) : Yes
     * Number of lines               : Multiple
     * Ends with newline             : Yes
     * -s                            : Not toggled
     * -w                            : Not toggled
     * -x                            : Not toggled
     * -r                            : Not toggled
     * -p                            : Not toggled
     */
    @Test
    public void test57() {
        String input = multipleLines;
        String expected = multipleLines;

        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


}
