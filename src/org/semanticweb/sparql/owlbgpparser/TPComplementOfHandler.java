package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectComplementOf;

public class TPComplementOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPComplementOfHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_COMPLEMENT_OF.getIRI());
    }

    protected ClassExpression translateEquivalentClass(String mainNode) {
        return ObjectComplementOf.create(consumer.translateClassExpression(mainNode));
    }
}
