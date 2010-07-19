package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataHasValue;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;

public class DataHasValueTranslator extends AbstractDataRestrictionTranslator {

    public DataHasValueTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    
    protected ClassExpression translateRestriction(Identifier mainNode) {
        Literal con=getLiteralObject(mainNode, Vocabulary.OWL_HAS_VALUE.getIRI(), true);
        return DataHasValue.create(translateOnProperty(mainNode), con);
    }
}
