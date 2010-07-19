package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectOneOf;

public class TPOneOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPOneOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_ONE_OF.getIRI());
    }
    protected ClassExpression translateEquivalentClass(Identifier mainNode) {
    	return ObjectOneOf.create(consumer.translateToIndividualSet(mainNode));
    }
}
