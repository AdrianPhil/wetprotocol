package test.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import ont.OntManager;
import ui.UiUtils;
import ui.property.PropertyAndIndividual;

public class TestCellEditor extends AbstractCellEditor implements TreeCellEditor {
	private TestController controller = new TestController();
	private PropertyAndIndividual propertyAndIndividual;

	TestCellEditor() {
		controller.getPanel().getSaveButton().addActionListener(e -> {
			stopCellEditing();
		});
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		// from NodeContent to panel values
		System.out.println("Editor getTreeCellEditorComponent called");// edit starts
		controller.setMode(Mode.EDIT);
		if (value != null && value instanceof DefaultMutableTreeNode) {
			Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
			if (userObject instanceof TestNodeContent) {
				TestNodeContent nodeContent = (TestNodeContent) userObject;
				controller.setModel(nodeContent);
				controller.fillUiFromModel();
			} else {
				UiUtils.showDialog(tree, "Uknown object type:" + value);
			}
		}
		return controller.getPanel();
	}

	@Override
	public Object getCellEditorValue() {// here is where I see the new value Model =Old value Panel =New value so I put
										// the ui value in model and return model
		System.out.println("editor getCellEditorValue called and controller is:" + controller);
		controller.fillModelFromUi();
		return controller.getModel();
	}

	@Override
	public boolean stopCellEditing() {
		System.out.println("stopCellEditing called");
		return super.stopCellEditing();
	}

	@Override
	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}