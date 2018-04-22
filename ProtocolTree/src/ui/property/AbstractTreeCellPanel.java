package ui.property;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public abstract class AbstractTreeCellPanel extends JPanel {
	public static final Icon iconObjectProperty = UIManager.getIcon("FileChooser.detailsViewIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html
	public static final Icon iconDataProperty = UIManager.getIcon("Tree.leafIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html
	static final ImageIcon createImageIcon = ResourceFindingDummyClass.createImageIcon("labcoat.png");
	JLabel icon = new JLabel("");
	JLabel localComponent = new JLabel("dummy local");
	JFormattedTextField valueComponent = new JFormattedTextField("dummy value ");
	JLabel rangeComponent = new JLabel("dummy range");
	JLabel domainComponent = new JLabel("dummy domain");
	JComboBox<WrappedOntProperty> individualOrClassChooser = new JComboBox();
	JLabel debug = new JLabel("debug");

	public void setIcon(Icon icon) {
		this.icon.setIcon(icon);
	}

	public String getValue() {
		// System.out.println("valueComponent:" + valueComponent);
		return valueComponent.getText();
	}

	public void setTextAndAddComponents(PropertyAndIndividual propertyAndIndividual) {
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		localComponent.setText(OntManager.isStandalone(ontProperty) ? "STANDALONE" + ontProperty.getLocalName() : "" + ontProperty.getLocalName() + ":");
		valueComponent.setText("" + propertyAndIndividual.getIndividual().getPropertyValue(ontProperty));
		rangeComponent.setText("Range:" + ontProperty.getRange().getLocalName().toString());
		domainComponent.setText("Domain:" + ontProperty.getDomain().getLocalName());
		add(icon);
		add(localComponent);
		valueComponent.setEditable(false);
		add(valueComponent);
		add(individualOrClassChooser);
		add(rangeComponent);
		add(domainComponent);
		add(debug);
		debug.setText(propertyAndIndividual.getNodeType().name());
		individualOrClassChooser.setVisible(false);
		switch (propertyAndIndividual.getNodeType()) {
		case LITERAL_NODE:
			localComponent.setForeground(Color.GREEN);
			break;
		case DATA_TYPE_NODE_FOR_LEAF_CLASS:
			localComponent.setForeground(Color.CYAN);
			break;
		case DATA_TYPE_NODE_FOR_CHOICE_SUBCLASS:
			localComponent.setForeground(Color.RED);
			individualOrClassChooser.setVisible(true);
			extractPossibleLeafClassValues(propertyAndIndividual, individualOrClassChooser);
			individualOrClassChooser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					WrappedOntProperty selectedProperty = (WrappedOntProperty) (individualOrClassChooser.getSelectedItem());
					System.out.println("selected:" + selectedProperty);
				}
			});
			break;
		case DATA_TYPE_NODE_FOR_CHOICE_STANDALONE_OBJECT:
			localComponent.setForeground(Color.YELLOW);
			break;
		default:
			localComponent.setForeground(Color.MAGENTA);
			break;
		}
	}

	private void extractPossibleLeafClassValues(PropertyAndIndividual propertyAndIndividual, JComboBox<WrappedOntProperty> individualOrClassChooser) {
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		// individualOrClassChooser.addItem(OntManager.getInstance().getStringClass("DummyClass"));
		for (OntClass subclassFromRange : ontProperty.getRange().asClass().listSubClasses(false).toSet()) {
			System.out.println("\t\t dealing with range subclass:" + subclassFromRange.getLocalName());
			individualOrClassChooser.addItem(new WrappedOntProperty(subclassFromRange));
			// DefaultMutableTreeNode newClassChild = new DefaultMutableTreeNode(subclassFromRange);
			// DefaultMutableTreeNode rangeLevelNode = currentTopNode;
			// currentTopNode.add(newClassChild);// would be Read Only as it's an object property
			// // currentTopNode = newClassChild;
			// // Set<OntProperty> props = OntologyManager.getInstance().dumpCalculatedPropertiesForAClass(subclassFromRange);
			// // for (OntProperty p : props) {
			// // System.out.println("\t\t\t dealing with range property:" + p.getLocalName());
			// // createNode(p, currentTopNode, true);
			// // }
			// currentTopNode = rangeLevelNode;
			System.out.println("\t\t finished dealing with range subclass:" + subclassFromRange.getLocalName());
		}
	}
	// public JComboBox<WrappedOntProperty> getIndividualOrClassChooser() {
	// return individualOrClassChooser;
	// }
}
// }
// for (OntClass subclassFromRange : range.asClass().listSubClasses(false).toSet()) {
// System.out.println("\t\t dealing with range subclass:" + subclassFromRange.getLocalName());
// DefaultMutableTreeNode newClassChild = new DefaultMutableTreeNode(subclassFromRange);
// DefaultMutableTreeNode rangeLevelNode = currentTopNode;
// currentTopNode.add(newClassChild);// would be Read Only as it's an object property
// // currentTopNode = newClassChild;
// // Set<OntProperty> props = OntologyManager.getInstance().dumpCalculatedPropertiesForAClass(subclassFromRange);
// // for (OntProperty p : props) {
// // System.out.println("\t\t\t dealing with range property:" + p.getLocalName());
// // createNode(p, currentTopNode, true);
// // }
// currentTopNode = rangeLevelNode;
// System.out.println("\t\t finished dealing with range subclass:" + subclassFromRange.getLocalName());
// }
// DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(new PropertyAndIndividual(ontProperty, individual, NodeType.DATA_TYPE_NODE_FOR_CHOICE_SUBCLASS));//, ontProperty.getRange().asClass().listSubClasses(false).toSet()));// Pop up
// System.out.println("\tfinished with range :" + range);
// // describe the property itself even if it do
// // if (!OntologyManager.isStandalone(ontProperty)) {
// // Set<OntProperty> rangeProps = OntologyManager.getInstance().dumpCalculatedPropertiesForAClass(range.asClass());
// // for (OntProperty rangeProperty : rangeProps) {
// // System.out.println("creating first level prop node for " + rangeProperty.getLocalName());
// // createNode(rangeProperty, currentTopNode, false);
// // }
// // }
// }
