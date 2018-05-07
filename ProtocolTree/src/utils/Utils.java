package utils;

import org.apache.jena.ontology.OntClass;

import ui.property.PropertyValueFormattedTextBox;

public class Utils {

	public static String createNiceIdividualName(OntClass ontClass, PropertyValueFormattedTextBox valueComponent) {
		if (valueComponent.isCustomText()) {
			return valueComponent.getText();
		} else {
			return Utils.firstLetterLowerCase(ontClass.getLocalName());
		}
	}
	
	public static  String firstLetterLowerCase(String string) {
		if(string==null) {
			return string;
		}
		if(string.length()==1) {
			return string.toLowerCase();
		}
		return string.substring(0, 1).toLowerCase()+string.substring(1, string.length());
	}
}
