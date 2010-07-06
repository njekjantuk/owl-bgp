package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectOneOf;

public class TPOneOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPOneOfHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_ONE_OF.getIRI());
    }
    protected ClassExpression translateEquivalentClass(String mainNode) {
    	return ObjectOneOf.create(consumer.translateToIndividualSet(mainNode));
    }
}
