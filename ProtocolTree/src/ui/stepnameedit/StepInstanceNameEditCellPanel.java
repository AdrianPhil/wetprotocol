package ui.stepnameedit;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.jena.ontology.Individual;
import ont.OntManager;
import uimain.WetProtocolMainPanel;
import uiutil.OntResourceNameFormattedTextBox;

@SuppressWarnings("serial")
public class StepInstanceNameEditCellPanel extends JPanel implements PropertyChangeListener {
	//private JButton saveButton;
	private final DefaultMutableTreeNode individualNode;
	private final JLabel icon = new JLabel("");
	private final JFormattedTextField valueComponent = new OntResourceNameFormattedTextBox();
	private final WetProtocolMainPanel wetProtocolMainPanel;// only for rename

	public StepInstanceNameEditCellPanel(Individual individual, DefaultMutableTreeNode individualNode, WetProtocolMainPanel wetProtocolMainPanel) {
		this.individualNode = individualNode;
		this.wetProtocolMainPanel = wetProtocolMainPanel;
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
		return OntManager.renameNode((Individual) individualNode.getUserObject(), valueComponent.getText(), wetProtocolMainPanel, true);
	}
}
