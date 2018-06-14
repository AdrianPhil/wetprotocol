package uiutil;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.InternationalFormatter;

@SuppressWarnings("serial")
public class AbstractProtocolFormattedTextBox extends JFormattedTextField {
	/** otherwise it stays in Override mode */
	@Override
	public void setValue(Object value) {
		((DefaultFormatter) this.getFormatter()).setOverwriteMode(false);
		super.setValue(value);
	}

//	/** this should go through value as the main setText is not hooked */
//	@Override
//	public void setText(String text) {
//		assert false;//all vlues should be set via setValue
//	}
}
