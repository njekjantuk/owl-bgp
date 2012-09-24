/* Copyright 2010-2012 by the developers of the OWL-BGP project. 

   This file is part of OWL-BGP.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP. If not, see <http://www.gnu.org/licenses/>.
 */

package  org.semanticweb.sparql.hermit;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.sparql.OWLReasonerSPARQLEngine;
import org.semanticweb.sparql.arq.OWLOntologyDataSet;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.monitor.MinimalPrintingMonitor;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;

public class TestSPARQLDL {
	public static final String LB = System.getProperty("line.separator") ; 

	public static void main(String[] args) throws Exception {
	    long t=System.currentTimeMillis();
	    OWLOntologyDataSet dataset=getSPARQLDLDataSet();
	    System.out.println("OWLOntology loaded in: "+(System.currentTimeMillis()-t));
	    OWLOntologyGraph graph=dataset.getDefaultGraph();
	    t=System.currentTimeMillis();	    
//	    graph.getReasoner().precomputeInferences(InferenceType.CLASS_HIERARCHY);
	    graph.getReasoner().precomputeInferences(InferenceType.CLASS_HIERARCHY, InferenceType.OBJECT_PROPERTY_HIERARCHY, InferenceType.DATA_PROPERTY_HIERARCHY/*, InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS*/);
	    System.out.println("Precomputation lasted: "+(System.currentTimeMillis()-t));
	    t=System.currentTimeMillis();
	    OWLReasonerSPARQLEngine sparqlEngine=new OWLReasonerSPARQLEngine(new MinimalPrintingMonitor());
//	    getLUBMQTest(sparqlEngine, dataset);
//	    getLUBMQ0(sparqlEngine, dataset);
	    t=System.currentTimeMillis();
	    
	    
	    
	    /*getLUBMQ1(sparqlEngine, dataset);
        getLUBMQ2(sparqlEngine, dataset);
        getLUBMQ3(sparqlEngine, dataset);
        getLUBMQ4(sparqlEngine, dataset);
        getLUBMQ5(sparqlEngine, dataset);*/
        getLUBMQ6(sparqlEngine, dataset);
        /*getLUBMQ7(sparqlEngine, dataset);
        getLUBMQ8(sparqlEngine, dataset);
        getLUBMQ9(sparqlEngine, dataset);
        getLUBMQ10(sparqlEngine, dataset);
        */
        System.out.println("The execution of the 10 queries finished in "+(System.currentTimeMillis()-t) +"  msec");
	}
	public static OWLOntologyDataSet getSPARQLDLDataSet() throws OWLOntologyCreationException {
	    OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
	    OWLOntology ont=manager.loadOntologyFromOntologyDocument(new File("evaluation/ontologies/univ-bench.owl"));
	    File dir = new File("evaluation/ontologies/LUBM-1");
       String[] children = dir.list();
        for (int i=0;i<children.length;i++){
            File file=new File("evaluation/ontologies/LUBM-1/"+children[i]); 
            if (file.isFile()) {
        	  OWLOntology tmp=manager.loadOntologyFromOntologyDocument(file);
	          manager.addAxioms(ont, tmp.getAxioms());
            }
        }
	    
	    //OWLOntology ont=manager.loadOntologyFromOntologyDocument(new File("evaluation/ontologies/LUBMwDep.owl"));
        /*try {
        	 File file = new File("evaluation/ontologies/LUBMwDep.owl");
        	 manager.saveOntology(ont, IRI.create(file.toURI()));
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        return new OWLOntologyDataSet(ont, null);
    }
	public static String getLUBMPrefix() {
	   String prefix="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+LB
	        + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+LB
	        + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "+LB
	        + "PREFIX pizza: <http://www.co-ode.org/ontologies/pizza/pizza.owl#>" +LB
	        + "PREFIX ub: <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#> " +LB
	        + "PREFIX g: <http://www.co-ode.org/ontologies/galen#>"
	        + "SELECT * WHERE { " +LB;
	    return prefix;
	}
	
	public static void getLUBMQ1(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
	    System.out.println("Q1");
       String queryString=getLUBMPrefix()
        + "?x rdf:type ub:GraduateStudent. " +LB
        + "?x ?y ?z. " +LB
        + "?y rdf:type owl:ObjectProperty." +LB
        + "?w rdf:type ub:Course." +LB
        + " } "+LB;
        long t=System.currentTimeMillis();
       Query query=QueryFactory.create(queryString);
       System.out.println("Query: "+(System.currentTimeMillis()-t));
       t=System.currentTimeMillis();
       sparqlEngine.execQuery(query,dataset);
       System.out.println("Result: "+(System.currentTimeMillis()-t));
	}
	public static void getLUBMQ2(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
	    System.out.println("Q2");
	    String queryString=getLUBMPrefix()
        + "?x rdf:type ub:Student." +LB
        + "?x rdf:type ?C." +LB
        + "?C rdfs:subClassOf ub:Employee."+LB
        + "?x ub:undergraduateDegreeFrom ?y." +LB
            + "} "+LB;
	    long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
	}
	public static void getLUBMQ3(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
	    System.out.println("Q3");
        String queryString=getLUBMPrefix()
        	+"?x rdf:type ub:Person." +LB
            +"?x ?y <http://www.Department0.University0.edu>." +LB
            +"?y rdf:type owl:ObjectProperty." +LB
            +"?y rdfs:subPropertyOf ub:memberOf." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
	public static void getLUBMQ4(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
	    System.out.println("Q4");
        String queryString=getLUBMPrefix()
            +"<http://www.Department0.University0.edu/GraduateStudent5> ?y ?w." +LB
            +"?w rdf:type ?z." +LB
            +"?z rdfs:subClassOf ub:Course." +LB
            + "?y rdf:type owl:ObjectProperty." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
	public static void getLUBMQ5(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
	    System.out.println("Q5");
        String queryString=getLUBMPrefix()
        	+"?x rdf:type ub:GraduateStudent." +LB
            +"?x ?y ?w." +LB
        	+"?y rdf:type owl:ObjectProperty." +LB
            +"?w rdf:type ?z." +LB
        	+"?z rdfs:subClassOf ub:Course." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
	public static void getLUBMQ6(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
	    System.out.println("Q6");
        String queryString=getLUBMPrefix()
        	+"?x rdf:type ub:GraduateStudent." +LB
            +"?x ?y ?w." +LB
            +"?y rdf:type owl:ObjectProperty." +LB
            +"?w rdf:type ?z." +LB
            +"?z owl:disjointWith ub:GraduateCourse." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
	public static void getLUBMQ7(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
	    System.out.println("Q7");
        String queryString=getLUBMPrefix()
            +"?c rdfs:subClassOf owl:Thing." +LB
            +"?x rdf:type ?c." +LB
            +"?x ub:takesCourse ?a." +LB
            +"?x ub:teachingAssistantOf ?a." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ8(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
        System.out.println("Q8");
        String queryString=getLUBMPrefix()
        	+"?x ub:advisor ?y." +LB
            +"?x rdf:type ?a." +LB
            +"?a rdfs:subClassOf ub:Person." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ9(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
        System.out.println("Q9");
        String queryString=getLUBMPrefix()
        	+"?a rdfs:subClassOf ub:Person." +LB
            +"?x rdf:type ?a." +LB
        	+"?x ub:teachingAssistantOf ?y." +LB
        	+"?y rdf:type ub:Course." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ10(OWLReasonerSPARQLEngine sparqlEngine, OWLOntologyDataSet dataset) {
        System.out.println("Q10");
        String queryString=getLUBMPrefix()
        	+"?x rdf:type ub:GraduateStudent." +LB
            +"?x ub:memberOf ?w." +LB
            +"?a ?p ?w." +LB
            +"?p rdf:type owl:ObjectProperty." +LB
            +"?a rdf:type ?b." +LB
        	+"?p rdfs:subPropertyOf ub:worksFor." +LB
            +"?b rdfs:subClassOf ub:Faculty." +LB
            +"?x ub:advisor ?a." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
    
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    
}
//OWLOntology: 13654
//Class classification: 6201
//Realisation: 42420
//Q1
//Query: 243, 253
//Cost estimation time: 0, computation time: 30, results size: 4
//Result: 311, 278
//Q2
//Query: 3, 3
//Cost estimation time: 0, computation time: 32, results size: 0
//Result: 278, 125
//Q3
//Query: 11, 2
//Cost estimation time: 0, computation time: 6, results size: 6
//Result: 29, 12
//Q4
//Query: 4, 2
//Cost estimation time: 0, computation time: 4, results size: 34
//Result: 33, 86

//Q5
//Query: 3, 1
//Cost estimation time: 0, computation time: 259, results size: 719
//Result: 318, 46
//Q6
//Query: 4, 2
//Cost estimation time: 0, computation time: 228, results size: 7790
//Result: 243, 117
//Q7
//Query: 727, 2
//Cost estimation time: 0, computation time: 40, results size: 67
//Result: 1414, 38
//Q8
//Query: 1, 1
//Cost estimation time: 0, computation time: 408, results size: 7790
//Result: 497, 853
//Q9
//Query: 3, 1
//Cost estimation time: 0, computation time: 7164, results size: 208
//Result: 235818, 221652
//Q10
//Query: 2, 1
//Cost estimation time: 0, computation time: 4, results size: 4
//Result: 12, 12
//Q11
//Query: 1, 2
//Cost estimation time: 0, computation time: 4, results size: 224
//Result: 11, 11
//Q12
//Query: 2, 1
//Cost estimation time: 0, computation time: 306, results size: 15
//Result: 2866, 8
//Q13
//Query: 3, 2
//Cost estimation time: 0, computation time: 0, results size: 1
//Result: 14, 5
//Q14
//Query: 2, 0
//Cost estimation time: 0, computation time: 133, results size: 5916
//Result: 149, 53

//OWLOntology: 9248
//Class classification: 3822
//Realisation: 26900
//Q1
//Query: 316
//Cost estimation time: 26, computation time: 58, results size: 4
//Result: 430
//Q2
//Query: 39
//Cost estimation time: 238, computation time: 20, results size: 0
//Result: 345
//Q3
//Query: 3
//Cost estimation time: 2, computation time: 4, results size: 6
//Result: 17
//Q4
//Query: 2
//Cost estimation time: 24, computation time: 51, results size: 34
//Result: 94
//Q5
//Query: 1
//Cost estimation time: 13, computation time: 29, results size: 719
//Result: 48
//Q6
//Query: 1
//Cost estimation time: 0, computation time: 105, results size: 7790
//Result: 110
//Q7
//Query: 1
//Cost estimation time: 13, computation time: 17, results size: 67
//Result: 37
//Q8
//Query: 2
//Cost estimation time: 184, computation time: 664, results size: 7790
//Result: 855
//Q9
//Query: 2
//Cost estimation time: 221190, computation time: 6631, results size: 208
//Result: 227827
//Q10
//Query: 1
//Cost estimation time: 3, computation time: 4, results size: 4
//Result: 14
//Q11
//Query: 1
//Cost estimation time: 0, computation time: 5, results size: 224
//Result: 27
//Q12
//Query: 1
//Cost estimation time: 2496, computation time: 166, results size: 15
//Result: 2669
//Q13
//Query: 1
//Cost estimation time: 1, computation time: 0, results size: 1
//Result: 7
//Q14
//Query: 1
//Cost estimation time: 1, computation time: 37, results size: 5916
//Result: 43