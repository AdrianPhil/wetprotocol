package ui.property;


@SuppressWarnings("serial")
public class RenderCellPanel extends AbstractTreeCellPanel {

	// called from Renderer
	public RenderCellPanel(PropertyAndIndividual propertyAndIndividual) {
		super(propertyAndIndividual);
		//should be OntManager.isPreexisting(ontProperty) ? "PREEXISTING" + ontProperty.getLocalName() : "XXX" + ontProperty.getLocalName(), "" + propertyAndIndividual.getIndividual().getPropertyValue(ontProperty), ontProperty.getRange().getLocalName().toString(), ontProperty.getDomain().getLocalName().toString()
		super.setTextAndAddComponents();
	}

}
