package uiutil;

import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class ToolTipJTree extends JTree {
	public ToolTipJTree(DefaultTreeModel stepChooserTreeModel) {
		super(stepChooserTreeModel);
	}

	public ToolTipJTree() {
		super();
	}

	@Override
	public String getToolTipText(MouseEvent evt) {
        if (getRowForLocation(evt.getX(), evt.getY()) == -1)
          return null;
        TreePath curPath = getPathForLocation(evt.getX(), evt.getY());
        return ""+ ((DefaultMutableTreeNode) curPath.getLastPathComponent()).getUserObject();
      }
}

