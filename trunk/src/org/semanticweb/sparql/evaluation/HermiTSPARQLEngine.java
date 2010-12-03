package org.semanticweb.sparql.evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.InferenceType;

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.sparql.engine.main.StageBuilder;
import com.hp.hpl.jena.sparql.engine.main.StageGenerator;

public class HermiTSPARQLEngine {
	public static final String LB = System.getProperty("line.separator") ; 
	
	public HermiTSPARQLEngine() {
	    HermiTQueryEngine.register();
	    // Get the standard stage generator 
	    StageGenerator orig=(StageGenerator)ARQ.getContext().get(ARQ.stageGenerator);
	    // Create a new one 
	    StageGenerator hermiTStageGenerator=new HermiTStageGenerator(orig) ;
	    // Register it
	    StageBuilder.setGenerator(ARQ.getContext(), hermiTStageGenerator) ;
    }
	public ResultSet execQuery(String sparqlQueryString, HermiTDataSet dataSet) {
		Query query=QueryFactory.create(sparqlQueryString, Syntax.syntaxSPARQL_11); // create Jena query object
        return execQuery(query,dataSet);
	}
	public ResultSet execQuery(Query query, HermiTDataSet dataSet) {
        List<String> graphURIs=query.getGraphURIs(); // FROM
        //List<String> namedGraphURIs=query.getNamedGraphURIs(); // FROM NAMED
        if (graphURIs.size()>0) {
            try {
                dataSet=new HermiTDataSet(graphURIs,dataSet.getNamedGraphURIsToGraphs());
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
                return null;
            }
        } 
        QueryExecution qexec=QueryExecutionFactory.create(query,dataSet);
        return qexec.execSelect();
    }

	public static void main(String[] args) throws Exception {
	    long t=System.currentTimeMillis();
//	    HermiTDataSet dataset=getLUBMDataSet();
//	    HermiTDataSet dataset=getPizzaDataSet();
	    HermiTDataSet dataset=getPizzaTestDataSet();
	    System.out.println("OWLOntology: "+(System.currentTimeMillis()-t));
	    t=System.currentTimeMillis();
	    HermiTGraph graph=dataset.getDefaultGraph();
	    graph.getReasoner().precomputeInferences(
	            InferenceType.CLASS_HIERARCHY, InferenceType.OBJECT_PROPERTY_HIERARCHY, InferenceType.DATA_PROPERTY_HIERARCHY, 
	            InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS);
	    System.out.println("HermiT: "+(System.currentTimeMillis()-t));
	    t=System.currentTimeMillis();
	    HermiTSPARQLEngine sparqlEngine=new HermiTSPARQLEngine();
//	    getPizzaQ1(sparqlEngine, dataset);
	    getPizzaTestQ1(sparqlEngine, dataset);
//        getLUBMQ1(sparqlEngine, dataset);
//        getLUBMQ2(sparqlEngine, dataset);
//        getLUBMQ3(sparqlEngine, dataset);
//        getLUBMQ4(sparqlEngine, dataset);
//        getLUBMQ5(sparqlEngine, dataset);
//        getLUBMQ6(sparqlEngine, dataset);
//        getLUBMQ7(sparqlEngine, dataset);
//        getLUBMQ8(sparqlEngine, dataset);
//        getLUBMQ9(sparqlEngine, dataset);
//        getLUBMQ10(sparqlEngine, dataset);
//        getLUBMQ11(sparqlEngine, dataset);
//        getLUBMQ12(sparqlEngine, dataset);
//        getLUBMQ13(sparqlEngine, dataset);
//        getLUBMQ14(sparqlEngine, dataset);
	}
	
	public static HermiTDataSet getPizzaDataSet() throws OWLOntologyCreationException {
	    return new HermiTDataSet("http://www.co-ode.org/ontologies/pizza/pizza.owl#");
	}
	public static HermiTDataSet getPizzaTestDataSet() throws OWLOntologyCreationException {
        File inputOntologyFile = new File("/Users/bglimm/Documents/workspace/SPARQLingHermiT/src/ontologies/test.owl");
        File inputNamed1File = new File("/Users/bglimm/Documents/workspace/SPARQLingHermiT/src/ontologies/pizza.owl");
        File inputNamed2File = new File("/Users/bglimm/Documents/workspace/SPARQLingHermiT/src/ontologies/test2.owl");
        List<File> named=new ArrayList<File>();
        named.add(inputNamed1File);
        named.add(inputNamed2File);
        return new HermiTDataSet(inputOntologyFile,named);
    }
	public static void getPizzaTestQ1(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
        String queryString="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+LB
        + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+LB
        + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "+LB
        + "PREFIX pizza: <http://www.co-ode.org/ontologies/pizza/pizza.owl#>" +LB
        + "SELECT ?x ?y "+LB
//      + "FROM <http://www.co-ode.org/ontologies/pizza/pizza.owl#> "+LB
        + "FROM NAMED <http://www.co-ode.org/ontologies/pizza/pizzaTest2.owl> "+LB
        + "WHERE { " +LB
        + "  GRAPH ?g { ?x pizza:r ?y . } " +LB
        + " } "+LB
        + "ORDER BY ASC(?x) LIMIT 2";
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        ResultSet result=sparqlEngine.execQuery(query,dataset);
        ResultSetFormatter.out(result) ;
        System.out.println("Result: "+(System.currentTimeMillis()-t));
	}
    public static void getPizzaQ1(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
        String queryString="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+LB
        + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+LB
        + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "+LB
        + "PREFIX pizza: <http://www.co-ode.org/ontologies/pizza/pizza.owl#>" +LB
        + "SELECT ?x "+LB
//      + "FROM <http://www.co-ode.org/ontologies/pizza/pizza.owl#> "+LB
        + "WHERE { " +LB
        + "?x a owl:Class . " +LB
        + "?x rdfs:subClassOf owl:Nothing . " +LB
        + " } "+LB
        + "ORDER BY ASC(?x) LIMIT 2";
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
	public static HermiTDataSet getLUBMDataSet() throws OWLOntologyCreationException {
	    File inputOntologyFile = new File("/Users/bglimm/Documents/workspace/SPARQLingHermiT/src/ontologies/University0_0.owl");
        return new HermiTDataSet(inputOntologyFile);
    }
	public static String getLUBMPrefix() {
	   String prefix="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+LB
	        + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+LB
	        + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "+LB
	        + "PREFIX pizza: <http://www.co-ode.org/ontologies/pizza/pizza.owl#>" +LB
	        + "PREFIX ub: <http://example.org/university.owl#> " +LB
	        + "PREFIX g: <http://www.co-ode.org/ontologies/galen#>"
	        + "SELECT * WHERE { " +LB;
	    return prefix;
	}
	public static void getLUBMQ0(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
	    System.out.println("Q0");
	    String queryString=getLUBMPrefix()
	        + "  ?x a owl:Class."
	        + "  ?x rdf:type ub:FullProfessor. " +LB
	        + " } "+LB;
	    long t=System.currentTimeMillis();
	    Query query=QueryFactory.create(queryString);
	    System.out.println("Query: "+(System.currentTimeMillis()-t));
	    t=System.currentTimeMillis();
	    ResultSet result=sparqlEngine.execQuery(query,dataset);
	    ResultSetFormatter.asText(result);
	    System.out.println("Result: "+(System.currentTimeMillis()-t));
	}
	public static void getLUBMQ1(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
	    System.out.println("Q1");
//	    OWLOntology: 6102
//	    HermiT: 188608
//	    Query: 727, 604
//	    Result: 289, 382
       String queryString=getLUBMPrefix()
        + "  ?x rdf:type ub:GraduateStudent. " +LB
        + "  ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>. " +LB
        + " } "+LB;
       long t=System.currentTimeMillis();
       Query query=QueryFactory.create(queryString);
       System.out.println("Query: "+(System.currentTimeMillis()-t));
       t=System.currentTimeMillis();
       sparqlEngine.execQuery(query,dataset);
       System.out.println("Result: "+(System.currentTimeMillis()-t));
	}
	public static void getLUBMQ2(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
	    System.out.println("Q2");
//	    OWLOntology: 5949
//	    HermiT: 184355
//	    Query: 867, 2
//	    Result: 323911, 256837
	    String queryString=getLUBMPrefix()
        	+ "  ?x rdf:type ub:GraduateStudent. " +LB
            + "  ?y rdf:type ub:University. " +LB
            + "  ?z rdf:type ub:Department. " +LB
            + "  ?x ub:memberOf ?z. " +LB
            + "  ?z ub:subOrganizationOf ?y. " +LB
            + "  ?x ub:undergraduateDegreeFrom ?y." +LB
            + "} "+LB;
	    long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
	}
	public static void getLUBMQ3(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
	    System.out.println("Q3");
//	    Query: 2
//	    Result: 45
        String queryString=getLUBMPrefix()
            + "  ?x rdf:type ub:Publication. " +LB
            + "  ?x ub:publicationAuthor <http://www.Department0.University0.edu/AssistantProfessor0>. " +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
	public static void getLUBMQ4(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
//	    Query: 2
//	    Result: 16
	    System.out.println("Q4");
        String queryString=getLUBMPrefix()
            + "?x rdf:type ub:Professor. " +LB
            + "?x ub:worksFor <http://www.Department0.University0.edu>. " +LB
            + "?x ub:name ?y1. " +LB
            + "?x ub:emailAddress ?y2. " +LB
            + "?x ub:telephone ?y3. " +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
	public static void getLUBMQ5(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
//	    Query: 1
//	    Result: 100
	    System.out.println("Q5");
        String queryString=getLUBMPrefix()
            + "?x rdf:type ub:Person. " +LB         
            + "?x ub:memberOf <http://www.Department0.University0.edu>. " +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
	public static void getLUBMQ6(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
//	    Query: 1
//	    Result: 161
	    System.out.println("Q6");
        String queryString=getLUBMPrefix()
            + "  ?x rdf:type ub:Student. " +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
	public static void getLUBMQ7(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
//	    Query: 2
//	    Result: 1537
	    System.out.println("Q7");
        String queryString=getLUBMPrefix()
            +"  ?x rdf:type ub:Student. " +LB
            +"  ?y rdf:type ub:Department. " +LB
            +"  ?x ub:memberOf ?y. " +LB
            +"  ?y ub:subOrganizationOf <http://www.University0.edu>."
            +"  ?x ub:emailAddress ?z." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ8(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
//        Query: 10
//        Result: 3201
        System.out.println("Q8");
        String queryString=getLUBMPrefix()
            +"  ?x rdf:type ub:Student. " +LB
            +"  ?y rdf:type ub:Department. " +LB
            +"  ?x ub:memberOf ?y. " +LB
            +"  ?y ub:subOrganizationOf <http://www.University0.edu>."
            +"  ?x ub:emailAddress ?z." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ9(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
        System.out.println("Q9");
        String queryString=getLUBMPrefix()
            +"  ?x rdf:type ub:Student. " +LB
            +"  ?y rdf:type ub:Faculty. " +LB
            +"  ?z rdf:type ub:Course. " +LB
            +"  ?x ub:advisor ?y. " +LB
            +"  ?y ub:teacherOf ?z. " +LB
            +"  ?x ub:takesCourse ?z. " +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ10(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
        System.out.println("Q10");
        String queryString=getLUBMPrefix()
            + "  ?x rdf:type ub:Student. " +LB
            + "  ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>"
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ11(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
        System.out.println("Q11");
        String queryString=getLUBMPrefix()
            + "  ?x rdf:type ub:ResearchGroup. " +LB   
            + "  ?x ub:subOrganizationOf <http://www.University0.edu>." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ12(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
        System.out.println("Q12");
        String queryString=getLUBMPrefix()
            + "  ?x rdf:type ub:Chair. " +LB
            + "  ?y rdf:type ub:Department. " +LB
            + "  ?x ub:worksFor ?y. "+LB
            + "  ?y ub:subOrganizationOf <http://www.University0.edu>." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ13(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
        System.out.println("Q13");
        String queryString=getLUBMPrefix()
            + "  ?x rdf:type ub:Person. " +LB
            + "  <http://www.University0.edu> ub:hasAlumnus ?x." +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
    public static void getLUBMQ14(HermiTSPARQLEngine sparqlEngine, HermiTDataSet dataset) {
        System.out.println("Q14");
        String queryString=getLUBMPrefix()
            + "  ?x rdf:type ub:UndergraduateStudent. " +LB
            + "} "+LB;
        long t=System.currentTimeMillis();
        Query query=QueryFactory.create(queryString);
        System.out.println("Query: "+(System.currentTimeMillis()-t));
        t=System.currentTimeMillis();
        sparqlEngine.execQuery(query,dataset);
        System.out.println("Result: "+(System.currentTimeMillis()-t));
    }
}

//PREFIX pizza: <http://www.co-ode.org/ontologies/pizza/pizza.owl#>
//	SELECT ?x WHERE { 
//	  ?x rdfs:subClassOf pizza:CheeseyPizza .
//	  ?x rdfs:subClassOf pizza:InterestingPizza . 
//	} ORDER BY ASC(?x) LIMIT 2
//
//	PREFIX pizza: <http://www.co-ode.org/ontologies/pizza/pizza.owl#>
//	SELECT ?x WHERE { 
//	  ?x rdf:type pizza:Country . 
//	  ?y rdf:type pizza:Country . 
//	  ?x owl:differentFrom ?y . 
//	}
