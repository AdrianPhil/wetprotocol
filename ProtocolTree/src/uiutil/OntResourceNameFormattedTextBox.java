package uiutil;

import java.awt.Container;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.InternationalFormatter;

@SuppressWarnings("serial")
/** used for individual names both in the step tree and in the properties for enclosed object */
public class OntResourceNameFormattedTextBox extends AbstractProtocolFormattedTextBox {
	Pattern pattern = Pattern.compile("\\p{Alpha}[\\w\\d-_]*"); // starts with letter followed by letter, digit or - w allows some chars too!!!

	public OntResourceNameFormattedTextBox() {
		setColumns(25);
		setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		setFormatter(new InternationalFormatter() {
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

	@Override
	public void addToContainer(Container container) {
		container.add(this);
		
	}
}
