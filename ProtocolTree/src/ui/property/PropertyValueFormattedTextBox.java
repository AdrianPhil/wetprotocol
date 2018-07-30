package ui.property;

import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.text.InternationalFormatter;

import uiutil.AbstractProtocolFormattedTextBox;

@SuppressWarnings("serial")
public class PropertyValueFormattedTextBox extends AbstractProtocolFormattedTextBox {
	Pattern pattern = Pattern.compile(".*]*"); // starts with letter followed by letter, digit or - w allows some chars too!!!
	boolean customText;

	public PropertyValueFormattedTextBox() {
		setColumns(20);
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
		//
		//
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				customText = true;
			}
		});
	}

	public boolean isCustomText() {
		return customText;
	}

	@Override
	public void addToContainer(Container container) {
		container.add(this);		
	}

}
