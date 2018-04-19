package ui.property;

import java.awt.Component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Literal;

import ont.OntologyManager;
import ont.PropertyAndIndividual;
import ui.UiUtils;

public class PropertyCellEditor extends AbstractCellEditor implements TreeCellEditor {
	private EditCellPanel editPanel;
	private PropertyAndIndividual propertyAndIndividual;

	@Override // I think it's called when somebody from outside want the edited value. Maybe when clicking outside
	public Object getCellEditorValue() { // builds and returns propertyAndIndividual from field EditRenderPanel, Returns the value contained in the editor.
		System.out.println("In EDITOR getCellEditorValue called");
		// fillPropertyAndIndividual
		Literal literalPropertyValue = OntologyManager.getInstance().createValueAsStringLiteral(editPanel.getValue());
		propertyAndIndividual.getIndividual().setPropertyValue(propertyAndIndividual.getOntProperty(), literalPropertyValue);
		return propertyAndIndividual;
	}

	// clearly this is called first of the 2 methods to show the display panel
	@Override // when we finish editing?
	public Component getTreeCellEditorComponent(JTree tree, Object propertyAndIndividualObject, boolean isSelected, boolean expanded, boolean leaf, int row) {
		System.out.print("In EDITOR getTreeCellEditorComponent called on property:");
		if (propertyAndIndividualObject != null && propertyAndIndividualObject instanceof DefaultMutableTreeNode) {
			Object userObject = ((DefaultMutableTreeNode) propertyAndIndividualObject).getUserObject();
			if (userObject instanceof OntClass) {
					return null;//new  DefaultCellEditor().getTreeCellEditorComponent( tree,  propertyAndIndividualObject,  isSelected,  expanded,  leaf,  row);
			}
			if (userObject instanceof PropertyAndIndividual) {
				propertyAndIndividual = (PropertyAndIndividual) userObject;
				System.out.println("on property:" + propertyAndIndividual.getOntProperty().getLocalName());
				editPanel = new EditCellPanel(propertyAndIndividual);
				if (propertyAndIndividual.getOntProperty().isDatatypeProperty()) {
					editPanel.getSaveButton().addActionListener(e -> {
						stopCellEditing();
					});
				}
				System.out.println();
			}
		} else {
			UiUtils.showDialog(tree, "Uknown object type:" + propertyAndIndividualObject);
		}
		return editPanel;
	}

	@Override
	public boolean stopCellEditing() {
		System.out.println("stopCellEditing called");
		return super.stopCellEditing();
	}

	@Override
	protected void fireEditingStopped() {
		// controller.setMode(Mode.RENDER);
		System.out.println("fireEditingStopped called");
		super.fireEditingStopped();
	}
}