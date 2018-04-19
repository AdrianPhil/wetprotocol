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

public abstract class AbstractTreeCellPanel extends JPanel {

	public static final Icon iconObjectProperty = UIManager.getIcon("FileChooser.detailsViewIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html
	public static final Icon iconDataProperty = UIManager.getIcon("Tree.leafIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html

	static final ImageIcon createImageIcon = ResourceFindingDummyClass.createImageIcon("labcoat.png");
	JLabel icon = new JLabel("");
	JLabel localComponent = new JLabel("dummy local");
	JFormattedTextField valueComponent = new JFormattedTextField("dummy value ");
	JLabel rangeComponent = new JLabel("dummy range");
	JLabel domainComponent = new JLabel("dummy domain");

	void setTextAndAddComponents(String local, String value, String range, String domain) {
		localComponent.setText(local + ":");
		valueComponent.setText("" + value);
		rangeComponent.setText("Range:" + range);
		domainComponent.setText("Domain:" + domain);
		add(icon);
		add(localComponent);
		valueComponent.setEditable(false);
		add(valueComponent);
		add(rangeComponent);
		add(domainComponent);
	}

	public void fillPropertyAndIndividual(PropertyAndIndividual propertyAndIndividual) {
		System.out.println("in editor fillPropertyAndIndividual called");
	}

	public void setIcon(Icon icon) {
		this.icon.setIcon(icon);
	}

	public String getValue() {
		//System.out.println("valueComponent:" + valueComponent);
		return valueComponent.getText();
	}

}
