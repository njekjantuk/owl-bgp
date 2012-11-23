package org.semanticweb.sparql.owlbgp.evaluation;


import junit.framework.TestCase;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.OWLBGPHermiT;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLOntologyDataSet;

public class TestGalenEntailment extends TestCase{
public static final String LB = System.getProperty("line.separator") ;
    
    protected final OWLOntologyManager manager;
    protected final OWLDataFactory factory;
    protected OWLOntology queriedOntology;
    protected OWLOntologyDataSet dataset;
    
    public TestGalenEntailment() {
        this(null);
    }
    
    public TestGalenEntailment(String name) {
        super(name);
        manager = OWLManager.createOWLOntologyManager();
        factory=manager.getOWLDataFactory();
    }
	
	protected void setUp() throws Exception {
        org.semanticweb.owlapi.model.IRI physicalIRI=org.semanticweb.owlapi.model.IRI.create(getClass().getResource("ontologies/Galen.owl").toURI());
        queriedOntology=manager.loadOntologyFromOntologyDocument(physicalIRI);
	}    
	
	public void testIsEntailed() {
	
        Configuration c=new Configuration();
        c.ignoreUnsupportedDatatypes=true;
        OWLReasoner hermit=new OWLBGPHermiT(c, dataset.getDefaultGraph().getOntology());
        OWLDataFactory factory=manager.getOWLDataFactory();
        OWLClass subClass1;
        OWLClass qualificationClass1;
        OWLObjectProperty qualificationProperty1;
        OWLClassExpression superClass1;
        OWLSubClassOfAxiom subClassOf1;
        
        OWLClass subClass2;
        OWLClass qualificationClass2;
        OWLObjectProperty qualificationProperty2;
        OWLClassExpression superClass2;
        OWLSubClassOfAxiom subClassOf2;
                
        subClass1=factory.getOWLClass(IRI.create("http://www.co-ode.org/ontologies/galen#Infection"));
        qualificationClass1=factory.getOWLClass(IRI.create("http://www.co-ode.org/ontologies/galen#DomainCategory"));
        qualificationProperty1=factory.getOWLObjectProperty(IRI.create("http://www.w3.org/2002/07/owl#topObjectProperty"));
        superClass1=factory.getOWLObjectSomeValuesFrom(qualificationProperty1, qualificationClass1);
        subClassOf1=factory.getOWLSubClassOfAxiom(subClass1, superClass1);
        if (hermit.isEntailed(subClassOf1))
            System.out.println("SubClassAxiom 1 is entailed!");
        //assertTrue(hermit.isEntailed(subClassOf1));
        
        subClass2=factory.getOWLClass(IRI.create("http://www.co-ode.org/ontologies/galen#Infection"));
        qualificationClass2=factory.getOWLClass(IRI.create("http://www.co-ode.org/ontologies/galen#DomainCategory"));
        qualificationProperty2=factory.getOWLObjectProperty(IRI.create("http://www.co-ode.org/ontologies/galen#Attribute"));
        superClass2=factory.getOWLObjectSomeValuesFrom(qualificationProperty2, qualificationClass2);
        subClassOf2=factory.getOWLSubClassOfAxiom(subClass2, superClass2);
        if (hermit.isEntailed(subClassOf2))
            System.out.println("SubClassAxiom 2 is entailed!");
        //assertTrue(hermit.isEntailed(subClassOf2));

	}
}
