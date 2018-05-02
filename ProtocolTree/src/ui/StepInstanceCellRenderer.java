package ui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.RDFNode;

import ont.OntManager;
import resources.ResourceFinding;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

public class StepInstanceCellRenderer implements TreeCellRenderer {
	static final ImageIcon createImageIcon = ResourceFinding.createImageIcon("icons/package.png");// TODO probably singleton it
	JLabel localName = new JLabel(" ");
	JLabel ontClass = new JLabel(" ");
	JLabel imageLabel = new JLabel();
	JPanel renderer = new JPanel();
	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
	Color backgroundSelectionColor;
	Color backgroundNonSelectionColor;

	public StepInstanceCellRenderer() {
		imageLabel.setIcon(createImageIcon);
		renderer.add(imageLabel);
		localName.setForeground(Color.BLACK);
		renderer.add(localName);
		ontClass.setForeground(Color.BLACK);
		renderer.add(ontClass);
		renderer.setBorder(null);
		backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
		backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if (value == null) {
			UiUtils.showDialog(tree, "In Protocol. The value to be rendered is null. Using default renderer");
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		if (!(value instanceof DefaultMutableTreeNode)) {
			UiUtils.showDialog(tree, "In Protocol. The value to be rendered not DefaultMutableTreeNode. Using default renderer");
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		Component returnValue = null;
		Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
		if (userObject instanceof Individual) {
			Individual individual = (Individual) userObject;
			if (individual == null) {
				assert false : "individual is null";
			}
			if (individual.getOntClass() == null) {
				assert false : "class is null";
			}
			// localName.setText(individual.getLocalName()+" label:"+individual.getLabel(null)+" location:"+individual.getPropertyValue(OntManager.getInstance().getStepLevelProperty()).asLiteral().getString());//TODO rename part
			RDFNode propertyValue = individual.getPropertyValue(OntManager.getInstance().getStepCoordinatesProperty());
			if (propertyValue != null) {//TODO remove this later an we don't need to show this
				localName.setText(individual.getLocalName() + " location:" + propertyValue.asLiteral().getString());// TODO rename part
			} else {
				localName.setText(individual.getLocalName());
			}
			ontClass.setText("<" + individual.getOntClass().getLocalName() + ">");
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