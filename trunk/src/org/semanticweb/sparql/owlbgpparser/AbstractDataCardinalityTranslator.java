package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataRange;
import org.semanticweb.sparql.owlbgp.model.Datatype;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;

public abstract class AbstractDataCardinalityTranslator extends AbstractDataRestrictionTranslator {

    public AbstractDataCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    
    protected abstract Identifier getCardinalityTriplePredicate();
    protected abstract Identifier getQualifiedCardinalityTriplePredicate();
    protected int translateCardinality(Identifier mainNode) {
        ILiteral cardiObject=(ILiteral)getLiteralObject(mainNode, getCardinalityTriplePredicate(), true);
        if (cardiObject == null) cardiObject=(ILiteral)getLiteralObject(mainNode, getQualifiedCardinalityTriplePredicate(), true);
        if (cardiObject == null) return -1;
        return Integer.parseInt(cardiObject.getLexicalForm());
    }
    protected DataRange translateFiller(Identifier mainNode) {
        Identifier onDataRangeObject=getResourceObject(mainNode, Vocabulary.OWL_ON_DATA_RANGE.getIRI(), true);
        if (onDataRangeObject == null) return Datatype.OWL2_DATATYPES.LITERAL.getDatatype();
        return getConsumer().translateDataRange(onDataRangeObject);
    }
    protected ClassExpression translateRestriction(Identifier mainNode) {
        int cardinality = translateCardinality(mainNode);
        if (cardinality < 0) return consumer.translateClassExpression(mainNode);
        DataPropertyExpression prop=translateOnProperty(mainNode);
        // TODO: check why we just return a class here
        if (prop==null) return Clazz.create((IRI)mainNode);
        return createRestriction(prop, cardinality, translateFiller(mainNode));
    }
    protected abstract ClassExpression createRestriction(DataPropertyExpression prop,int cardi,DataRange filler);
}
