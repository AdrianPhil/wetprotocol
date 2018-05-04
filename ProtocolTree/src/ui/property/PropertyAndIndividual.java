package ui.property;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.RDFNode;

import ui.property.ClassPropertyEditorPanel.NodeType;

public class PropertyAndIndividual {
	OntProperty ontProperty;
	Individual individual;
	boolean fromRangeSuperclasses = false;
	NodeType nodeType;

	public PropertyAndIndividual(OntProperty ontProperty, Individual individual, NodeType nodeType) {
		this.ontProperty = ontProperty;
		this.individual = individual;
		this.nodeType = nodeType;
	}

	public PropertyAndIndividual(OntProperty ontProperty, Individual individual, boolean fromRangeSuperclasses) {
		this.ontProperty = ontProperty;
		this.individual = individual;
		this.fromRangeSuperclasses = fromRangeSuperclasses;
	}

	public OntProperty getOntProperty() {
		return ontProperty;
	}

	public void setOntProperty(OntProperty ontProperty) {
		this.ontProperty = ontProperty;
	}

	public RDFNode getValue() {
		return individual.getPropertyValue(ontProperty);
	}

	public void setValue(RDFNode value) {
		individual.setPropertyValue(ontProperty, value);
	}

	public Individual getIndividual() {
		return individual;
	}

	public NodeType getNodeType() {
		return nodeType;
	}
//
//	public void setIndividual(Individual createIndividual) {
//		this.individual = individual;
//	}
	// public DisplayType getDisplayType() {
	// if (fromRangeSuperclasses)
	// return DisplayType.FROM_RANGE_SUPERCLASS_PROPERTY_OBJECT;
	// if (ontProperty.isLiteral())
	// return DisplayType.LITERAL;
	// return DisplayType.IN_CLASS_PROPERTY_OBJECT;
	// }
	//
	// public enum DisplayType {
	// LITERAL, FROM_RANGE_SUPERCLASS_PROPERTY_OBJECT, IN_CLASS_PROPERTY_OBJECT
	// }
}
