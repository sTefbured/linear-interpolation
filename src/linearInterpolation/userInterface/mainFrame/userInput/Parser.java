package linearInterpolation.userInterface.mainFrame.userInput;

import javax.swing.*;
import java.text.NumberFormat;
import java.text.ParseException;

public class Parser {
    public static Double parseField(JTextField field, NumberFormat format) throws ParseException {
        String text = field.getText();
        return format.parse(text).doubleValue();
    }
}
