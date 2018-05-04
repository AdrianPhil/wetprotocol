package ui.stepchooser;

import ont.OntManager;
import resources.ResourceFinding;
import ui.UiUtils;
import ui.WetProtocolMainPanel;
import ui.WetProtocolMainPanel.WhereToAddStepNode;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import static ui.UiUtils.expandTree;

public class StepChooserPanel extends JPanel implements TreeSelectionListener {
	private JEditorPane htmlPane;
	private URL helpURL;
	// private JButton addNewSiblingNodeButton = new JButton("New Sibling");
	private JButton okButton = new JButton("OK");
	private JButton expandTreeButton = new JButton("Expand Tree");
	// Create the nodes.
	// private DefaultMutableTreeNode protocolTreeParentNode;
	// most below could be cached
	private final JTree jProtocolTree;
	private final WhereToAddStepNode whereToAddStepNode;
	private DefaultTreeModel stepChooserTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Please choose a step"));
	private JTree jStepChooserTree = new JTree(stepChooserTreeModel);
	StepChooserCellEditor cellEditor ;//important for stop editing

	public StepChooserPanel(JTree jProtocolTree, WhereToAddStepNode whereToAddStepNode) {
		super(new GridLayout(1, 1));
		this.jProtocolTree = jProtocolTree;
		this.whereToAddStepNode = whereToAddStepNode;
		initiateTree();
		JPanel treeViewPanel = new JPanel(new BorderLayout());
		treeViewPanel.add(jStepChooserTree, BorderLayout.PAGE_START);
		JPanel treeViewButtonPanel = new JPanel();
		// treeViewButtonPanel.add(addNewSiblingNodeButton);
		treeViewButtonPanel.add(okButton);
		treeViewButtonPanel.add(expandTreeButton);
		treeViewPanel.add(treeViewButtonPanel, BorderLayout.PAGE_END);
		// Create the scroll pane and add the tree view panel to it.
		JScrollPane treeView = new JScrollPane(treeViewPanel);
		// Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(true);
		// initHelp();
		JScrollPane htmlView = new JScrollPane(htmlPane);
		treeView.setPreferredSize(new Dimension(400, 300));
		// Add the split pane to this panel.
		//////////////
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);
		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(600); // XXX: ignored in some releases
		// of Swing. bug 4101306
		// workaround for bug 4101306:
		splitPane.setPreferredSize(new Dimension(400, 600));
		// Add the split pane to this panel.
		add(splitPane);
		addTreeButtonListeners(treeView);
	}

	private void initiateTree() {
		// Operation topOperation = (Operation) root.getUserObject();
		// jClassTree.setEditable(true);
		jStepChooserTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jStepChooserTree.setShowsRootHandles(true);
		StepChooserUtils.createEmpyStepNodesToChooseFrom(jStepChooserTree);// this should be cached or we could cache the whole chooser
		expandTree(jStepChooserTree);
		// Enable tool tips.
		ToolTipManager.sharedInstance().registerComponent(jStepChooserTree);
		jStepChooserTree.setCellRenderer(new StepChooserCellRenderer());
		cellEditor = new StepChooserCellEditor();
		jStepChooserTree.setCellEditor(cellEditor);
		jStepChooserTree.setEditable(true);
		// Listen for when the selection changes.
		jStepChooserTree.addTreeSelectionListener(this);
		addTreeNodeMouseListeners();
	}

	private void addTreeButtonListeners(Component splitPane) {
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				classChosenResponse(splitPane);
			}
		});
		expandTreeButton.addActionListener(e -> {
			expandTree(jStepChooserTree);
		});
	}

	private void classChosenResponse(Component splitPane) {
		cellEditor.stopCellEditing();
		DefaultMutableTreeNode selectedClassNode = (DefaultMutableTreeNode) jStepChooserTree.getLastSelectedPathComponent();
		if (selectedClassNode == null) {
			UiUtils.showDialog(splitPane, "Selected node is null");// should never get here because the root should be selected
			return;
		}
		// TreeNode root = selectedClassNode.getRoot();
		DefaultMutableTreeNode selectedStepNode = (DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent();
		DefaultMutableTreeNode newChildStepNode = new DefaultMutableTreeNode
				(OntManager.createStepIndividual((ClassAndIndividualName) selectedClassNode.getUserObject() ));
		if (whereToAddStepNode == WetProtocolMainPanel.WhereToAddStepNode.NEW_CHILD_STEP_NODE) {// insert a child
			selectedStepNode.insert(newChildStepNode, selectedStepNode.getChildCount());// insert as last child
			// DefaultMutableTreeNode insertedChild = (DefaultMutableTreeNode) selectedStepNode.getChildAt(selectedStepNode.getChildCount() - 1);
			// jProtocolTree.expandPath(new TreePath(protocolTreeParentNode.getPath()));
			jProtocolTree.setSelectionPath(new TreePath(newChildStepNode.getPath()));// select it
		} else {// insert sibling after the selected one
			int childIndex = selectedStepNode.getParent().getIndex(selectedStepNode) + 1;
			((DefaultMutableTreeNode) selectedStepNode.getParent()).insert(newChildStepNode, childIndex);
			// DefaultMutableTreeNode addedChild = (DefaultMutableTreeNode) selectedStepNode.getParent().getChildAt(childIndex);
			jProtocolTree.setSelectionPath(new TreePath(newChildStepNode.getPath()));// select it
		}
		jProtocolTree.updateUI();
		((JFrame) StepChooserPanel.this.getTopLevelAncestor()).dispose();
	}

	/**
	 * Required by TreeSelectionListener interface.
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jStepChooserTree.getLastSelectedPathComponent();
		// if (node == null) return;
		// Object nodeInfo = node.getUserObject();
		// if (node.isLeaf()) {
		// Operation book = (Operation) nodeInfo;
		// displayURL(book.url);
		// if (DEBUG) {
		// System.out.print(book.url + ": \n ");
		// }
		// } else {
		// displayURL(helpURL);
		// }
		// if (DEBUG) {
		// System.out.println(nodeInfo.toString());
		// }
	}

	private void addTreeNodeMouseListeners() {
		jStepChooserTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) jStepChooserTree.getLastSelectedPathComponent();
					if (node == null)
						return;
					classChosenResponse(jStepChooserTree);
				}
			}
		});
	}

	// private void initHelp() {
	// String s = "TreeDemoHelp.html";
	// if (helpURL == null) {
	// System.err.println("Couldn't open help file: " + s);
	// } else if (DEBUG) {
	// System.out.println("Help URL is " + helpURL);
	// }
	// displayURL(helpURL);
	// }
	private void displayURL(URL url) {
		try {
			if (url != null) {
				htmlPane.setPage(url);
			} else { // null url
				htmlPane.setText("File Not Found");
				if (UiUtils.DEBUG) {
					System.out.println("Attempted to display a null URL.");
				}
			}
		} catch (IOException e) {
			System.err.println("Attempted to read a bad URL: " + url);
		}
	}

}
