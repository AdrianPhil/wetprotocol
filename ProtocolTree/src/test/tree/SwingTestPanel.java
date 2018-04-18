package test.tree;

import resources.ResourceFindingDummyClass;
import ui.UiUtils;

import javax.swing.*;
import javax.swing.tree.*;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;

import oldStuff.DummyPane;
import oldStuff.TreeNodeEditor;
import ont.OntologyManager;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.io.IOException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static ui.UiUtils.*;

public class SwingTestPanel extends JPanel implements TreeSelectionListener {
	// renderer.getUploadButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));       renderer.getDownloadButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	private JTree jProtocolTree;
	private DefaultTreeModel protocolTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(new TestNodeContent("Just a string")));

	private SwingTestPanel() {
		super(new GridLayout(1, 1));
		initiateTree();
		JPanel treeViewPanel = new JPanel(new BorderLayout());
		treeViewPanel.add(jProtocolTree, BorderLayout.PAGE_START);
		treeViewPanel.setMinimumSize(new Dimension(100, 50));
		treeViewPanel.setPreferredSize(new Dimension(900, 300));
		add(treeViewPanel);

	}

	private void initiateTree() {
		// Operation topOperation = (Operation) root.getUserObject();
		jProtocolTree = new JTree(protocolTreeModel);
		// Create a jProtocolTree that allows one selection at a time.
		jProtocolTree.setEditable(false);
		jProtocolTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jProtocolTree.setShowsRootHandles(true);
		createDummyNodes((DefaultMutableTreeNode) (protocolTreeModel.getRoot()));
		jProtocolTree.setCellRenderer(new TestCellRenderer());
		//jProtocolTree.setCellEditor(new TreeNodeEditor());
		jProtocolTree.setCellEditor(new TestCellEditor());
		jProtocolTree.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "startEditing");//can edit with space now
		//see also https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TableFTFEditDemoProject/src/components/IntegerEditor.java for	jProtocolTree.setInvokesStopCellEditing(true);//this helps stop editing within focus of tree
		jProtocolTree.setEditable(true);
		// todo add editor
		// Listen for when the selection changes.
		jProtocolTree.addTreeSelectionListener(this);
		jProtocolTree.setSelectionRow(0);// select root
	}

	private void createDummyNodes(DefaultMutableTreeNode defaultMutableTreeNode) {
		((DefaultMutableTreeNode) (protocolTreeModel.getRoot())).add(new DefaultMutableTreeNode(new TestNodeContent("second string value")));
	}

	/**
	 * Tree node selection
	 */

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		System.out.println("valueChanged(TreeSelectionEvent e");
	}

	private void showEditorPanel(DefaultMutableTreeNode node) {// todo maybe put a real class in here
		System.out.println("showEditorPanel");
		try {
			if (node != null) {
				// classPropertyEditorPanel.setPage(url);
			} else { // null url
				// classPropertyEditorPanel.setText("File Not Found");
			}
		} catch (Exception e) {// todo
			System.err.println("Attempted to read a bad node: " + node.getUserObject().toString());
		}
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> UiUtils.createAndShowNewFrameGUI(new SwingTestPanel(), "test"));
	}

}
