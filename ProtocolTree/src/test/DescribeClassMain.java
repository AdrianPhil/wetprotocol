package test;

// Package

///////////////

// Imports
///////////////
import java.util.Iterator;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;

import resources.ResourceFinding;

/**
 * <p>
 * Execution wrapper for describe-class example
 * </p>
 */
public class DescribeClassMain {
	// Constants
	//////////////////////////////////

	// Static variables
	//////////////////////////////////

	// Instance variables
	//////////////////////////////////

	// Constructors
	//////////////////////////////////

	// External signature methods
	//////////////////////////////////

	public static void main(String[] args) {
		OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
		m.read(ResourceFinding.getResource("AdrianProtocol.owl").getFile());
		System.out.println("get all classes as objects");
		
		m.listClasses();
		
		DescribeClass dc = new DescribeClass();
		for (Iterator<OntClass> i = m.listClasses(); i.hasNext();) {
			// now list the classes
			OntClass cl = i.next();
			System.out.println("\nDeclared poperties for class:"+cl.getLocalName()+":");
			JavaOntologyLoadAndListTest.printSet(cl.listDeclaredProperties(true).toSet());
			System.out.println("Class Description:");
			dc.describeClass(System.out, cl);
			System.out.println();
		}
		
	}

}
