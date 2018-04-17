package oldStuff;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.semanticweb.owlapi.model.OWLClass;

import java.awt.*;

import static resources.ResourceFindingDummyClass.createImageIcon;

public class MyRenderer extends DefaultTreeCellRenderer {
    private static ImageIcon tutorialIcon = createImageIcon("images/middle.gif");//adrian
    //Set the icon for leaf nodes.
    // ImageIcon tutorialIcon = new DynamicIcon(new Dimension(20,10)) ;

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        //String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (leaf && isTutorialBook(value)) {
            setIcon(tutorialIcon);
            setToolTipText("This book is in the Tutorial series.");
        } else {
            setToolTipText(null); //no tool tip
        }
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    boolean isTutorialBook(Object value) {
        if(true) return false;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();
        if (!(userObject instanceof OWLClass)) {
            System.out.println("User object is:" + userObject);
        }
        OWLClass nodeInfo = (OWLClass) (node.getUserObject());
        String title = nodeInfo.toString();
        //noinspection IndexOfReplaceableByContains
        if (title.contains("Pipette")) {//todo
            return true;
        }
        return false;
    }
}