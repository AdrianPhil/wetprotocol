package ui.property;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;

import ont.OntManager;
import ui.property.PropertyEditorBigPanel.NodeType;
import uiutil.UiUtils;
import utils.Utils;

@SuppressWarnings("serial")
public class EditCellPanel extends AbstractTreeCellPanel {
	private JButton applyButton;
	final CellEditor cellEditor;// just to fire the stop edit when the save button in pressed
	// final DefaultMutableTreeNode propertyAndIndividualNode;

	public EditCellPanel(PropertyAndIndividual propertyAndIndividual, DefaultMutableTreeNode propertyAndIndividualNode, CellEditor cellEditor) {
		super(propertyAndIndividual);
		this.cellEditor = cellEditor;// if (propertyValue != null) {
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		setTextAndAddComponents();
		// only for literal properties
		if (propertyAndIndividual.getNodeType() == NodeType.LITERAL_NODE) {
			//RDFNode propertyValue = propertyAndIndividual.getIndividual().getPropertyValue(ontProperty);
			valueComponent.setEditable(true);
			// setProperFormatter(valueComponent, propertyAndIndividual);
			individualOrClassChooser.setVisible(false);
		} else if (propertyAndIndividual.getNodeType() == NodeType.DATA_TYPE_NODE_FOR_CHOICE_SUBCLASS || propertyAndIndividual.getNodeType() == NodeType.DATA_TYPE_NODE_FOR_CHOICE_PREEXISTING_OBJECT) {
			RDFNode existingIndividual = propertyAndIndividual.getIndividual().getPropertyValue(ontProperty);// the value for existing individual
			if (existingIndividual != null && propertyAndIndividual.getNodeType() != NodeType.DATA_TYPE_NODE_FOR_CHOICE_PREEXISTING_OBJECT) {
				individualOrClassChooser.setEnabled(false);
				return;// --------------------------------------->
			}
			applyButton = new JButton("Apply Choice");
			applyButton.addActionListener(e -> {
				cellEditor.stopCellEditing();
				Individual newIndividual;
				if (propertyAndIndividual.getNodeType() == NodeType.DATA_TYPE_NODE_FOR_CHOICE_PREEXISTING_OBJECT) {
					if (individualOrClassChooser.getModel().getSize() == 0) {// I can have an empty box
						return;// -------------------------------------->
					}
					newIndividual = (Individual) getComboClassOrIndividualSelection();
					propertyAndIndividual.getIndividual().setPropertyValue(ontProperty, newIndividual);
				} else {
					OntClass ontClass = (OntClass) getComboClassOrIndividualSelection();
					newIndividual = OntManager.createNewIndividualOfSelectedClass(ontClass, Utils.createNiceIdividualName(ontClass, valueComponent));
					System.out.println("As result of apply we Created a new individual:" + newIndividual.getLocalName());
					propertyAndIndividual.getIndividual().setPropertyValue(ontProperty, newIndividual);
					PropertyEditorBigPanel.createNodesForClass(ontClass, (DefaultMutableTreeNode) propertyAndIndividualNode, newIndividual);
				}
				// if (existingIndividual != null) {// individual already existing
				// UiUtils.showDialog(this, "we will overwrite the current individual:" + ((OntResource) existingIndividual).asIndividual().getLocalName() + " with a new individual of other class:" + newIndividual);
				// //UiUtils.showDialog(this, "we will overwrite the current individual:" + ((OntResource) existingIndividual).asIndividual().getLocalName() + " with a new individual of other class:" + newIndividual);
				// //TODO here I need to delete all under propertyAndIndividualNode and all ontModel like existingIndividual and the tree because of it
				// }
				// here I need to change the individual if we had any
				// propertyAndIndividualNode.set
				UiUtils.expandTree(cellEditor.jPropertyAndIndividualTree);
			});
			applyButton.setEnabled(true);
			add(applyButton, this.getComponentCount() - 3);
			individualOrClassChooser.setVisible(true);
		}
	}

	@SuppressWarnings("incomplete-switch")
	public Object getNewIndividualValuesScrapedFromEditPanel() {
		Object value = getValue();
		System.out.println("in getCellEditorValue value returned is:" + value);
		OntProperty ontProperty = propertyAndIndividual.getOntProperty();
		switch (propertyAndIndividual.getNodeType()) {
		case LITERAL_NODE:
			Literal literalPropertyValue = OntManager.createValueAsStringLiteral((String) value);
			System.out.println("Putting: " + literalPropertyValue + " value in:" + propertyAndIndividual.getIndividual().getLocalName());
			propertyAndIndividual.getIndividual().setPropertyValue(ontProperty, literalPropertyValue);
		case DATA_TYPE_NODE_FOR_CHOICE_SUBCLASS:
			/// BIG BANG
			if (propertyAndIndividual.getIndividual().getPropertyValue(ontProperty) instanceof OntResource) {//can be individual or literal. we already populated the individual name when we said Apply
				Individual renamedIndividual = OntManager.renameNode(((OntResource) propertyAndIndividual.getIndividual().getPropertyValue(ontProperty)).asIndividual(),
						"" + value, cellEditor.wetProtocolMainPanel, false);
				propertyAndIndividual.getIndividual().setPropertyValue(ontProperty, renamedIndividual);
				System.out.println("HERE RENAME !!!!!!!!!!!!");
				//cellEditor.jPropertyAndIndividualTree.invalidate();
				//loadStepsTreeFromModel(cellEditor.wetProtocolMainPanel.getjProtocolTree()); already contained in the stuff above
			}else {//we typed a String for a future Individual name because is pre-apply
				System.out.println("getNewIndividualValuesScrapedFromEditPanel for a future individual name");
			}
		}
		//
		// if (value instanceof String) {
		// Literal literalPropertyValue = OntManager.getInstance().createValueAsStringLiteral((String) value);
		// System.out.println("Putting: " + literalPropertyValue + " value in:" + propertyAndIndividual.getIndividual().getLocalName());
		// propertyAndIndividual.getIndividual().setPropertyValue(propertyAndIndividual.getOntProperty(), literalPropertyValue);
		// }else if(value instanceof Individual) {
		// System.out.println("HERA!!!!!!!!!!!!");
		// //Individual renameNode(Individual resource, String newValue, DefaultMutableTreeNode topStepNode, JTree jStepsTree) {
		// }
		return propertyAndIndividual;
	}

	// this is what really updates the propertyValue!!!!
	public Object getValue() {
		switch (propertyAndIndividual.getNodeType()) {
		case LITERAL_NODE:
			return valueComponent.getText();
		case DATA_TYPE_NODE_FOR_LEAF_CLASS:
			assert false;
			// localComponent.setForeground(Color.CYAN);
			// icon.setIcon(EditCellPanel.ICON_LEAF_CLASS);
			return null;
		case DATA_TYPE_NODE_FOR_CHOICE_SUBCLASS:
			// return propertyAndIndividual.getIndividual();
			return valueComponent.getText();
		// localComponent.setForeground(Color.RED);
		// individualOrClassChooser.setVisible(true);
		// extractPossibleLeafClassValues(individualOrClassChooser);
		// individualOrClassChooser.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// WrappedOntProperty selectedProperty = (WrappedOntProperty) (individualOrClassChooser.getSelectedItem());
		// System.out.println("selected:" + selectedProperty);
		// }
		// });
		// icon.setIcon(EditCellPanel.ICON_CHOICE_SUBCLASS);
		// return null;
		case DATA_TYPE_NODE_FOR_CHOICE_PREEXISTING_OBJECT:
			// localComponent.setForeground(Color.PINK);
			// individualOrClassChooser.setVisible(true);
			// extractPossibleIndividualValues(individualOrClassChooser);
			return null;
		default:
			assert false;
			// localComponent.setForeground(Color.MAGENTA);
			// // too error dialog
			return null;
		}
	}

	// private void setProperFormatter(JFormattedTextField valueComponent, PropertyAndIndividual propertyAndIndividual) {
	// switch (propertyAndIndividual.getOntProperty().getRange().getLocalName()) {
	// case "int":
	// case "integer":
	// System.out.println("integer");
	// break;
	// case "decimal":
	// System.out.println("decimal");
	// break;
	// case "string":
	// System.out.println("string");
	// break;
	// }
	// }
	public void setTextAndAddComponents() {
		super.setTextAndAddComponents();
		if (individualOrClassChooser.isVisible()) {
			individualOrClassChooser.setEnabled(true);
			individualOrClassChooser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {// just to default the new instanceName
					WrappedOntResource<?> selectedProperty = (WrappedOntResource<?>) (individualOrClassChooser.getSelectedItem());
					OntResource wrappedResource = ((WrappedOntResource<?>) individualOrClassChooser.getSelectedItem()).getWrappedResource();
					if (wrappedResource instanceof OntClass) {
						valueComponent.setValue(Utils.createNiceIdividualName((OntClass) wrappedResource, valueComponent));
					}
					System.out.println("selected:" + selectedProperty);
				}
			});
		}
		setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	public OntResource getComboClassOrIndividualSelection() {// returns Individual or ontClass from the combo box
		OntResource wrappedResource = ((WrappedOntResource<?>) individualOrClassChooser.getSelectedItem()).getWrappedResource();
		return wrappedResource;
	}
}
