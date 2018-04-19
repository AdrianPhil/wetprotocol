package ui.property;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;

import ont.PropertyAndIndividual;
import ui.UiUtils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

public class PropertyCellRenderer implements TreeCellRenderer {
	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
	Color backgroundSelectionColor;
	Color backgroundNonSelectionColor;
	// ImageIcon createImageIcon =
	// ResourceFindingDummyClass.createImageIcon("pipette.png");

	public PropertyCellRenderer() {
		backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
		backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
	}

	@Override // gets the user object returns the panel
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		// System.out.println("getTreeCell RENDERER Component called");
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
		if ((userObject instanceof OntClass)) {
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		if (!(userObject instanceof PropertyAndIndividual)) {
			UiUtils.showDialog(tree, "The propertyAndIndividual (userObject is NOT instanceof propertyAndIndividual:" + userObject.toString());
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		PropertyAndIndividual propertyAndIndividual = (PropertyAndIndividual) userObject;
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		RenderCellPanel editRenderPanel = new RenderCellPanel(ontProperty.getLocalName(), "" + propertyAndIndividual.getIndividual().getPropertyValue(ontProperty), ontProperty.getRange().getLocalName().toString(), ontProperty.getDomain().getLocalName().toString());
		if (selected) {
			editRenderPanel.setBackground(backgroundSelectionColor);
		} else {
			editRenderPanel.setBackground(backgroundNonSelectionColor);
		}
		if (propertyAndIndividual.getOntProperty().isObjectProperty()) {
			editRenderPanel.setIcon(EditCellPanel.iconObjectProperty);
		} else if (propertyAndIndividual.getOntProperty().isDatatypeProperty()) {
			editRenderPanel.setIcon(EditCellPanel.iconDataProperty);
		} else {
			UiUtils.showDialog(tree, "This property is not a data property nor an object property");
		}
		switch (propertyAndIndividual.getDisplayType()) {
		case FROM_RANGE_SUPERCLASS_PROPERTY_OBJECT:
			editRenderPanel.setBackground(Color.YELLOW);
			break;
		case IN_CLASS_PROPERTY_OBJECT:
			editRenderPanel.setBackground(Color.CYAN);
			break;// todo should fix this
		case LITERAL:
			editRenderPanel.setBackground(Color.GREEN);
			break;
		}
		editRenderPanel.setEnabled(tree.isEnabled());
		returnComponent = editRenderPanel;
		if (returnComponent == null) {
			UiUtils.showDialog(tree, "The propertyAndIndividual to be rendered for:" + ((PropertyAndIndividual) userObject).getOntProperty().getLocalName() + " is wrong!!. Using default renderer");
			returnComponent = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		return returnComponent;
	}
}
