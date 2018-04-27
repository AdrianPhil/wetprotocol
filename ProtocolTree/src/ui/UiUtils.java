package ui;

import ont.OntManager;
import ui.property.AbstractTreeCellPanel;
import ui.property.ClassPropertyEditorPanel;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static ui.WetProtocolMainPanel.WITH_OF_PROTOCOL_TREE;

import java.awt.*;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class UiUtils {
	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
	 */
	public static void createAndShowNewFrameGUI(JPanel whatPanel, String title) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		whatPanel.setOpaque(true); // content panes must be opaque
		frame.setContentPane(whatPanel);
		// Display the window.
		frame.pack();
		frame.setLocationRelativeTo(null); // *** this will center the app ***
		frame.setVisible(true);
	}

	public static void createAndShowStepChooserGUI(DefaultMutableTreeNode protocolTreeModel, JTree jProtocolTree) {// todo maybe in here we'll do a dialog
		JFrame frame = new JFrame("Step Chooser");
		// frame.setDefaultCloseOperation(EXIT_ON_CLOSE);//todo
		// Create and set up the content pane.
		ClassChooserPanel newContentPane = new ClassChooserPanel(protocolTreeModel, jProtocolTree);
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);
		// Display the window.
		frame.pack();
		JFrame mainFrame = UiUtils.getProtocolFrame(jProtocolTree);
		frame.setLocation(mainFrame.getX() + WITH_OF_PROTOCOL_TREE + 10, 0);
		frame.setVisible(true);
	}

	public static void expandTree(JTree jTree) {
		for (int i = 0; i < jTree.getRowCount(); i++) {
			jTree.expandRow(i);
		}
	}

	public static void createEmpyClassNodes(JTree jTree, Set<OntClass> classesInSignature) {// for classChooserPanel
		DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		if (root.getChildCount() > 0) {
			return; // tree is already populated
		}
		classesInSignature.stream().forEach(ontClass -> {
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(ontClass);
			root.add(newNode);
		});
		model.reload(root);// TODO I dont't know if necessary
	}

	public static void createInstanceNodes(DefaultMutableTreeNode topProtocolNode) {
		DefaultMutableTreeNode firstStepNode;
		Individual myProtejeCreatedMicroCentrifugeTube = OntManager.getInstance().getProtejeCreatedMicrocetrifugeTube();
		firstStepNode = new DefaultMutableTreeNode(myProtejeCreatedMicroCentrifugeTube);
		topProtocolNode.add(firstStepNode);
		// JFC Swing Tutorial
		// book = new DefaultMutableTreeNode(
		// new Operation("The JFC Swing Tutorial: A Guide to Constructing GUIs",
		// "swingtutorial.html"));
		// topProtocolNode.add(book);
		// // Bloch
		// book = new DefaultMutableTreeNode(new Operation("Effective Java Programming
		// Language Guide", "bloch.html"));
		// topProtocolNode.add(book);
	}
	// public static void createPropertyNodes(DefaultMutableTreeNode topPropertyNode) {
	// DefaultMutableTreeNode firstStepNode;
	// Individual myProtejeCreatedMicroCentrifugeTube = OntologyManager.getInstance().getProtejeCreatedMicrocetrifugeTube();
	// firstStepNode = new DefaultMutableTreeNode(myProtejeCreatedMicroCentrifugeTube);
	// topPropertyNode.add(firstStepNode);
	// // JFC Swing Tutorial
	// // book = new DefaultMutableTreeNode(
	// // new Operation("The JFC Swing Tutorial: A Guide to Constructing GUIs",
	// // "swingtutorial.html"));
	// // topProtocolNode.add(book);
	// // // Bloch
	// // book = new DefaultMutableTreeNode(new Operation("Effective Java Programming
	// // Language Guide", "bloch.html"));
	// // topProtocolNode.add(book);
	// }

	// public static void createChildNode(DefaultMutableTreeNode newChild, DefaultMutableTreeNode parent, JTree builtTree, int childPosition) {
	// parent.insert(newChild, childPosition);// needs the updateUI to work
	// // ((DefaultTreeModel)builtTree.getModel()).insertNodeInto(newChild, parent,
	// // parent.getChildCount());// this works without the updateUI
	// builtTree.updateUI();
	// // Make sure the user can see the new node.
	// builtTree.scrollPathToVisible(new TreePath(newChild.getPath()));
	// }
	public static void showDialog(Component aComponent, String message) {
		if (aComponent != null) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(aComponent), message, message, JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, message, message, JOptionPane.WARNING_MESSAGE);
		}
	}

	public static JFrame getProtocolFrame(Component c) {
		do {
			c = c.getParent();
		} while (!(c instanceof JFrame));
		return (JFrame) c;
	}

	public static void showConditionDialog(boolean b, Component c, String message) {
		if (b) {
			showDialog(c, message);
		}
	}
}
