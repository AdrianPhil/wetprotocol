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
import ont.PropertyAndIndividual;
import resources.ResourceFindingDummyClass;

public class RenderCellPanel extends AbstractTreeCellPanel {

	// called from Renderer
	public RenderCellPanel(String local, String value, String range, String domain) {
		super.setTextAndAddComponents(local, value, range, domain);
	}

}
