package ui.stepchooser;

import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.jena.ontology.OntClass;

import ont.OntManager;
import ui.UiUtils;

public class StepChooserUtils {
	

	public static void createEmpyStepNodesToChooseFrom(JTree jTree) {
		OntClass stepOntClass = OntManager.getOntClass("Step");// TODO cache Step
		Set<OntClass> stepClasses;
		if (UiUtils.DEBUG) {
			stepClasses = OntManager.getInstance().getClassesInSignature();
		} else {
			stepClasses = stepOntClass.listSubClasses().toSet();
		}
		DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		if (root.getChildCount() > 0) {
			return; // tree is already populated
		}
		stepClasses.stream().forEach(ontClass -> {
			root.add(new DefaultMutableTreeNode(new ClassAndIndividualName(ontClass,ontClass.getLocalName())));
		});
		model.reload(root);// TODO I dont't know if necessary
	}
}
