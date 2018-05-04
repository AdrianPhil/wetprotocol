package ui.instancenameedit;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;
import ont.OntManager;
import ui.OntResourceNameFormattedTextBox;

public class StepInstanceNameEditCellPanel extends JPanel implements PropertyChangeListener {
	private JButton saveButton;
	final DefaultMutableTreeNode individualNode;
	JLabel icon = new JLabel("");
	JFormattedTextField valueComponent = new OntResourceNameFormattedTextBox("dummy value 2");
	JTree jStepTree;// only for rename

	public StepInstanceNameEditCellPanel(Individual individual, DefaultMutableTreeNode individualNode, JTree jStepTree) {
		this.individualNode = individualNode;
		this.jStepTree = jStepTree;
		valueComponent.setText(individual.getLocalName());
		valueComponent.addPropertyChangeListener("value", this);
		valueComponent.setEditable(true);
		add(valueComponent);
		add(icon);
		icon.setIcon(StepInstanceCellRenderer.createImageIcon);
		setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("propertyc change event");
	}

	public Individual getNewIndividualValueScrapedFromEditPanel() {
		// OntManager.renameNode((OntResource)((DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent()).getUserObject());
		// return (Individual)OntManager.renameNode((OntResource) individualNode.getUserObject(), valueComponent.getText());
		// Individual individual=
		return OntManager.renameNode((Individual) individualNode.getUserObject(), valueComponent.getText(), (DefaultMutableTreeNode) (individualNode.getRoot()), jStepTree);
		// (Individual)individualNode.getUserObject(); this works well
	}
}
