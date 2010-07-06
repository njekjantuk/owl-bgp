package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Axiom;
import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataRange;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Individual;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class AbstractTripleHandler {

    protected final OWLRDFConsumer consumer;
    
    public AbstractTripleHandler(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public OWLRDFConsumer getConsumer() {
        return consumer;
    }
    protected void consumeTriple(String subject, String predicate, String object) {
        consumer.consumeTriple(subject, predicate, object);
    }
    protected void consumeTriple(String subject, String predicate, ILiteral object) {
        consumer.consumeTriple(subject, predicate, object);
    }
    protected void addAxiom(Axiom axiom) {
        consumer.addAxiom(axiom);
    }
    protected ClassExpression translateClassExpression(String iri) {
        return consumer.translateClassExpression(iri);
    }
    protected ObjectPropertyExpression translateObjectProperty(String iri) {
        return consumer.translateObjectPropertyExpression(iri);
    }
    protected DataPropertyExpression translateDataProperty(String iri) {
        return consumer.translateDataPropertyExpression(iri);
    }
    protected DataRange translateDataRange(String iri) {
        return consumer.translateDataRange(iri);
    }
    protected Individual translateIndividual(String iri) {
        return consumer.translateIndividual(iri);
    }
}
