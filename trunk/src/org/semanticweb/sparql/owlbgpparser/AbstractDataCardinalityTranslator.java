package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataRange;
import org.semanticweb.sparql.owlbgp.model.Datatype;
import org.semanticweb.sparql.owlbgp.model.ILiteral;

public abstract class AbstractDataCardinalityTranslator extends AbstractDataRestrictionTranslator {

    public AbstractDataCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    
    protected abstract String getCardinalityTriplePredicate();
    protected abstract String getQualifiedCardinalityTriplePredicate();
    protected int translateCardinality(String mainNode) {
        ILiteral cardiObject=(ILiteral)getLiteralObject(mainNode, getCardinalityTriplePredicate(), true);
        if (cardiObject == null) cardiObject=(ILiteral)getLiteralObject(mainNode, getQualifiedCardinalityTriplePredicate(), true);
        if (cardiObject == null) return -1;
        return Integer.parseInt(cardiObject.getLexicalForm());
    }
    protected DataRange translateFiller(String mainNode) {
        String onDataRangeObject=getResourceObject(mainNode, OWLRDFVocabulary.OWL_ON_DATA_RANGE.getIRI(), true);
        if (onDataRangeObject == null) return Datatype.RDFS_LITERAL;
        return getConsumer().translateDataRange(onDataRangeObject);
    }
    protected ClassExpression translateRestriction(String mainNode) {
        int cardinality = translateCardinality(mainNode);
        if (cardinality < 0) return Clazz.create(mainNode);
        DataPropertyExpression prop=translateOnProperty(mainNode);
        if (prop==null) return Clazz.create(mainNode);
        return createRestriction(prop, cardinality, translateFiller(mainNode));
    }
    protected abstract ClassExpression createRestriction(DataPropertyExpression prop,int cardi,DataRange filler);
}
