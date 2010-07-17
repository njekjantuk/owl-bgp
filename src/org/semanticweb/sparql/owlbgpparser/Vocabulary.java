package org.semanticweb.sparql.owlbgpparser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Prefixes;

public enum Vocabulary {
    OWL_THING(Prefixes.s_semanticWebPrefixes.get("owl"), "Thing"),
    OWL_NOTHING(Prefixes.s_semanticWebPrefixes.get("owl"), "Nothing"),
    OWL_CLASS(Prefixes.s_semanticWebPrefixes.get("owl"), "Class"),
    OWL_ONTOLOGY(Prefixes.s_semanticWebPrefixes.get("owl"), "Ontology"),
    OWL_IMPORTS(Prefixes.s_semanticWebPrefixes.get("owl"), "imports"),
    OWL_VERSION_IRI(Prefixes.s_semanticWebPrefixes.get("owl"), "versionIRI"),
    OWL_VERSION_INFO(Prefixes.s_semanticWebPrefixes.get("owl"), "versionInfo"),
    OWL_EQUIVALENT_CLASS(Prefixes.s_semanticWebPrefixes.get("owl"), "equivalentClass"),
    OWL_OBJECT_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "ObjectProperty"),
    OWL_DATA_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "DatatypeProperty"),
    OWL_FUNCTIONAL_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "FunctionalProperty"),
    OWL_INVERSE_FUNCTIONAL_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "InverseFunctionalProperty"),
    OWL_ASYMMETRIC_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "AsymmetricProperty"),
    OWL_SYMMETRIC_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "SymmetricProperty"),
    OWL_RESTRICTION(Prefixes.s_semanticWebPrefixes.get("owl"), "Restriction"),
    OWL_ON_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "onProperty"),
    OWL_INTERSECTION_OF(Prefixes.s_semanticWebPrefixes.get("owl"), "intersectionOf"),
    OWL_UNION_OF(Prefixes.s_semanticWebPrefixes.get("owl"), "unionOf"),
    OWL_ALL_VALUES_FROM(Prefixes.s_semanticWebPrefixes.get("owl"), "allValuesFrom"),
    OWL_SOME_VALUES_FROM(Prefixes.s_semanticWebPrefixes.get("owl"), "someValuesFrom"),
    OWL_HAS_VALUE(Prefixes.s_semanticWebPrefixes.get("owl"), "hasValue"),
    OWL_DISJOINT_WITH(Prefixes.s_semanticWebPrefixes.get("owl"), "disjointWith"),
    OWL_ONE_OF(Prefixes.s_semanticWebPrefixes.get("owl"), "oneOf"),
    OWL_HAS_SELF(Prefixes.s_semanticWebPrefixes.get("owl"), "hasSelf"),
    OWL_DISJOINT_UNION_OF(Prefixes.s_semanticWebPrefixes.get("owl"), "disjointUnionOf"),
    OWL_MIN_CARDINALITY(Prefixes.s_semanticWebPrefixes.get("owl"), "minCardinality"),
    OWL_MIN_QUALIFIED_CARDINALITY(Prefixes.s_semanticWebPrefixes.get("owl"),  "minQualifiedCardinality"),
    OWL_CARDINALITY(Prefixes.s_semanticWebPrefixes.get("owl"), "cardinality"),
    OWL_QUALIFIED_CARDINALITY(Prefixes.s_semanticWebPrefixes.get("owl"), "qualifiedCardinality"),
    OWL_ANNOTATION_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "AnnotationProperty"),
    OWL_ANNOTATION(Prefixes.s_semanticWebPrefixes.get("owl"), "Annotation"),
    OWL_INDIVIDUAL(Prefixes.s_semanticWebPrefixes.get("owl"), "Individual"),
    OWL_LITERAL(Prefixes.s_semanticWebPrefixes.get("owl"), "Literal"),
    OWL_NAMED_INDIVIDUAL(Prefixes.s_semanticWebPrefixes.get("owl"), "NamedIndividual"),
    OWL_DATATYPE(Prefixes.s_semanticWebPrefixes.get("owl"), "Datatype"),
    RDFS_SUBCLASS_OF(Prefixes.s_semanticWebPrefixes.get("rdfs"), "subClassOf"),
    RDFS_SUB_PROPERTY_OF(Prefixes.s_semanticWebPrefixes.get("rdfs"), "subPropertyOf"),
    RDF_TYPE(Prefixes.s_semanticWebPrefixes.get("rdf"), "type"),
    RDF_NIL(Prefixes.s_semanticWebPrefixes.get("rdf"), "nil"),
    RDF_REST(Prefixes.s_semanticWebPrefixes.get("rdf"), "rest"),
    RDF_FIRST(Prefixes.s_semanticWebPrefixes.get("rdf"), "first"),
    RDF_LIST(Prefixes.s_semanticWebPrefixes.get("rdf"), "List"),
    OWL_MAX_CARDINALITY(Prefixes.s_semanticWebPrefixes.get("owl"), "maxCardinality"),
    OWL_MAX_QUALIFIED_CARDINALITY(Prefixes.s_semanticWebPrefixes.get("owl"), "maxQualifiedCardinality"),
    OWL_NEGATIVE_PROPERTY_ASSERTION(Prefixes.s_semanticWebPrefixes.get("owl"), "NegativePropertyAssertion"),
    RDFS_LABEL(Prefixes.s_semanticWebPrefixes.get("rdfs"), "label"),
    RDFS_COMMENT(Prefixes.s_semanticWebPrefixes.get("rdfs"), "comment"),
    RDFS_SEE_ALSO(Prefixes.s_semanticWebPrefixes.get("rdfs"), "seeAlso"),
    RDFS_IS_DEFINED_BY(Prefixes.s_semanticWebPrefixes.get("rdfs"), "isDefinedBy"),
    RDFS_RESOURCE(Prefixes.s_semanticWebPrefixes.get("rdfs"), "Resource"),
    RDFS_LITERAL(Prefixes.s_semanticWebPrefixes.get("rdfs"), "Literal"),
    RDF_PLAIN_LITERAL(Prefixes.s_semanticWebPrefixes.get("rdf"), "PlainLiteral"),
    RDFS_DATATYPE(Prefixes.s_semanticWebPrefixes.get("rdfs"), "Datatype"),
    OWL_TRANSITIVE_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "TransitiveProperty"),
    OWL_REFLEXIVE_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "ReflexiveProperty"),
    OWL_IRREFLEXIVE_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "IrreflexiveProperty"),
    OWL_INVERSE_OF(Prefixes.s_semanticWebPrefixes.get("owl"), "inverseOf"),
    OWL_COMPLEMENT_OF(Prefixes.s_semanticWebPrefixes.get("owl"), "complementOf"),
    OWL_DATATYPE_COMPLEMENT_OF(Prefixes.s_semanticWebPrefixes.get("owl"), "datatypeComplementOf"),
    OWL_ALL_DIFFERENT(Prefixes.s_semanticWebPrefixes.get("owl"), "AllDifferent"),
    OWL_DISTINCT_MEMBERS(Prefixes.s_semanticWebPrefixes.get("owl"), "distinctMembers"),
    OWL_SAME_AS(Prefixes.s_semanticWebPrefixes.get("owl"), "sameAs"),
    OWL_DIFFERENT_FROM(Prefixes.s_semanticWebPrefixes.get("owl"), "differentFrom"),
    OWL_DEPRECATED_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "DeprecatedProperty"),
    OWL_EQUIVALENT_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "equivalentProperty"),
    OWL_DEPRECATED_CLASS(Prefixes.s_semanticWebPrefixes.get("owl"), "DeprecatedClass"),
    OWL_DATA_RANGE(Prefixes.s_semanticWebPrefixes.get("owl"), "DataRange"),
    RDFS_DOMAIN(Prefixes.s_semanticWebPrefixes.get("rdfs"), "domain"),
    RDFS_RANGE(Prefixes.s_semanticWebPrefixes.get("rdfs"), "range"),
    RDFS_CLASS(Prefixes.s_semanticWebPrefixes.get("rdfs"), "Class"),
    RDF_PROPERTY(Prefixes.s_semanticWebPrefixes.get("rdf"), "Property"),
    RDF_DESCRIPTION(Prefixes.s_semanticWebPrefixes.get("rdf"), "Description"),
    RDF_XML_LITERAL(Prefixes.s_semanticWebPrefixes.get("rdf"), "XMLLiteral"),
    OWL_PRIOR_VERSION(Prefixes.s_semanticWebPrefixes.get("owl"), "priorVersion"),
    OWL_DEPRECATED(Prefixes.s_semanticWebPrefixes.get("owl"), "deprecated"),
    OWL_BACKWARD_COMPATIBLE_WITH(Prefixes.s_semanticWebPrefixes.get("owl"), "backwardCompatibleWith"),
    OWL_INCOMPATIBLE_WITH(Prefixes.s_semanticWebPrefixes.get("owl"), "incompatibleWith"),
    OWL_OBJECT_PROPERTY_DOMAIN(Prefixes.s_semanticWebPrefixes.get("owl"), "objectPropertyDomain"),
    OWL_DATA_PROPERTY_DOMAIN(Prefixes.s_semanticWebPrefixes.get("owl"), "dataPropertyDomain"),
    OWL_DATA_PROPERTY_RANGE(Prefixes.s_semanticWebPrefixes.get("owl"), "dataPropertyRange"),
    OWL_OBJECT_PROPERTY_RANGE(Prefixes.s_semanticWebPrefixes.get("owl"), "objectPropertyRange"),
    OWL_SUB_OBJECT_PROPERTY_OF(Prefixes.s_semanticWebPrefixes.get("owl"), "subObjectPropertyOf"),
    OWL_SUB_DATA_PROPERTY_OF(Prefixes.s_semanticWebPrefixes.get("owl"), "subDataPropertyOf"),
    OWL_DISJOINT_DATA_PROPERTIES(Prefixes.s_semanticWebPrefixes.get("owl"), "disjointDataProperties"),
    OWL_DISJOINT_OBJECT_PROPERTIES(Prefixes.s_semanticWebPrefixes.get("owl"), "disjointObjectProperties"),
    OWL_PROPERTY_DISJOINT_WITH(Prefixes.s_semanticWebPrefixes.get("owl"), "propertyDisjointWith"),
    OWL_EQUIVALENT_DATA_PROPERTIES(Prefixes.s_semanticWebPrefixes.get("owl"), "equivalentDataProperty"),
    OWL_EQUIVALENT_OBJECT_PROPERTIES(Prefixes.s_semanticWebPrefixes.get("owl"), "equivalentObjectProperty"),
    OWL_FUNCTIONAL_DATA_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "FunctionalDataProperty"),
    OWL_FUNCTIONAL_OBJECT_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "FunctionalObjectProperty"),
    OWL_ON_CLASS(Prefixes.s_semanticWebPrefixes.get("owl"), "onClass"),
    OWL_ON_DATA_RANGE(Prefixes.s_semanticWebPrefixes.get("owl"), "onDataRange"),
    OWL_ON_DATA_TYPE(Prefixes.s_semanticWebPrefixes.get("owl"), "onDatatype"),
    OWL_WITH_RESTRICTIONS(Prefixes.s_semanticWebPrefixes.get("owl"), "withRestrictions"),
    OWL_INVERSE_OBJECT_PROPERTY_EXPRESSION(Prefixes.s_semanticWebPrefixes.get("owl"), "inverseObjectPropertyExpression"),
    OWL_AXIOM(Prefixes.s_semanticWebPrefixes.get("owl"), "Axiom"),
    OWL_PROPERTY_CHAIN_AXIOM(Prefixes.s_semanticWebPrefixes.get("owl"), "propertyChainAxiom"),
    OWL_ALL_DISJOINT_CLASSES(Prefixes.s_semanticWebPrefixes.get("owl"), "AllDisjointClasses"),
    OWL_MEMBERS(Prefixes.s_semanticWebPrefixes.get("owl"), "members"),
    OWL_ALL_DISJOINT_PROPERTIES(Prefixes.s_semanticWebPrefixes.get("owl"), "AllDisjointProperties"),
    OWL_TOP_OBJECT_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "topObjectProperty"),
    OWL_BOTTOM_OBJECT_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "bottomObjectProperty"),
    OWL_TOP_DATA_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "topDataProperty"),
    OWL_BOTTOM_DATA_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "bottomDataProperty"),
    OWL_HAS_KEY(Prefixes.s_semanticWebPrefixes.get("owl"), "hasKey"),
    OWL_ANNOTATED_SOURCE(Prefixes.s_semanticWebPrefixes.get("owl"), "annotatedSource"),
    OWL_ANNOTATED_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "annotatedProperty"),
    OWL_ANNOTATED_TARGET(Prefixes.s_semanticWebPrefixes.get("owl"), "annotatedTarget"),
    OWL_SOURCE_INDIVIDUAL(Prefixes.s_semanticWebPrefixes.get("owl"), "sourceIndividual"),
    OWL_ASSERTION_PROPERTY(Prefixes.s_semanticWebPrefixes.get("owl"), "assertionProperty"),
    OWL_TARGET_INDIVIDUAL(Prefixes.s_semanticWebPrefixes.get("owl"), "targetIndividual"),
    OWL_TARGET_VALUE(Prefixes.s_semanticWebPrefixes.get("owl"), "targetValue");

    String namespace;
    String shortName;

    Vocabulary(String namespace, String shortName) {
        this.namespace=namespace.intern();
        this.shortName=shortName.intern();
    }
    public IRI getIRI() {
        return IRI.create(namespace.toString()+shortName);
    }
    public String getNamespace() {
        return namespace;
    }
    public String getShortName() {
        return shortName;
    }
    public String toString() {
        return getIRI().toString();
    }
    public static final Set<IRI> BUILT_IN_VOCABULARY_IRIS;
    static {
        BUILT_IN_VOCABULARY_IRIS = new HashSet<IRI>();
        for (Vocabulary v : Vocabulary.values()) {
            BUILT_IN_VOCABULARY_IRIS.add(v.getIRI());
        }
    }
    public static final Set<IRI> BUILT_IN_ANNOTATION_PROPERTY_IRIS;
    static {
        BUILT_IN_ANNOTATION_PROPERTY_IRIS=new HashSet<IRI>();
        BUILT_IN_ANNOTATION_PROPERTY_IRIS.add(RDFS_LABEL.getIRI());
        BUILT_IN_ANNOTATION_PROPERTY_IRIS.add(RDFS_COMMENT.getIRI());
        BUILT_IN_ANNOTATION_PROPERTY_IRIS.add(OWL_VERSION_INFO.getIRI());
        BUILT_IN_ANNOTATION_PROPERTY_IRIS.add(OWL_BACKWARD_COMPATIBLE_WITH.getIRI());
        BUILT_IN_ANNOTATION_PROPERTY_IRIS.add(OWL_PRIOR_VERSION.getIRI());
        BUILT_IN_ANNOTATION_PROPERTY_IRIS.add(RDFS_SEE_ALSO.getIRI());
        BUILT_IN_ANNOTATION_PROPERTY_IRIS.add(RDFS_IS_DEFINED_BY.getIRI());
        BUILT_IN_ANNOTATION_PROPERTY_IRIS.add(OWL_INCOMPATIBLE_WITH.getIRI());
        BUILT_IN_ANNOTATION_PROPERTY_IRIS.add(OWL_DEPRECATED.getIRI());
    }
}