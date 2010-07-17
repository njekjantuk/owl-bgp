package org.semanticweb.sparql.owlbgpparser;

import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.HasKey;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.PropertyExpression;

public class TPHasKeyHandler extends TriplePredicateHandler {

    private OptimisedListTranslator<PropertyExpression> listTranslator;

    public TPHasKeyHandler(OWLRDFConsumer consumer) {
        super(consumer, Vocabulary.OWL_HAS_KEY.getIRI());
        this.listTranslator=new OptimisedListTranslator<PropertyExpression>(consumer, new HasKeyListItemTranslator(consumer));
    }

    public boolean canHandleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        return false;
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        consumeTriple(subject, predicate, object);
        ClassExpression ce = translateClassExpression(subject);
        Set<PropertyExpression> props = listTranslator.translateToSet(object);
        addAxiom(HasKey.create(ce, props));
    }
}
