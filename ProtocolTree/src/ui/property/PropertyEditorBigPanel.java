package ui.property;

//root nodes) after tree.setRootVisible(false) call tree.setShowsRootHandles(true).
import ont.OntManager;
import uimain.WetProtocolMainPanel;
import uiutil.ToolTipJTree;
import uiutil.UiUtils;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.RDFNode;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import static uiutil.UiUtils.expandTree;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

@SuppressWarnings("serial")
public class PropertyEditorBigPanel extends JPanel implements TreeSelectionListener {
	Object defaultCellRenderer = new DefaultTreeCellRenderer();
	private JEditorPane htmlPane;
	//private URL helpURL;
	// private JButton addNewSiblingNodeButton = new JButton("New Sibling");
	private JButton okButton = new JButton("OK");
	private JButton expandTreeButton = new JButton("Expand Tree");
	// Create the nodes.
	private DefaultMutableTreeNode protocolTreeNode;// the individual to change properties for
	// most below could be cached
	WetProtocolMainPanel wetProtocolMainPanel;
	Individual individual = null;
	private final DefaultMutableTreeNode TOP_NODE_AS_PROPERTY_HOLDER = new DefaultMutableTreeNode("top");// this is empty
	private DefaultTreeModel protocolTreeModel = new DefaultTreeModel(TOP_NODE_AS_PROPERTY_HOLDER);
	private JTree jPropertyAndIndividualTree = new ToolTipJTree(protocolTreeModel);// my property tree

	public PropertyEditorBigPanel(DefaultMutableTreeNode protocolTreeNode, WetProtocolMainPanel wetProtocolMainPanel) {// we pass the panel so we can get the pstepTree so we could rename property node individuals
		super(new GridLayout(1, 1));
		this.wetProtocolMainPanel = wetProtocolMainPanel;
		this.protocolTreeNode = protocolTreeNode;
		if (protocolTreeNode == null) {
			UiUtils.showDialog(this, "In ClassPropertyEditorPanel constructor. Passed node is null");
			return;// todo should close this window?
		}
		Object userObject = protocolTreeNode.getUserObject();
		if (userObject == null || !(userObject instanceof Individual)) {
			UiUtils.showDialog(this, "In ClassPropertyEditorPanel constructor. Passed node is null or not an instance of Individual");
		}
		individual = (Individual) userObject;
		TOP_NODE_AS_PROPERTY_HOLDER.setUserObject("Properties for Individual:" + individual.getLocalName());
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
		jPropertyAndIndividualTree.setCellRenderer(new CellRenderer());
		jPropertyAndIndividualTree.setCellEditor(new CellEditor(jPropertyAndIndividualTree,wetProtocolMainPanel));
		jPropertyAndIndividualTree.setInvokesStopCellEditing(true);//keep the changes when focus is lost
		jPropertyAndIndividualTree.setEditable(true);
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
	 * Required by TreeSelectionListener interface.
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jPropertyAndIndividualTree.getLastSelectedPathComponent();
		if (node == null)
			return;
		//Object nodeInfo = node.getUserObject();
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
		createNodesForClass(individual.getOntClass(), TOP_NODE_AS_PROPERTY_HOLDER, individual);
	}

	public static void createNodesForClass(OntClass ontClass, DefaultMutableTreeNode currentTopNode, Individual stepIndividual) {
		Set<OntProperty> props = OntManager.getInstance().calculateHierarchicalPropertiesForAClass(ontClass);
		for (OntProperty ontProperty : props) {
			System.out.println("creating first level prop node for property:" + ontProperty.getLocalName());
			createNode(ontProperty, currentTopNode, stepIndividual);
		}
	}

	// recursive
	private static DefaultMutableTreeNode createNode(OntProperty ontProperty, DefaultMutableTreeNode currentTopNode, Individual individualThatHasTheProperty) {
		if (ontProperty.isDatatypeProperty()) {
			System.out.println("\tcreating literal prop node for " + ontProperty.getLocalName());
			currentTopNode.add(new DefaultMutableTreeNode(new PropertyAndIndividual(ontProperty, individualThatHasTheProperty, NodeType.LITERAL_NODE)));
			return currentTopNode;// --------------------->
		} else {// object type property
			System.out.println("\tcreating object type prop node for property: " + ontProperty.getLocalName());
			if (OntManager.isPreexisting(ontProperty)) { // PREEXISTING INDIVIDUAL DROPBOX takes precedence
				DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(new PropertyAndIndividual(ontProperty, individualThatHasTheProperty, NodeType.DATA_TYPE_NODE_FOR_CHOICE_PREEXISTING_OBJECT));// Pop up
				currentTopNode.add(newChild);// would be Read Only as it's an object property
				return currentTopNode;// --------------------->
				// return createNode(ontProperty, newChild);
			} // else it must be an object property
			OntResource range = ontProperty.getRange();
			System.out.println("\trange:" + range);
			if (OntManager.isLeafClass(range.asClass())) {// subClass(OntologyManager.NOTHING_SUBCLASS)) { // no subclasses like volume for instance //todo use subclass for speed
				Object subIndividualObject = individualThatHasTheProperty.getPropertyValue(ontProperty);
				Individual subIndividual;
				if (subIndividualObject == null) {
					subIndividual = OntManager.createLeafIndividual(ontProperty.getRange().asClass(), "newLeafIndividual");
					// System.out.println("We Created a new LEAF CLASS individual:"+subIndividual.getLocalName()+
					// " and we insert it in:"+individualThatHasTheProperty.getLocalName() );
					individualThatHasTheProperty.setPropertyValue(ontProperty, subIndividual);
				} else {
					subIndividual = ((OntResource) subIndividualObject).asIndividual();
				}
				DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(new PropertyAndIndividual(ontProperty, individualThatHasTheProperty, NodeType.DATA_TYPE_NODE_FOR_LEAF_CLASS));// Pop up
				currentTopNode.add(newChild);// would be Read Only as it's an object property
				Set<OntProperty> rangeProps = OntManager.getInstance().calculateHierarchicalPropertiesForAClass(range.asClass());
				for (OntProperty rageProperty : rangeProps) {
					System.out.println("creating range property for " + ontProperty.getLocalName());
					createNode(rageProperty, newChild, subIndividual);// ---------------------> recursion
				}
				return newChild;// --------------------->
			} else {// SELECT A CLASS DROPOX
				System.out.println("nasty case where the property value is an instance and could be ANY number of classes coming from range subclasses");
				DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(new PropertyAndIndividual(ontProperty, individualThatHasTheProperty, NodeType.DATA_TYPE_NODE_FOR_CHOICE_SUBCLASS));// , ontProperty.getRange().asClass().listSubClasses(false).toSet()));// Pop up
				currentTopNode.add(newChild);// would be Read Only as it's an object property
				RDFNode propertyValueAsIndividual = (individualThatHasTheProperty.getPropertyValue(ontProperty));
				if (propertyValueAsIndividual != null) {// already selected at one point
					// same as for DATA_TYPE_NODE_FOR_LEAF_CLASS
					Individual subIndividual = propertyValueAsIndividual.as(Individual.class);
					;
					Set<OntProperty> rangeProps = OntManager.getInstance().calculateHierarchicalPropertiesForAClass(subIndividual.getOntClass());
					for (OntProperty rageProperty : rangeProps) {
						System.out.println("creating range property for " + ontProperty.getLocalName());
						createNode(rageProperty, newChild, subIndividual);// ---------------------> recursion
					}
				}
				return currentTopNode;// --------------------->
			}
		}
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(Color.RED);
	}

	public enum NodeType {
		LITERAL_NODE, DATA_TYPE_NODE_FOR_CHOICE_PREEXISTING_OBJECT, DATA_TYPE_NODE_FOR_LEAF_CLASS, DATA_TYPE_NODE_FOR_CHOICE_SUBCLASS, TOP_PROPERTY_AND_INDIVIDUAL
	}

	/**
	 * Todo need to update the tree node with the value of the properties and maybe change it's icon and refresh to show the changes
	 */
	private void acceptPropertiesResponse(Component splitPane) {
		jPropertyAndIndividualTree.stopEditing();
	}
}
