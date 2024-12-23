The **adjusttxt** project is an altered version of a final, individual project for a masters-level software engineering course I completed. It showcases the end result of a test-driven development process for building a Java application that implements a command-line utility called adjusttxt. The final grade received was 96.1%, which encompasses both test case coverage and app implementation. 

### Background and Specs ###
Due to university policies, the entire specification cannot be included for reference. The base premise is as follows:
- **Command Syntax**: `adjusttxt [OPTIONS] FILE`
- **FILE** is a text file to be parsed and altered. The adjusted text is ouputted to the terminal. If not empty, it must be terminated with a newline character.
- **Options**
    - `-s <number>`: Skip lines. The number can only be the integers 0 or 1, which represent skipping even or odd numbers respectively
    - `-w <spacing>`: remove whitespaces. The possible spacing arguments are "leading", "trailing", or "all", representing which whitespaces to be removed per line.
    - `-x`: remove all empty lines. This option cannot be included alongside `-w`.
    - `-r <target>`: Reverse line. The possible reversal (target) arguments are "words" or "text", representing reversal of either word order or entire text respectively. Both options are per line.
    - `p <prefix>`: Add prefix string to the beginning of each line.
- Options can be written in any order so long as the text file is at the end. All string arguments are simply written as words without any quotation marks.
- **Good example command**: `adjusttxt -s 0 -p $$ -x some_file.txt`. This uses *some_file.txt* as input, skips even lines, adds the prefix string "$$" to each outputted line, and removes any empty lines.
- **Bad example command**: `adjusttxt -s 1.0 -p $$ -x some_file.txt`. The `-s` option must be an integer.
- Although option order does not matter in the initial command, options are executed by the adjusttxt program in the order listed above (skip lines first, then whitespace removal, and so on).
- Errors are outputted to stderr. As noted above, successful output is written to stdout

#### Environment and Compatibility ####
Development done using
- Java version 21
- IntelliJ IDEA 2024.x
- JUnit 

Compatibility: Any Windows, macOS, Linux. Newlines are also OS-agnostic.

### How to run ###
Suggested IDE: IntelliJ

1. In terminal, clone from GitHub using `git clone https://github.com/rhinnawi/adjusttxt.git`
2. Navigate to `adjusttxt/test/edu/gatech/seclass/adjusttxt/`
3. Run `MyMainTest`. In IntelliJ, this may done by right-clicking the test file and selecting "Run 'MyMainTest'"
4. Refer to "Run" tab for all test results

Alternatively, the project may be built like any other Java app and then run from terminal using the command syntax above and any valid input text file.