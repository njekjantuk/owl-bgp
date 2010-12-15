package org.semanticweb.sparql.bgpevaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.axioms.ReflexiveObjectProperty;

public class QO_ReflexiveObjectProperty extends QO_ObjectPropertyAxiom<ReflexiveObjectProperty> {

    public QO_ReflexiveObjectProperty(ReflexiveObjectProperty axiomTemplate, OWLOntologyGraph graph) {
        super(axiomTemplate, graph);
    }
    
    protected OWLAxiom getEntailmentAxiom(OWLObjectPropertyExpression ope) {
        return m_dataFactory.getOWLReflexiveObjectPropertyAxiom(ope);
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
