package uiutil;

import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.InternationalFormatter;

@SuppressWarnings("serial")
public abstract class AbstractProtocolFormattedTextBox extends JFormattedTextField implements UITextValueInterface {
	/** otherwise it stays in Override mode */
	@Override
	public void setValue(Object value) {
		((DefaultFormatter) this.getFormatter()).setOverwriteMode(false);
		super.setValue(value);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		// TODO Auto-generated method stub
	}

	/** have no idea what this does */
	public boolean isCustomText() {
		return false;
	}
}
