package ui.property;

import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.InternationalFormatter;

import uiutil.AbstractProtocolFormattedTextBox;
import uiutil.UITextValueInterface;

@SuppressWarnings("serial")
public class PropertyValueTextArea extends JTextArea implements UITextValueInterface{
	Pattern pattern = Pattern.compile(".*]*"); // starts with letter followed by letter, digit or - w allows some chars too!!!
	boolean customText;

	public PropertyValueTextArea() {
		setColumns(20);
		setRows(5);
//		setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
//				addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				customText = true;
//			}
//		});
	}

	public void addToContainer(Container container) {
		container.add(this);
		
	}

	@Override
	public void setValue(Object value) {
		setText(""+value);
		
	}

	@Override
	public boolean isCustomText() {
		// TODO Auto-generated method stub
		return false;
	}

//	public boolean isCustomText() {
//		return customText;
//	}

}
