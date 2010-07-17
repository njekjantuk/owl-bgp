package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.ObjectHasSelf;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class SelfRestrictionTranslator extends AbstractObjectRestrictionTranslator {

    public SelfRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }

    protected ClassExpression translateRestriction(Identifier mainNode) {
        // Consume the triple that specifies the description is a has self
        consumer.getLiteralObject(mainNode, Vocabulary.OWL_HAS_SELF.getIRI(), true);
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        return ObjectHasSelf.create(prop);
    }
}
