package org.semanticweb.sparql.bgpevaluation.monitor;

import java.io.PrintStream;

import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;


public class PrintingMonitor extends TimingMonitor {
    protected final PrintStream m_out;
    
    public PrintingMonitor() {
        m_out=System.out;
    }
    public PrintingMonitor(PrintStream out) {
        m_out=out;
    }
    
    public void bgpEvaluationFinished(int resultSize) {
        super.bgpEvaluationFinished(resultSize);
        m_out.println("BGP Evalation finished in: "+getLastBGPEvaluationTime()+" ms with "+resultSize+" results. ");
    }
    public void bgpParsingFinished() {
        super.bgpParsingFinished();
        m_out.println("BGP parsing finished in: "+getLastBGPParsingTime()+" ms. ");
    }
    public void connectedComponentsComputationFinished(int numComponents) {
        super.connectedComponentsComputationFinished(numComponents);
        m_out.println(getNumberOfComponents()+" connected components computed in: "+getLastConnectedComponentComputationTime()+" ms. ");
    }
    public void queryObjectEvaluationFinished(int results) {
        super.queryObjectEvaluationFinished(results);
        m_out.println("Query object evaluation finished in: "+getLastQueryObjectEvaluationTime()+" ms with "+getLastQueryObjectResultSize()+" results. ");
    }
    public void componentsEvaluationFinished(int results) {
        super.componentsEvaluationFinished(results);
        m_out.println("Component "+(componentIndex-1)+" evaluated in: "+getLastComponentEvaluationTime()+" ms with "+getLastComponentResultSize()+" results. ");
    }
    public void costEvaluationFinished(QueryObject<? extends Axiom> cheapest) {
        super.costEvaluationFinished(cheapest);
        m_out.println("Cost evaluation finished in "+getLastCostEstimationTime()+" ms.");
    }
}
