package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectComplementOf;

public class TPComplementOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPComplementOfHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_COMPLEMENT_OF.getIRI());
    }

    protected ClassExpression translateEquivalentClass(Identifier mainNode) {
        return ObjectComplementOf.create(consumer.translateClassExpression(mainNode));
    }
}
