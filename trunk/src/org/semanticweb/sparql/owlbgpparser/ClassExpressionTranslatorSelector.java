package org.semanticweb.sparql.owlbgpparser;

public class ClassExpressionTranslatorSelector {

    protected final OWLRDFConsumer consumer;
    protected IntersectionOfTranslator intersectionOfTranslator;
    protected UnionOfTranslator unionOfTranslator;
    protected ComplementOfTranslator complementOfTranslator;
    protected OneOfTranslator oneOfTranslator;
    protected SelfRestrictionTranslator selfRestrictionTranslator;
    protected ObjectAllValuesFromTranslator objectAllValuesFromTranslator;
    protected ObjectSomeValuesFromTranslator objectSomeValuesFromTranslator;
    protected ObjectHasValueTranslator objectHasValueTranslator;
    protected ObjectMinCardinalityTranslator objectMinCardinalityTranslator;
    protected ObjectCardinalityTranslator objectCardinalityTranslator;
    protected ObjectMaxCardinalityTranslator objectMaxCardinalityTranslator;
    protected DataAllValuesFromTranslator dataAllValuesFromTranslator;
    protected DataSomeValuesFromTranslator dataSomeValuesFromTranslator;
    protected DataHasValueTranslator dataHasValueTranslator;
    protected DataMinCardinalityTranslator dataMinCardinalityTranslator;
    protected DataCardinalityTranslator dataCardinalityTranslator;
    protected DataMaxCardinalityTranslator dataMaxCardinalityTranslator;
    protected NamedClassTranslator namedClassTranslator;

    public ClassExpressionTranslatorSelector(OWLRDFConsumer con) {
        this.consumer=con;
        intersectionOfTranslator=new IntersectionOfTranslator(con);
        unionOfTranslator=new UnionOfTranslator(con);
        complementOfTranslator=new ComplementOfTranslator(con);
        oneOfTranslator=new OneOfTranslator(con);
        selfRestrictionTranslator=new SelfRestrictionTranslator(con);
        objectAllValuesFromTranslator=new ObjectAllValuesFromTranslator(con);
        objectSomeValuesFromTranslator=new ObjectSomeValuesFromTranslator(con);
        objectHasValueTranslator=new ObjectHasValueTranslator(con);
        objectMinCardinalityTranslator=new ObjectMinCardinalityTranslator(con);
        objectCardinalityTranslator=new ObjectCardinalityTranslator(con);
        objectMaxCardinalityTranslator=new ObjectMaxCardinalityTranslator(con);
        dataAllValuesFromTranslator=new DataAllValuesFromTranslator(con);
        dataSomeValuesFromTranslator=new DataSomeValuesFromTranslator(con);
        dataHasValueTranslator=new DataHasValueTranslator(con);
        dataMinCardinalityTranslator=new DataMinCardinalityTranslator(con);
        dataCardinalityTranslator=new DataCardinalityTranslator(con);
        dataMaxCardinalityTranslator=new DataMaxCardinalityTranslator(con);
        namedClassTranslator=new NamedClassTranslator(con);
    }

    public boolean isObjectRestriction(String mainNode, String property) {
        if (consumer.isObjectPropertyOnly(property)
            || isClassExpressionObject(mainNode, Vocabulary.OWL_SOME_VALUES_FROM.getIRI()) 
            || isClassExpressionObject(mainNode, Vocabulary.OWL_ALL_VALUES_FROM.getIRI())
            || isClassExpressionObject(mainNode, Vocabulary.OWL_MAX_QUALIFIED_CARDINALITY.getIRI())
            || isClassExpressionObject(mainNode, Vocabulary.OWL_MAX_QUALIFIED_CARDINALITY.getIRI()) 
            || isClassExpressionObject(mainNode, Vocabulary.OWL_QUALIFIED_CARDINALITY.getIRI())
            || consumer.getResourceObject(mainNode, Vocabulary.OWL_HAS_VALUE.getIRI(),false)!=null) return true;
        return false;
    }
    protected boolean isClassExpressionObject(String mainNode, String predicate) {
        String object=consumer.getResourceObject(mainNode, predicate, false);
        return object!=null && consumer.isClass(object);
    }
    public boolean isDataRestriction(String mainNode,String property) {
        if (consumer.isDataPropertyOnly(property)
            || isDataRangeObject(mainNode, Vocabulary.OWL_SOME_VALUES_FROM.getIRI())
            || isDataRangeObject(mainNode, Vocabulary.OWL_ALL_VALUES_FROM.getIRI())
            || isDataRangeObject(mainNode, Vocabulary.OWL_MIN_QUALIFIED_CARDINALITY.getIRI())
            || isDataRangeObject(mainNode, Vocabulary.OWL_MAX_QUALIFIED_CARDINALITY.getIRI())
            || isDataRangeObject(mainNode, Vocabulary.OWL_QUALIFIED_CARDINALITY.getIRI())
            || consumer.getLiteralObject(mainNode, Vocabulary.OWL_HAS_VALUE.getIRI(), false)!=null) return true;
        return false;
    }
    protected boolean isDataRangeObject(String mainNode, String predicate) {
        String object=consumer.getResourceObject(mainNode, predicate, false);
        return object!=null && consumer.isDataRange(object);
    }
    /**
     * Gets a translator for a class expression. The selector ensures that the necessary triples are present.
     * @param mainNode The main node of the class expression
     * @return The translator that should be used to translate the class expression
     */
    public ClassExpressionTranslator getClassExpressionTranslator(String mainNode) {
        if (consumer.isRestriction(mainNode)) {
            // Check that the necessary triples are there
            String onPropertyIRI=consumer.getResourceObject(mainNode, Vocabulary.OWL_ON_PROPERTY.getIRI(), false);
            if (isObjectRestriction(mainNode, onPropertyIRI)) return getObjectRestrictionTranslator(mainNode);
            if (isDataRestriction(mainNode, onPropertyIRI)) return getDataRestrictionTranslator(mainNode);
            throw new IllegalArgumentException("A restriction did not have an object or a data property. Main node: "+mainNode);
        }
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_INTERSECTION_OF.getIRI())) return intersectionOfTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_UNION_OF.getIRI())) return unionOfTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_COMPLEMENT_OF.getIRI())) return complementOfTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_ONE_OF.getIRI())) return oneOfTranslator;
        return namedClassTranslator;
    }

    protected ClassExpressionTranslator getObjectRestrictionTranslator(String mainNode) {
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_SOME_VALUES_FROM.getIRI())) return objectSomeValuesFromTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_ALL_VALUES_FROM.getIRI())) return objectAllValuesFromTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_HAS_VALUE.getIRI())) return objectHasValueTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_MIN_CARDINALITY.getIRI()) || consumer.hasPredicate(mainNode, Vocabulary.OWL_MIN_QUALIFIED_CARDINALITY.getIRI())) return objectMinCardinalityTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_CARDINALITY.getIRI()) || consumer.hasPredicate(mainNode, Vocabulary.OWL_QUALIFIED_CARDINALITY.getIRI())) return objectCardinalityTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_MAX_CARDINALITY.getIRI()) || consumer.hasPredicate(mainNode, Vocabulary.OWL_MAX_QUALIFIED_CARDINALITY.getIRI())) return objectMaxCardinalityTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_HAS_SELF.getIRI())) return selfRestrictionTranslator;
        return namedClassTranslator;
    }

    protected ClassExpressionTranslator getDataRestrictionTranslator(String mainNode) {
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_SOME_VALUES_FROM.getIRI())) return dataSomeValuesFromTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_ALL_VALUES_FROM.getIRI())) return dataAllValuesFromTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_HAS_VALUE.getIRI())) return dataHasValueTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_MIN_CARDINALITY.getIRI()) || consumer.hasPredicate(mainNode, Vocabulary.OWL_MIN_QUALIFIED_CARDINALITY.getIRI())) return dataMinCardinalityTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_CARDINALITY.getIRI()) || consumer.hasPredicate(mainNode, Vocabulary.OWL_QUALIFIED_CARDINALITY.getIRI())) return dataCardinalityTranslator;
        if (consumer.hasPredicate(mainNode, Vocabulary.OWL_MAX_CARDINALITY.getIRI()) || consumer.hasPredicate(mainNode, Vocabulary.OWL_MAX_QUALIFIED_CARDINALITY.getIRI())) return dataMaxCardinalityTranslator;
        return namedClassTranslator;
    }
}
