package ui;

import resources.ResourceFindingDummyClass;
import ui.instancenameedit.InstanceNameCellEditor;
import ui.property.ClassPropertyEditorPanel;

import javax.swing.*;
import javax.swing.tree.*;

import org.apache.jena.ontology.Individual;
import org.apache.jena.util.ResourceUtils;
import org.apache.jena.ontology.OntResource;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import ont.OntManager;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URISyntaxException;
import java.net.URL;
import static ui.UiUtils.*;

public class WetProtocolMainPanel extends JPanel implements TreeSelectionListener {
	private URL helpURL;
	private JButton addNewSiblingNodeButton = new JButton("New Step");
	private JButton addChildNodeButton = new JButton("New Substep");
	private JButton deleteChildNodeButton = new JButton("Delete Step");
	private JButton expandTreeButton = new JButton("Expand Tree");
	private JButton saveProtocolButton = new JButton("Save Protocol");
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private DefaultTreeModel protocolTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(OntManager.getInstance().getTopProtocoInstancel()));// todo we might not need this
	private JTree jProtocolTree;
	private static boolean DEBUG = true; // adrian
	public static final int WITH_OF_PROTOCOL_TREE = 200;

	private WetProtocolMainPanel() {
		super(new GridLayout(1, 1));
		initiateTree();
		JPanel treeViewPanel = new JPanel(new BorderLayout());
		treeViewPanel.add(jProtocolTree, BorderLayout.PAGE_START);
		JPanel treeViewButtonPanel = new JPanel();
		treeViewButtonPanel.add(addNewSiblingNodeButton);
		treeViewButtonPanel.add(addChildNodeButton);
		treeViewButtonPanel.add(deleteChildNodeButton);
		treeViewButtonPanel.add(expandTreeButton);
		treeViewButtonPanel.add(saveProtocolButton);
		treeViewPanel.add(treeViewButtonPanel, BorderLayout.PAGE_END);
		// Create the scroll pane and add the tree view panel to it.
		JScrollPane treeViewScrollPane = new JScrollPane(treeViewPanel);
		// Add the scroll panes to a split pane.
		splitPane.setLeftComponent(treeViewScrollPane);
		createNewClassPropertyEditorPanel();
		treeViewScrollPane.setMinimumSize(new Dimension(100, 50));
		splitPane.setDividerLocation(WITH_OF_PROTOCOL_TREE); // XXX: ignored in some releases
		// of Swing. bug 4101306
		// workaround for bug 4101306:
		// treeView.setPreferredSize(new Dimension(100, 100));
		splitPane.setPreferredSize(new Dimension(1200, 400));
		// Add the split pane to this panel.
		add(splitPane);
		this.setLocation(100, this.getX());// todo remove
		AddTreeButtonListeners(splitPane);
		// addNodeEventListeners();
	}

	private void initiateTree() {
		jProtocolTree = new JTree(protocolTreeModel);
		jProtocolTree.setEditable(false);
		jProtocolTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jProtocolTree.setShowsRootHandles(true);
		createInstanceNodes((DefaultMutableTreeNode) (protocolTreeModel.getRoot()));
		expandTree(jProtocolTree);
		// Enable tool tips.
		ToolTipManager.sharedInstance().registerComponent(jProtocolTree);
		jProtocolTree.setCellRenderer(new ProtocolInstanceCellRenderer());
		
		//jProtocolTree.setCellEditor(new InstanceNameCellEditor());
		//jProtocolTree.setEditable(true);
		
		// Listen for when the selection changes.
		jProtocolTree.addTreeSelectionListener(this);
		jProtocolTree.setSelectionRow(1);// select first after root
	}

	private void AddTreeButtonListeners(JSplitPane splitPane) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) jProtocolTree.getModel().getRoot();
		addNewSiblingNodeButton.addActionListener(e -> {
			// display/center the jDialog when the button is pressed
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent();
			if (selectedNode == null) {
				// UiUtils.showDialog(splitPane, "Please select a node");
				TreeModel model = jProtocolTree.getModel();
				selectedNode = (DefaultMutableTreeNode) model.getChild(root, model.getChildCount(root) - 1);// add sibling to last root
			}
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
			if (parent == null) {
				UiUtils.showDialog(splitPane, "Can not add a sibling to root");
				return;
			}
			UiUtils.createAndShowStepChooserGUI(parent, jProtocolTree);// this will update the protocol tree model
			// UiUtils.createChildNode(newClass, parent, jProtocolTree,
			// parent.getChildCount());
			// Make sure the user can see the new node.
		});
		addChildNodeButton.addActionListener(e -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent();
			// if (selectedNode == null) {
			// // UiUtils.showDialog(splitPane, "Selected node is null");
			// selectedNode = root;// add child to root todo strange does not work
			// jProtocolTree.getSelectionModel().setSelectionPath(new TreePath(((DefaultTreeModel) jProtocolTree.getModel()).getPathToRoot(root)));// select root
			// }
			UiUtils.createAndShowStepChooserGUI(selectedNode, jProtocolTree);// this will update the protocol tree model
			// UiUtils.createChildNode(newClass, selectedNode, jProtocolTree,
			// selectedNode.getChildCount());
		});
		expandTreeButton.addActionListener(e -> {
			expandTree(jProtocolTree);
		});
		saveProtocolButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select a place and name for saving your owl file");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Owl files", "owl");
				fileChooser.addChoosableFileFilter(filter);
				String path = null;
				try {
					path = ResourceFindingDummyClass.getResource(OntManager.PROTOCOL_FILE).toURI().getPath();
				} catch (URISyntaxException e2) {
					e2.printStackTrace();
				}
				fileChooser.setCurrentDirectory(new File(path.substring(1, path.length() - OntManager.PROTOCOL_FILE.length())));
				int retval = fileChooser.showSaveDialog(null);
				if (retval == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (file == null) {
						return;
					}
					try (FileOutputStream output = new FileOutputStream(file)) {
						OntManager.getOntologyModel().writeAll(output, "RDF/XML", OntManager.NS);
					} catch (Exception e1) {
						UiUtils.showDialog(jProtocolTree, "Cannot open output file" + e1.getLocalizedMessage());
					}
				}
			}
		});
		deleteChildNodeButton.addActionListener(e -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent();
			if (selectedNode.getParent() != null && !selectedNode.isRoot()) {
				protocolTreeModel.removeNodeFromParent(selectedNode);
			}
		});
	}
	// public void addNodeEventListeners() {
	// // for double click for rename individual
	// jProtocolTree.addMouseListener(new MouseAdapter() {
	// @Override
	// public void mouseClicked(MouseEvent e) {
	// if (e.getClickCount() == 2) {
	// DefaultMutableTreeNode node = (DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent();
	// if (node == null)
	// return;
	// UiUtils.renameNodeUI(node.getUserObject());
	// }
	// }
	// });
	// }

	/*
	 * Tree node selection
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		createNewClassPropertyEditorPanel();
		// //UiUtils.createAndShowNewFrameGUI(classPropertyEditorPanel, "");
		// if (node == null)
		// return;
		// Object nodeInfo = node.getUserObject();
		// if (node.isLeaf()) {
		// OntResource protocolInstanceObject = (OntResource) nodeInfo;
		// System.out.println(protocolInstanceObject);
		// showEditorPanel(node);
		// } else {
		// showEditorPanel(node);
		// }
	}

	private void createNewClassPropertyEditorPanel() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent();
		if (node == null) {
			node = (DefaultMutableTreeNode) protocolTreeModel.getRoot();
		}
		ClassPropertyEditorPanel classPropertyEditorPanel = new ClassPropertyEditorPanel(node, jProtocolTree);
		classPropertyEditorPanel.setMinimumSize(new Dimension(50, 50));
		classPropertyEditorPanel.setPreferredSize(new Dimension(200, 200));
		classPropertyEditorPanel.setMaximumSize(new Dimension(200, 200));
		splitPane.setRightComponent(new JScrollPane(classPropertyEditorPanel));
	}

	// private void initHelp() {
	// String s = "TreeDemoHelp.html";
	// helpURL = ResourceFindingDummyClass.getResource(s);
	// if (helpURL == null) {
	// System.err.println("Couldn't open help file: " + s);
	// } else if (DEBUG) {
	// System.out.println("Help URL is " + helpURL);
	// }
	// showEditorPanel(null);// todo this should be the protocol node
	// }
	private void showEditorPanel(DefaultMutableTreeNode node) {// todo maybe put a real class in here
		try {
			if (node != null) {
				// classPropertyEditorPanel.setPage(url);
			} else { // null url
				// classPropertyEditorPanel.setText("File Not Found");
				if (DEBUG) {
					System.out.println("Attempted to display a null URL.");
				}
			}
		} catch (Exception e) {// todo
			System.err.println("Attempted to read a bad node: " + node.getUserObject().toString());
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		// Create and set up the content pane.
		javax.swing.SwingUtilities.invokeLater(() -> UiUtils.createAndShowNewFrameGUI(new WetProtocolMainPanel(), "Wet Protocol"));
	}
}
