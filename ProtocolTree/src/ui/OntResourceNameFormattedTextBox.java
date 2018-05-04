package ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;

public class OntResourceNameFormattedTextBox extends JFormattedTextField {
	Pattern pattern = Pattern.compile("\\p{Alpha}[\\w\\d-_]*"); // starts with letter followed by letter, digit or - w allows some chars too!!!

	public OntResourceNameFormattedTextBox(String text) {
		super(text);
		setColumns(25);
		setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		setFormatter(new DefaultFormatter() {
			@Override
			public Object stringToValue(String text) throws ParseException {
				setAllowsInvalid(false);// very important
				Matcher matcher = pattern.matcher(text);
				if (matcher.matches()) {
					return super.stringToValue(text);
				} else {
					System.out.println("PleaseEnterBetterName");
					throw new ParseException("The name format did not match. This exception will be eaten by the text field", 0);
				}
			}
		});
	}
}
