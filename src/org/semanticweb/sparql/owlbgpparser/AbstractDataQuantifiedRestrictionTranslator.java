package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataRange;
import org.semanticweb.sparql.owlbgp.model.Datatype;

public abstract class AbstractDataQuantifiedRestrictionTranslator extends AbstractDataRestrictionTranslator {
    public AbstractDataQuantifiedRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression translateRestriction(String mainNode) {
        DataPropertyExpression prop=translateOnProperty(mainNode);
        if (prop == null) return Clazz.create(mainNode);
        String fillerObject=getResourceObject(mainNode, getFillerTriplePredicate(), true);
        DataRange dataRange;
        if (fillerObject != null) 
            dataRange=consumer.translateDataRange(fillerObject);
        else
            dataRange=Datatype.RDFS_LITERAL;
        return createRestriction(prop, dataRange);
    }
    protected abstract String getFillerTriplePredicate();
    protected abstract ClassExpression createRestriction(DataPropertyExpression prop, DataRange filler);
}
