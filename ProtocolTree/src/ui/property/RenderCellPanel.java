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

import ont.OntManager;
import resources.ResourceFinding;

public class RenderCellPanel extends AbstractTreeCellPanel {

	// called from Renderer
	public RenderCellPanel(PropertyAndIndividual propertyAndIndividual) {
		super(propertyAndIndividual);
		//should be OntManager.isStandalone(ontProperty) ? "STANDALONE" + ontProperty.getLocalName() : "XXX" + ontProperty.getLocalName(), "" + propertyAndIndividual.getIndividual().getPropertyValue(ontProperty), ontProperty.getRange().getLocalName().toString(), ontProperty.getDomain().getLocalName().toString()
		super.setTextAndAddComponents();
	}

}
