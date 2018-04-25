package ui.property;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.RDFNode;

import ont.OntManager;
import ont.PropertyAndIndividual;
import resources.ResourceFindingDummyClass;
import ui.UiUtils;

public abstract class AbstractTreeCellPanel extends JPanel {
	protected PropertyAndIndividual propertyAndIndividual;
	public static final Icon ICON_LEAF_CLASS = UIManager.getIcon("FileChooser.detailsViewIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html
	// maybe next is application_edit
	public static final Icon ICON_LITERAL = UIManager.getIcon("Tree.leafIcon");// http://en-human-begin.blogspot.ca/2007/11/javas-icons-by-default.html
	static final ImageIcon ICON_STANDALONE_OBJECT = ResourceFindingDummyClass.createImageIcon("icons/brick_add.png");
	static final ImageIcon ICON_CHOICE_SUBCLASS = ResourceFindingDummyClass.createImageIcon("icons/page_white_ruby.png");
	JLabel icon = new JLabel("");
	JLabel localComponent = new JLabel("dummy local");
	JFormattedTextField valueComponent = new JFormattedTextField("dummy value ");
	JLabel rangeComponent = new JLabel("dummy range");
	JLabel domainComponent = new JLabel("dummy domain");
	JComboBox<WrappedOntResource> individualOrClassChooser = new JComboBox();
	JLabel debug = new JLabel("debug");

	public AbstractTreeCellPanel(PropertyAndIndividual propertyAndIndividual) {
		this.propertyAndIndividual = propertyAndIndividual;
		debug.setText("individual:" + propertyAndIndividual.getIndividual().getLocalName());
	}

	public void setIcon(Icon icon) {
		this.icon.setIcon(icon);
	}

	public void setTextAndAddComponents() {
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		localComponent.setText(ontProperty.getLocalName() + ":");
		UiUtils.showConditionDialog(ontProperty.getRange() == null, this, "Property:" + ontProperty + " must have a non null range");
		rangeComponent.setText("Range:" + ontProperty.getRange().getLocalName().toString());
		domainComponent.setText("Coming from super class:" + ontProperty.getDomain().getLocalName());
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
		RDFNode propertyValue = propertyAndIndividual.getIndividual().getPropertyValue(ontProperty);
		switch (propertyAndIndividual.getNodeType()) {// todo check order to avoid weird side effects
		case LITERAL_NODE:
			valueComponent.setText(propertyValue != null ? "" + propertyValue.asLiteral().getValue() : "---");// todo this should extract the type and the value properly and attach the formatter
			// use getDataType to set the type
			localComponent.setForeground(Color.GREEN);
			icon.setIcon(EditCellPanel.ICON_LITERAL);
			break;
		case DATA_TYPE_NODE_FOR_CHOICE_STANDALONE_OBJECT:
			OntManager.loadPossibleIndividualValues(individualOrClassChooser, ontProperty.getRange().asClass());
			if (propertyValue != null) {// individual already created
				Individual individual = ((OntResource) propertyValue).asIndividual();
				valueComponent.setText(individual.getLocalName());
				setSelectedItemByComparing(individualOrClassChooser,individual.getLocalName());
			} else {
				valueComponent.setText("please select an existing object from the combo box");
			}
			localComponent.setForeground(Color.PINK);
			individualOrClassChooser.setVisible(true);
			individualOrClassChooser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					WrappedOntResource selectedProperty = (WrappedOntResource) (individualOrClassChooser.getSelectedItem());
					System.out.println("selected:" + selectedProperty);
				}
			});
			icon.setIcon(EditCellPanel.ICON_STANDALONE_OBJECT);
			break;
		case DATA_TYPE_NODE_FOR_LEAF_CLASS:
			valueComponent.setText("" + (((OntResource) propertyValue).asIndividual().getLocalName()));
			localComponent.setForeground(Color.CYAN);
			icon.setIcon(EditCellPanel.ICON_LEAF_CLASS);
			break;
		case DATA_TYPE_NODE_FOR_CHOICE_SUBCLASS://RED RUBY
			valueComponent.setEditable(false);
			loadPossibleLeafClassValues(individualOrClassChooser);
			if (propertyValue != null) {// individual already created
				String localName = "" + ((OntResource) propertyValue).asIndividual().getLocalName();
				valueComponent.setText(localName);
				setSelectedItemByComparing(individualOrClassChooser, ((OntResource) propertyValue).asIndividual().getOntClass().getLocalName());
				//we need to keep going down in here as long as it's populated
			} else {
				valueComponent.setText("please select from the combo box");
			}
			localComponent.setForeground(Color.RED);
			individualOrClassChooser.setVisible(true);
			individualOrClassChooser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					WrappedOntResource selectedProperty = (WrappedOntResource) (individualOrClassChooser.getSelectedItem());
					System.out.println("selected:" + selectedProperty);
				}
			});
			icon.setIcon(EditCellPanel.ICON_CHOICE_SUBCLASS);
			break;
		default:
			localComponent.setForeground(Color.MAGENTA);
			// too error dialog
			break;
		}
	}

	private void loadPossibleLeafClassValues(JComboBox individualOrClassChooser2) {
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		// individualOrClassChooser.addItem(OntManager.getInstance().getStringClass("DummyClass"));
		for (OntClass subclassFromRange : ontProperty.getRange().asClass().listSubClasses(false).toSet()) {
			// System.out.println("\t\t dealing with range subclass:" + subclassFromRange.getLocalName());
			if (OntManager.isLeafClass(subclassFromRange)) { // only leaf classes
				individualOrClassChooser2.addItem(new WrappedOntResource(subclassFromRange));
			}
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
		}
	}

	private void setSelectedItemByComparing(JComboBox<WrappedOntResource> individualOrClassChooser2, String localName) {
		ComboBoxModel<WrappedOntResource> model = individualOrClassChooser2.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			WrappedOntResource elementAt = model.getElementAt(i);
			if (elementAt.toString().equalsIgnoreCase(localName)) {
				individualOrClassChooser2.setSelectedItem(elementAt);
				return;
			}
		}
		assert (true);// ,"I should have found the selection") ;
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
