package org.semanticweb.sparql.bgpevaluation.monitor;

import java.util.List;

import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;

public interface Monitor {
    void bgpEvaluationStarted();
    void bgpEvaluationFinished(int resultSize);
    void bgpParsingStarted();
    void bgpParsingFinished();
    void connectedComponentsComputationStarted();
    void connectedComponentsComputationFinished(int numComponents);
    void componentsEvaluationStarted(List<QueryObject<? extends Axiom>> queryObjectsInComponent);
    void componentsEvaluationFinished(int results);
    void costEvaluationStarted();
    void costEvaluationStarted(QueryObject<? extends Axiom> qo);
    void costEvaluationFinished(double costEstimate, double resultSizeEstimate);
    void costEvaluationFinished(QueryObject<? extends Axiom> cheapest);
    void queryObjectEvaluationStarted(QueryObject<? extends Axiom> queryObject);
    void queryObjectEvaluationFinished(int resultSize);
}
