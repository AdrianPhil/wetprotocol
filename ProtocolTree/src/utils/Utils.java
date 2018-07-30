package utils;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.jena.ontology.OntClass;

import ui.property.PropertyValueFormattedTextBox;
import uiutil.UITextValueInterface;

public class Utils {

	public static String createNiceIdividualName(OntClass ontClass, UITextValueInterface valueComponent) {
		if (valueComponent.isCustomText()) {
			return valueComponent.getText();
		} else {
			return Utils.firstLetterLowerCase(ontClass.getLocalName());
		}
	}
	
	public static  String firstLetterLowerCase(String string) {
		if(string==null) {
			return string;
		}
		if(string.length()==1) {
			return string.toLowerCase();
		}
		return string.substring(0, 1).toLowerCase()+string.substring(1, string.length());
	}

	public static DefaultMutableTreeNode getRoot(JTree jTree) {
		return (DefaultMutableTreeNode)(getModel(jTree).getRoot());
	}
	public static DefaultTreeModel getModel(JTree jTree) {
		return (DefaultTreeModel) (jTree.getModel());
	}
}
