package ui.instancenameedit;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;
import ont.OntManager;

public class InstanceNameEditCellPanel extends JPanel implements PropertyChangeListener {
	private JButton saveButton;
	final DefaultMutableTreeNode individualNode;
	JLabel icon = new JLabel("");
	JFormattedTextField valueComponent = new JFormattedTextField("dummy value 2");

	public InstanceNameEditCellPanel(Individual individual, DefaultMutableTreeNode individualNode) {
		this.individualNode = individualNode;
		valueComponent.setText(individual.getLocalName());
		valueComponent.addPropertyChangeListener("value", this);
		valueComponent.setEditable(true);
		add(valueComponent);
		setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("propertyc change event");
	}

	public Individual getNewIndividualValueScrapedFromEditPanel() {
		// OntManager.renameNode((OntResource)((DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent()).getUserObject());
		//return (Individual)OntManager.renameNode((OntResource) individualNode.getUserObject(),  valueComponent.getText());
		//Individual individual=
				OntManager.renameNode((Individual) individualNode.getUserObject(),  valueComponent.getText());
		// (Individual)individualNode.getUserObject(); this works well
		return null;//individual; 
	}
}
