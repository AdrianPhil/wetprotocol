package ont;

import resources.ResourceFindingDummyClass;
import ui.property.WrappedOntProperty;
import ui.property.ClassPropertyEditorPanel.NodeType;
import ui.property.WrappedIndividual;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.swing.JComboBox;

public class OntManager {
	private static OntManager instance;
	private static OntModel ontologyModel;
	public static final String PROTOCOL_FILE = "AdrianProtocol.owl";
	private static String ONTOLOGY_LOCATION = ResourceFindingDummyClass.getResource(PROTOCOL_FILE).getFile();
	public static final String NS = "http://www.wet.protocol#";// namespace and #
	public static OntProperty STANDALONE;
	private static Individual topProtocolInstance;
	public static Resource NOTHING_SUBCLASS;
	public static AtomicInteger counter = new AtomicInteger(0);
	//

	public static final OntManager getInstance() {
		if (instance == null) {
			instance = new OntManager();
			ontologyModel = ModelFactory.createOntologyModel();// full? hierarchy reasoner
			ontologyModel.read(ONTOLOGY_LOCATION);
			STANDALONE = ontologyModel.getOntProperty(NS + "standalone");
			NOTHING_SUBCLASS = ontologyModel.getOntClass("owl:Nothing");
		}
		return instance;
	}

	public Set<OntClass> getClassesInSignature() {
		// used to populate the add step pop-up
		return ontologyModel.listClasses().toSet();
	}

	public Individual getTopProtocoInstancel() {
		if (topProtocolInstance == null) {
			OntClass protocolClass = ontologyModel.getOntClass(NS + "Protocol");
			topProtocolInstance = ontologyModel.createIndividual(NS + "TadaMySampleProtocol", protocolClass);
			topProtocolInstance.setPropertyValue(ontologyModel.getOntProperty(NS + "version"), ontologyModel.createTypedLiteral("Version 0.0"));
		}
		return topProtocolInstance;
	}

	public static Individual createIndividual(OntClass ontClass) {
		return OntManager.getInstance().createIndividual("newInternalIndividual" + counter.incrementAndGet() + "_OfClass_" + ontClass.getLocalName(), ontClass);
	}

	private Individual createIndividual(String instanceName, OntClass ontClass) {// todo make this private
		return ontologyModel.createIndividual(NS + instanceName, ontClass);
	}

	public Individual createIndividual(String instanceName, String className) {
		OntClass ontClass = OntManager.getInstance().getOntClass(className);
		return ontologyModel.createIndividual(instanceName, ontClass);
	}

	public OntClass getOntClass(String clazz) {
		return OntManager.getInstance().getOntologyModel().getOntClass(NS + clazz);
	}

	public void dumpPropertiesAndValuesInIndividual(Individual individual) {
		Set<Statement> objectProperties = individual.listProperties().toSet();
		// show the properties of this individual
		System.out.println("dumpProperties and values for individual:" + individual);
		objectProperties.forEach(prop -> {
			System.out.print("    " + prop.getPredicate().getLocalName() + " -> ");
			if (prop.getObject().isLiteral()) {
				System.out.println("Literal " + prop.getLiteral().getLexicalForm());
			} else if (prop.getObject().isAnon()) {
				System.out.println("Anon " + prop.getObject());
			} else if (prop.getObject().isResource()) {
				System.out.println("Resource " + prop.getObject());
			} else if (prop.getObject().isURIResource()) {
				System.out.println("URIResource " + prop.getObject());
			} else {
				System.out.println("No Literal Anon Resource etc" + prop.getObject());
			}
		});
		// printSet(instance.getPropertiesInIndividual(tinyValueIndividual));
	}

	private void dumpPropertiesForAllClasses() {
		Set<OntClass> classesSet = ontologyModel.listClasses().toSet();
		final int indent = 1;
		classesSet.forEach(ontClass -> {
			dumpAllPropertiesForAClass(ontClass);
		});
	}

	public void dumpAllPropertiesForAClass(OntClass ontClass) {
		final int indent = 1;
		Set<OntProperty> declaredOntProperties = ontClass.listDeclaredProperties().toSet();
		ontClass.listSuperClasses(true).toSet().forEach(superClass -> {
			dumpAllDirectPropertiesForAClass(superClass, declaredOntProperties);
		});
		declaredOntProperties.forEach(ontProperty -> {
			System.out.println(String.join("", Collections.nCopies(indent, "\t")) + (ontProperty.isObjectProperty() ? "object property:" : "data property:") + ontProperty.getLocalName() + "<" + ontProperty.getRange() + "> of Type:" + ontProperty.getRDFType());
		});
	}

	public void dumpAllDirectPropertiesForAClass(OntClass ontClass, Set<OntProperty> declaredOntProperties) {
		final int indent = 1;
		System.out.println("class:" + ontClass.getLocalName());
		declaredOntProperties.addAll(ontClass.listDeclaredProperties().toSet());
		ontClass.listSuperClasses(true).toSet().forEach(superClass -> {
			dumpAllDirectPropertiesForAClass(superClass, declaredOntProperties);
		});
		System.out.println("class:" + ontClass.getLocalName());
	}

	public Set<OntProperty> calculateHierarchicalPropertiesForAClass(OntClass ontClass) {
		Set<OntProperty> collected = ontologyModel.listAllOntProperties().toSet().stream().filter(dataTypeProperty -> {
			return dataTypeProperty.hasDomain(ontClass);
		}).collect(Collectors.toSet());
		ontClass.listSuperClasses(true).toSet().forEach(superClass -> {
			dumpCalculatedPropertiesForAClass(superClass, collected);
		});
		collected.forEach(System.out::println);
		return collected;
	}

	public void dumpCalculatedPropertiesForAClass(OntClass ontClass, final Set<OntProperty> collected) {
		// System.out.println("calculated for class:" + ontClass.getLocalName());
		collected.addAll(ontologyModel.listAllOntProperties().toSet().stream().filter(dataTypeProperty -> {
			return dataTypeProperty.hasDomain(ontClass);
		}).collect(Collectors.toSet()));
		ontClass.listSuperClasses(true).toSet().forEach(superClass -> {
			dumpCalculatedPropertiesForAClass(superClass, collected);
		});
	}

	public Literal createValueAsStringLiteral(String newValue) {
		return ontologyModel.createTypedLiteral(newValue);
	}

	public static void printSet(Set<?> set, int indent) {
		set.forEach(elem -> System.out.println(String.join("", Collections.nCopies(indent, "\t")) + elem));
	}

	public static void printSet(String message, Set set) {
		System.out.println("Printing " + message);
		printSet(set, 0);
	}

	public static void printSet(String message, Set set, int indent) {
		System.out.println(message);
		printSet(set, indent);
	}

	public Individual getProtejeCreatedMicrocetrifugeTube() {
		return ontologyModel.getIndividual(NS + "myProtejeCreatedMicroCentrifugeTube");
	}

	public static boolean isStandalone(OntProperty ontProperty) {
		return ontProperty.hasSuperProperty(OntManager.STANDALONE, true);
	}

	public static OntModel getOntologyModel() {
		getInstance();// make sure it's loaded
		return ontologyModel;
	}

	public static boolean isLeafClass(OntClass ontClass) {
		// )!subclassFromRange.hasSubClass() todo should be a faster way but sometimes you get these ghost subclasses
		return ontClass.listSubClasses().toList().isEmpty();
	}

	// will load in the combo box all existing individuals of the given class
	public static void loadPossibleIndividualValues(JComboBox individualOrClassChooser, OntClass ontClass) {
		Set<Individual> individualsSet = getOntologyModel().listIndividuals(ontClass).toSet();
		for (Individual individual : individualsSet) {
			individualOrClassChooser.addItem(new WrappedIndividual(individual));
		}
	}
}
