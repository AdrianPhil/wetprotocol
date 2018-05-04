package ui;

import ont.OntManager;
import ui.WetProtocolMainPanel.WhereToAddStepNode;
import ui.property.AbstractTreeCellPanel;
import ui.property.ClassPropertyEditorPanel;
import ui.stepchooser.StepChooserPanel;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.RDFNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static ui.WetProtocolMainPanel.WITH_OF_PROTOCOL_TREE;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class UiUtils {
	public static final boolean DEBUG = false;

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

	public static void showStepChooserGuiAndCreateNewStepTree(WhereToAddStepNode whereToAddStepNode, JTree jProtocolTree) {// todo maybe in here we'll do a dialog
		JFrame stepChooserFrame = new JFrame("Step Chooser");
		// frame.setDefaultCloseOperation(EXIT_ON_CLOSE);//todo
		// Create and set up the content pane.
		StepChooserPanel newContentPane = new StepChooserPanel(jProtocolTree, whereToAddStepNode);
		newContentPane.setOpaque(true); // content panes must be opaque
		stepChooserFrame.setContentPane(newContentPane);
		// Display the window.
		stepChooserFrame.pack();
		JFrame mainFrame = UiUtils.getProtocolFrame(jProtocolTree);
		stepChooserFrame.setLocation(mainFrame.getX() + mainFrame.getWidth() /2, 0);
		stepChooserFrame.setVisible(true);
	}

	public static void expandTree(JTree jTree) {
		for (int i = 0; i < jTree.getRowCount(); i++) {
			jTree.expandRow(i);
		}
	}


	// * this practically does the whole UI if the model was changed*/
	public static void loadStepsTreeFromModel(DefaultMutableTreeNode topStepNode) {
		topStepNode.removeAllChildren();
		List<Individual> list = OntManager.getInstance().calculateStepIndividuals();
		list.sort((Individual i1, Individual i2) -> i1.getPropertyValue(OntManager.getStepCoordinatesProperty()).asLiteral().getString().compareTo(i2.getPropertyValue(OntManager.getStepCoordinatesProperty()).asLiteral().getString()));
		DefaultMutableTreeNode lastAddedNode = topStepNode;
		OntProperty STEP_COORDINATES_PROPERTY = OntManager.getStepCoordinatesProperty();
		for (int i = 0; i < list.size(); i++) {// skip the top
			Individual step = list.get(i);
			NodeCoordinates nodeLocation = new NodeCoordinates(step);
			step.removeAll(STEP_COORDINATES_PROPERTY);// clean up saved coordinates from the memory model once the tree is reassembled
			DefaultMutableTreeNode newStep = new DefaultMutableTreeNode(step);
			if (lastAddedNode.getLevel() == nodeLocation.depth) {// add sibling
				((DefaultMutableTreeNode) lastAddedNode.getParent()).add(newStep);
			} else if (lastAddedNode.getLevel() < nodeLocation.depth) {// add child
				lastAddedNode.add(newStep);
			} else {
				DefaultMutableTreeNode newParent = getEarlierParent(lastAddedNode, lastAddedNode.getLevel() - nodeLocation.depth);
				newParent.add(newStep);
			}
			System.out.println("Step:" + step.getLocalName() + " vertical:" + nodeLocation.vertical + " depth:" + nodeLocation.depth);
			lastAddedNode = newStep;
		}
		// need to clean the model
	}

	private static DefaultMutableTreeNode getEarlierParent(DefaultMutableTreeNode lastAddedNode, int i) {
		TreeNode earlierParent = lastAddedNode.getParent();
		while (i-- > 0) {
			earlierParent = earlierParent.getParent();
		}
		return (DefaultMutableTreeNode) earlierParent;
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

	public static class NodeCoordinates {
		int vertical;
		int depth;

		public NodeCoordinates(Individual i) {
			String coordinates = i.getPropertyValue(OntManager.getStepCoordinatesProperty()).asLiteral().getString();
			String[] split = coordinates.split("\\.");
			vertical = new Integer(split[0]);
			depth = new Integer(split[1]);
		}

		@Override
		public String toString() {
			return "NodeCoordinates [vertical=" + vertical + ", depth=" + depth + "]";
		}
	}
}
