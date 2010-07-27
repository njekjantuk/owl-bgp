package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectHasSelf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class SelfRestrictionTranslator extends AbstractObjectRestrictionTranslator {

    public SelfRestrictionTranslator(TripleConsumer consumer) {
        super(consumer);
    }

    protected ClassExpression translateRestriction(Identifier mainNode) {
        // Consume the triple that specifies the description is a has self
        consumer.getLiteralObject(mainNode, Vocabulary.OWL_HAS_SELF.getIRI(), true);
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        return ObjectHasSelf.create(prop);
    }
}
