package ui.property;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.enhanced.EnhGraph;
import org.apache.jena.graph.Node;
import org.apache.jena.ontology.AllDifferent;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.ComplementClass;
import org.apache.jena.ontology.DataRange;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.EnumeratedClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.IntersectionClass;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.ontology.Profile;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.ontology.UnionClass;
import org.apache.jena.ontology.impl.OntClassImpl;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFVisitor;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;

import ui.UiUtils;

public class WrappedOntResource<T extends OntResource>  {

	private T wrappedOntResource;

	public WrappedOntResource(T wrappedOntResource) {
		this.wrappedOntResource=wrappedOntResource;
	}

	@Override
	public String toString() {
		if(wrappedOntResource instanceof Individual) {			
			return ((Individual)wrappedOntResource).getLocalName();
		}else 	if(wrappedOntResource instanceof OntClass) {			
			return ((OntClass)wrappedOntResource).getLocalName();
		}
		UiUtils.showDialog(null, "WrappedResources can be only Individuals or OntClass-es and this one is:"+wrappedOntResource);
		return "problem";
	}

	public T getWrappedResource() {
		return wrappedOntResource;
	}
	
}
