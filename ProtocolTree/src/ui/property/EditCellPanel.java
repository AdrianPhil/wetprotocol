package ui.property;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;

import ont.OntManager;
import ont.PropertyAndIndividual;
import resources.ResourceFindingDummyClass;
import ui.property.ClassPropertyEditorPanel.NodeType;

public class EditCellPanel extends AbstractTreeCellPanel implements PropertyChangeListener {
	private JButton saveButton;

	public EditCellPanel(PropertyAndIndividual propertyAndIndividual) {
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		setTextAndAddComponents(propertyAndIndividual);
		// only for literal properties
		if (propertyAndIndividual.getNodeType() == NodeType.LITERAL_NODE) {
			valueComponent.addPropertyChangeListener("value", this);
			valueComponent.setEditable(true);
			setProperFormatter(valueComponent, propertyAndIndividual);
			individualOrClassChooser.setVisible(false);
		} else if (propertyAndIndividual.getNodeType() == NodeType.DATA_TYPE_NODE_FOR_CHOICE_SUBCLASS || propertyAndIndividual.getNodeType() == NodeType.DATA_TYPE_NODE_FOR_CHOICE_STANDALONE_OBJECT) {
			saveButton = new JButton("Save");
			saveButton.setEnabled(true);
			add(saveButton);
			individualOrClassChooser.setVisible(true);
		} else {// leaf class
		}
	}

	private void setProperFormatter(JFormattedTextField valueComponent, PropertyAndIndividual propertyAndIndividual) {
		switch (propertyAndIndividual.getOntProperty().getRange().getLocalName()) {
		case "int":
		case "integer":
			System.out.println("integer");
			break;
		case "decimal":
			System.out.println("decimal");
			break;
		case "string":
			System.out.println("string");
			break;
		}
	}

	public void setTextAndAddComponents(PropertyAndIndividual propertyAndIndividual) {
		super.setTextAndAddComponents(propertyAndIndividual);
		setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (saveButton != null) {
			saveButton.setEnabled(true);
		}
		System.out.println("value changed:" + evt);
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public OntClass getComboSelection() {
		return ((WrappedOntProperty) (individualOrClassChooser.getSelectedItem())).getOntClass();
	}
}
