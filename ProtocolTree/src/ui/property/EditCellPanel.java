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
import org.apache.jena.ontology.OntProperty;

import ont.OntologyManager;
import ont.PropertyAndIndividual;
import resources.ResourceFindingDummyClass;

public class EditCellPanel extends AbstractTreeCellPanel implements PropertyChangeListener {
	private JButton saveButton;

	public EditCellPanel(PropertyAndIndividual propertyAndIndividual) {
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		setTextAndAddComponents(ontProperty.getLocalName(), "" + propertyAndIndividual.getIndividual().getPropertyValue(ontProperty), ontProperty.getRange().getLocalName(), ontProperty.getDomain().getLocalName());
		// only for literal properties
		if (propertyAndIndividual.getOntProperty().isDatatypeProperty()) {
			saveButton = new JButton("Save");
			saveButton.setEnabled(false);
			add(saveButton);
			valueComponent.addPropertyChangeListener("value", this);
			valueComponent.setEditable(true);
			setProperFormatter(valueComponent, propertyAndIndividual);
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

	void setTextAndAddComponents(String local, String value, String range, String domain) {
		super.setTextAndAddComponents(local, value, range, domain);
		setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		saveButton.setEnabled(true);
		System.out.println("value changed:" + evt);
	}

	public JButton getSaveButton() {
		return saveButton;
	}
}
