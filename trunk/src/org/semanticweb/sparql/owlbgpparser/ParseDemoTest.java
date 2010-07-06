package org.semanticweb.sparql.owlbgpparser;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class ParseDemoTest {
	
		public static void main(String[] args) throws Exception { 
			
			OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
	        
	       // File inputOntologyFile = new File("C:/Users/user/Documents/sparqltest.ttl");
			
			File inputOntologyFile = new File("C:/Users/user/Documents/testagain.ttl");
	        
			OWLOntology ontology=manager.loadOntologyFromOntologyDocument(inputOntologyFile);
	        
	        OWLOntologyFormat format = manager.getOntologyFormat(ontology);
	                     System.out.println("    format: " + format);
	                     File file = new File("C:/Users/user/Documents/testInFSS.ttl");
	                     
	                     OWLFunctionalSyntaxOntologyFormat fssFormat = new OWLFunctionalSyntaxOntologyFormat();
	                     
	                     if(format.isPrefixOWLOntologyFormat()) {
	                         fssFormat.copyPrefixesFrom(format.asPrefixOWLOntologyFormat());
	                     }
	                     manager.saveOntology(ontology, fssFormat, IRI.create(file.toURI()));
	         
			
			
			//	OWLRDFConsumerAdapter handler1=new OWLRDFConsumerAdapter();
			//ConsoleTripleHandler handler1=new ConsoleTripleHandler();
			//try {TurtleParser parser = new TurtleParser(new StringReader("?y <http://www.w3.org/2001/vcard-rdf/3.0#Family> ?givenName."),handler1,null); 
		//	try {TurtleParser parser = new TurtleParser(new FileReader("C:/Users/user/Documents/sparqltest.txt"),handler1,null);
		//parser.parseDocument () ;
//		} 

		//catch (Exception ex) 
	//	{ex.printStackTrace() ;} 
	  }
}