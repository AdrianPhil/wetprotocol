package junit;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Literal;
import org.junit.Test;

import ont.*;
import resources.ResourceFindingDummyClass;
import ui.property.ClassPropertyEditorPanel.NodeType;

class OntologyTest {
	private static PropertyAndIndividual TOP_PROPERTY_AND_INDIVIDUAL;
	OntManager ontologyManager = OntManager.getInstance();
	OntModel ontologyModel = ontologyManager.getOntologyModel();

	@Test
	void test() {
		fail("Not yet implemented");
	}

	public Literal getTypedLiteral(String text) {
		return null;
		// return ontologyModel.createTypedLiteral(text);
	}

	private void testCreateIndividualAndAssignLiteralAndClassPropertyValues() {
		Individual newlyCreatedIndividual = ontologyManager.createIndividual("myCodeCreatedMicroCentrifugeTube", "MicroCentrifugeTube");
		System.out.println(newlyCreatedIndividual);
		// https://jena.apache.org/documentation/notes/typed-literals.html
		// will create a typed literal with the lexical value "2", of type xsd:int.
		// Could use model.createTypedLiteral(value, datatype).
		// model.createLiteral(25); still works but is deprecated because it does string
		// conversions
		Literal literalPropertyValue = ontologyModel.createTypedLiteral("QIGEN");
		OntProperty stringValueProperty = ontologyModel.getOntProperty(OntManager.NS + "manufacturer");
		newlyCreatedIndividual.setPropertyValue(stringValueProperty, literalPropertyValue);
		ontologyManager.dumpPropertiesAndValuesInIndividual(newlyCreatedIndividual);
		System.out.println("-------------");
	}

	public PropertyAndIndividual getTopPropertyAndIndividual() {
		if (TOP_PROPERTY_AND_INDIVIDUAL == null) {
			Individual dummyIndividual = ontologyModel.getIndividual(OntManager.NS + "dummyIndividual");
			TOP_PROPERTY_AND_INDIVIDUAL = new PropertyAndIndividual(ontologyModel.getOntProperty(OntManager.NS + "dummyDataProperty"), dummyIndividual, NodeType.TOP_PROPERTY_AND_INDIVIDUAL);
			// the value will be null
			// no values for properties as this ode will be invisible
		}
		// System.out.println("topPropertyInstance:" + topPropertyInstance);
		return TOP_PROPERTY_AND_INDIVIDUAL;
	}

	public void printStringClassNames() {
		ontologyManager.getClassesInSignature().forEach(System.out::println);
		// for (OWLClass owlClass : classesInSignature) {
		// System.out.println("Class Name:" + owlClass.getIRI().getFragment());
		// //showSetSuperclasses(owlClass.getSuperClasses(myOntology));
		// }
	}


}
