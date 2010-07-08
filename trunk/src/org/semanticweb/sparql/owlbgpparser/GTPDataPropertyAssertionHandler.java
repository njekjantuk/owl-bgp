package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.DataPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.Facet;
import org.semanticweb.sparql.owlbgp.model.ILiteral;

public class GTPDataPropertyAssertionHandler extends AbstractLiteralTripleHandler {

    public GTPDataPropertyAssertionHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }
    public boolean canHandle(String subject, String predicate, ILiteral object) {
        if (getConsumer().isAnnotationProperty(predicate) || getConsumer().isOntology(subject)) return false;
        return !Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains(predicate) && !Facet.OWL_FACETS.contains(predicate);
    }
    public boolean canHandleStreaming(String subject, String predicate, ILiteral object) {
        return false;
    }
    public void handleTriple(String subject, String predicate, ILiteral object) {
        addAxiom(DataPropertyAssertion.create(translateDataProperty(predicate), translateIndividual(subject), object));
        consumeTriple(subject, predicate, object);
    }
}
