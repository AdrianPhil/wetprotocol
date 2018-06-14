package ui.stepchooser;


import java.awt.Component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import uiutil.UiUtils;

@SuppressWarnings("serial")
public class StepChooserCellEditor extends AbstractCellEditor implements TreeCellEditor {
	private EditCellPanel stepChooserNameEditCellPanel;
	// ClassAndIndividualName classAndIndividualName;

	@Override // for sure Called when somebody from outside want the edited value. Maybe when clicking outside
	public Object getCellEditorValue() { // builds and returns propertyAndClassNameAndIndividualName from field EditRenderPanel, Returns the value contained in the editor.
		System.out.println("in getCellEditorValue =================");
		return stepChooserNameEditCellPanel.getNewUserObjectScrapedFromEditPanel();
		// we disregard the editor!!!!
	}

	// this is called from outside with a passed in user object and it initializes and display the edit panel
	// clearly this is called first of the 2 methods to show the display panel
	@Override // when we finish editing?
	public Component getTreeCellEditorComponent(JTree tree, Object classAndIndividualNameNode, boolean isSelected, boolean expanded, boolean leaf, int row) {
		if (classAndIndividualNameNode != null && classAndIndividualNameNode instanceof DefaultMutableTreeNode) {
			Object userObject = ((DefaultMutableTreeNode) classAndIndividualNameNode).getUserObject();
			if (userObject instanceof ClassAndIndividualName) {
				stepChooserNameEditCellPanel = new EditCellPanel((ClassAndIndividualName) userObject, (DefaultMutableTreeNode) classAndIndividualNameNode);
			}
		} else {
			UiUtils.showDialog(tree, "Uknown object type:" + classAndIndividualNameNode);
		}
		return stepChooserNameEditCellPanel;
	}

	@Override
	public boolean stopCellEditing() {
		System.out.println("in stopCellEditing");
		return super.stopCellEditing();
	}

	@Override
	protected void fireEditingStopped() {
		System.out.println("in fireEditingStopped");
		super.fireEditingStopped();
	}
}