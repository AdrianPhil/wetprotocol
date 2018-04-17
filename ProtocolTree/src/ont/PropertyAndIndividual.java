package ont;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.RDFNode;

public class PropertyAndIndividual {
	OntProperty ontProperty;
	Individual individual;

	public PropertyAndIndividual(OntProperty ontProperty, Individual individual) {
		this.ontProperty = ontProperty;
		this.individual = individual;
	}

	@Override
	public String toString() {
		return "PropertyAndIndividual [ontProperty=" + ontProperty + ", individual=" + individual + " and value:" + getValue() + "]";
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

}
