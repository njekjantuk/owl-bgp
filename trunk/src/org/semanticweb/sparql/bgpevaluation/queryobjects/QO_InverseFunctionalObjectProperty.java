package org.semanticweb.sparql.bgpevaluation.queryobjects;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.axioms.InverseFunctionalObjectProperty;

public class QO_InverseFunctionalObjectProperty extends QO_ObjectPropertyAxiom<InverseFunctionalObjectProperty> {

    public QO_InverseFunctionalObjectProperty(InverseFunctionalObjectProperty axiomTemplate, OWLOntologyGraph graph) {
        super(axiomTemplate, graph);
    }
    
    protected OWLAxiom getEntailmentAxiom(OWLObjectPropertyExpression ope) {
        return m_dataFactory.getOWLInverseFunctionalObjectPropertyAxiom(ope);
    }
    public <O> O accept(QueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}
