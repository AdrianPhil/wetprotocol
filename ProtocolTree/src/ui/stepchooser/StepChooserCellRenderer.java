package ui.stepchooser;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;


import resources.ResourceFinding;
import uiutil.UiUtils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

public class StepChooserCellRenderer implements TreeCellRenderer {
	static final ImageIcon createImageIcon = ResourceFinding.createImageIcon("icons/brick_add.png");
	JLabel imageLabel = new JLabel();
	//JLabel localName = new JLabel(" ");
	JLabel className = new JLabel(" ");
	JPanel renderer = new JPanel();

	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
	Color backgroundSelectionColor;
	Color backgroundNonSelectionColor;

	public StepChooserCellRenderer() {
		imageLabel.setIcon(createImageIcon);
		renderer.add(imageLabel);
		//localName.setForeground(Color.BLACK);
		//renderer.add(localName);
		renderer.add(className);
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
		if (userObject instanceof String) {// user has chosen the ont class chooser root "please choose a step" 
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		if (userObject instanceof ClassAndIndividualName) {
			//localName.setText(((ClassAndIndividualName)userObject).getName());
			className.setText("<"+((ClassAndIndividualName)userObject).getOntClass().getLocalName()+">");
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
