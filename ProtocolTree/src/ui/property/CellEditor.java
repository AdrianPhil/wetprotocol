package ui.property;


import java.awt.Component;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import uimain.WetProtocolMainPanel;
import uiutil.UiUtils;

@SuppressWarnings("serial")
public class CellEditor extends AbstractCellEditor implements TreeCellEditor {
	private EditCellPanel editPanel;
	private PropertyAndIndividual propertyAndIndividual;
	JTree jPropertyAndIndividualTree;
	WetProtocolMainPanel wetProtocolMainPanel;

	public CellEditor(JTree jPropertyAndIndividualTree, WetProtocolMainPanel wetProtocolMainPanel/* for rename only*/) {
		this.jPropertyAndIndividualTree = jPropertyAndIndividualTree;
		this.wetProtocolMainPanel=wetProtocolMainPanel;
	}

	@Override // I think it's called when somebody from outside want the edited value. Maybe when clicking outside
	public Object getCellEditorValue() { // builds and returns propertyAndIndividual from field EditRenderPanel, Returns the value contained in the editor.
		System.out.println("In EDITOR getCellEditorValue called");
		// fillPropertyAndIndividual
		return editPanel.getNewIndividualValuesScrapedFromEditPanel();
	}

	// clearly this is called first of the 2 methods to show the display panel
	@Override // this builds and returns the edit panel for editing
	public Component getTreeCellEditorComponent(JTree tree, Object propertyAndIndividualNode, boolean isSelected, boolean expanded, boolean leaf, int row) {
		if (propertyAndIndividualNode == null || !(propertyAndIndividualNode instanceof DefaultMutableTreeNode)) {
			UiUtils.showDialog(tree, "Unknown object type:" + propertyAndIndividualNode);
			return null;// -------------->
		}
		Object userObject = ((DefaultMutableTreeNode) propertyAndIndividualNode).getUserObject();
		if (!(userObject instanceof PropertyAndIndividual)) {
			UiUtils.showDialog(tree, "User object is not PropertyAndIndividual type:" + propertyAndIndividualNode);
			return null;// -------------->
		}
		System.out.print("In EDITOR getTreeCellEditorComponent called on node:");
		propertyAndIndividual = (PropertyAndIndividual) userObject;
		System.out.println("" + propertyAndIndividual.toString());
		editPanel = new EditCellPanel(propertyAndIndividual, (DefaultMutableTreeNode) propertyAndIndividualNode, this);
		return editPanel;
	}

	@Override
	public boolean stopCellEditing() {
		System.out.println("stopCellEditing called");
		return super.stopCellEditing();
	}

	@Override
	protected void fireEditingStopped() {
		System.out.println("fireEditingStopped called");
		super.fireEditingStopped();
	}

	@Override
	public boolean isCellEditable(EventObject e) {//TODO does not work returns null always
		System.out.println("sel pth:"+((JTree)( e.getSource())).getSelectionPath());
		if (((JTree) e.getSource()).getSelectionPath() == null) {
			return false;
		} else {
			return true;
		}
	}
}