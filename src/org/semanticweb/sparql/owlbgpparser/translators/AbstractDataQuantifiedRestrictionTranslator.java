package org.semanticweb.sparql.owlbgpparser.translators;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgpparser.TripleConsumer;

public abstract class AbstractDataQuantifiedRestrictionTranslator extends AbstractDataRestrictionTranslator {
    public AbstractDataQuantifiedRestrictionTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression translateRestriction(Identifier mainNode) {
        DataPropertyExpression prop=translateOnProperty(mainNode);
        if (prop == null) 
            throw new RuntimeException("Error: There was no data property for a data property restriction. Current triple subject: "+mainNode);
        Identifier fillerObject=consumer.getResourceObject(mainNode, getFillerTriplePredicate(), true);
        DataRange dataRange;
        if (fillerObject != null) 
            dataRange=consumer.translateDataRange(fillerObject);
        else
            dataRange=Datatype.OWL2_DATATYPES.LITERAL.getDatatype();
        return createRestriction(prop, dataRange);
    }
    protected abstract Identifier getFillerTriplePredicate();
    protected abstract ClassExpression createRestriction(DataPropertyExpression prop, DataRange filler);
}
