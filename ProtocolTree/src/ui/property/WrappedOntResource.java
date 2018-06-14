package ui.property;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;

import uiutil.UiUtils;

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
