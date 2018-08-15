package ui.stepchooser;

import org.apache.jena.ontology.OntClass;

public class ClassAndIndividualName implements Cloneable {
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
		name = text;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "ClassAndIndividualName [ontClass=" + ontClass + ", name=" + name + "]";
	}

	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Cloning issue", e);
			// return null;
		}
	}
}
