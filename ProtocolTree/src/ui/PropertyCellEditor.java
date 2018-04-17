package ui;

import java.awt.Component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import org.apache.jena.rdf.model.Literal;

import ont.OntologyManager;
import ont.PropertyAndIndividual;

public class PropertyCellEditor extends AbstractCellEditor implements TreeCellEditor {
	private JTextField val;
	private JLabel label;
	private JPanel editPanel;
	private PropertyAndIndividual propertyAndIndividual;

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		System.out.println("getTreeCellEditorComponent called");
		if (value != null && value instanceof DefaultMutableTreeNode) {
			Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
			if (userObject instanceof PropertyAndIndividual) {
				propertyAndIndividual = (PropertyAndIndividual) userObject;
				if (propertyAndIndividual.getOntProperty().isDatatypeProperty() && propertyAndIndividual.getOntProperty().isLiteral()) {
					System.out.println("	if (propertyAndIndividual.getOntProperty().isDatatypeProperty() && propertyAndIndividual.getOntProperty().isLiteral()) {");
					label.setText(propertyAndIndividual.getValue().toString());// to check usage of the tostring
					val.setText(propertyAndIndividual.getValue().toString());
					editPanel.add(label);
					editPanel.add(val);
				}
			} else {
				UiUtils.showDialog(tree, "Uknown object type:" + value);
			}
		}
		return editPanel;
	}

	@Override
	public Object getCellEditorValue() {
		System.out.println("getCellEditorValue called");
		System.out.println();
		System.out.println("getCellEditoValue returns :" + val.getText());
		propertyAndIndividual.setValue(OntologyManager.getInstance().getTypedLiteral(val.getText()));
		return propertyAndIndividual;
	}
}