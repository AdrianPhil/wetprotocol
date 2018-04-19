package ui.property;

import java.awt.Component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Literal;

import ont.OntologyManager;
import ont.PropertyAndIndividual;
import ui.UiUtils;

public class PropertyCellEditor extends AbstractCellEditor implements TreeCellEditor {
	private EditRenderPanel editPanel;
	private PropertyAndIndividual propertyAndIndividual;
	String thevalue;

	@Override
	public Object getCellEditorValue() { // builds and returns propertyAndIndividual from field EditRenderPanel
		System.out.println("In EDITOR getCellEditorValue called");
		editPanel.getSaveButton().addActionListener(e -> {
			stopCellEditing();
		});
		// fillPropertyAndIndividual
		Literal literalPropertyValue = OntologyManager.getInstance().getRandomLiteral(editPanel.getValue());
		propertyAndIndividual.getIndividual().setPropertyValue(propertyAndIndividual.getOntProperty(), literalPropertyValue);
		return propertyAndIndividual;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object propertyAndIndividualObject, boolean isSelected, boolean expanded, boolean leaf, int row) {
		System.out.println("In EDITOR getTreeCellEditorComponent called");
		if (propertyAndIndividualObject != null && propertyAndIndividualObject instanceof DefaultMutableTreeNode) {
			Object userObject = ((DefaultMutableTreeNode) propertyAndIndividualObject).getUserObject();
			if (userObject instanceof PropertyAndIndividual) {
				propertyAndIndividual = (PropertyAndIndividual) userObject;
				editPanel = new EditRenderPanel(propertyAndIndividual);
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