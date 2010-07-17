package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.DataHasValue;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public class DataHasValueTranslator extends AbstractDataRestrictionTranslator {

    public DataHasValueTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    
    protected ClassExpression translateRestriction(Identifier mainNode) {
        ILiteral con=getLiteralObject(mainNode, Vocabulary.OWL_HAS_VALUE.getIRI(), true);
        return DataHasValue.create(translateOnProperty(mainNode), con);
    }
}
