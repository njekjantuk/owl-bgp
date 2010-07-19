package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.axioms.DataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.dataranges.Facet;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public class GTPDataPropertyAssertionHandler extends AbstractLiteralTripleHandler {

    public GTPDataPropertyAssertionHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public boolean canHandle(Identifier subject, Identifier predicate, Literal object) {
        if (getConsumer().isAnnotationProperty(predicate) || getConsumer().isOntology(subject)) return false;
        return !Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate) && !Facet.OWL_FACETS.contains(predicate);
    }
    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Literal object) {
        return false;
    }
    public void handleTriple(Identifier subject, Identifier predicate, Literal object) {
        addAxiom(DataPropertyAssertion.create(translateDataProperty(predicate), translateIndividual(subject), object));
        consumeTriple(subject, predicate, object);
    }
}
