package org.semanticweb.sparql;

import java.util.List;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.sparql.arq.OWLOntologyDataSet;
import org.semanticweb.sparql.bgpevaluation.OWLReasonerStageGenerator;
import org.semanticweb.sparql.bgpevaluation.monitor.Monitor;
import org.semanticweb.sparql.bgpevaluation.monitor.MonitorAdapter;

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.sparql.engine.main.StageBuilder;
import com.hp.hpl.jena.sparql.engine.main.StageGenerator;

public class OWLReasonerSPARQLEngine {

    public OWLReasonerSPARQLEngine() {
        this(null);
    }
	public OWLReasonerSPARQLEngine(Monitor bgpEvaluationMonitor) {
	    StageGenerator orig=(StageGenerator)ARQ.getContext().get(ARQ.stageGenerator);
	    if (bgpEvaluationMonitor==null)
	        bgpEvaluationMonitor=new MonitorAdapter();
	    StageGenerator hermiTStageGenerator=new OWLReasonerStageGenerator(orig, bgpEvaluationMonitor);
	    StageBuilder.setGenerator(ARQ.getContext(), hermiTStageGenerator) ;
    }
	public ResultSet execQuery(String sparqlQueryString, OWLOntologyDataSet dataSet) {
		Query query=QueryFactory.create(sparqlQueryString, Syntax.syntaxSPARQL_11); // create Jena query object
        return execQuery(query,dataSet);
	}
	public ResultSet execQuery(Query query, OWLOntologyDataSet dataSet) {
        List<String> graphURIs=query.getGraphURIs(); // FROM
        if (graphURIs.size()>0) {
            try {
                dataSet=new OWLOntologyDataSet(graphURIs,dataSet.getNamedGraphURIsToGraphs());
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
                return null;
            }
        } 
        QueryExecution engine=QueryExecutionFactory.create(query,dataSet);
        return engine.execSelect();
    }
}