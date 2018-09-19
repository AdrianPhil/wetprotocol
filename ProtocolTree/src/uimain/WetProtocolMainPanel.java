package uimain;

import resources.ResourceFinding;
import ui.property.PropertyEditorBigPanel;
import ui.stepnameedit.StepInstanceCellRenderer;
import ui.stepnameedit.StepInstanceNameCellEditor;
import uiutil.OntResourceNameFormattedTextBox;
import uiutil.ToolTipJTree;
import uiutil.TreeTransferHandler;
import uiutil.UiUtils;
import utils.Utils;

import javax.swing.*;
import javax.swing.tree.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.Resource;

import java.io.File;

import ont.OntManager;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import static uiutil.UiUtils.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("serial")
public class WetProtocolMainPanel extends JPanel implements TreeSelectionListener {
	// private URL helpURL;
	private JButton addNewSiblingNodeButton = new JButton("New Step");
	private JButton addChildNodeButton = new JButton("New Substep");
	private JButton deleteChildNodeButton = new JButton("Delete Step");
	private JButton saveProtocolButton = new JButton("Save Protocol");
	private JButton loadProtocolButton = new JButton("Load Protocol");
	private JButton saveAsProtocolButton = new JButton("Save Protocol As");
	private JButton expandTreeButton = new JButton("Expand Tree");
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JTree jStepTree;
	public static final int WITH_OF_STEPS_TREE = 400;
	private final static String FRAME_TITLE = "Wet Protocol";
	// private DefaultTreeModel stepsTreeModel;

	private WetProtocolMainPanel() {
		super(new GridLayout(1, 1));
		initiateTreeAndTreeModel();
		JPanel treeViewPanel = new JPanel(new BorderLayout());
		treeViewPanel.add(jStepTree, BorderLayout.PAGE_START);
		JPanel treeViewButtonPanel = new JPanel();
		treeViewButtonPanel.setLayout(new GridLayout(3, 1)); // columns ,rows
		treeViewButtonPanel.add(addNewSiblingNodeButton);
		treeViewButtonPanel.add(addChildNodeButton);
		treeViewButtonPanel.add(deleteChildNodeButton);
		treeViewButtonPanel.add(loadProtocolButton);
		treeViewButtonPanel.add(saveProtocolButton);
		treeViewButtonPanel.add(saveAsProtocolButton);
		treeViewButtonPanel.add(expandTreeButton);
		treeViewButtonPanel.add(new OntResourceNameFormattedTextBox());
		// Create the scroll pane and add the tree view panel to it.
		JScrollPane treeViewScrollPane = new JScrollPane(treeViewPanel);
		// create a left panel with treeViewScollPane on top and buttons on bottom
		JPanel leftPanelWithScrollableTreeAndButtonPanel = new JPanel(new BorderLayout());
		leftPanelWithScrollableTreeAndButtonPanel.add(treeViewScrollPane, BorderLayout.CENTER);
		leftPanelWithScrollableTreeAndButtonPanel.add(treeViewButtonPanel, BorderLayout.PAGE_END);
		// Add the scroll panes to a split pane.
		splitPane.setLeftComponent(leftPanelWithScrollableTreeAndButtonPanel);
		createNewClassPropertyEditorPanel();
		treeViewScrollPane.setMinimumSize(new Dimension(100, 50));
		treeViewScrollPane.setPreferredSize(new Dimension(WITH_OF_STEPS_TREE, 50));
		splitPane.setDividerLocation(WITH_OF_STEPS_TREE); // XXX: ignored in some releases
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
		jStepTree = new ToolTipJTree();
		jStepTree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);// was single tree selection
		jStepTree.setDragEnabled(true);
		jStepTree.setDropMode(DropMode.ON_OR_INSERT);
		jStepTree.setTransferHandler(new TreeTransferHandler());
		ToolTipManager.sharedInstance().registerComponent(jStepTree);
		jStepTree.setShowsRootHandles(true);
		initiateOrRefreshTreeModelAndRest();
		jStepTree.setExpandsSelectedPaths(true);
		MouseListener popupListener = new MouseAdapter() {// right click enters edit mode
			public void mousePressed(MouseEvent e) {// need to go low level to get the selected path for the selected node
				int selRow = jStepTree.getRowForLocation(e.getX(), e.getY());
				if (selRow == -1)
					return;// --------------->
				TreePath selPath = jStepTree.getPathForLocation(e.getX(), e.getY());// todo probably we could check the path instead of row =-1
				if (SwingUtilities.isRightMouseButton(e)) {
					// pop up the context sensitive menu
					myPopUpMenu(e);
				}
			}

			private void myPopUpMenu(MouseEvent e) {
				TreePath selPath = jStepTree.getPathForLocation(e.getX(), e.getY());// todo probably we could check the path instead of row =-1
				jStepTree.setSelectionPath(selPath);
				DefaultMutableTreeNode selectedStepNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
				Object selectedIndividual = selectedStepNode.getUserObject();
				if (!(selectedIndividual instanceof Individual)) {
					return;
				}
				int x = e.getX();
				int y = e.getY();
				JTree tree = (JTree) e.getSource();
				TreePath path = tree.getPathForLocation(x, y);
				if (path == null)
					return;
				tree.setSelectionPath(path);
				JPopupMenu popup = new JPopupMenu();
				JMenuItem editMenuItem = new JMenuItem("edit " + ((Individual) selectedIndividual).getLocalName());
				editMenuItem.addActionListener((ignoredEvent) -> {
					editNodeAction(selPath);
				});
				popup.add(editMenuItem);
				JMenuItem cloneMenuItem = new JMenuItem("copy " + ((Individual) selectedIndividual).getLocalName());
				popup.add(cloneMenuItem);
				cloneMenuItem.addActionListener((ignoredEvent) -> {
					copyNodeAction(selectedStepNode, jStepTree);
				});
				//
				popup.show(tree, x, y);
			}
		};
		// jProtocolTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startEditing");//also need to jProtocolTree.setEditable(true);
		jStepTree.addMouseListener(popupListener);
	}

	public void initiateOrRefreshTreeModelAndRest() {
		OntManager.getInstance();// loads the seeded ontology model or the initial one if not already seeded
		DefaultTreeModel stepsTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(OntManager.getTopStepsInstance()));// todo we might not need this
		jStepTree.setModel(stepsTreeModel);
		// protocolTreeModel.reload(root);//maybe not necessary
		UiUtils.loadStepsTreeFromModel(jStepTree);
		expandTree(jStepTree);
		// Enable tool tips.
		jStepTree.setCellRenderer(new StepInstanceCellRenderer(jStepTree));// no need to change to WetProtocolMainPanel because is not stored
		jStepTree.setCellEditor(new StepInstanceNameCellEditor(this));
		jStepTree.setInvokesStopCellEditing(true);// keep the changes when focus is lost
		// jProtocolTree.setEditable(true);
		// jProtocolTree.setEditable(true);
		// Listen for when the selection changes.
		jStepTree.addTreeSelectionListener(this);
		jStepTree.setSelectionRow(0);// select root and this should refresh the ClassPropertyEditorPanel
	}

	private void AddTreeButtonListeners(JSplitPane splitPane) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) jStepTree.getModel().getRoot();
		addNewSiblingNodeButton.addActionListener(e -> {
			// display/center the jDialog when the button is pressed
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jStepTree.getLastSelectedPathComponent();
			if (selectedNode == null) {
				TreeModel model = jStepTree.getModel();
				if (model.getChildCount(root) == 0) {// empty model
					jStepTree.setSelectionRow(0);
				} else {
					selectedNode = (DefaultMutableTreeNode) model.getChild(root, model.getChildCount(root) - 1);// add sibling to last root
				}
			}
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
			if (parent == null) {
				UiUtils.showDialog(splitPane, "Can not add a sibling to root");
				return;
			}
			UiUtils.showStepChooserGuiAndCreateNewStepTree(WhereToAddStepNode.NEW_SIBBLING_STEP_NODE, jStepTree);// this will update the protocol tree model
		});
		addChildNodeButton.addActionListener(e -> {
			UiUtils.showStepChooserGuiAndCreateNewStepTree(WhereToAddStepNode.NEW_CHILD_STEP_NODE, jStepTree);// this will update the protocol tree model
		});
		expandTreeButton.addActionListener(e -> {
			expandTree(jStepTree);
		});
		saveProtocolButton.addActionListener(e -> {
			if (OntManager.getOwlFileName() != null) {
				OntManager.saveOntologyAndCoordinates(new File(OntManager.getOwlFileName()), jStepTree);
			} else {
				saveAsAction();
				UiUtils.getProtocolFrame(this).setTitle(FRAME_TITLE + " " + OntManager.getOwlFileName());
			}
		});
		saveAsProtocolButton.addActionListener(e -> {
			saveAsAction();
			UiUtils.getProtocolFrame(this).setTitle(FRAME_TITLE + " " + OntManager.getOwlFileName());
		});
		loadProtocolButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Select an owl file");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Owl files", "owl");
			fileChooser.addChoosableFileFilter(filter);
			String fn = OntManager.getOwlFileName();
			if (fn != null) {
				fileChooser.setCurrentDirectory(new File(fn).getParentFile());
			} else {
				fileChooser.setCurrentDirectory(new File(ResourceFinding.getOntFileDir()));
			}
			int retval = fileChooser.showOpenDialog(null);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file == null) {
					UiUtils.showDialog(jStepTree, "Cannot open the file");
					return;
				}
				try {
					OntManager.loadModelFromFileAndResetOntManager(file.getAbsolutePath());
				} catch (Exception ex) {
					UiUtils.showDialog(jStepTree, "Cannot parse the file:" + file.getAbsolutePath());
					return;
				}
				OntManager.setOwlFileName(file.toString());
				UiUtils.getProtocolFrame(this).setTitle(FRAME_TITLE + " " + file.getAbsolutePath());
				// DefaultTreeModel model = (DefaultTreeModel) jProtocolTree.getModel();
				// DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
				// model.reload(root);//maybe not necessary
				initiateOrRefreshTreeModelAndRest();
				// System.out.println(OntManager.getOntologyModel().listIndividuals().toList());
			}
		});
		deleteChildNodeButton.addActionListener(e -> {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jStepTree.getLastSelectedPathComponent();
			if (selectedNode.getParent() != null && !selectedNode.isRoot()) {
				if (selectedNode.getUserObject() instanceof Individual) {
					((DefaultTreeModel) jStepTree.getModel()).removeNodeFromParent(selectedNode);
					// xox
					OntManager.getInstance();
					OntManager.getOntologyModel().removeAll((Resource) selectedNode.getUserObject(), null, null);
				} else {
					UiUtils.showDialog(this, "Could not remove node. Not an instance of an Individual");
				}
			}
		});
	}

	private void editNodeAction(TreePath path) {
		// if (e.getClickCount() == 1) {
		jStepTree.setEditable(true); // editing action starts here
		jStepTree.startEditingAtPath(path);
		
	}

	private void copyNodeAction(DefaultMutableTreeNode selectedStepNode, JTree jStepTree) {
		// clone or copy stuff
		Individual individual = (Individual) (selectedStepNode.getUserObject());
		DefaultMutableTreeNode newClonedStepNode = new DefaultMutableTreeNode(OntManager.createClonedStepIndividual(individual.getLocalName(), individual.getOntClass()));
		((DefaultMutableTreeNode) selectedStepNode.getParent()).insert(newClonedStepNode, selectedStepNode.getParent().getIndex(selectedStepNode) + 1);
		// DefaultMutableTreeNode addedChild = (DefaultMutableTreeNode) selectedStepNode.getParent().getChildAt(childIndex);
		jStepTree.setSelectionPath(new TreePath(newClonedStepNode.getPath()));// select it
		jStepTree.updateUI();
	}

	private void saveAsAction() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select a place and name for saving your owl file");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Owl files", "owl");
		fileChooser.addChoosableFileFilter(filter);
		if (OntManager.getOwlFileName() != null) {
			fileChooser.setCurrentDirectory(new File(OntManager.getOwlFileName()));
		} else {
			fileChooser.setCurrentDirectory(new File(ResourceFinding.getOntFileDir()));
		}
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
			OntManager.saveOntologyAndCoordinates(file, jStepTree);
			OntManager.setOwlFileName(file.toString());
		}
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
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jStepTree.getLastSelectedPathComponent();
		if (node == null) {
			node = Utils.getRoot(jStepTree);
		}
		PropertyEditorBigPanel classPropertyEditorBigPanel = new PropertyEditorBigPanel(node, this);
		classPropertyEditorBigPanel.setMinimumSize(new Dimension(50, 50));
		classPropertyEditorBigPanel.setPreferredSize(new Dimension(200, 400));
		classPropertyEditorBigPanel.setMaximumSize(new Dimension(200, 200));
		if (splitPane.getRightComponent() != null) {// in case we want to refresh it
			this.remove(splitPane.getRightComponent());
		}
		splitPane.setRightComponent(new JScrollPane(classPropertyEditorBigPanel));
	}

	public JTree getjStepTree() {
		return jStepTree;
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
