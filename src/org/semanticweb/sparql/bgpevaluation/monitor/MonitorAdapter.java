package org.semanticweb.sparql.bgpevaluation.monitor;

import java.util.List;

import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;

public class MonitorAdapter implements Monitor {
    public MonitorAdapter() {
    }
    public void bgpEvaluationStarted() {
    }
    public void bgpEvaluationFinished(int resultSize) {
    }
    public void bgpParsingStarted() {
    }
    public void bgpParsingFinished() {
    }
    public void connectedComponentsComputationStarted() {
    }
    public void connectedComponentsComputationFinished(int numComponents) {
    }
    public void componentsEvaluationStarted(List<QueryObject<? extends Axiom>> queryObjectsInComponent) {
    }
    public void componentsEvaluationFinished(int results) {
    }
    public void costEvaluationStarted() {
    }
    public void costEvaluationStarted(QueryObject<? extends Axiom> qo) {
    }
    public void costEvaluationFinished(double costEstimate, double resultSizeEstimate) {
    }
    public void costEvaluationFinished(QueryObject<? extends Axiom> cheapest) {   
    }
    public void queryObjectEvaluationStarted(QueryObject<? extends Axiom> queryObject) {
    }
    public void queryObjectEvaluationFinished(int resultSize) {
    }
}
