package ui;

import resources.ResourceFinding;
import ui.property.PropertyEditorBigPanel;
import ui.stepnameedit.StepInstanceCellRenderer;
import ui.stepnameedit.StepInstanceNameCellEditor;

import javax.swing.*;
import javax.swing.tree.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.util.ResourceUtils;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;

import java.io.File;
import java.io.FileInputStream;
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
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import static ui.UiUtils.*;

public class WetProtocolMainPanel extends JPanel implements TreeSelectionListener {
	private URL helpURL;
	private JButton addNewSiblingNodeButton = new JButton("New Step");
	private JButton addChildNodeButton = new JButton("New Substep");
	private JButton deleteChildNodeButton = new JButton("Delete Step");
	private JButton expandTreeButton = new JButton("Expand Tree");
	private JButton saveProtocolButton = new JButton("Save Protocol");
	private JButton loadProtocolButton = new JButton("Load Protocol");
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JTree jProtocolTree;
	public static final int WITH_OF_PROTOCOL_TREE = 400;
	private final static String FRAME_TITLE = "Wet Protocol";
	private DefaultTreeModel protocolTreeModel;

	private WetProtocolMainPanel() {
		super(new GridLayout(1, 1));
		initiateTreeAndTreeModel();
		JPanel treeViewPanel = new JPanel(new BorderLayout());
		treeViewPanel.add(jProtocolTree, BorderLayout.PAGE_START);
		JPanel treeViewButtonPanel = new JPanel();
		treeViewButtonPanel.setLayout(new GridLayout(2, 1));
		treeViewButtonPanel.add(addNewSiblingNodeButton);
		treeViewButtonPanel.add(addChildNodeButton);
		treeViewButtonPanel.add(deleteChildNodeButton);
		treeViewButtonPanel.add(expandTreeButton);
		treeViewButtonPanel.add(saveProtocolButton);
		treeViewButtonPanel.add(loadProtocolButton);
		treeViewPanel.add(treeViewButtonPanel, BorderLayout.PAGE_END);
		// Create the scroll pane and add the tree view panel to it.
		JScrollPane treeViewScrollPane = new JScrollPane(treeViewPanel);
		// Add the scroll panes to a split pane.
		splitPane.setLeftComponent(treeViewScrollPane);
		createNewClassPropertyEditorPanel();
		treeViewScrollPane.setMinimumSize(new Dimension(100, 50));
		treeViewScrollPane.setPreferredSize(new Dimension(WITH_OF_PROTOCOL_TREE, 50));
		splitPane.setDividerLocation(WITH_OF_PROTOCOL_TREE); // XXX: ignored in some releases
		// of Swing. bug 4101306
		// workaround for bug 4101306:
		// treeView.setPreferredSize(new Dimension(100, 100));
		splitPane.setPreferredSize(new Dimension(1300, 500));
		// Add the split pane to this panel.
		add(splitPane);
		this.setLocation(100, this.getX());// todo remove
		AddTreeButtonListeners(splitPane);
		// addNodeEventListeners();
	}

	private void initiateTreeAndTreeModel() {
		jProtocolTree = new ToolTipJTree();
		jProtocolTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		ToolTipManager.sharedInstance().registerComponent(jProtocolTree);
		jProtocolTree.setShowsRootHandles(true);
		initiateOrRefreshTreeModelAndRest();
		jProtocolTree.setExpandsSelectedPaths(true);
		MouseListener ml = new MouseAdapter() {// right click enters edit mode
			public void mousePressed(MouseEvent e) {
				int selRow = jProtocolTree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = jProtocolTree.getPathForLocation(e.getX(), e.getY());
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = jProtocolTree.getRowForLocation(e.getX(), e.getY());
					TreePath path = jProtocolTree.getPathForLocation(e.getX(), e.getY());
					if (row != -1) {
						// if (e.getClickCount() == 1) {
						jProtocolTree.setEditable(true);
						jProtocolTree.startEditingAtPath(path);
						// }
					}
				}
			}
		};
		// jProtocolTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startEditing");//also need to jProtocolTree.setEditable(true);
		jProtocolTree.addMouseListener(ml);
	}

	private void initiateOrRefreshTreeModelAndRest() {
		OntManager.getInstance();// loads the seeded ont model or the initial one if not already seeded
		protocolTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(OntManager.getTopProtocolInstance()));// todo we might not need this
		jProtocolTree.setModel(protocolTreeModel);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) protocolTreeModel.getRoot();
		// protocolTreeModel.reload(root);//maybe not necessary
		loadStepsTreeFromModel(root);
		expandTree(jProtocolTree);
		// Enable tool tips.
		jProtocolTree.setCellRenderer(new StepInstanceCellRenderer(jProtocolTree));
		jProtocolTree.setCellEditor(new StepInstanceNameCellEditor(jProtocolTree));
		jProtocolTree.setInvokesStopCellEditing(true);// keep the changes when focus is lost
		// jProtocolTree.setEditable(true);
		// jProtocolTree.setEditable(true);
		// Listen for when the selection changes.
		jProtocolTree.addTreeSelectionListener(this);
		jProtocolTree.setSelectionRow(0);// select root and this should refresh the ClassPropertyEditorPanel
	}

	private void AddTreeButtonListeners(JSplitPane splitPane) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) jProtocolTree.getModel().getRoot();
		addNewSiblingNodeButton.addActionListener(e -> {
			// display/center the jDialog when the button is pressed
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent();
			if (selectedNode == null) {
				TreeModel model = jProtocolTree.getModel();
				if (model.getChildCount(root) == 0) {// empty model
					jProtocolTree.setSelectionRow(0);
				} else {
					selectedNode = (DefaultMutableTreeNode) model.getChild(root, model.getChildCount(root) - 1);// add sibling to last root
				}
			}
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
			if (parent == null) {
				UiUtils.showDialog(splitPane, "Can not add a sibling to root");
				return;
			}
			UiUtils.showStepChooserGuiAndCreateNewStepTree(WhereToAddStepNode.NEW_SIBBLING_STEP_NODE, jProtocolTree);// this will update the protocol tree model
		});
		addChildNodeButton.addActionListener(e -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent();
			UiUtils.showStepChooserGuiAndCreateNewStepTree(WhereToAddStepNode.NEW_CHILD_STEP_NODE, jProtocolTree);// this will update the protocol tree model
		});
		expandTreeButton.addActionListener(e -> {
			expandTree(jProtocolTree);
		});
		saveProtocolButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Select a place and name for saving your owl file");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Owl files", "owl");
			fileChooser.addChoosableFileFilter(filter);
			fileChooser.setCurrentDirectory(new File(ResourceFinding.getOntFileDir()));
			int retval = fileChooser.showSaveDialog(null);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file == null) {
					return;
				}
				if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("owl")) {
					// filename is OK as-is
				} else {
					file = new File(file.toString() + ".owl");
					// file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName())+".xml"); // ALTERNATIVELY: remove the extension (if any) and replace it with ".xml"
				}
				OntManager.saveOntologyAndCoordinates(file, jProtocolTree);
			}
		});
		loadProtocolButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Select an owl file");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Owl files", "owl");
			fileChooser.addChoosableFileFilter(filter);
			fileChooser.setCurrentDirectory(new File(ResourceFinding.getOntFileDir()));
			int retval = fileChooser.showOpenDialog(null);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file == null) {
					UiUtils.showDialog(jProtocolTree, "Cannot open the file");
					return;
				}
				OntManager.loadModelFromFileAndResetOntManager(file.getAbsolutePath());
				UiUtils.getProtocolFrame(this).setTitle(FRAME_TITLE + " " + file.getAbsolutePath());
				// DefaultTreeModel model = (DefaultTreeModel) jProtocolTree.getModel();
				// DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
				// model.reload(root);//maybe not necessary
				initiateOrRefreshTreeModelAndRest();
				// System.out.println(OntManager.getOntologyModel().listIndividuals().toList());
			}
		});
		deleteChildNodeButton.addActionListener(e -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jProtocolTree.getLastSelectedPathComponent();
			if (selectedNode.getParent() != null && !selectedNode.isRoot()) {
				if (selectedNode.getUserObject() instanceof Individual) {
					protocolTreeModel.removeNodeFromParent(selectedNode);
					OntManager.getInstance().getOntologyModel().removeAll((Resource) selectedNode.getUserObject(), null, null);
				} else {
					UiUtils.showDialog(this, "Could not remove node. Not an instance of an Individual");
				}
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
		PropertyEditorBigPanel classPropertyEditorBigPanel = new PropertyEditorBigPanel(node, jProtocolTree);
		classPropertyEditorBigPanel.setMinimumSize(new Dimension(50, 50));
		classPropertyEditorBigPanel.setPreferredSize(new Dimension(200, 400));
		classPropertyEditorBigPanel.setMaximumSize(new Dimension(200, 200));
		if (splitPane.getRightComponent() != null) {// in case we want to refresh it
			this.remove(splitPane.getRightComponent());
		}
		splitPane.setRightComponent(new JScrollPane(classPropertyEditorBigPanel));
	}

	// private void initHelp() {
	// String s = "TreeDemoHelp.html";
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
		javax.swing.SwingUtilities.invokeLater(() -> {
			UiUtils.createAndShowNewFrameGUI(new WetProtocolMainPanel(), FRAME_TITLE + OntManager.ONTOLOGY_LOCATION);
		});
	}

	public enum WhereToAddStepNode {
		NEW_SIBBLING_STEP_NODE, NEW_CHILD_STEP_NODE
	}
}
