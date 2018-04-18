package oldStuff;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;

import ont.PropertyAndIndividual;
import ont.PropertyAndIndividual.DisplayType;

import javax.swing.tree.DefaultTreeCellRenderer;

public class SimpleTreeCellRenderer extends DefaultTreeCellRenderer {
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if(!(((DefaultMutableTreeNode)value).getUserObject() instanceof String)){
		PropertyAndIndividual propertyAndIndividual = (PropertyAndIndividual) ((DefaultMutableTreeNode)value).getUserObject();
		DisplayType displayType = propertyAndIndividual.getDisplayType();
		if (displayType == PropertyAndIndividual.DisplayType.FROM_RANGE_SUPERCLASS_PROPERTY_OBJECT) {
			setBorderSelectionColor(Color.YELLOW);
		}
		if (displayType == PropertyAndIndividual.DisplayType.IN_CLASS_PROPERTY_OBJECT) {
			setBorderSelectionColor(Color.BLACK);
		}
		if (displayType == PropertyAndIndividual.DisplayType.LITERAL) {
			setBorderSelectionColor(Color.GREEN);
		}
		else {
			setBorderSelectionColor(Color.LIGHT_GRAY);
		}
		}
		Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		if(!(((DefaultMutableTreeNode)value).getUserObject() instanceof String)){
		PropertyAndIndividual propertyAndIndividual = (PropertyAndIndividual) ((DefaultMutableTreeNode)value).getUserObject();
		DisplayType displayType = propertyAndIndividual.getDisplayType();
		if (displayType == PropertyAndIndividual.DisplayType.FROM_RANGE_SUPERCLASS_PROPERTY_OBJECT) {
			c.setBackground(Color.YELLOW);
		}
		if (displayType == PropertyAndIndividual.DisplayType.IN_CLASS_PROPERTY_OBJECT) {
			setBorderSelectionColor(Color.BLACK);
		}
		if (displayType == PropertyAndIndividual.DisplayType.LITERAL) {
			c.setBackground(Color.GREEN);
		}
		else {
			c.setBackground(Color.ORANGE);
		}
		}		
		return c;
	}
}
