package uiutil;

import java.awt.Color;
import java.awt.Container;
import java.beans.PropertyChangeListener;

public interface UITextValueInterface {
	public void setValue(Object value);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

	public void setEditable(boolean editable);

	public void addToContainer(Container container);

	public void setForeground(Color color);

	public void setColumns(int columns);

	public String getText();

	public boolean isCustomText();
}
