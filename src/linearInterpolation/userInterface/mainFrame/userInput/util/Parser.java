package linearInterpolation.userInterface.mainFrame.userInput.util;

import javax.swing.*;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * <code>Parser</code> is a utility class used for parsing JTextField objects.
 *
 * @author Kotikov S.G.
 */
public abstract class Parser {
    /**
     * Tries to parse <code>double</code> value from a text field using specified <code>NumberFormat</code>.
     *
     * @param field text field to be parsed.
     * @param format required number format.
     * @return parsed <code>Double</code> object which fits the <code>format</code>.
     * @throws ParseException if the beginning of the specified string cannot be parsed.
     */
    public static Double parseDoubleField(JTextField field, NumberFormat format) throws ParseException {
        String text = field.getText();
        return format.parse(text).doubleValue();
    }
}
