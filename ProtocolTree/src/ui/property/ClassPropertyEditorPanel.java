package ui.property;

//root nodes) after tree.setRootVisible(false) call tree.setShowsRootHandles(true).
import ont.OntologyManager;
import ont.PropertyAndIndividual;
import ui.UiUtils;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Set;

import static ui.UiUtils.expandTree;

public class ClassPropertyEditorPanel extends JPanel implements TreeSelectionListener {
	Object o = new DefaultTreeCellRenderer();
	private JEditorPane htmlPane;
	private URL helpURL;
	// private JButton addNewSiblingNodeButton = new JButton("New Sibling");
	private JButton okButton = new JButton("OK");
	private JButton expandTreeButton = new JButton("Expand Tree");
	// Create the nodes.
	private DefaultMutableTreeNode protocolTreeNode;// the individual to change properties for
	// most below could be cached
	private JTree jProtocolTree;
	Individual individual = null;
	DefaultMutableTreeNode topNodeAsPropertyHolder = new DefaultMutableTreeNode("dummy");// this is empty
	private DefaultTreeModel protocolTreeModel = new DefaultTreeModel(topNodeAsPropertyHolder);
	private JTree jPropertyAndIndividualTree = new JTree(protocolTreeModel);// my property tree

	public ClassPropertyEditorPanel(DefaultMutableTreeNode protocolTreeNode, JTree jProtocolTree) {// todo we don't need to pass the tree in here ?
		super(new GridLayout(1, 1));
		this.jProtocolTree = jProtocolTree;
		this.protocolTreeNode = protocolTreeNode;
		if (protocolTreeNode == null) {
			UiUtils.showDialog(jProtocolTree, "In ClassPropertyEditorPanel constructor. Passed node is null");
			return;// todo should be close this window
		}
		Object userObject = protocolTreeNode.getUserObject();
		if (userObject == null || !(userObject instanceof Individual)) {
			UiUtils.showDialog(jProtocolTree, "In ClassPropertyEditorPanel constructor. Passed node is null or not an instance of Individual");
		}
		individual = (Individual) userObject;
		initiateTree();
		JPanel treeViewPanel = new JPanel(new BorderLayout());
		treeViewPanel.add(jPropertyAndIndividualTree, BorderLayout.PAGE_START);
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
		htmlPane.setText("Properties for Individual:" + individual.getLocalName());// todo show the getComment with .setPage
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
		splitPane.setDividerLocation(400); // XXX: ignored in some releases
		// of Swing. bug 4101306
		// workaround for bug 4101306:
		splitPane.setPreferredSize(new Dimension(400, 600));
		// Add the split pane to this panel.
		add(splitPane);
		AddTreeButtonListeners(treeView);
	}

	private void initiateTree() {
		jPropertyAndIndividualTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jPropertyAndIndividualTree.setShowsRootHandles(true);
		createNodes();
		expandTree(jPropertyAndIndividualTree);
		jPropertyAndIndividualTree.setCellRenderer(new PropertyCellRenderer());
		jPropertyAndIndividualTree.setEditable(true);
		jPropertyAndIndividualTree.setCellEditor(new PropertyCellEditor());
		// Enable tool tips.
		ToolTipManager.sharedInstance().registerComponent(jPropertyAndIndividualTree);
		// Listen for when the selection changes.
		jPropertyAndIndividualTree.addTreeSelectionListener(this);
		addTreeNodeMouseListeners();
	}

	private void AddTreeButtonListeners(Component splitPane) {
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptPropertiesResponse(splitPane);
			}
		});
		// todo add cancel button
		expandTreeButton.addActionListener(e -> {
			expandTree(jPropertyAndIndividualTree);
		});
	}

	/**
	 * Todo need to update the protocol tree node with the value of the properties and maybe change it's icon and refresh to show the changes
	 */
	private void acceptPropertiesResponse(Component splitPane) {
		// DefaultMutableTreeNode selectedClassNode = (DefaultMutableTreeNode)
		// jTree.getLastSelectedPathComponent();
		// if (selectedClassNode == null) {
		// UiUtils.showDialog(splitPane, "Selected node is null");
		// } else {
		// // UiUtils.showDialog(splitPane, "okButton called");
		// DefaultMutableTreeNode selectedProtocolNode = (DefaultMutableTreeNode)
		// jProtocolTree.getLastSelectedPathComponent();
		// if (selectedProtocolNode == protocolTreeNode) {// insert child as last child
		// of parent
		// protocolTreeNode.insert(selectedClassNode, protocolTreeNode.getChildCount());
		// DefaultMutableTreeNode insertedChild = (DefaultMutableTreeNode)
		// protocolTreeNode.getChildAt(protocolTreeNode.getChildCount() - 1);
		// jProtocolTree.expandPath(new TreePath(protocolTreeNode.getPath()));
		// jProtocolTree.setSelectionPath(new TreePath(insertedChild.getPath()));//
		// select it
		// } else {// insert sibling after the selected one
		// protocolTreeNode.insert(selectedClassNode,
		// selectedProtocolNode.getParent().getIndex(selectedProtocolNode) + 1);
		// DefaultMutableTreeNode addedChild = (DefaultMutableTreeNode)
		// protocolTreeNode.getChildAt(selectedProtocolNode.getParent().getIndex(selectedProtocolNode)
		// + 1);
		// jProtocolTree.setSelectionPath(new TreePath(addedChild.getPath()));// select
		// it
		// }
		// jProtocolTree.updateUI();
		// ((JFrame) ClassPropertyEditorPanel.this.getTopLevelAncestor()).dispose();
		// }
	}

	/**
	 * Required by TreeSelectionListener interface.
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jPropertyAndIndividualTree.getLastSelectedPathComponent();
		if (node == null)
			return;
		Object nodeInfo = node.getUserObject();
		// displayURL(null);
		System.out.println(" PropertyTreeSelectionChanged");
	}

	private void addTreeNodeMouseListeners() {
		jPropertyAndIndividualTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) jPropertyAndIndividualTree.getLastSelectedPathComponent();
					if (node == null)
						return;
					acceptPropertiesResponse(jPropertyAndIndividualTree);
				}
			}
		});
	}

	public void createNodes() {
		Object userObject = protocolTreeNode.getUserObject();// in protocol we have individuals
		assert userObject instanceof Individual;
		Individual individual = ((Individual) userObject);
		OntClass ontClass = individual.getOntClass();
		DefaultMutableTreeNode currentTopNode = topNodeAsPropertyHolder;
		Set<OntProperty> props = OntologyManager.getInstance().dumpCalculatedPropertiesForAClass(ontClass);
		for (OntProperty ontProperty : props) {
			System.out.println("creating first level prop node for " + ontProperty.getLocalName());
			createNode(ontProperty, currentTopNode, false);
		}
	}

	// recursive
	private DefaultMutableTreeNode createNode(OntProperty ontProperty, DefaultMutableTreeNode currentTopNode, boolean isFromRangeSubclass) {
		DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(new PropertyAndIndividual(ontProperty, individual));
		if (ontProperty.isDatatypeProperty()) {
			System.out.println("\tcreating literal prop node for " + ontProperty.getLocalName());
			currentTopNode.add(newChild);// Editable
		} // object type property{
		System.out.println("\tcreating object type prop node for " + ontProperty.getLocalName());
		currentTopNode.add(newChild);// would be Read Only as it's an object property
		currentTopNode = newChild;
		// break down this object property
		OntResource range = ontProperty.getRange();
		System.out.println("\trange:" + range);
		// for (OntClass subclassFromRange : range.asClass().listSubClasses(false).toSet()) {
		// System.out.println("\t\t dealing with range subclass:"+subclassFromRange.getLocalName());
		// DefaultMutableTreeNode newClassChild = new DefaultMutableTreeNode(subclassFromRange);
		// DefaultMutableTreeNode rangeLevelNode=currentTopNode;
		// currentTopNode.add(newClassChild);// would be Read Only as it's an object property
		// currentTopNode = newClassChild;
		// Set<OntProperty> props = OntologyManager.getInstance().dumpCalculatedPropertiesForAClass(subclassFromRange);
		// for (OntProperty p : props) {
		// System.out.println("\t\t\t dealing with range property:"+p.getLocalName());
		// createNode(p, currentTopNode, true);
		// }
		// currentTopNode=rangeLevelNode;
		// System.out.println("\t\t finished dealing with range subclass:"+subclassFromRange.getLocalName());
		// }
		System.out.println("\tfinished with range :" + range);
		// describe the property itself even if it do
		if (!OntologyManager.isStandalone(ontProperty)) {
			Set<OntProperty> rangeProps = OntologyManager.getInstance().dumpCalculatedPropertiesForAClass(range.asClass());
			for (OntProperty rangeProperty : rangeProps) {
				System.out.println("creating first level prop node for " + rangeProperty.getLocalName());
				createNode(rangeProperty, currentTopNode, false);
			}
		}
		return currentTopNode;
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(Color.RED);
	}
}
