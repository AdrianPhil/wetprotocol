package ui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;

import resources.ResourceFindingDummyClass;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

public class ClassCellRenderer implements TreeCellRenderer {
	static final ImageIcon createImageIcon = ResourceFindingDummyClass.createImageIcon("icons/brick_add.png");
	JLabel localName = new JLabel(" ");
	JLabel imageLabel = new JLabel();
	JPanel renderer = new JPanel();

	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
	Color backgroundSelectionColor;
	Color backgroundNonSelectionColor;

	public ClassCellRenderer() {
		imageLabel.setIcon(createImageIcon);
		renderer.add(imageLabel);
		localName.setForeground(Color.BLACK);
		renderer.add(localName);
		renderer.setBorder(null);
		backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
		backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if (value == null) {
			UiUtils.showDialog(tree, "In Class chooser. The value to be rendered is null. Using default renderer");
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		if (!(value instanceof DefaultMutableTreeNode)) {
			UiUtils.showDialog(tree, "In Class chooser. The value to be rendered not DefaultMutableTreeNode. Using default renderer");
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		Component returnValue = null;
		Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
		if (userObject instanceof String) {// user has chosen the tree root
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		if (userObject instanceof OntClass) {
			OntClass ontClass = (OntClass) userObject;
			localName.setText(ontClass.getLocalName());
			if (selected) {
				renderer.setBackground(backgroundSelectionColor);
			} else {
				renderer.setBackground(backgroundNonSelectionColor);
			}
			renderer.setEnabled(tree.isEnabled());
			returnValue = renderer;
		}
		return returnValue;
	}
}
