package ui.property;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.RDFNode;

import ont.PropertyAndIndividual;
import resources.ResourceFindingDummyClass;
import ui.UiUtils;

public class EditRenderPanel extends JPanel {

	public static final Icon iconObjectProperty = UIManager.getIcon("FileChooser.detailsViewIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html
	public static final Icon iconDataProperty = UIManager.getIcon("Tree.leafIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html

	static final ImageIcon createImageIcon = ResourceFindingDummyClass.createImageIcon("labcoat.png");
	JLabel icon = new JLabel("");
	JButton saveButton = new JButton("Save");
	JLabel localComponent = new JLabel("dummy local");
	JTextField valueComponent = new JTextField("dummy value ");
	JLabel rangeComponent = new JLabel("dummy range");
	JLabel domainComponent = new JLabel("dummy domain");

	public EditRenderPanel(String local, String value, String range, String domain) {
		setTextAndAddComponents(local, value, range, domain);
	}

	public EditRenderPanel(PropertyAndIndividual propertyAndIndividual) {
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		setTextAndAddComponents(ontProperty.getLocalName(), "" + propertyAndIndividual.getIndividual().getPropertyValue(ontProperty), ontProperty.getRange().getLocalName(), ontProperty.getDomain().getLocalName());
	}

	private void setTextAndAddComponents(String local, String value, String range, String domain) {
		localComponent.setText(local+":" );
		valueComponent.setText(""+ value);
		rangeComponent.setText("Range:" + range);
		domainComponent.setText("Domain:" + domain);
		add(icon);
		add(localComponent);
		add(valueComponent);
		add(rangeComponent);
		add(domainComponent);
		add(saveButton);
	}

	
	public void fillPropertyAndIndividual(PropertyAndIndividual propertyAndIndividual){
		System.out.println("fillPropertyAndIndividual called");
	}
	
	// public JButton getSaveButton() {
	// return saveButton;
	// }
	//
	// public String getLocal() {
	// return localComponent.getText();
	// }
	//
	// public void setLocal(String localText) {
	// localComponent.setText(localText);
	// }
	//
	// public String getValue() {
	// return valueComponent.getText();
	// }
	//
	// public void setValue(String valueText) {
	// localComponent.setText(valueText);
	// }
	//
	// public String getRange() {
	// return rangeComponent.getText();
	// }
	//
	// public void setRange(String text) {
	// localComponent.setText(text);
	// }
	//
	// public String getDomain() {
	// return domainComponent.getText();
	// }
	//
	// public void setDomain(String text) {
	// localComponent.setText(text);
	// }
	//
	public void setIcon(Icon icon) {
		this.icon.setIcon(icon);
	}

	public String getValue() {
		System.out.println("valueComponent:"+valueComponent);
		return valueComponent.getText();
	}

	public JButton getSaveButton() {
		return saveButton;
	}
}
