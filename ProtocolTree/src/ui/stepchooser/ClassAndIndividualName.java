package ui.stepchooser;


import org.apache.jena.ontology.OntClass;

public class ClassAndIndividualName {
	OntClass ontClass;
	String name;

	public ClassAndIndividualName(OntClass ontClass, String name) {
		this.ontClass = ontClass;
		this.name = name;
	}

	public OntClass getOntClass() {
		return ontClass;
	}

	public void setOntClass(OntClass ontClass) {
		this.ontClass = ontClass;
	}

	public String getString() {
		return name;
	}

	public void setName(String text) {
		name=text;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "ClassAndIndividualName [ontClass=" + ontClass + ", name=" + name + "]";
	}
}
