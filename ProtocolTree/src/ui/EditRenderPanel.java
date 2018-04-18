package ui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.ResourceFindingDummyClass;

public class EditRenderPanel extends JPanel {
	static final ImageIcon createImageIcon = ResourceFindingDummyClass.createImageIcon("labcoat.png");
	JLabel imageLabel = new JLabel(" ");
	JLabel localNameLabel = new JLabel("L");
	JLabel propertyAndIndividualValue = new JLabel("value:?");
	JLabel propertyAndIndividualRangeLabel = new JLabel(" ");
	JLabel propertyAndIndividualDomainLabel = new JLabel(" ");

	public EditRenderPanel() {
		imageLabel.setIcon(createImageIcon);
		add(imageLabel);
		add(localNameLabel);
		add(propertyAndIndividualValue);
		add(propertyAndIndividualRangeLabel);
		add(propertyAndIndividualDomainLabel);
		setBorder(null);
	}

	public String getLocalName() {
		return localNameLabel.getText();
	}

	public void setLocalName(String localNameText) {
		localNameLabel.setText(localNameText);
	}

	public String getValue() {
		return propertyAndIndividualValue.getText();
	}

	public void setValue(String valueText) {
		propertyAndIndividualValue.setText(valueText);
	}

	public String getRange() {
		return propertyAndIndividualRangeLabel.getText();
	}

	public void setRange(String rangeText) {
		propertyAndIndividualRangeLabel.setText(rangeText);
	}

	public String getDomain() {
		return propertyAndIndividualDomainLabel.getText();
	}

	public void setDomain(String domainText) {
		propertyAndIndividualDomainLabel.setText(domainText);
	}

	public void setIcon(Icon icon) {
		imageLabel.setIcon(icon);
	}
}
