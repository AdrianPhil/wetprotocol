package ui.stepchooser;


import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import ui.OntResourceNameFormattedTextBox;
import utils.Utils;

@SuppressWarnings("serial")
public class EditCellPanel extends JPanel implements PropertyChangeListener {
	protected ClassAndIndividualName classAndIndividualName;
	final DefaultMutableTreeNode theNode;
	JLabel icon = new JLabel("");
	JLabel classNameComponent = new JLabel();
	JTextField editableValueComponent = new OntResourceNameFormattedTextBox("DummyValue");

	public EditCellPanel(ClassAndIndividualName classAndIndividualName, DefaultMutableTreeNode theNode) {
		this.classAndIndividualName = classAndIndividualName;
		this.theNode = theNode;
		classNameComponent.setText("<" + classAndIndividualName.getOntClass().getLocalName() + ">");
		editableValueComponent.setText(Utils.firstLetterLowerCase(classAndIndividualName.getOntClass().getLocalName()));
		editableValueComponent.addPropertyChangeListener("value", this);
		editableValueComponent.setEditable(true);
		add(editableValueComponent);
		add(classNameComponent);
		setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// System.out.println("EditCellPanel property change event:" + evt);
		// classAndIndividualName.setName(""+evt.getNewValue());
	}

	public ClassAndIndividualName getNewUserObjectScrapedFromEditPanel() {
		classAndIndividualName.setName(editableValueComponent.getText());
		return classAndIndividualName;
	}
}
