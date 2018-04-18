package oldStuff;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

//from https://stackoverflow.com/questions/8119718/treecelleditor-must-select-cell-to-edit-even-if-shouldselectcell-return-false
public class TreeNodeEditor extends AbstractCellEditor implements TreeCellEditor {
	private static final long serialVersionUID = 1L;
	private JButton saveButton;
	private JPanel panel;
	// JW: do not modify the node inside the editor
	// private DefaultMutableTreeNode node = null;
	private DefaultTreeCellRenderer defaultRenderer;
	private Object editorValue;

	public TreeNodeEditor() {
		super();
		panel = new JPanel();
		defaultRenderer = new DefaultTreeCellRenderer();
		saveButton = new JButton("Save");
		saveButton.setOpaque(true);
		saveButton.setIcon(new ImageIcon("trash.png"));
		saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveButton.setAction(createAction("save", "Save"));
		panel.add(defaultRenderer);
		panel.add(saveButton);
	}

	private Action createAction(final String actionCommand, String display) {
		Action action = new AbstractAction(display) {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Stop Editing Action Invoked");
				stopEditing(actionCommand);
			}
		};
		return action;
	}

	/**
	 * @param actionCommand
	 */
	protected void stopEditing(String actionCommand) {
		editorValue = actionCommand;
		stopCellEditing();
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		// in order to do some actions on a node
		// if (value instanceof DefaultMutableTreeNode) {
		// node = (DefaultMutableTreeNode) value;
		// }
		defaultRenderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, true);
		return panel;
	}

	/**
	 * 
	 */
	private void reset() {
		editorValue = null;
	}

	/**
	 * At this point in time the component is added to the tree (not documented!)
	 * but tree's internal cleanup might not yet be ready
	 */
	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		reset();
		if (anEvent instanceof MouseEvent) {
			redirect((MouseEvent) anEvent);
		}
		return false;
	}

	private void redirect(final MouseEvent anEvent) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MouseEvent ev = SwingUtilities.convertMouseEvent(anEvent.getComponent(), anEvent, panel);
				panel.dispatchEvent(ev);
			}
		});
	}

	@Override
	public Object getCellEditorValue() {
		return editorValue;
	}
}