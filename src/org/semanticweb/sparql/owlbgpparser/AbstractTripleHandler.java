package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class AbstractTripleHandler {

    protected final OWLRDFConsumer consumer;
    
    public AbstractTripleHandler(OWLRDFConsumer consumer) {
        this.consumer = consumer;
    }
    public OWLRDFConsumer getConsumer() {
        return consumer;
    }
    protected void consumeTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumer.consumeTriple(subject, predicate, object);
    }
    protected void consumeTriple(Identifier subject, Identifier predicate, Literal object) {
        consumer.consumeTriple(subject, predicate, object);
    }
    protected void addAxiom(Axiom axiom) {
        consumer.addAxiom(axiom);
    }
    protected ClassExpression translateClassExpression(Identifier iri) {
        return consumer.translateClassExpression(iri);
    }
    protected ObjectPropertyExpression translateObjectProperty(Identifier iri) {
        return consumer.translateObjectPropertyExpression(iri);
    }
    protected DataPropertyExpression translateDataProperty(Identifier iri) {
        return consumer.translateDataPropertyExpression(iri);
    }
    protected DataRange translateDataRange(Identifier iri) {
        return consumer.translateDataRange(iri);
    }
    protected Individual translateIndividual(Identifier iri) {
        return consumer.translateIndividual(iri);
    }
}
