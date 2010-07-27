package org.semanticweb.sparql.owlbgp.parser;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.AbstractExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.AnnotationValue;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Import;
import org.semanticweb.sparql.owlbgp.model.Ontology;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataIntersectionOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataOneOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataUnionOf;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeRestriction;
import org.semanticweb.sparql.owlbgp.model.dataranges.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.model.dataranges.FacetRestriction;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype.OWL2_DATATYPES;
import org.semanticweb.sparql.owlbgp.model.individuals.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.literals.Literal;
import org.semanticweb.sparql.owlbgp.model.literals.LiteralVariable;
import org.semanticweb.sparql.owlbgp.model.literals.TypedLiteral;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.DataProperty;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectInverseOf;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyVariable;
import org.semanticweb.sparql.owlbgp.parser.translators.ClassExpressionListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.ClassExpressionTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.ClassExpressionTranslatorSelector;
import org.semanticweb.sparql.owlbgp.parser.translators.DataPropertyListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.DataRangeListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.FacetRestrictionListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.IndividualListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.ObjectPropertyListItemTranslator;
import org.semanticweb.sparql.owlbgp.parser.translators.OptimisedListTranslator;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractLiteralTripleHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.AbstractResourceTripleHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TriplePredicateHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPAllValuesFromHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPAnnotatedPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPAnnotatedSourceHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPAnnotatedTargetHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPAnnotatedTargetLiteralHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPComplementOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDataPropertDomainHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDataPropertyRangeHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDifferentFromHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDisjointDataPropertiesHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDisjointObjectPropertiesHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDisjointUnionHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDisjointWithHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPDistinctMembersHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPEquivalentClassHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPEquivalentDataPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPEquivalentObjectPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPEquivalentPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPFirstLiteralHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPFirstResourceHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPHasKeyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPImportsHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPIntersectionOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPInverseOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPObjectPropertyDomainHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPObjectPropertyRangeHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPOnPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPOneOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPPropertyChainAxiomHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPPropertyDisjointWithHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPPropertyDomainHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPPropertyRangeHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPRestHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPSameAsHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPSomeValuesFromHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPSubClassOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPSubDataPropertyOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPSubObjectPropertyOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPSubPropertyOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPTypeHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPUnionOfHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate.TPVersionIRIHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.general.GTPAnnotationLiteralHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.general.GTPAnnotationResourceTripleHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.general.GTPDataPropertyAssertionHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.general.GTPObjectPropertyAssertionHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.BuiltInTypeHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeAllDifferentHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeAllDisjointClassesHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeAllDisjointPropertiesHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeAnnotationHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeAnnotationPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeAsymmetricPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeAxiomHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeClassHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeDataPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeDataRangeHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeDatatypeHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeFunctionalDataPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeFunctionalObjectPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeFunctionalPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeInverseFunctionalPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeIrreflexivePropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeListHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeNamedIndividualHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeNegativePropertyAssertionHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeObjectPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeOntologyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeReflexivePropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeRestrictionHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeSymmetricPropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypeTransitivePropertyHandler;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.rdftype.TypedConstantListItemTranslator;
import org.xml.sax.SAXException;

public class TripleConsumer {
    protected Set<Identifier> classIRIs=new HashSet<Identifier>();
    protected Set<Identifier> objectPropertyIRIs=new HashSet<Identifier>();
    protected Set<Identifier> dataPropertyIRIs=new HashSet<Identifier>();
    protected Set<Identifier> annotationPropertyIRIs=new HashSet<Identifier>();
    protected Set<Identifier> customDatatypes=new HashSet<Identifier>();
    protected Set<Identifier> dataRangeIRIs=new HashSet<Identifier>();
    protected Set<Identifier> individualIRIs=new HashSet<Identifier>();
//    protected Set<Identifier> propertyIRIs=new HashSet<Identifier>(); // things neither typed as a data or object property - bad!
    protected Set<Identifier> ontologyPropertyIRIs=new HashSet<Identifier>();
//    protected Set<Identifier> rdfType=new HashSet<Identifier>();
    protected Set<Identifier> restrictionIRIs=new HashSet<Identifier>(); // IRIs that had a type triple to owl:Restriction
//    protected Set<Identifier> axiomIRIs=new HashSet<Identifier>();
    
    protected Identifier      firstOntologyIRI; // The IRI of the first resource that is typed as an ontology
    protected Set<Identifier> ontologyIRIs=new HashSet<Identifier>(); // IRIs that had a type triple to owl:Ontology
    protected Identifier      ontologyIRI;
    protected Set<Identifier> versionIRIs=new HashSet<Identifier>();
    protected Identifier      xmlBase;
    protected Ontology        ontology;
    
//    protected Set<Identifier> annotationIRIs=new HashSet<Identifier>();
//    protected Set<Annotation> annotations=new HashSet<Annotation>();
    
    protected Map<Identifier, Set<Identifier>> annoSource2annoMainNode=new HashMap<Identifier, Set<Identifier>>();
    protected Map<Identifier, Set<Identifier>> annoProp2annoMainNode=new HashMap<Identifier, Set<Identifier>>();
    protected Map<Identifier, Set<Identifier>> annoTarget2annoMainNode=new HashMap<Identifier, Set<Identifier>>();
    
    protected Map<Identifier, Annotation> annotationIRI2Annotation=new HashMap<Identifier, Annotation>();
//    protected Map<Identifier, Set<Annotation>> annotationsBySubject=new HashMap<Identifier, Set<Annotation>>();
//    protected Set<Annotation> pendingAnnotations=new HashSet<Annotation>();
//    protected Map<Identifier, Set<Identifier>> annotatedAnonSource2AnnotationMap=new HashMap<Identifier, Set<Identifier>>();
    protected Set<Annotation> ontologyAnnotations=new HashSet<Annotation>();
    
    protected Map<Identifier,Identifier> listRestTripleMap=new HashMap<Identifier, Identifier>(); // Maps rdf:next triple subjects to objects
    protected Map<Identifier,Identifier> listFirstResourceTripleMap=new HashMap<Identifier, Identifier>();
    protected Map<Identifier,Literal> listFirstLiteralTripleMap=new HashMap<Identifier, Literal>();
    protected OptimisedListTranslator<ClassExpression> classExpressionListTranslator; // A translator for lists of class expressions (such lists are used in intersections, unions etc.)
    protected OptimisedListTranslator<Individual> individualListTranslator; // A translator for individual lists (such lists are used in object oneOf constructs)
    protected OptimisedListTranslator<ObjectPropertyExpression> objectPropertyListTranslator;
    protected OptimisedListTranslator<Literal> constantListTranslator;
    protected OptimisedListTranslator<DataPropertyExpression> dataPropertyListTranslator;
    protected OptimisedListTranslator<DataRange> dataRangeListTranslator;
    protected OptimisedListTranslator<FacetRestriction> faceRestrictionListTranslator;

    protected Map<Identifier,BuiltInTypeHandler> builtInTypeTripleHandlers=new HashMap<Identifier, BuiltInTypeHandler>();
    // Handler for triples that denote nodes which represent axioms.
    // i.e., AllDisjointClasses, AllDisjointProperties, AllDifferent, NegativePropertyAssertion, Axiom
    // These need to be handled separately from other types, because the base triples for annotated
    // axioms should be in the ontology before annotations on the annotated versions of these axioms are parsed.
    protected Map<Identifier,BuiltInTypeHandler> axiomTypeTripleHandlers=new HashMap<Identifier,BuiltInTypeHandler>();
    // Handlers for build in predicates
    protected Map<Identifier, TriplePredicateHandler> predicateHandlers=new HashMap<Identifier, TriplePredicateHandler>();
    // Handlers for general literal triples (i.e. triples which
    // have predicates that are not part of the built in OWL/RDFS/RDF
    // vocabulary.  Such triples either constitute annotationIRIs of
    // relationships between an individual and a data literal (typed or
    // untyped)
    protected List<AbstractLiteralTripleHandler> literalTripleHandlers=new ArrayList<AbstractLiteralTripleHandler>();
    // Handlers for general resource triples (i.e. triples which
    // have predicates that are not part of the built in OWL/RDFS/RDF
    // vocabulary.  Such triples either constitute annotationIRIs or
    // relationships between an individual and another individual.
    protected List<AbstractResourceTripleHandler> resourceTripleHandlers=new ArrayList<AbstractResourceTripleHandler>();
    
    // Subject, predicate, object
    protected Map<Identifier, Map<Identifier, Set<Identifier>>> resTriplesBySubject=new HashMap<Identifier, Map<Identifier,Set<Identifier>>>();
    // Predicate, subject, object
    protected Map<Identifier, Map<Identifier, Identifier>> singleValuedResTriplesByPredicate=new HashMap<Identifier, Map<Identifier,Identifier>>();
    // Literal triples
    protected Map<Identifier, Map<Identifier, Set<Literal>>> litTriplesBySubject=new HashMap<Identifier, Map<Identifier,Set<Literal>>>();
    // Predicate, subject, object
    protected Map<Identifier, Map<Identifier, Literal>> singleValuedLitTriplesByPredicate=new HashMap<Identifier, Map<Identifier,Literal>>();

//    protected Axiom lastAddedAxiom;
    protected Set<Axiom> axioms=new HashSet<Axiom>();
    protected Set<Import> imports=new HashSet<Import>();
    
    protected Map<Identifier,ObjectPropertyExpression> translatedObjectPropertyExpressions=new HashMap<Identifier,ObjectPropertyExpression>();
    protected ClassExpressionTranslatorSelector classExpressionTranslatorSelector;
    protected Map<Identifier,ClassExpression> translatedClassExpression=new HashMap<Identifier,ClassExpression>();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public TripleConsumer() {
        classExpressionTranslatorSelector=new ClassExpressionTranslatorSelector(this);
        for (Identifier iri : Vocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS) annotationPropertyIRIs.add(iri);
        addDublinCoreAnnotationIRIs();
        ontologyPropertyIRIs.add(Vocabulary.OWL_PRIOR_VERSION.getIRI());
        ontologyPropertyIRIs.add(Vocabulary.OWL_BACKWARD_COMPATIBLE_WITH.getIRI());
        ontologyPropertyIRIs.add(Vocabulary.OWL_INCOMPATIBLE_WITH.getIRI());
        
        classExpressionListTranslator=new OptimisedListTranslator<ClassExpression>(this, new ClassExpressionListItemTranslator(this));
        individualListTranslator=new OptimisedListTranslator<Individual>(this, new IndividualListItemTranslator(this));
        constantListTranslator=new OptimisedListTranslator<Literal>(this, new TypedConstantListItemTranslator(this));
        objectPropertyListTranslator=new OptimisedListTranslator<ObjectPropertyExpression>(this, new ObjectPropertyListItemTranslator(this));
        dataPropertyListTranslator=new OptimisedListTranslator<DataPropertyExpression>(this, new DataPropertyListItemTranslator(this));
        dataRangeListTranslator=new OptimisedListTranslator<DataRange>(this, new DataRangeListItemTranslator(this));
        faceRestrictionListTranslator=new OptimisedListTranslator<FacetRestriction>(this, new FacetRestrictionListItemTranslator(this));
        setupTypeTripleHandlers();
        setupPredicateHandlers();
        literalTripleHandlers.add(new GTPDataPropertyAssertionHandler(this));
        literalTripleHandlers.add(new TPFirstLiteralHandler(this));
        literalTripleHandlers.add(new GTPAnnotationLiteralHandler(this));
        literalTripleHandlers.add(new TPAnnotatedTargetLiteralHandler(this));
        resourceTripleHandlers.add(new GTPObjectPropertyAssertionHandler(this));
        resourceTripleHandlers.add(new GTPAnnotationResourceTripleHandler(this));
        for (OWL2_DATATYPES dt : OWL2_DATATYPES.values()) {
            dataRangeIRIs.add(dt.getDatatype().getIdentifier());
        }
        dataRangeIRIs.add(Vocabulary.RDFS_LITERAL.getIRI());        
        classIRIs.add(Vocabulary.OWL_THING.getIRI());
        classIRIs.add(Vocabulary.OWL_NOTHING.getIRI());
        objectPropertyIRIs.add(Vocabulary.OWL_TOP_OBJECT_PROPERTY.getIRI());
        objectPropertyIRIs.add(Vocabulary.OWL_BOTTOM_OBJECT_PROPERTY.getIRI());
        dataPropertyIRIs.add(Vocabulary.OWL_TOP_DATA_PROPERTY.getIRI());
        dataPropertyIRIs.add(Vocabulary.OWL_BOTTOM_DATA_PROPERTY.getIRI());
        setupSinglePredicateMaps();
    }
    
    public void setOntologyIRI(Identifier ontologyIRI) {
        this.ontologyIRI=ontologyIRI;
    }
    public void addVersionIRI(Identifier versionIRI) {
        versionIRIs.add(versionIRI);
    }
    public void setClassesInOntologySignature(Set<Identifier> classIRIs) {
        this.classIRIs.addAll(classIRIs);
    }
    public void setObjectPropertiesInOntologySignature(Set<Identifier> objectPropertieIRIs) {
        this.objectPropertyIRIs.addAll(objectPropertieIRIs);
    }
    public void setDataPropertiesInOntologySignature(Set<Identifier> dataPropertieIRIs) {
        this.dataPropertyIRIs.addAll(dataPropertieIRIs);
    }
    public void setIndividualsInOntologySignature(Set<Identifier> individualIRIs) {
        this.individualIRIs.addAll(individualIRIs);
    }
    public void setCustomDatatypesInOntologySignature(Set<Identifier> customDatatypeIRIs) {
        this.dataRangeIRIs.addAll(customDatatypeIRIs);
    }
    
    protected void addSingleValuedResPredicate(Vocabulary v) {
        Map<Identifier, Identifier> map=new HashMap<Identifier, Identifier>();
        singleValuedResTriplesByPredicate.put(v.getIRI(), map);
    }
//    protected void addrdfType(Identifier obj) {
//        rdfType.add(obj);
//    }
    protected void setupSinglePredicateMaps() {
        addSingleValuedResPredicate(Vocabulary.OWL_ON_PROPERTY);
        addSingleValuedResPredicate(Vocabulary.OWL_SOME_VALUES_FROM);
        addSingleValuedResPredicate(Vocabulary.OWL_ALL_VALUES_FROM);
        addSingleValuedResPredicate(Vocabulary.OWL_ON_CLASS);
        addSingleValuedResPredicate(Vocabulary.OWL_ON_DATA_RANGE);
        addSingleValuedResPredicate(Vocabulary.OWL_SOURCE_INDIVIDUAL);
        addSingleValuedResPredicate(Vocabulary.OWL_ASSERTION_PROPERTY);
        addSingleValuedResPredicate(Vocabulary.OWL_TARGET_INDIVIDUAL);
    }
    protected void addBuiltInTypeTripleHandler(BuiltInTypeHandler handler) {
        builtInTypeTripleHandlers.put(handler.getTypeIRI(), handler);
    }
    protected void addAxiomTypeTripleHandler(BuiltInTypeHandler handler) {
        axiomTypeTripleHandlers.put(handler.getTypeIRI(), handler);
    }
    protected void setupTypeTripleHandlers() {
        addBuiltInTypeTripleHandler(new TypeClassHandler(this));
        addBuiltInTypeTripleHandler(new TypeDataPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeDatatypeHandler(this));
        addBuiltInTypeTripleHandler(new TypeAsymmetricPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeFunctionalDataPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeFunctionalObjectPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeFunctionalPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeInverseFunctionalPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeIrreflexivePropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeObjectPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeReflexivePropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeSymmetricPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeTransitivePropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeRestrictionHandler(this));
        addBuiltInTypeTripleHandler(new TypeListHandler(this));
        
        addBuiltInTypeTripleHandler(new TypeOntologyHandler(this));
        addBuiltInTypeTripleHandler(new TypeAnnotationPropertyHandler(this));
//        addBuiltInTypeTripleHandler(new TypeDeprecatedClassHandler(this));
//        addBuiltInTypeTripleHandler(new TypeDeprecatedPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeDataRangeHandler(this));
        addBuiltInTypeTripleHandler(new TypeNamedIndividualHandler(this));
        addBuiltInTypeTripleHandler(new TypeAnnotationHandler(this));
        
        
        addAxiomTypeTripleHandler(new TypeAxiomHandler(this));
        addAxiomTypeTripleHandler(new TypeAnnotationHandler(this));
        addAxiomTypeTripleHandler(new TypeAllDisjointClassesHandler(this));
        addAxiomTypeTripleHandler(new TypeAllDisjointPropertiesHandler(this));
        addAxiomTypeTripleHandler(new TypeAllDifferentHandler(this));
        addAxiomTypeTripleHandler(new TypeNegativePropertyAssertionHandler(this));
    }
    protected void addPredicateHandler(TriplePredicateHandler predicateHandler) {
        predicateHandlers.put(predicateHandler.getPredicateIRI(), predicateHandler);
    }
    protected void setupPredicateHandlers() {
        addPredicateHandler(new TPDataPropertDomainHandler(this));
        addPredicateHandler(new TPDataPropertyRangeHandler(this));
        addPredicateHandler(new TPDisjointDataPropertiesHandler(this));
        addPredicateHandler(new TPDisjointObjectPropertiesHandler(this));
        addPredicateHandler(new TPDisjointUnionHandler(this));
        addPredicateHandler(new TPDisjointWithHandler(this));
        addPredicateHandler(new TPEquivalentClassHandler(this));
        addPredicateHandler(new TPEquivalentDataPropertyHandler(this));
        addPredicateHandler(new TPEquivalentObjectPropertyHandler(this));
        addPredicateHandler(new TPEquivalentPropertyHandler(this));
        addPredicateHandler(new TPObjectPropertyDomainHandler(this));
        addPredicateHandler(new TPObjectPropertyRangeHandler(this));
        addPredicateHandler(new TPPropertyDomainHandler(this));
        addPredicateHandler(new TPPropertyRangeHandler(this));
        addPredicateHandler(new TPSameAsHandler(this));
        addPredicateHandler(new TPDifferentFromHandler(this));
        addPredicateHandler(new TPSubClassOfHandler(this));
        addPredicateHandler(new TPSubDataPropertyOfHandler(this));
        addPredicateHandler(new TPSubObjectPropertyOfHandler(this));
        addPredicateHandler(new TPSubPropertyOfHandler(this));
        addPredicateHandler(new TPTypeHandler(this));
        addPredicateHandler(new TPInverseOfHandler(this));
        addPredicateHandler(new TPDistinctMembersHandler(this));
        addPredicateHandler(new TPImportsHandler(this));
        addPredicateHandler(new TPIntersectionOfHandler(this));
        addPredicateHandler(new TPUnionOfHandler(this));
        addPredicateHandler(new TPComplementOfHandler(this));
        addPredicateHandler(new TPOneOfHandler(this));
        addPredicateHandler(new TPOnPropertyHandler(this));
        addPredicateHandler(new TPSomeValuesFromHandler(this));
        addPredicateHandler(new TPAllValuesFromHandler(this));
        addPredicateHandler(new TPRestHandler(this));
        addPredicateHandler(new TPFirstResourceHandler(this));
        addPredicateHandler(new TPHasKeyHandler(this));
        addPredicateHandler(new TPVersionIRIHandler(this));
        addPredicateHandler(new TPPropertyChainAxiomHandler(this));
        addPredicateHandler(new TPPropertyDisjointWithHandler(this));
        addPredicateHandler(new TPAnnotatedSourceHandler(this));
        addPredicateHandler(new TPAnnotatedPropertyHandler(this));
        addPredicateHandler(new TPAnnotatedTargetHandler(this));
        //addPredicateHandler(new TPAssertionPropertyHandler(this));
    }
    protected void addDublinCoreAnnotationIRIs() {
        String[] iris=new String[] {"contributor", "coverage", "creator", "date", "description", "format", "identifier", "language", "publisher", "relation", "rights", "source", "subject", "title", "type"};
        for (String iri : iris)
            annotationPropertyIRIs.add(IRI.create("http://purl.org/dc/elements/1.1/"+iri));
    }
    
    // We cache IRIs to save memory!!
    protected Map<Identifier, Identifier> IRIMap=new HashMap<Identifier, Identifier>();
    int currentBaseCount=0;

    public boolean isAnonymous(Identifier iri) {
        return (iri instanceof AnonymousIndividual);
    }
    public boolean isVariable(Identifier iri) {
        return (iri instanceof Variable);
    }
    public boolean isVariableLiteral(Literal literal) {
        return (literal instanceof Variable);
    }
    public void addAxiom(Axiom axiom) {
        axioms.add(axiom);
//        lastAddedAxiom=axiom;
    }
    public void addImport(Import imported) {
        imports.add(imported);
    }
    public void removeAxiom(Axiom axiom) {
        axioms.remove(axiom);
    }
    public Set<Axiom> getParsedAxioms() {
        return axioms;
    }
    public Ontology getParsedOntology() {
        return ontology;
    }
//    public Axiom getLastAddedAxiom() {
//        return lastAddedAxiom;
//    }
//    public Set<Annotation> getPendingAnnotations() {
//        if (!pendingAnnotations.isEmpty()) {
//            Set<Annotation> annos=new HashSet<Annotation>(pendingAnnotations);
//            pendingAnnotations.clear();
//            return annos;
//        } else return new HashSet<Annotation>();
//    }
//    public void setPendingAnnotations(Set<Annotation> annotations) {
//        pendingAnnotations.clear();
//        this.annotations.addAll(annotations);
//        pendingAnnotations.addAll(annotations);
//    }
    public void addClass(Identifier iri) {
        if (!isAnonymous(iri)) classIRIs.add(iri);
    }
    public void addObjectProperty(Identifier iri) {
        objectPropertyIRIs.add(iri);
    }
    public void addDataProperty(Identifier iri) {
        dataPropertyIRIs.add(iri);
    }
    public void addIndividual(Identifier iri) {
        individualIRIs.add(iri);
    }
//    public void addLiteral(Literal iri) {
//        if (isVariableLiteral(iri)) literalVariables.add(iri);
//    }
    public void addDataRange(Identifier iri) {
//        if (isVariable(iri)) {
//            datatypeVariables.add(iri);
//        } else {
            dataRangeIRIs.add(iri);
            if (!isAnonymous(iri)) {
                Datatype dt=Datatype.create((IRI)iri);
                if (!dt.isOWL2Datatype()) customDatatypes.add(iri);
            }
//        }
    }
    public void addOntologyAnnotation(Annotation annotation) {
        ontologyAnnotations.add(annotation);
    }
    public void addAnnotationProperty(Identifier iri) {
        annotationPropertyIRIs.add(iri);
    }
//    public void addAnnotationIRI(Identifier iri) {
//        annotationIRIs.add(iri);
//    }
//    public boolean isAnnotation(Identifier iri) {
//        return annotationIRIs.contains(iri);
//    }
    public void addRestriction(Identifier iri) {
        restrictionIRIs.add(iri);
    }
    public boolean isRestriction(Identifier iri) {
        return restrictionIRIs.contains(iri);
    }
//    public void addOnProperty(Identifier iri) {
//        propertyIRIs.add(iri); // owl:onProperty
//    }
//    public void addAxiomIRI(Identifier axiomIRI) {
//        axiomIRIs.add(axiomIRI);
//    }
    public boolean isAxiom(Identifier iri) {
        return axioms.contains(iri);
    }
//    public boolean isOnProperty(Identifier iri) {
//        return propertyIRIs.contains(iri);
//    }

    public boolean isClass(Identifier iri) {
        return classIRIs.contains(iri);
    }
    public boolean isObjectPropertyOnly(Identifier iri) {
        if (iri==null) return false;
        if (dataPropertyIRIs.contains(iri)) return false;
        if (objectPropertyIRIs.contains(iri)) return true;
        else return false;
    }
    public boolean isDataPropertyOnly(Identifier iri) {
        if (objectPropertyIRIs.contains(iri)) return false;
        if (dataPropertyIRIs.contains(iri)) return true;
        else return false;
    }
    public boolean isOntologyProperty(Identifier iri) {
        return ontologyPropertyIRIs.contains(iri);
    }
    public boolean isAnnotationProperty(Identifier iri) {
        return annotationPropertyIRIs.contains(iri);
    }
    public boolean isIndividual(Identifier iri) {
        return individualIRIs.contains(iri);
    }
    public boolean isOntology(Identifier iri) {
        return ontologyIRIs.contains(iri);
    }
    public Set<Identifier> getOntologyIRIs() {
        return ontologyIRIs;
    }
    public void addOntologyIRI(Identifier iri) {
        ontologyIRIs.add(iri);
        if (firstOntologyIRI==null && !isAnonymous(iri)) firstOntologyIRI=iri; 
    }
    public void addAnnoSource2annoMainNode(Identifier annoSource,Identifier annoMainNode) {
        Set<Identifier> map=annoSource2annoMainNode.get(annoSource);
        if (map==null) {
            map=new HashSet<Identifier>();
            annoSource2annoMainNode.put(annoSource, map);
        }
        map.add(annoMainNode);
    }
    public void addAnnoProp2annoMainNode(Identifier annoProp,Identifier annoMainNode) {
        Set<Identifier> map=annoProp2annoMainNode.get(annoProp);
        if (map==null) {
            map=new HashSet<Identifier>();
            annoProp2annoMainNode.put(annoProp, map);
        }
        map.add(annoMainNode);
    }
    public void addAnnoTarget2annoMainNode(Identifier annoTarget,Identifier annoMainNode) {
        Set<Identifier> map=annoTarget2annoMainNode.get(annoTarget);
        if (map==null) {
            map=new HashSet<Identifier>();
            annoTarget2annoMainNode.put(annoTarget, map);
        }
        map.add(annoMainNode);
    }
    public Set<Annotation> getAnnotations(Identifier subject,Identifier predicate, Identifier object, Identifier axiomOrAnnotation) {
        Set<Annotation> annos=new HashSet<Annotation>();
        Set<Identifier[]> consumed=new HashSet<Identifier[]>();
        boolean hadTypeAxiomOrAnnotationTriple=false;
        Set<Identifier> mainNodesSubject=annoSource2annoMainNode.get(subject);
        if (mainNodesSubject!=null) { 
            Set<Identifier> mainNodesPredicate=annoProp2annoMainNode.get(predicate);
            if (mainNodesPredicate!=null) mainNodesSubject.retainAll(mainNodesPredicate);
            else mainNodesSubject.clear();
            if (!mainNodesSubject.isEmpty()) {
                Set<Identifier> mainNodesTarget=annoTarget2annoMainNode.get(object);
                if (mainNodesTarget!=null) mainNodesSubject.retainAll(mainNodesTarget);
                else mainNodesSubject.clear();
                for (Identifier annoMainNode : mainNodesSubject) {
                    Map<Identifier,Set<Identifier>> predicatesAndObjectsForMainNode=resTriplesBySubject.get(annoMainNode);
                    if (predicatesAndObjectsForMainNode!=null) {
                        for (Identifier annoProperty : predicatesAndObjectsForMainNode.keySet()) {
                            Set<Identifier> annoValues=predicatesAndObjectsForMainNode.get(annoProperty);
                            if (annoValues!=null) {
                                for (Identifier annoValue : annoValues) {
                                    if (annoProperty==(Identifier)Vocabulary.RDF_TYPE.getIRI() && annoValue==axiomOrAnnotation) {
                                        hadTypeAxiomOrAnnotationTriple=true;
                                    } else {
                                        annos.add(Annotation.create(AnnotationProperty.create((IRI)annoProperty),(AnnotationValue)annoValue, getAnnotations(annoMainNode, annoProperty, annoValue, (Identifier)Vocabulary.OWL_ANNOTATION.getIRI())));
                                    }
                                    consumed.add(new Identifier[] {annoMainNode, annoProperty, annoValue});
                                }
                            }
                        }
                    }
                    Map<Identifier,Set<Literal>> predicatesAndLiteralsForMainNode=litTriplesBySubject.get(annoMainNode);
                    if (predicatesAndLiteralsForMainNode!=null) {
                        for (Identifier annoProperty : predicatesAndLiteralsForMainNode.keySet()) {
                            Set<Literal> annoValues=predicatesAndLiteralsForMainNode.get(annoProperty);
                            if (annoValues!=null)
                                for (Identifier annoValue : annoValues) {
                                    annos.add(Annotation.create(AnnotationProperty.create((IRI)annoProperty),(AnnotationValue)annoValue, getAnnotations(annoMainNode, annoProperty, annoValue, (Identifier)Vocabulary.OWL_ANNOTATION.getIRI())));
                                    consumed.add(new Identifier[] {annoMainNode, annoProperty, annoValue});
                                }
                       }
                   }
                }
            }
        }
        if (!annos.isEmpty()&&axiomOrAnnotation!=null&&!hadTypeAxiomOrAnnotationTriple) {
            StringBuffer buffer=new StringBuffer();
            buffer.append(AbstractExtendedOWLObject.LB);
            for (Annotation anno : annos)
                buffer.append(anno.toString()+AbstractExtendedOWLObject.LB);
            throw new IllegalArgumentException("Error: The following annotations are missing the required triple (annotationMainNode rdf:type owl:Axiom): "+buffer.toString());
        }
        for (Identifier[] consumedTriple : consumed) {
            if (consumedTriple[2] instanceof Literal)
                consumeTriple(consumedTriple[0], consumedTriple[1], (Literal)consumedTriple[2]);
            else 
                consumeTriple(consumedTriple[0], consumedTriple[1], consumedTriple[2]);
        }
        return annos;
    }
    public Set<Annotation> getAnnotations(Identifier mainNode) {
        // Are we the subject of an annotation?  If so, we need to ensure that the annotations annotate us.  This
        // will only happen if we are an annotation!
        return new HashSet<Annotation>();
//        Set<Annotation> annosOnMainNodeAnnotations=new HashSet<Annotation>();
//        Set<Identifier> annotationMainNodes=getAnnotatedSourceAnnotationMainNodes(mainNode);
//  if (!annotationMainNodes.isEmpty()) 
//      for (Identifier annotationMainNode : annotationMainNodes) 
//          annosOnMainNodeAnnotations.addAll(translateAnnotations(annotationMainNode));
//  Set<Annotation> mainNodeAnnotations=new HashSet<Annotation>();
//  Set<Identifier> predicates=getPredicatesBySubject(mainNode);
//  for (Identifier predicate : predicates) {
//      if (isAnnotationProperty(predicate)) {
//          AnnotationPropertyExpression prop;
//          if (isVariable(predicate)) prop=AnnotationPropertyVariable.create(predicate.toString());
//          else if (isAnonymous(predicate)) throw new RuntimeException("Annotation properties cannot be blank nodes, but here we have "+predicate+" as annotation property. ");
//          else prop=AnnotationProperty.create((IRI)predicate);
//          Identifier resVal = getResourceObject(mainNode, predicate, true);
//          while (resVal != null) {
//              if (isAnonymous(resVal)) {
//                  mainNodeAnnotations.add(Annotation.create(prop, (AnonymousIndividual)resVal, annosOnMainNodeAnnotations));
//              } else if (isVariable(resVal)) throw new RuntimeException("Annotation values cannot be variables, but here we have "+resVal+" as annotation value variable. ");
//              else mainNodeAnnotations.add(Annotation.create(prop, (IRI)resVal, annosOnMainNodeAnnotations));
//              resVal = getResourceObject(mainNode, predicate, true);
//          }
//          Literal litVal=getLiteralObject(mainNode, predicate, true);
//          while (litVal != null) {
//              mainNodeAnnotations.add(Annotation.create(prop, litVal, annosOnMainNodeAnnotations));
//              litVal=getLiteralObject(mainNode, predicate, true);
//          }
//      }
//  }
//  return mainNodeAnnotations;
    }
//    public void addAnnotatedSource(Identifier annotatedAnonSource,Identifier annotationMainNode) {
//        Set<Identifier> annotationMainNodes=annotatedAnonSource2AnnotationMap.get(annotatedAnonSource);
//        if (annotationMainNodes==null) {
//            annotationMainNodes=new HashSet<Identifier>();
//            annotatedAnonSource2AnnotationMap.put(annotatedAnonSource, annotationMainNodes);
//        }
//        annotationMainNodes.add(annotationMainNode);
//    }
//    public Set<Identifier> getAnnotatedSourceAnnotationMainNodes(Identifier source) {
//        Set<Identifier> mainNodes=annotatedAnonSource2AnnotationMap.get(source);
//        if (mainNodes!=null) return mainNodes;
//        else return Collections.emptySet();
//    }
    public void consumeTriple(Identifier subject, Identifier predicate, Identifier object) {
        isTriplePresent(subject, predicate, object, true);
    }
    public void consumeTriple(Identifier subject, Identifier predicate, Literal con) {
        isTriplePresent(subject, predicate, con, true);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    ////
    ////  RDFConsumer implementation
    ////
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected long t0;

    public void handle(Identifier subject, Identifier predicate, Identifier object) {
        if (predicate.equals(Vocabulary.RDF_TYPE.getIRI())) {
            BuiltInTypeHandler typeHandler=builtInTypeTripleHandlers.get(object);
            if (typeHandler!=null) {
                typeHandler.handleTriple(subject, predicate, object);
                // Consumed the triple - no further processing
                return;
            } else if (axiomTypeTripleHandlers.get(object) == null) {
                // C(a)
                Individual ind=translateIndividual(subject);
                ClassExpression ce=translateClassExpression(object);
                Set<Annotation> annos=getAnnotations(subject,predicate,object,null);
                addAxiom(ClassAssertion.create(ce, ind, annos));
            }
        } else {
            TriplePredicateHandler handler=predicateHandlers.get(predicate);
            if (handler!=null&&handler.canHandle(subject, predicate, object)) {
                handler.handleTriple(subject, predicate, object);
            } else {
                for (AbstractResourceTripleHandler resHandler : resourceTripleHandlers) {
                    if (resHandler.canHandle(subject, predicate, object)) {
                        resHandler.handleTriple(subject, predicate, object);
                        break;
                    }
                }
            }
        }
    }
    public void handle(Identifier subject, Identifier predicate, Literal object) {
        for (AbstractLiteralTripleHandler handler : literalTripleHandlers) {
            if (handler.canHandle(subject, predicate, object)) {
                handler.handleTriple(subject, predicate, object);
                break;
            }
        }
    }
    protected static void printTriple(Object subject, Object predicate, Object object, PrintWriter w) {
        w.append(subject.toString());
        w.append(" -> ");
        w.append(predicate.toString());
        w.append(" -> ");
        w.append(object.toString());
        w.append("\n");
    }
    protected void dumpRemainingTriples(PrintWriter w) {
        for (Identifier predicate : singleValuedResTriplesByPredicate.keySet()) {
            Map<Identifier, Identifier> map=singleValuedResTriplesByPredicate.get(predicate);
            for (Identifier subject : map.keySet()) {
                Identifier object=map.get(subject);
                printTriple(subject, predicate, object, w);
            }
        }
        for (Identifier predicate : singleValuedLitTriplesByPredicate.keySet()) {
            Map<Identifier, Literal> map=singleValuedLitTriplesByPredicate.get(predicate);
            for (Identifier subject : map.keySet()) {
                Literal object=map.get(subject);
                printTriple(subject, predicate, object, w);
            }
        }
        for (Identifier subject : resTriplesBySubject.keySet()) {
            Map<Identifier, Set<Identifier>> map=resTriplesBySubject.get(subject);
            for (Identifier predicate : map.keySet()) {
                Set<Identifier> objects=map.get(predicate);
                for (Identifier object : objects) {
                    printTriple(subject, predicate, object, w);
                }
            }
        }
        for (Identifier subject : litTriplesBySubject.keySet()) {
            Map<Identifier, Set<Literal>> map=litTriplesBySubject.get(subject);
            for (Identifier predicate : map.keySet()) {
                Set<Literal> objects=map.get(predicate);
                for (Literal object : objects) {
                    printTriple(subject, predicate, object, w);
                }
            }
        }
        w.flush();
    }
    public void endModel() throws SAXException {
        IRIMap.clear();
        // We need to mop up all remaining triples.  These triples will be in the
        // triples by subject map. Other triples which reside in the triples by
        // predicate (single valued) triple aren't "root" triples for axioms.  First
        // we translate all system triples and then go for triples whose predicates
        // are not system/reserved vocabulary IRIs to translate these into ABox assertions
        // or annotationIRIs
        iterateResourceTriples(new ResourceTripleIterator() {
            public void handleResourceTriple(Identifier subject, Identifier predicate, Identifier object) {
                handle(subject, predicate, object);
            }
        });

        iterateLiteralTriples(new LiteralTripleIterator() {
            public void handleLiteralTriple(Identifier subject, Identifier predicate, Literal object) {
                handle(subject, predicate, object);
            }
        });
        consumeNonReservedPredicateTriples(); // Now handle non-reserved predicate triples
        consumeAnnotatedAxioms(); // Now axiom annotations
        // Do we need to change the ontology IRI?
        chooseOntologyIRI();
        ontology=Ontology.create(ontologyIRI, versionIRIs, imports, axioms, ontologyAnnotations);
        cleanup();
    }
    protected void consumeNonReservedPredicateTriples() {
        iterateResourceTriples(new ResourceTripleIterator() {
            public void handleResourceTriple(Identifier subject, Identifier predicate, Identifier object) {
                if (predicate.getIdentifier() instanceof IRI && Vocabulary.BUILT_IN_VOCABULARY_IRIS.contains((IRI)predicate.getIdentifier())) {
                    for (AbstractResourceTripleHandler resTripHandler : resourceTripleHandlers) {
                        if (resTripHandler.canHandle(subject, predicate, object)) {
                            resTripHandler.handleTriple(subject, predicate, object);
                            break;
                        }
                    }
                }
            }
        });
    }
    protected void consumeAnnotatedAxioms() {
        iterateResourceTriples(new ResourceTripleIterator() {
            public void handleResourceTriple(Identifier subject, Identifier predicate, Identifier object) {
                BuiltInTypeHandler builtInTypeHandler=axiomTypeTripleHandlers.get(object);
                if (builtInTypeHandler!=null) 
                    if (builtInTypeHandler.canHandle(subject, predicate, object))
                        builtInTypeHandler.handleTriple(subject, predicate, object);
            }
        });
    }
    protected void chooseOntologyIRI() {
        if (ontologyIRIs.size()==1) {
            // Exactly one ontologyIRI
            Identifier ontologyIRI=ontologyIRIs.iterator().next();
            if (!isAnonymous(ontologyIRI)) this.ontologyIRI=ontologyIRI;
        } else if (ontologyIRIs.size() > 1) {
            // We have multiple to choose from
            // Choose one that isn't the object of an annotation assertion
            Set<Identifier> candidateIRIs=new HashSet<Identifier>(ontologyIRIs);
            for (Identifier id : ontologyIRIs)
                if (isAnonymous(id)) candidateIRIs.remove(id);
//            for (Annotation anno : annotations) {
//                if (anno.getAnnotationValue() instanceof Identifier) {
//                    Identifier iri=(Identifier)anno.getAnnotationValue();
//                    if (ontologyIRIs.contains(iri)) candidateIRIs.remove(iri);
//                }
//            }
            if (candidateIRIs.contains(firstOntologyIRI)) ontologyIRI=firstOntologyIRI;
            else if (!candidateIRIs.isEmpty()) ontologyIRI=candidateIRIs.iterator().next();
        }
    }

    protected void cleanup() {
        classIRIs.clear();
        objectPropertyIRIs.clear();
        dataPropertyIRIs.clear();
        customDatatypes.clear();
        dataRangeIRIs.clear();
        individualIRIs.clear();
        annotationPropertyIRIs.clear();
        ontologyIRIs.clear();
        ontologyPropertyIRIs.clear();
        firstOntologyIRI=null;
        listFirstLiteralTripleMap.clear();
        listFirstResourceTripleMap.clear();
        listRestTripleMap.clear();
        resTriplesBySubject.clear();
        litTriplesBySubject.clear();
        singleValuedLitTriplesByPredicate.clear();
        singleValuedResTriplesByPredicate.clear();
        translatedClassExpression.clear();
        translatedObjectPropertyExpressions.clear();
    }
    public void statementWithLiteralValue(Identifier subject, Identifier predicate, String lexicalForm, String lang, Datatype datatype) throws SAXException {
        handleStreaming(subject, predicate, lexicalForm, lang, datatype);
    }
    public void statementWithResourceValue(Identifier subject, Identifier predicate, Identifier object) throws SAXException {
        handleStreaming(subject, predicate, object);
    }
    protected void handleStreaming(Identifier subject, Identifier predicate, Identifier object) {
        boolean consumed = false;
        if (predicate.equals(Vocabulary.RDF_TYPE.getIRI())) {
            BuiltInTypeHandler handler=builtInTypeTripleHandlers.get(object);
            if (handler!=null) {
                if (handler.canHandleStreaming(subject, predicate, object)) {
                    handler.handleTriple(subject, predicate, object);
                    consumed = true;
                }
            } else if (axiomTypeTripleHandlers.get(object)==null) {
                addIndividual(subject); // Not a built in type
            } 
//            else 
//                addAxiomIRI(subject);
        } else {
            AbstractResourceTripleHandler handler = predicateHandlers.get(predicate);
            if (handler!=null) {
                if (handler.canHandleStreaming(subject, predicate, object)) {
                    handler.handleTriple(subject, predicate, object);
                    consumed = true;
                }
            }
        }
        // Not consumed, so add the triple
        if (!consumed) addTriple(subject, predicate, object);
    }
    protected void handleStreaming(Identifier subject, Identifier predicate, String lexicalForm, String lang, Datatype datatype) {
    	Literal con=getConstant(lexicalForm, lang, datatype);
        for (AbstractLiteralTripleHandler handler : literalTripleHandlers) {
            if (handler.canHandleStreaming(subject, predicate, con)) {
            	handler.handleTriple(subject, predicate, con);
                return;
            }
        }
        addTriple(subject, predicate, con);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Basic node translation - translation of entities
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ObjectPropertyExpression translateObjectPropertyExpression(Identifier mainNode) {
        ObjectPropertyExpression prop=translatedObjectPropertyExpressions.get(mainNode);
        if (prop!=null) return prop;
        if (isAnonymous(mainNode)) {
            // Inverse of a property expression
            Identifier inverseOfObject=getResourceObject(mainNode, Vocabulary.OWL_INVERSE_OF.getIRI(), true);
            if (inverseOfObject!=null) {
                if (isAnonymous(inverseOfObject)) {
                    // double inverse, cancel out
                    Identifier inverseOfInverseObject=getResourceObject(mainNode, Vocabulary.OWL_INVERSE_OF.getIRI(), true);
                    return translateObjectPropertyExpression(inverseOfInverseObject);
                } else {
                    ObjectPropertyExpression otherProperty=translateObjectPropertyExpression(inverseOfObject);
                    prop=ObjectInverseOf.create(otherProperty);
                }
            } else prop=ObjectInverseOf.create(ObjectProperty.create((IRI)mainNode));
        } else if (isVariable(mainNode)) {
            prop=ObjectPropertyVariable.create(mainNode.toString());
            addObjectProperty(mainNode);
        } else {
            prop=ObjectProperty.create((IRI)mainNode.getIdentifier());
            addObjectProperty(mainNode);
        }
        translatedObjectPropertyExpressions.put(mainNode, prop);
        return prop;
    }
    public DataPropertyExpression translateDataPropertyExpression(Identifier identifier) {
        addDataProperty(identifier);
        DataPropertyExpression dpe;
        if (isVariable(identifier)) {
            dpe=DataPropertyVariable.create(identifier.toString());
        } else if (isAnonymous(identifier)) {
            throw new RuntimeException("A data property cannot be a blank node, but here we have a blank node data property "+identifier.toString());
        } else {
            dpe=DataProperty.create((IRI)identifier);
        }
        return dpe;
    }
    public AnnotationPropertyExpression translateAnnotationPropertyExpression(Identifier identifier) {
        addAnnotationProperty(identifier);
        AnnotationPropertyExpression ape;
        if (isVariable(identifier)) {
            ape=AnnotationPropertyVariable.create(identifier.toString());
        } else if (isAnonymous(identifier)) {
            throw new RuntimeException("An annotation property cannot be a blank node, but here we have a blank node annotation property "+identifier.toString());
        } else {
            ape=AnnotationProperty.create((IRI)identifier);
        }
        return ape;
    }
    public Individual translateIndividual(Identifier iri) {
        Individual ind;
        if (isVariable(iri))
            ind=IndividualVariable.create(iri.toString());
        else 
            if (isAnonymous(iri)) ind=(AnonymousIndividual)iri;
            else ind=NamedIndividual.create((IRI)iri);
        addIndividual(ind.getIdentifier());
        return ind;
    }
    protected Literal getConstant(String literal, String lang, Datatype datatype) {
        Literal lit=TypedLiteral.create(literal, lang, datatype);
//        addLiteral(lit);
        return lit;
    }

    public ClassExpression translateClassExpression(Identifier mainNode) {
    	if (isAnonymous(mainNode)) {
            ClassExpression desc=translatedClassExpression.get(mainNode);
            if (desc==null) {
                ClassExpressionTranslator translator=classExpressionTranslatorSelector.getClassExpressionTranslator(mainNode);
                if (translator!=null) {
                	desc=translator.translate(mainNode);
                    translatedClassExpression.put(mainNode, desc);
                    restrictionIRIs.remove(mainNode);
                } else 
                    throw new RuntimeException("Cannot translate class expression with main node "+mainNode);
            }
            return desc;
    	} else if (isVariable(mainNode))
            return ClassVariable.create(mainNode.toString());
        else 
            return Clazz.create((IRI)mainNode);
    }
    public DataRange translateDataRange(Identifier iri) {
        Identifier oneOfObject=getResourceObject(iri, Vocabulary.OWL_ONE_OF.getIRI(), true);
        if (oneOfObject!=null) {
            Set<Literal> literals=translateToConstantSet(oneOfObject);
            Set<Literal> typedConstants=new HashSet<Literal>(literals.size());
            for (Literal con : literals) {
                if (con instanceof LiteralVariable) {
                    typedConstants.add((LiteralVariable) con);
                } else {
                    typedConstants.add((TypedLiteral)con);
                }
            }
            return DataOneOf.create(typedConstants);
        }
        Identifier intersectionOfObject=getResourceObject(iri, Vocabulary.OWL_INTERSECTION_OF.getIRI(), true);
        if (intersectionOfObject!=null) {
            Set<DataRange> dataRanges=translateToDataRangeSet(intersectionOfObject);
            return DataIntersectionOf.create(dataRanges);
        }
        Identifier unionOfObject=getResourceObject(iri, Vocabulary.OWL_UNION_OF.getIRI(), true);
        if (unionOfObject!=null) {
            Set<DataRange> dataRanges=translateToDataRangeSet(unionOfObject);
            return DataUnionOf.create(dataRanges);
        }
        Identifier onDatatypeObject=getResourceObject(iri, Vocabulary.OWL_ON_DATA_TYPE.getIRI(), true);
        if (onDatatypeObject!=null) {
            Datatype restrictedDataRange=(Datatype)translateDataRange(onDatatypeObject);
            // Consume the datatype type triple
            getResourceObject(iri, Vocabulary.RDF_TYPE.getIRI(), true);
            Set<FacetRestriction> restrictions=new HashSet<FacetRestriction>();
            Identifier facetRestrictionList=getResourceObject(iri, Vocabulary.OWL_WITH_RESTRICTIONS.getIRI(), true);
            if (facetRestrictionList!=null) {
                restrictions=translateToFacetRestrictionSet(facetRestrictionList);
            }
            return DatatypeRestriction.create(restrictedDataRange, restrictions);
        }
        if (isVariable(iri)) return DatatypeVariable.create(iri.toString());
        else if (isAnonymous(iri)) throw new RuntimeException("A blank node occurred as datatype and could not be resolved to any custom or built-in datatype or data range. ");
        return Datatype.create((IRI)iri);
    }
    public ClassExpression getClassExpressionIfTranslated(Identifier mainNode) {
        return translatedClassExpression.get(mainNode);
    }
    public List<ObjectPropertyExpression> translateToObjectPropertyList(Identifier mainNode) {
        return objectPropertyListTranslator.translateList(mainNode);
    }
    public List<DataPropertyExpression> translateToDataPropertyList(Identifier mainNode) {
        return dataPropertyListTranslator.translateList(mainNode);
    }
    public Set<ClassExpression> translateToClassExpressionSet(Identifier mainNode) {
        return classExpressionListTranslator.translateToSet(mainNode);
    }
    public Set<Literal> translateToConstantSet(Identifier mainNode) {
        return constantListTranslator.translateToSet(mainNode);
    }
    public Set<Individual> translateToIndividualSet(Identifier mainNode) {
        return individualListTranslator.translateToSet(mainNode);
    }
    public Set<DataRange> translateToDataRangeSet(Identifier mainNode) {
        return dataRangeListTranslator.translateToSet(mainNode);
    }
    public Set<FacetRestriction> translateToFacetRestrictionSet(Identifier mainNode) {
        return faceRestrictionListTranslator.translateToSet(mainNode);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Set<Identifier> getPredicatesBySubject(Identifier subject) {
        Set<Identifier> IRIs=new HashSet<Identifier>();
        Map<Identifier, Set<Identifier>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            IRIs.addAll(predObjMap.keySet());
        }
        Map<Identifier, Set<Literal>> predObjMapLit=litTriplesBySubject.get(subject);
        if (predObjMapLit!=null) {
            IRIs.addAll(predObjMapLit.keySet());
        }
        return IRIs;
    }
    public Identifier getResourceObject(Identifier subject, Identifier predicate, boolean consume) {
        Map<Identifier, Identifier> subjPredMap=singleValuedResTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            Identifier obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
            return obj;
        }
        Map<Identifier, Set<Identifier>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<Identifier> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (!objects.isEmpty()) {
                    Identifier object=objects.iterator().next();
                    if (consume) objects.remove(object);
                    if (objects.isEmpty()) {
                        predObjMap.remove(predicate);
                        if (predObjMap.isEmpty()) resTriplesBySubject.remove(subject);
                    }
                    return object;
                }
            }
        }
        return null;
    }
    public Literal getLiteralObject(Identifier subject, Identifier predicate, boolean consume) {
        Map<Identifier, Literal> subjPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            Literal obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
            return obj;
        }
        Map<Identifier, Set<Literal>> predObjMap=litTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<Literal> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (!objects.isEmpty()) {
                    Literal object=objects.iterator().next();
                    if (consume) objects.remove(object);
                    if (objects.isEmpty()) predObjMap.remove(predicate);
                    return object;
                }
            }
        }
        Map<Identifier, Set<Identifier>> predObjMap1=resTriplesBySubject.get(subject);
        if (predObjMap1!=null) {
            Set<Identifier> objects=predObjMap1.get(predicate);
            if (objects!=null) {
                if (!objects.isEmpty()) {
                    Identifier object=objects.iterator().next();
                    if (consume) objects.remove(object);
                    if (objects.isEmpty()) predObjMap1.remove(predicate);
//                    if ((isVariable(object)) && (!rdfType.contains(object))) {
                    if ((isVariable(object))) {
                        Literal litvar=LiteralVariable.create(object.toString());
                        return litvar;  
                   	}
                }
            }
        }	        
        return null;
    }
    public boolean isTriplePresent(Identifier subject, Identifier predicate, Identifier object, boolean consume) {
        Map<Identifier, Identifier> subjPredMap=singleValuedResTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            Identifier obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
            return obj!=null;
        }
        Map<Identifier, Set<Identifier>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<Identifier> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (objects.contains(object)) {
                    if (consume) {
                        objects.remove(object);
                        if (objects.isEmpty()) {
                            predObjMap.remove(predicate);
                            if (predObjMap.isEmpty()) resTriplesBySubject.remove(subject);
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    public boolean isTriplePresent(Identifier subject, Identifier predicate, Literal object, boolean consume) {
        Map<Identifier, Literal> subjPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            Literal obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
            return obj!=null;
        }
        Map<Identifier, Set<Literal>> predObjMap=litTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<Literal> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (objects.contains(object)) {
                    if (consume) {
                        objects.remove(object);
                        if (objects.isEmpty()) {
                            predObjMap.remove(predicate);
                            if (predObjMap.isEmpty()) litTriplesBySubject.remove(subject);
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    public boolean hasPredicate(Identifier subject, Identifier predicate) {
        Map<Identifier, Identifier> resPredMap=singleValuedResTriplesByPredicate.get(predicate);
        if (resPredMap!=null) return resPredMap.containsKey(subject);
        Map<Identifier, Literal> litPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (litPredMap!=null) return litPredMap.containsKey(subject);
        Map<Identifier, Set<Identifier>> resPredObjMap=resTriplesBySubject.get(subject);
        if (resPredObjMap!=null) {
            boolean b=resPredObjMap.containsKey(predicate);
            if (b) return true;
        }
        Map<Identifier, Set<Literal>> litPredObjMap=litTriplesBySubject.get(subject);
        if (litPredObjMap!=null) return litPredObjMap.containsKey(predicate);
        return false;
    }
    public boolean hasPredicateObject(Identifier subject, Identifier predicate, Identifier object) {
        Map<Identifier, Identifier> predMap=singleValuedResTriplesByPredicate.get(predicate);
        if (predMap!=null) {
            Identifier objectIRI=predMap.get(subject);
            if (objectIRI==null) return false;
            return objectIRI.equals(object);
        }
        Map<Identifier, Set<Identifier>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<Identifier> objects=predObjMap.get(predicate);
            if (objects!=null) return objects.contains(object);
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addRest(Identifier subject, Identifier object) {
        listRestTripleMap.put(subject, object);
    }
    public void addFirst(Identifier subject, Identifier object) {
        listFirstResourceTripleMap.put(subject, object);
    }
    public Identifier getFirstResource(Identifier subject, boolean consume) {
        if (consume) return listFirstResourceTripleMap.remove(subject);
        else return listFirstResourceTripleMap.get(subject);
    }
    public Literal getFirstLiteral(Identifier subject) {
        return listFirstLiteralTripleMap.get(subject);
    }
    public Identifier getRest(Identifier subject, boolean consume) {
        if (consume) return listRestTripleMap.remove(subject);
        else return listRestTripleMap.get(subject);
    }
    public void addFirst(Identifier subject, Literal object) {
        listFirstLiteralTripleMap.put(subject, object);
    }
    public boolean isDataRange(Identifier iri) {
        return dataRangeIRIs.contains(iri);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////  Triple Stuff
    //////
    //////
    //////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface ResourceTripleIterator {
        void handleResourceTriple(Identifier subject, Identifier predicate, Identifier object);
    }

    private interface LiteralTripleIterator {
        void handleLiteralTriple(Identifier subject, Identifier predicate, Literal object);
    }

    public void iterateResourceTriples(ResourceTripleIterator iterator) {
        for (Identifier subject : new ArrayList<Identifier >(resTriplesBySubject.keySet())) {
            Map<Identifier,Set<Identifier>> map = resTriplesBySubject.get(subject);
            if (map == null) continue;
            for (Identifier predicate : new ArrayList<Identifier>(map.keySet())) {
                Set<Identifier> objects = map.get(predicate);
                if (objects == null) continue;
                for (Identifier object : new ArrayList<Identifier>(objects)) 
                    iterator.handleResourceTriple(subject, predicate, object);
            }
        }
    }

    public void iterateLiteralTriples(LiteralTripleIterator iterator) {
        for (Identifier subject : new ArrayList<Identifier>(litTriplesBySubject.keySet())) {
            Map<Identifier , Set<Literal>> map=litTriplesBySubject.get(subject);
            if (map == null) continue;
            for (Identifier predicate : new ArrayList<Identifier>(map.keySet())) {
                Set<Literal> objects=map.get(predicate);
                for (Literal object : new ArrayList<Literal>(objects))
                    iterator.handleLiteralTriple(subject, predicate, object);
            }
        }
    }
    
    /*
        Originally we had a special Triple class, which was specialised into ResourceTriple and
        LiteralTriple - this was used to store triples.  However, with very large ontologies this
        proved to be inefficient in terms of memory usage.  Now we just store raw subjects, predicates and
        object directly in various maps.
    */
    // Resource triples
    
    public void addTriple(Identifier subject, Identifier predicate, Identifier object) {
        Map<Identifier, Identifier> subjObjMap=singleValuedResTriplesByPredicate.get(predicate);
        if (subjObjMap!=null) subjObjMap.put(subject, object);
        else {
            Map<Identifier, Set<Identifier>> map=resTriplesBySubject.get(subject);
            if (map==null) {
                map=new HashMap<Identifier, Set<Identifier>>();
                resTriplesBySubject.put(subject, map);
            }
            Set<Identifier> objects=map.get(predicate);
            if (objects==null) {
                objects=new FakeSet<Identifier>();
                map.put(predicate, objects);
            }
            objects.add(object);
        }
    }
    public void addTriple(Identifier subject, Identifier predicate, Literal con) {
        Map<Identifier, Literal> subjObjMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjObjMap!=null) subjObjMap.put(subject, con);
        else {
            Map<Identifier, Set<Literal>> map=litTriplesBySubject.get(subject);
            if (map==null) {
                map=new HashMap<Identifier, Set<Literal>>();
                litTriplesBySubject.put(subject, map);
            }
            Set<Literal> objects=map.get(predicate);
            if (objects==null) {
                objects=new FakeSet<Literal>();
                map.put(predicate, objects);
            }
            objects.add(con);
        }
    }
    protected static class FakeSet<O> extends ArrayList<O> implements Set<O> {
        private static final long serialVersionUID = 3972721521801599944L;
        public FakeSet() {}
        public FakeSet(Collection<? extends O> c) {
            super(c);
        }
    }
    
    public void handleBaseDirective(Identifier base) {
        xmlBase=base;
    }
    public void handleComment(String comment) {}
    public void handlePrefixDirective(String prefixName, Identifier prefix) {}
    public void handleEnd() {
        try {
            endModel();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleLiteralTriple(Identifier subject,Identifier predicate,String lexicalForm,Datatype datatype) {
        handleLiteralTriple(subject, predicate, lexicalForm, "", datatype);
    }
    public void handleLiteralTriple(Identifier subject,Identifier predicate,String lexicalForm,String langTag,Datatype datatype) {
        try {
            statementWithLiteralValue(subject, predicate, lexicalForm, langTag, datatype);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleTriple(Identifier subject,Identifier predicate,Identifier object) {
        try {
            statementWithResourceValue(subject, predicate, object);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
