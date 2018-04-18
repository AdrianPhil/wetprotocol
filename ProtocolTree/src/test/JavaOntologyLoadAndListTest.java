package test;

import java.util.Collection;

//see also vocabulary
public class JavaOntologyLoadAndListTest {
	public static void main(String[] args) {
		try {
//			OntologyManager ontologyManager = OntologyManager.getInstance();
//			Individual pipette = ontologyManager.createIndividual("myFirstPipette", "Pipette");
//			Set<OWLObjectProperty> objectPropertiesInSignature = pipette.getObjectPropertiesInSignature();
//			pipette.setPropertyValue("Manufacturer","Siemens Scient");
//			// Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataPropertyValues =
			// pipette.getOwlIndividual().getDataPropertyValues(myOntology);
			// printSet(dataPropertyValues);
			// System.out.println();
			// printSet( pipette.getOwlOntology().getObjectPropertiesInSignature(true));
			// Object[] arr = myOntology.getObjectPropertiesInSignature().toArray();
			// OWLObjectProperty owlObjectProperty = (OWLObjectProperty)arr[0];
			// System.out.println(owlObjectProperty);
			// Set<OWLClass> classesInSignature = owlObjectProperty.getClassesInSignature();
			// printSet(classesInSignature);
			// System.out.println(pipette.getOwlIndividual().getObjectPropertiesInSignature());
			// System.out.println(pipette.getOwlIndividual().getObjectPropertyValues(ontologyManager.getMyOntology()));
			// fullRun();
			// System.out.println("class names");
			// ontologyManager.printStringClassNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printSet(Collection set) {
		set.forEach(System.out::println);

	}
}
// public static void main1(String[] args) {
// try {
// OWLOntology myOntology = getMyOnthology();
// Set<OWLClass> classesInSignature = myOntology.getClassesInSignature();
// showSet(classesInSignature, myOntology);
// OntologyFactory myfactory = new OntologyFactory(myOntology);
// DefaultCajun cajuninstance = (DefaultCajun)
// myfactory.createCajun("myCajuninstance");
// cajuninstance.addHasBase( myfactory.createDeepPanBase("myDeepPanBase"));
// cajuninstance.addHasBase( myfactory.createThinAndCrispyBase("myCrispyBase"));
// cajuninstance.hasHasBase();
// cajuninstance.getHasBase();
//
// System.out.println("cajuninstance:" + cajuninstance);
// System.out.println("cajuninstance.hasHasBase():"+cajuninstance.hasHasBase());
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// private static void fullRun() {
//
// ProtocolFactory protocolFactory =
// OntologyManager.getInstance().getProtocolFactory();
// // set up the bench
// // pipette
// Pipette myFirstPipette = protocolFactory.createPipette("myFirstpipette");
// myFirstPipette.addManufacturer("Fisher Scientific");
// Volume maximumPipetteVolume = protocolFactory.createVolume("minimumVolume");
// maximumPipetteVolume.addNumericValue(200);// standard eppedorf tube:1.5 ml
// maximumPipetteVolume.addUnit("liter");
// maximumPipetteVolume.addMultiplication(-6);// microLiter
// myFirstPipette.addType("P20-200 ul");
// myFirstPipette.addMaximumVolume(maximumPipetteVolume);
// Volume minimumPipetteVolume = protocolFactory.createVolume("maximumVolume");
// minimumPipetteVolume.addNumericValue(20);
// minimumPipetteVolume.addUnit("liter");
// minimumPipetteVolume.addMultiplication(-6);// microLiter
// myFirstPipette.addMinimumVolume(minimumPipetteVolume);
// // end pipette
// // put pipette on the bench
// Place myBench = protocolFactory.createBench("myCupboard");
// Put putMyPipette = protocolFactory.createPut("putMyPipette");
// putMyPipette.addMovableObject(myFirstPipette);
// putMyPipette.addDestination(myBench);
// //
// Volume standardCentrifugeTubeVolume =
// protocolFactory.createVolume("StandardCentrifugeTubeVolume");// standard
// eppendorf tube:1.5 ml
// standardCentrifugeTubeVolume.addUnit("litre");// todo should be constant enum
// type
// standardCentrifugeTubeVolume.addMultiplication(-6);
// standardCentrifugeTubeVolume.addNumericValue(1500);
// // do this 100 times
// CentrifugeTube centrifugeTube1 =
// protocolFactory.createCentrifugeTube("CentrifugeTube1");
// centrifugeTube1.addVolume(standardCentrifugeTubeVolume);
// //
// Place myBenchRack = protocolFactory.createBench("myBenchRack");
// Put putMyCentrifugeTube = protocolFactory.createPut("putMyCentrifugeTube");
// putMyCentrifugeTube.addMovableObject(centrifugeTube1);
// putMyCentrifugeTube.addDestination(myBenchRack);// TODO trouble because it
// should go in the first well
// // end setting up the bench
// // objects are immutable? so we could reuse them
// Liquid plasmidLiquid = protocolFactory.createLiquid("plasmidLiquid"); // here
// we need to fully describe the liquid like what and concentration
// CentrifugeTube centrifugeTubeForPlasmids =
// protocolFactory.createCentrifugeTube("CentrifugeTube1");
// centrifugeTubeForPlasmids.addVolume(standardCentrifugeTubeVolume);
// centrifugeTubeForPlasmids.addContent(plasmidLiquid);// that's the other way
// to do the put. Born in here as opposed to put from
// // outside
// // above was provisioned
// // do this 100 times
// CentrifugeTube centrifugeTube2 =
// protocolFactory.createCentrifugeTube("CentrifugeTube2");
// centrifugeTube2.addVolume(standardCentrifugeTubeVolume);
// //
// PipetteAspiration firstPipetteAspire =
// protocolFactory.createPipetteAspiration("firstPipetteAspire");
// firstPipetteAspire.addSource(centrifugeTubeForPlasmids);// todo in here we
// need to update the volume in the source
// firstPipetteAspire.addDestination(myFirstPipette);
// Volume firstVolumeToBeTakenFromPlasmid =
// protocolFactory.createVolume("firstVolumeToBeTakenFromPlasmid");
// standardCentrifugeTubeVolume.addUnit("litre");// todo should be constant enum
// type
// standardCentrifugeTubeVolume.addMultiplication(-6);
// standardCentrifugeTubeVolume.addNumericValue(50);
// firstPipetteAspire.addVolume(firstVolumeToBeTakenFromPlasmid);// this results
// in depressing the pipette
// //
// PipetteRelease pipetteRelease =
// protocolFactory.createPipetteRelease("firstPipetteRelease");
// pipetteRelease.addSource(myFirstPipette);
// pipetteRelease.addDestination(centrifugeTube1);
// pipetteRelease.addVolume(firstVolumeToBeTakenFromPlasmid);
// // here todo new protocol and then operations
// // System.out.println("cajuninstance:" + cajuninstance);
// // System.out.println("cajuninstance.hasHasBase():" +
// // cajuninstance.hasHasBase());
// }
// }