package ui.stepnameedit;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.RDFNode;

import ont.OntManager;
import resources.ResourceFinding;
import ui.UiUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class StepInstanceCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {
	public static final ImageIcon createImageIcon = ResourceFinding.createImageIcon("icons/package.png");// TODO probably singleton it
	JLabel localName = new JLabel(" ");
	JLabel labelName = new JLabel(" ");
	JLabel ontClass = new JLabel(" ");
	JLabel imageLabel = new JLabel();
	JPanel renderer = new JPanel();
	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
	Color backgroundSelectionColor;
	Color backgroundNonSelectionColor;

	public StepInstanceCellRenderer(JTree jProtocolTree) {
		imageLabel.setIcon(createImageIcon);
		renderer.add(imageLabel);
		localName.setForeground(Color.BLACK);
		renderer.add(localName);
		renderer.add(labelName);
		ontClass.setForeground(Color.BLACK);
		renderer.add(ontClass);
		renderer.setBorder(null);
		backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
		backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
		imageLabel.addMouseListener(new MouseAdapter() {//TODO not used
			public void mouseClicked(MouseEvent e) {
				System.out.println("Icon clicked!!");
				 jProtocolTree.setEditable(true);
				
			}
		});
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
			localName.setText(individual.getLocalName());
			//labelName.setText(" label:" + individual.getLabel(null));
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
