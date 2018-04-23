package ui.property;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;

import ont.OntManager;
import ont.PropertyAndIndividual;
import ui.UiUtils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

public class CellRenderer implements TreeCellRenderer {
	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
	Color backgroundSelectionColor;
	Color backgroundNonSelectionColor;
	// ImageIcon createImageIcon =
	// ResourceFindingDummyClass.createImageIcon("pipette.png");

	public CellRenderer() {
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
		if (userObject instanceof OntClass) {
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		if (!(userObject instanceof PropertyAndIndividual)) {
			UiUtils.showDialog(tree, "The propertyAndIndividual (userObject is NOT instanceof propertyAndIndividual:" + userObject.toString());
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		PropertyAndIndividual propertyAndIndividual = (PropertyAndIndividual) userObject;
		//OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		RenderCellPanel renderPanel = new RenderCellPanel(propertyAndIndividual);
		if (selected) {
			renderPanel.setBackground(backgroundSelectionColor);
		} else {
			renderPanel.setBackground(backgroundNonSelectionColor);
		}
		renderPanel.setEnabled(tree.isEnabled());
		returnComponent = renderPanel;
		if (returnComponent == null) {
			UiUtils.showDialog(tree, "The propertyAndIndividual to be rendered for:" + ((PropertyAndIndividual) userObject).getOntProperty().getLocalName() + " is wrong!!. Using default renderer");
			returnComponent = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		return returnComponent;
	}
}
