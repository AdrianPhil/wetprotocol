package test.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.apache.jena.ontology.Individual;

import resources.ResourceFindingDummyClass;
import ui.UiUtils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

public class TestCellRenderer implements TreeCellRenderer {

	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
	Color backgroundSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
	Color backgroundNonSelectionColor;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if (value == null) {
			UiUtils.showDialog(tree, "In Protocol. The value to be rendered is null. Using default renderer");
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		if (!(value instanceof DefaultMutableTreeNode)) {
			UiUtils.showDialog(tree, "In Protocol. The value to be rendered not DefaultMutableTreeNode. Using default renderer");
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
		if (!(userObject instanceof TestNodeContent)) {
			UiUtils.showDialog(tree, "In Protocol. The value to be rendered not NodeContent. Using default renderer");
			return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		TestNodeContent nodeContent = (TestNodeContent) userObject;
		System.out.println("in renderer getTreeCellRendererComponent and node content:" + nodeContent);
		TestEditRenderPanel testEditRenderPanel = new TestEditRenderPanel();
		testEditRenderPanel.setField1LabelText(nodeContent.getString1());
		testEditRenderPanel.setField2LabelText(nodeContent.getString2());
		if (selected)
		{
			testEditRenderPanel.setBackground(backgroundSelectionColor);
		} else {
			testEditRenderPanel.setBackground(backgroundNonSelectionColor);
		}
		testEditRenderPanel.setEnabled(tree.isEnabled());
		return testEditRenderPanel;
	}
}
