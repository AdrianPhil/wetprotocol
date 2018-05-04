package ont;

import resources.ResourceFinding;
import ui.property.ClassPropertyEditorPanel.NodeType;
import ui.UiUtils;
import ui.UiUtils.NodeCoordinates;
import ui.WetProtocolMainPanel;
import ui.property.WrappedOntResource;
import ui.stepchooser.ClassAndIndividualName;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.reasoner.InfGraph;
import org.apache.jena.sparql.function.library.print;
import org.apache.jena.util.ResourceUtils;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.UniqueFilter;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.ReasonerVocabulary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class OntManager {
	private static OntManager instance;
	private static OntModel ontologyModel;
	// public static final String PROTOCOL_FILE = "WetProtocolWithTopProtocolInstanceFromProteje.owl";
	// it needs to be saved and reloaded in Jena to show the proper class
	public static final String PROTOCOL_FILE = "WetProtocolWithBasicProvisions.owl";
	public static String ONTOLOGY_LOCATION = ResourceFinding.getResource(PROTOCOL_FILE).getFile();// this will be in the bin directory because that is where the class is
	public static final String NS = "http://www.wet.protocol#";// namespace and #
	public static OntProperty STANDALONE;
	private static Individual topProtocolInstance;
	private static OntProperty stepCoordinatesProperty;
	// public static Resource NOTHING_SUBCLASS;
	public static AtomicInteger counter = new AtomicInteger(0);
	//

	public static final OntManager getInstance() {
		if (instance == null) {
			instance = resetModelInstance(ONTOLOGY_LOCATION);
		}
		return instance;
	}

	public static final OntManager resetModelInstance(String pathOfOntFileToLoad) {
		instance = null;
		instance = new OntManager();
		ontologyModel = null;
		ontologyModel = ModelFactory.createOntologyModel();// OntModelSpec.OWL_MEM);// OntModelSpec.OWL_LITE_MEM);// "" isfull? hierarchy reasoner; OWL_MEM
		ontologyModel.read(pathOfOntFileToLoad);
		ontologyModel.setStrictMode(true);
		System.out.println("after loding the ontology the individuals are:" + OntManager.getOntologyModel().listIndividuals().toList());
		STANDALONE = ontologyModel.getOntProperty(NS + "standalone");
		// NOTHING_SUBCLASS = ontologyModel.getOntClass("owl:Nothing");
		topProtocolInstance = ontologyModel.getIndividual(NS + "topProtocolInstance");
		//
		// topProtocolInstance.setOntClass(getOntClass("Protocol"));// need to correct the NamedIndividual nonsense that appears when saving in Jena
		// Resource namedIndividual = ontologyModel.getResource("http://www.w3.org/2002/07/owl#NamedIndividual");
		// System.out.println("in resetModelInstance. Found NamedIndividual:"+namedIndividual);
		// if (namedIndividual != null && namedIndividual instanceof OntResource) {
		// ((OntResource) namedIndividual).remove();
		// System.out.println("in resetModelInstance. removed NamedIndividual:");
		// }
		//
		// topProtocolInstance.setPropertyValue(ontologyModel.getOntProperty(NS + "version"), ontologyModel.createTypedLiteral("Version 0.0")); //TODO reinstate
		stepCoordinatesProperty = (ontologyModel.getOntProperty(NS + "stepCoordinatesProperty"));
		return instance;
	}

	public Set<OntProperty> calculateHierarchicalPropertiesForAClass(OntClass ontClass) {
		Set<OntProperty> collected = ontologyModel.listAllOntProperties().toSet().stream().filter(dataTypeProperty -> {
			return dataTypeProperty.hasDomain(ontClass);
		}).collect(Collectors.toSet());
		ontClass.listSuperClasses(true).toSet().forEach(superClass -> {
			calculatePropertiesForClass(superClass, collected);
		});
		collected.forEach(System.out::println);
		return collected;
	}

	public List<Individual> calculateStepIndividuals() {
		// the top protocol is already added so we can filter out
		OntClass stepOntClass = OntManager.getInstance().getOntClass("Step");// TODO cache
		return ontologyModel.listIndividuals(stepOntClass).toList();
		// List<Individual> allIndividuals = ontologyModel.listIndividuals().toList();
		// for (Individual ind : allIndividuals) {
		// System.out.println("Individual :" + ind);
		// Resource rdfType = ind.getRDFType(false);
		// System.out.println("hasOntClass:"+ ind.hasOntClass(stepOntClass));
		// System.out.println("\trdfType:"+rdfType +" and OntClass:"+ind.listOntClasses(true).toList()+" primary class:"+ind.getOntClass(false));
		// System.out.println("\trdfType:"+rdfType +" primary class:"+ind.getOntClass(false));
		// }
	}

	public Set<OntClass> getClassesInSignature() {
		// used to populate the add step pop-up
		return ontologyModel.listClasses().toSet();
	}

	public static Individual getTopProtocolInstance() {
		return topProtocolInstance;
	}

	public static Individual createStepIndividual(ClassAndIndividualName classAndIndividualName) {
		Individual createdIndividual = OntManager.getInstance().createIndividual(classAndIndividualName.getName() + counter.incrementAndGet(), classAndIndividualName.getOntClass());
		if (createdIndividual == null) {
			System.out.println("!!!!!!!!!!!createdIndividual == null");
		}
		createdIndividual.addLabel("Label:" + createdIndividual.getLocalName(), null);
		return createdIndividual;
	}

	public static Individual createIndividual(OntClass ontClass, String prefix) {
		Individual createdIndividual = OntManager.getInstance().createIndividual(prefix + counter.incrementAndGet() + "_ofClass_" + ontClass.getLocalName(), ontClass);
		if (createdIndividual == null) {
			System.out.println();
		}
		createdIndividual.addLabel("Label:" + createdIndividual.getLocalName(), null);
		return createdIndividual;
	}

	/** base one */
	private Individual createIndividual(String instanceName, OntClass ontClass) {
		Individual createdIndividual = ontologyModel.createIndividual(NS + instanceName, ontClass);
		return createdIndividual;
	}

	//
	// private Individual createIndividual(String instanceName, String className) {
	// OntClass ontClass = OntManager.getInstance().getOntClass(className);
	// return ontologyModel.createIndividual(instanceName, ontClass);
	// }
	public static OntClass getOntClass(String clazz) {
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

	public void calculatePropertiesForClass(OntClass ontClass, final Set<OntProperty> collected) {
		// System.out.println("calculated for class:" + ontClass.getLocalName());
		collected.addAll(ontologyModel.listAllOntProperties().toSet().stream().filter(dataTypeProperty -> {
			return dataTypeProperty.hasDomain(ontClass);
		}).collect(Collectors.toSet()));
		ontClass.listSuperClasses(true).toSet().forEach(superClass -> {
			calculatePropertiesForClass(superClass, collected);
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
	// TODO I think this one should be done like the listStepIndividuals
	public static void loadPossibleIndividualValues(JComboBox individualOrClassChooser, OntClass ontClass) {
		Set<Individual> individualsSet = getOntologyModel().listIndividuals(ontClass).toSet();
		for (Individual individual : individualsSet) {
			individualOrClassChooser.addItem(new WrappedOntResource(individual));
		}
	}

	public static Individual renameNode(Individual resource, String newValue, DefaultMutableTreeNode topStepNode, JTree jStepsTree) {
		Path tempFile;
		try {
			tempFile = Files.createTempFile("wettempfile", ".tmp");
		} catch (IOException e) {
			UiUtils.showDialog(null, "some issues creating temp ontology file" + e.getLocalizedMessage());
			e.printStackTrace();
			return resource;// did no rename
		}
		File file = tempFile.toFile();
		System.out.println("temporary file created at:"+file.getAbsolutePath());
		file.deleteOnExit();
		Path path = null;
		saveOntologyAndCoordinates(file, jStepsTree);
		try (FileOutputStream output = new FileOutputStream(file)) {
			OntManager.getOntologyModel().writeAll(output, "RDF/XML");// TODO maybe without NS
			path = Paths.get(file.getAbsolutePath());
			Charset charset = StandardCharsets.UTF_8;
			String content = new String(Files.readAllBytes(path), charset);
			content = content.replaceAll(resource.getLocalName(), NS + newValue);
			Files.write(path, content.getBytes(charset));
		} catch (Exception e1) {
			UiUtils.showDialog(null, "some issues writing temp ontology file" + e1.getLocalizedMessage());
		}
		OntManager.resetModelInstance(path.toString());// these 2 lines reset the whole model and UI
		// UiUtils.loadStepsTreeFromModel(topStepNode);
		return OntManager.getOntologyModel().getIndividual(NS + newValue);
	}

	public static OntProperty getStepCoordinatesProperty() {
		return stepCoordinatesProperty;
	}

	public static void saveOntologyAndCoordinates(File file, JTree jStepsTree) {
		Enumeration<?> preorderEnumeration = ((DefaultMutableTreeNode) jStepsTree.getModel().getRoot()).preorderEnumeration();
		int verticalDistance = 0;
		while (preorderEnumeration.hasMoreElements()) {
			DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) (preorderEnumeration.nextElement());
			Individual individual = (Individual) (defaultMutableTreeNode.getUserObject());
			individual.addLiteral(OntManager.getStepCoordinatesProperty(), verticalDistance++ + "." + defaultMutableTreeNode.getLevel());
		}
		try (FileOutputStream output = new FileOutputStream(file)) {
			OntManager.getOntologyModel().write(output, "RDF/XML", null);// OntManager.NS);
		} catch (Exception e1) {
			UiUtils.showDialog(jStepsTree, "Cannot open output file" + e1.getLocalizedMessage());
		}
	}
}
// TODO for some reason it seams that only the top protocol is saved with rdf type as </owl:NamedIndividual> but all the other newly created nodes are saved withe the right class and without rdf t