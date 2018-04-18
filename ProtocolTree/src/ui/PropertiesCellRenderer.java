package ui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import ont.PropertyAndIndividual;
import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

public class PropertiesCellRenderer implements TreeCellRenderer {
	EditRenderPanel rendererPanel = new EditRenderPanel();
	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
	Color backgroundSelectionColor;
	Color backgroundNonSelectionColor;
	// ImageIcon createImageIcon =
	// ResourceFindingDummyClass.createImageIcon("pipette.png");
	Icon iconObjectProperty = UIManager.getIcon("FileChooser.detailsViewIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html
	Icon iconDataProperty = UIManager.getIcon("Tree.leafIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html

	public PropertiesCellRenderer() {
		backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
		backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		System.out.println("getTreeCell RENDERER Component called");
		if ((((DefaultMutableTreeNode) value).getUserObject() instanceof String))// for the dummy string
		{
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		if (value == null || !(value instanceof DefaultMutableTreeNode)) {
			UiUtils.showDialog(tree, "The tree value is null or not a value instanceof DefaultMutableTreeNode. Using default renderer");
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);// ----------------->
		}
		Component returnComponent = null;
		Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
		if (!(userObject instanceof PropertyAndIndividual)) {
			UiUtils.showDialog(tree, "The propertyAndIndividual (userObject is NOT instanceof propertyAndIndividual:" + userObject.toString());
			returnComponent = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		PropertyAndIndividual propertyAndIndividual = (PropertyAndIndividual) userObject;
		rendererPanel.setLocalName(propertyAndIndividual.getOntProperty().getLocalName());
		rendererPanel.setValue(": "+String.valueOf(propertyAndIndividual.getValue()));
		rendererPanel.setRange(" Range:" + propertyAndIndividual.getOntProperty().getRange().getLocalName());
		rendererPanel.setDomain(" Domain:" + propertyAndIndividual.getOntProperty().getDomain().getLocalName());
		if (selected) {
			rendererPanel.setBackground(backgroundSelectionColor);
		} else {
			rendererPanel.setBackground(backgroundNonSelectionColor);
		}
		if (propertyAndIndividual.getOntProperty().isObjectProperty()) {
			rendererPanel.setIcon(iconObjectProperty);
		} else if (userObject instanceof PropertyAndIndividual) {
			rendererPanel.setIcon(iconDataProperty);
		} else {
			UiUtils.showDialog(tree, "This property is not a data property nor an object property");
		}
		switch(propertyAndIndividual.getDisplayType()) {
		case FROM_RANGE_SUPERCLASS_PROPERTY_OBJECT: 
			rendererPanel.setBackground(Color.YELLOW);break;
		case IN_CLASS_PROPERTY_OBJECT: 
			rendererPanel.setBackground(Color.CYAN);break;//todo should fix this
		case LITERAL: 
			rendererPanel.setBackground(Color.GREEN);break;
			
		}
		
		
		rendererPanel.setEnabled(tree.isEnabled());
		returnComponent = rendererPanel;
		if (returnComponent == null) {
			UiUtils.showDialog(tree, "The propertyAndIndividual to be rendered for:" + ((PropertyAndIndividual) userObject).getOntProperty().getLocalName() + " is wrong!!. Using default renderer");
			returnComponent = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		return returnComponent;
	}
}
