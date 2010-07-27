package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataHasValue;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class DataHasValueTranslator extends AbstractDataRestrictionTranslator {

    public DataHasValueTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    
    protected ClassExpression translateRestriction(Identifier mainNode) {
        Literal con=consumer.getLiteralObject(mainNode, Vocabulary.OWL_HAS_VALUE.getIRI(), true);
        return DataHasValue.create(translateOnProperty(mainNode), con);
    }
}
