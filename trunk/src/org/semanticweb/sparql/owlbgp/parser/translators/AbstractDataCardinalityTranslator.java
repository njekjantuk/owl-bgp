package org.semanticweb.sparql.owlbgp.parser.translators;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public abstract class AbstractDataCardinalityTranslator extends AbstractDataRestrictionTranslator {

    public AbstractDataCardinalityTranslator(TripleConsumer consumer) {
        super(consumer);
    }
    
    protected abstract Identifier getCardinalityTriplePredicate();
    protected abstract Identifier getQualifiedCardinalityTriplePredicate();
    protected int translateCardinality(Identifier mainNode) {
        Literal cardiObject=(Literal)consumer.getLiteralObject(mainNode, getCardinalityTriplePredicate(), true);
        if (cardiObject==null) cardiObject=(Literal)consumer.getLiteralObject(mainNode, getQualifiedCardinalityTriplePredicate(), true);
        if (cardiObject==null) return -1;
        return Integer.parseInt(cardiObject.getLexicalForm());
    }
    protected DataRange translateFiller(Identifier mainNode) {
        Identifier onDataRangeObject=consumer.getResourceObject(mainNode, Vocabulary.OWL_ON_DATA_RANGE.getIRI(), true);
        if (onDataRangeObject==null) return Datatype.OWL2_DATATYPES.LITERAL.getDatatype();
        return consumer.translateDataRange(onDataRangeObject);
    }
    protected ClassExpression translateRestriction(Identifier mainNode) {
        int cardinality=translateCardinality(mainNode);
        if (cardinality < 0) return consumer.translateClassExpression(mainNode);
        DataPropertyExpression prop=translateOnProperty(mainNode);
        // TODO: check why we just return a class here
        if (prop==null) return Clazz.create((IRI)mainNode);
        return createRestriction(prop, cardinality, translateFiller(mainNode));
    }
    protected abstract ClassExpression createRestriction(DataPropertyExpression prop,int cardi,DataRange filler);
}
