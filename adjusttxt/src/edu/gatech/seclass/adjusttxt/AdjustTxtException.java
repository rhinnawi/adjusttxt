/**
 * Originally authored by GA Tech staff
 */

package edu.gatech.seclass.adjusttxt;

/** Signals that an error occurred when running AdjustTxt. */
public class AdjustTxtException extends Exception {
    /**
     * Constructs a AdjustTxtException with the specified message describing the error.
     *
     * @param s the error message
     */
    AdjustTxtException(String s) {
        super(s);
    }
}
