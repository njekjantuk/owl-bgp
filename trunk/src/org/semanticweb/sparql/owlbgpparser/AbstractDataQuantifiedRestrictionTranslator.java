package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;

public abstract class AbstractDataQuantifiedRestrictionTranslator extends AbstractDataRestrictionTranslator {
    public AbstractDataQuantifiedRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression translateRestriction(Identifier mainNode) {
        DataPropertyExpression prop=translateOnProperty(mainNode);
        // TODO: check why we just return a class here
        if (prop == null) return Clazz.create((IRI)mainNode);
        Identifier fillerObject=getResourceObject(mainNode, getFillerTriplePredicate(), true);
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
