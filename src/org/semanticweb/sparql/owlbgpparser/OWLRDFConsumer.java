package org.semanticweb.sparql.owlbgpparser;

import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDF_TYPE;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.Annotation;
import org.semanticweb.sparql.owlbgp.model.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.model.AnnotationPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.AnnotationPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.Axiom;
import org.semanticweb.sparql.owlbgp.model.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.DataIntersectionOf;
import org.semanticweb.sparql.owlbgp.model.DataOneOf;
import org.semanticweb.sparql.owlbgp.model.DataProperty;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.DataRange;
import org.semanticweb.sparql.owlbgp.model.DataUnionOf;
import org.semanticweb.sparql.owlbgp.model.Datatype;
import org.semanticweb.sparql.owlbgp.model.DatatypeRestriction;
import org.semanticweb.sparql.owlbgp.model.DatatypeVariable;
import org.semanticweb.sparql.owlbgp.model.FacetRestriction;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.IRI;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.Individual;
import org.semanticweb.sparql.owlbgp.model.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.Literal;
import org.semanticweb.sparql.owlbgp.model.LiteralVariable;
import org.semanticweb.sparql.owlbgp.model.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.ObjectInverseOf;
import org.semanticweb.sparql.owlbgp.model.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Datatype.OWL2_DATATYPES;
import org.xml.sax.SAXException;

public class OWLRDFConsumer {
    protected Set<Identifier> classIRIs=new HashSet<Identifier>();
    protected Set<Identifier> classVariables=new HashSet<Identifier>();
    protected Set<Identifier> objectPropertyIRIs=new HashSet<Identifier>();
    protected Set<Identifier> objectPropertyVariables=new HashSet<Identifier>();
    protected Set<Identifier> dataPropertyIRIs=new HashSet<Identifier>();
    protected Set<Identifier> dataPropertyVariables=new HashSet<Identifier>();
    protected Set<Identifier> customDatatypes=new HashSet<Identifier>();
    protected Set<Identifier> dataRangeIRIs=new HashSet<Identifier>();
    protected Set<Identifier> datatypeVariables=new HashSet<Identifier>();
    protected Set<Identifier> individualIRIs=new HashSet<Identifier>();
    protected Set<Identifier> individualVariables=new HashSet<Identifier>();
    protected Set<ILiteral> literalVariables=new HashSet<ILiteral>();
    protected Set<Identifier> propertyIRIs=new HashSet<Identifier>(); // things neither typed as a data or object property - bad!
    protected Set<Identifier> ontologyPropertyIRIs=new HashSet<Identifier>();
    protected Set<Identifier> rdfType=new HashSet<Identifier>();
    protected Set<Identifier> restrictionIRIs=new HashSet<Identifier>(); // IRIs that had a type triple to owl:Restriction
    protected Set<Identifier> axiomIRIs=new HashSet<Identifier>();
    
    protected Identifier firstOntologyIRI; // The IRI of the first resource that is typed as an ontology
    protected Set<Identifier> ontologyIRIs=new HashSet<Identifier>(); // IRIs that had a type triple to owl:Ontology
    protected Identifier ontologyIRI;
    protected Identifier xmlBase;
    
    protected Set<Identifier> annotationPropertyIRIs=new HashSet<Identifier>();
    protected Set<Identifier> annotationPropertyVariables=new HashSet<Identifier>();
    protected Set<Identifier> annotationIRIs=new HashSet<Identifier>();
    protected Map<Identifier, Annotation> annotationIRI2Annotation=new HashMap<Identifier, Annotation>();
    protected Map<Identifier, Set<Annotation>> annotationsBySubject=new HashMap<Identifier, Set<Annotation>>();
    protected Set<Annotation> pendingAnnotations=new HashSet<Annotation>();
    protected Map<Identifier, Set<Identifier>> annotatedAnonSource2AnnotationMap=new HashMap<Identifier, Set<Identifier>>();
    protected Set<Annotation> ontologyAnnotations=new HashSet<Annotation>();
    
    protected Set<Identifier> listIRIs=new HashSet<Identifier>(); // IRIs that had a type triple to rdf:List
    protected Map<Identifier,Identifier> listRestTripleMap=new HashMap<Identifier, Identifier>(); // Maps rdf:next triple subjects to objects
    protected Map<Identifier,Identifier> listFirstResourceTripleMap=new HashMap<Identifier, Identifier>();
    protected Map<Identifier,ILiteral> listFirstLiteralTripleMap=new HashMap<Identifier, ILiteral>();
    protected OptimisedListTranslator<ClassExpression> classExpressionListTranslator; // A translator for lists of class expressions (such lists are used in intersections, unions etc.)
    protected OptimisedListTranslator<Individual> individualListTranslator; // A translator for individual lists (such lists are used in object oneOf constructs)
    protected OptimisedListTranslator<ObjectPropertyExpression> objectPropertyListTranslator;
    protected OptimisedListTranslator<ILiteral> constantListTranslator;
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
    protected Map<Identifier, Map<Identifier, Set<ILiteral>>> litTriplesBySubject=new HashMap<Identifier, Map<Identifier,Set<ILiteral>>>();
    // Predicate, subject, object
    protected Map<Identifier, Map<Identifier, ILiteral>> singleValuedLitTriplesByPredicate=new HashMap<Identifier, Map<Identifier,ILiteral>>();

    protected Axiom lastAddedAxiom;
    protected Set<Axiom> axioms=new HashSet<Axiom>();
     
    protected Map<Identifier,ObjectPropertyExpression> translatedObjectPropertyExpressions=new HashMap<Identifier,ObjectPropertyExpression>();
    protected ClassExpressionTranslatorSelector classExpressionTranslatorSelector;
    protected Map<Identifier,ClassExpression> translatedClassExpression=new HashMap<Identifier,ClassExpression>();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public OWLRDFConsumer() {
        classExpressionTranslatorSelector=new ClassExpressionTranslatorSelector(this);
        for (Identifier iri : Vocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS) annotationPropertyIRIs.add(iri);
        addDublinCoreAnnotationIRIs();
        ontologyPropertyIRIs.add(Vocabulary.OWL_PRIOR_VERSION.getIRI());
        ontologyPropertyIRIs.add(Vocabulary.OWL_BACKWARD_COMPATIBLE_WITH.getIRI());
        ontologyPropertyIRIs.add(Vocabulary.OWL_INCOMPATIBLE_WITH.getIRI());
        
        classExpressionListTranslator=new OptimisedListTranslator<ClassExpression>(this, new ClassExpressionListItemTranslator(this));
        individualListTranslator=new OptimisedListTranslator<Individual>(this, new IndividualListItemTranslator(this));
        constantListTranslator=new OptimisedListTranslator<ILiteral>(this, new TypedConstantListItemTranslator(this));
        objectPropertyListTranslator=new OptimisedListTranslator<ObjectPropertyExpression>(this, new ObjectPropertyListItemTranslator(this));
        dataPropertyListTranslator=new OptimisedListTranslator<DataPropertyExpression>(this, new DataPropertyListItemTranslator(this));
        dataRangeListTranslator=new OptimisedListTranslator<DataRange>(this, new DataRangeListItemTranslator(this));
        faceRestrictionListTranslator=new OptimisedListTranslator<FacetRestriction>(this, new FacetRestrictionListItemTranslator(this));
        setupTypeTripleHandlers();
        setupPredicateHandlers();
        literalTripleHandlers.add(new GTPDataPropertyAssertionHandler(this));
        literalTripleHandlers.add(new TPFirstLiteralHandler(this));
        literalTripleHandlers.add(new GTPAnnotationLiteralHandler(this));
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
    protected void addrdfType(Identifier obj) {
        rdfType.add(obj);
    }
    protected void setupSinglePredicateMaps() {
        addSingleValuedResPredicate(Vocabulary.OWL_ON_PROPERTY);
        addSingleValuedResPredicate(Vocabulary.OWL_SOME_VALUES_FROM);
        addSingleValuedResPredicate(Vocabulary.OWL_ALL_VALUES_FROM);
        addSingleValuedResPredicate(Vocabulary.OWL_ON_CLASS);
        addSingleValuedResPredicate(Vocabulary.OWL_ON_DATA_RANGE);
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
        addBuiltInTypeTripleHandler(new TypeAnnotationPropertyHandler(this));
//        addBuiltInTypeTripleHandler(new TypeDeprecatedClassHandler(this));
//        addBuiltInTypeTripleHandler(new TypeDeprecatedPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeDataRangeHandler(this));
//        addBuiltInTypeTripleHandler(new TypeOntologyHandler(this));
        addBuiltInTypeTripleHandler(new TypeNamedIndividualHandler(this));
        addBuiltInTypeTripleHandler(new TypeAnnotationHandler(this));
        
        addAxiomTypeTripleHandler(new TypeAxiomHandler(this));
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
    }
    protected void addDublinCoreAnnotationIRIs() {
        String[] iris=new String[] {"contributor", "coverage", "creator", "date", "description", "format", "identifier", "language", "publisher", "relation", "rights", "source", "subject", "title", "type"};
        for (String iri : iris)
            annotationIRIs.add(IRI.create("http://purl.org/dc/elements/1.1/"+iri));
    }
    
    // We cache IRIs to save memory!!
    protected Map<Identifier, Identifier> IRIMap=new HashMap<Identifier, Identifier>();
    int currentBaseCount=0;

    protected boolean isAnonymousNode(Identifier iri) {
        return (iri instanceof AnonymousIndividual);
    }
    protected boolean isVariableNode(Identifier iri) {
        return (iri instanceof Variable);
    }
    protected boolean isVariableLiteral(ILiteral literal) {
        return (literal instanceof Variable);
    }
    protected void addAxiom(Axiom axiom) {
        axioms.add(axiom);
        lastAddedAxiom=axiom;
    }
    protected void removeAxiom(Axiom axiom) {
        axioms.remove(axiom);
    }
    public Set<Axiom> getParsedAxioms() {
        return axioms;
    }
    public Axiom getLastAddedAxiom() {
        return lastAddedAxiom;
    }
    public Set<Annotation> getPendingAnnotations() {
        if (!pendingAnnotations.isEmpty()) {
            Set<Annotation> annos=new HashSet<Annotation>(pendingAnnotations);
            pendingAnnotations.clear();
            return annos;
        } else return new HashSet<Annotation>();
    }
    public void setPendingAnnotations(Set<Annotation> annotations) {
        pendingAnnotations.clear();
        pendingAnnotations.addAll(annotations);
    }
    protected void addClass(Identifier iri) {
        if (isVariableNode(iri)) classVariables.add(iri);
        else if (!isAnonymousNode(iri)) classIRIs.add(iri);
    }
    protected void addObjectProperty(Identifier iri) {
        if (isVariableNode(iri)) objectPropertyVariables.add(iri);
        else objectPropertyIRIs.add(iri);
    }
    protected void addDataProperty(Identifier iri) {
        if (isVariableNode(iri)) dataPropertyVariables.add(iri);
        else dataPropertyIRIs.add(iri);
    }
    protected void addIndividual(Identifier iri) {
        if (isVariableNode(iri)) individualVariables.add(iri);
        else individualIRIs.add(iri);
    }
    protected void addLiteral(ILiteral iri) {
        if (isVariableLiteral(iri)) literalVariables.add(iri);
    }
    protected void addDataRange(Identifier iri) {
        if (isVariableNode(iri)) {
            datatypeVariables.add(iri);
        } else {
            dataRangeIRIs.add(iri);
            Datatype dt=Datatype.create((IRI)iri);
            if (!isAnonymousNode(iri) && !dt.isOWL2Datatype()) customDatatypes.add(iri);
        }
    }
    protected void addOntologyAnnotation(Annotation annotation) {
        ontologyAnnotations.add(annotation);
    }
    protected void addAnnotationProperty(Identifier iri) {
        if (isVariableNode(iri)) annotationPropertyVariables.add(iri); 
        else annotationPropertyIRIs.add(iri);
    }
    protected void addAnnotationIRI(Identifier iri) {
        annotationIRIs.add(iri);
    }
    protected boolean isAnnotation(Identifier iri) {
        return annotationIRIs.contains(iri);
    }
    protected void addRestriction(Identifier iri) {
        restrictionIRIs.add(iri);
    }
    protected void addOnProperty(Identifier iri) {
        propertyIRIs.add(iri); // owl:onProperty
    }
    public void addAxiomIRI(Identifier axiomIRI) {
        axiomIRIs.add(axiomIRI);
    }
    public boolean isAxiom(Identifier iri) {
        return axioms.contains(iri);
    }
    protected boolean isOnProperty(Identifier iri) {
        return propertyIRIs.contains(iri);
    }
    public boolean isRestriction(Identifier iri) {
        return restrictionIRIs.contains(iri);
    }
    protected boolean isClass(Identifier iri) {
        if (classIRIs.contains(iri) || classVariables.contains(iri)) return true;
        return false;
    }
    protected boolean isObjectPropertyOnly(Identifier iri) {
        if (iri==null) return false;
        if (dataPropertyIRIs.contains(iri) || dataPropertyVariables.contains(iri)) return false;
        if (objectPropertyIRIs.contains(iri) || objectPropertyVariables.contains(iri)) return true;
        else return false;
    }
    protected boolean isDataPropertyOnly(Identifier iri) {
        if (objectPropertyIRIs.contains(iri) || objectPropertyVariables.contains(iri)) return false;
        if (dataPropertyIRIs.contains(iri) || dataPropertyVariables.contains(iri)) return true;
        else return false;
    }
    protected boolean isOntologyProperty(Identifier iri) {
        return ontologyPropertyIRIs.contains(iri);
    }
    protected boolean isAnnotationProperty(Identifier iri) {
        if (annotationPropertyIRIs.contains(iri)||annotationPropertyVariables.contains(iri)) return true;
        else return false;
    }
    protected boolean isIndividual(Identifier iri) {
        return individualIRIs.contains(iri) || individualVariables.contains(iri);
    }
    protected boolean isOntology(Identifier iri) {
        return ontologyIRIs.contains(iri);
    }
    public Set<Identifier> getAnnotatedSourceAnnotationMainNodes(Identifier source) {
        Set<Identifier> mainNodes=annotatedAnonSource2AnnotationMap.get(source);
        if (mainNodes!=null) return mainNodes;
        else return Collections.emptySet();
    }
    protected void consumeTriple(Identifier subject, Identifier predicate, Identifier object) {
        isTriplePresent(subject, predicate, object, true);
    }
    protected void consumeTriple(Identifier subject, Identifier predicate, ILiteral con) {
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
                addAxiom(ClassAssertion.create(ce, ind, getPendingAnnotations()));
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
    public void handle(Identifier subject, Identifier predicate, ILiteral object) {
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
            Map<Identifier, ILiteral> map=singleValuedLitTriplesByPredicate.get(predicate);
            for (Identifier subject : map.keySet()) {
                ILiteral object=map.get(subject);
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
            Map<Identifier, Set<ILiteral>> map=litTriplesBySubject.get(subject);
            for (Identifier predicate : map.keySet()) {
                Set<ILiteral> objects=map.get(predicate);
                for (ILiteral object : objects) {
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
        for (Identifier subject : resTriplesBySubject.keySet()) {
            Map<Identifier, Set<Identifier>> map=resTriplesBySubject.get(subject);
            if (map==null) continue;
            for (Identifier predicate : map.keySet()) {
                Set<Identifier> objects=map.get(predicate);
                if (objects==null) continue;
                for (Identifier object : objects)
                    if (!(predicate.equals(Vocabulary.RDF_TYPE.getIRI()) && (object.equals(Vocabulary.OWL_AXIOM.getIRI()) || object.equals(Vocabulary.OWL_ALL_DISJOINT_CLASSES.getIRI()))))
                        handle(subject, predicate, object);
            }
        }
        // Now handle axiom annotations
        // TODO: TIDY UP THIS COPY AND PASTE HACK!
        for (Identifier subject : resTriplesBySubject.keySet()) {
            Map<Identifier, Set<Identifier>> map=resTriplesBySubject.get(subject);
            if (map==null) continue;
            for (Identifier predicate : map.keySet()) {
                Set<Identifier> objects=map.get(predicate);
                if (objects==null) continue;
                for (Identifier object : objects)
                    if ((predicate.equals(Vocabulary.RDF_TYPE.getIRI()) && (object.equals(Vocabulary.OWL_AXIOM.getIRI()) || object.equals(Vocabulary.OWL_ALL_DISJOINT_CLASSES.getIRI())))) 
                        handle(subject, predicate, object);
            }
        }
        // TODO: TIDY UP!  This is a copy and paste hack!!
        // Now for the ABox assertions and annotationIRIs
        for (Identifier subject : resTriplesBySubject.keySet()) {
            Map<Identifier, Set<Identifier>> map=resTriplesBySubject.get(subject);
            if (map==null) continue;
            for (Identifier predicate : map.keySet()) {
                Set<Identifier> objects=map.get(predicate);
                if (objects==null) continue;
                for (Identifier object : objects) {
                    if (isVariableNode(object) && isDataPropertyOnly(predicate)) {
                    	ILiteral obj=(ILiteral)object;
                    	for (AbstractLiteralTripleHandler handler : literalTripleHandlers) {
                            if (handler.canHandle(subject, predicate, obj)) {
                                handler.handleTriple(subject, predicate, obj);
                                continue;
                            }
                        }
                    } else {		
                	    for (AbstractResourceTripleHandler resTripHandler : resourceTripleHandlers) {
                            if (resTripHandler.canHandle(subject, predicate, object)) {
                                resTripHandler.handleTriple(subject, predicate, object);
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (Identifier subject : litTriplesBySubject.keySet()) {
            Map<Identifier, Set<ILiteral>> map=litTriplesBySubject.get(subject);
            if (map==null) continue;
            for (Identifier predicate : map.keySet()) {
                for (ILiteral object : map.get(predicate))
                    handle(subject, predicate, object);
            }
        }
        cleanup();
    }
    protected void cleanup() {
        classIRIs.clear();
        classVariables.clear();
        objectPropertyIRIs.clear();
        objectPropertyVariables.clear();
        dataPropertyIRIs.clear();
        dataPropertyVariables.clear();
        customDatatypes.clear();
        dataRangeIRIs.clear();
        datatypeVariables.clear();
        individualIRIs.clear();
        individualVariables.clear();
        literalVariables.clear();
        propertyIRIs.clear();
        annotationPropertyIRIs.clear();
        annotationPropertyVariables.clear();
        annotationIRIs.clear();
        ontologyIRIs.clear();
        ontologyPropertyIRIs.clear();
        firstOntologyIRI=null;
        restrictionIRIs.clear();
        listIRIs.clear();
        listFirstLiteralTripleMap.clear();
        listFirstResourceTripleMap.clear();
        listRestTripleMap.clear();
        resTriplesBySubject.clear();
        litTriplesBySubject.clear();
        singleValuedLitTriplesByPredicate.clear();
        singleValuedResTriplesByPredicate.clear();
        annotatedAnonSource2AnnotationMap.clear();
        lastAddedAxiom=null;
        rdfType.clear(); 
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
        if (predicate.equals(RDF_TYPE.getIRI())) {
            BuiltInTypeHandler handler=builtInTypeTripleHandlers.get(object);
            if (handler != null) {
                if (handler.canHandleStreaming(subject, predicate, object)) {
                    handler.handleTriple(subject, predicate, object);
                    consumed = true;
                }
            } else if (axiomTypeTripleHandlers.get(object)==null) {
                addIndividual(subject); // Not a built in type
            } else addAxiomIRI(subject);
        }
        else {
            AbstractResourceTripleHandler handler = predicateHandlers.get(predicate);
            if (handler != null) {
                if (handler.canHandleStreaming(subject, predicate, object)) {
                    handler.handleTriple(subject, predicate, object);
                    consumed = true;
                }
            }
        }
        // Not consumed, so add the triple
        if (!consumed) addTriple(subject, predicate, object);
        if (predicate.equals(Vocabulary.RDF_TYPE.getIRI())) {
            // TODO: set types of variables 
        	if (isVariableNode(subject)) rdfType.add(object);
        	BuiltInTypeHandler handler=null;
        	if (!isVariableNode(subject) 
        	        || object.equals(Vocabulary.OWL_FUNCTIONAL_PROPERTY.getIRI()) 
        	        || object.equals(Vocabulary.OWL_INVERSE_FUNCTIONAL_PROPERTY.getIRI()) 
        	        || object.equals(Vocabulary.OWL_REFLEXIVE_PROPERTY.getIRI()) 
        	        || object.equals(Vocabulary.OWL_IRREFLEXIVE_PROPERTY.getIRI()) 
        	        || object.equals(Vocabulary.OWL_SYMMETRIC_PROPERTY.getIRI()) 
        	        || object.equals(Vocabulary.OWL_ASYMMETRIC_PROPERTY.getIRI()) 
        	        || object.equals(Vocabulary.OWL_TRANSITIVE_PROPERTY.getIRI())) {
               handler=builtInTypeTripleHandlers.get(object);
            } else {
        	    if (object.equals(Vocabulary.OWL_CLASS.getIRI())) handler=builtInTypeTripleHandlers.get(Vocabulary.OWL_CLASS.getIRI());
        	    else if (object.equals(Vocabulary.OWL_OBJECT_PROPERTY.getIRI())) handler=builtInTypeTripleHandlers.get(Vocabulary.OWL_OBJECT_PROPERTY.getIRI());  
          	    else if (object.equals(Vocabulary.OWL_DATA_PROPERTY.getIRI())) handler=builtInTypeTripleHandlers.get(Vocabulary.OWL_DATA_PROPERTY.getIRI());	
          	    else if (object.equals(Vocabulary.OWL_NAMED_INDIVIDUAL.getIRI())) handler=builtInTypeTripleHandlers.get(Vocabulary.OWL_NAMED_INDIVIDUAL.getIRI());
            }
            if (handler!=null) {
                if (handler.canHandleStreaming(subject, predicate, object)) {
                    handler.handleTriple(subject, predicate, object);
                    return;
                }
            } else {
                addIndividual(subject); // Individual?
            }
        }
        AbstractResourceTripleHandler handler=predicateHandlers.get(predicate);
        if (handler!=null) {
            if (handler.canHandleStreaming(subject, predicate, object)) {
                handler.handleTriple(subject, predicate, object);
                return;
            }
        }
        addTriple(subject, predicate, object);
    }
    protected void handleStreaming(Identifier subject, Identifier predicate, String lexicalForm, String lang, Datatype datatype) {
    	ILiteral con=getConstant(lexicalForm, lang, datatype);
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
        if (isAnonymousNode(mainNode)) {
            // Inverse of a property expression
            Identifier inverseOfObject=getResourceObject(mainNode, Vocabulary.OWL_INVERSE_OF.getIRI(), true);
            if (inverseOfObject!=null) {
                if (isAnonymousNode(inverseOfObject)) {
                    // double inverse, cancel out
                    Identifier inverseOfInverseObject=getResourceObject(mainNode, Vocabulary.OWL_INVERSE_OF.getIRI(), true);
                    return translateObjectPropertyExpression(inverseOfInverseObject);
                } else {
                    ObjectProperty otherProperty=(ObjectProperty)translateObjectPropertyExpression(inverseOfObject);
                    prop=ObjectInverseOf.create(otherProperty);
                }
            } else prop=ObjectInverseOf.create(ObjectProperty.create((IRI)mainNode));
        } else if (isVariableNode(mainNode)) {
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
        if (isVariableNode(identifier)) {
            dpe=DataPropertyVariable.create(identifier.toString());
        } else if (isAnonymousNode(identifier)) {
            throw new RuntimeException("A data property cannot be a blank node, but here we have a blank node data property "+identifier.toString());
        } else {
            dpe=DataProperty.create((IRI)identifier);
        }
        return dpe;
    }
    public AnnotationPropertyExpression translateAnnotationPropertyExpression(Identifier identifier) {
        addAnnotationProperty(identifier);
        AnnotationPropertyExpression ape;
        if (isVariableNode(identifier)) {
            ape=AnnotationPropertyVariable.create(identifier.toString());
        } else if (isAnonymousNode(identifier)) {
            throw new RuntimeException("An annotation property cannot be a blank node, but here we have a blank node annotation property "+identifier.toString());
        } else {
            ape=AnnotationProperty.create((IRI)identifier);
        }
        return ape;
    }
    public Individual translateIndividual(Identifier iri) {
        Individual ind;
        if (isVariableNode(iri))
            ind=IndividualVariable.create(iri.toString());
        else 
            if (isAnonymousNode(iri)) ind=(AnonymousIndividual)iri;
            else ind=NamedIndividual.create((IRI)iri);
        addIndividual(ind.getIdentifier());
        return ind;
    }
    protected ILiteral getConstant(String literal, String lang, Datatype datatype) {
        ILiteral lit=Literal.create(literal, lang, datatype);
        addLiteral(lit);
        return lit;
    }
    public Set<Annotation> translateAnnotations(Identifier mainNode) {
        // Are we the subject of an annotation?  If so, we need to ensure that the annotations annotate us.  This
        // will only happen if we are an annotation!
        Set<Annotation> annosOnMainNodeAnnotations=new HashSet<Annotation>();
        Set<Identifier> annotationMainNodes=getAnnotatedSourceAnnotationMainNodes(mainNode);
        if (!annotationMainNodes.isEmpty()) {
            for (Identifier annotationMainNode : annotationMainNodes) {
                annosOnMainNodeAnnotations.addAll(translateAnnotations(annotationMainNode));
            }
        }
        Set<Annotation> mainNodeAnnotations=new HashSet<Annotation>();
        Set<Identifier> predicates=getPredicatesBySubject(mainNode);
        for (Identifier predicate : predicates) {
            if (isAnnotationProperty(predicate)) {
                AnnotationPropertyExpression prop;
                if (isVariableNode(predicate)) prop=AnnotationPropertyVariable.create(predicate.toString());
                else if (isAnonymousNode(predicate)) throw new RuntimeException("Annotation properties cannot be blank nodes, but here we have "+predicate+" as annotation property. ");
                else prop=AnnotationProperty.create((IRI)predicate);
                Identifier resVal = getResourceObject(mainNode, predicate, true);
                while (resVal != null) {
                    if (isAnonymousNode(resVal)) {
                        mainNodeAnnotations.add(Annotation.create(prop, (AnonymousIndividual)resVal, annosOnMainNodeAnnotations));
                    } else if (isVariableNode(resVal)) throw new RuntimeException("Annotation values cannot be variables, but here we have "+resVal+" as annotation value variable. ");
                    else mainNodeAnnotations.add(Annotation.create(prop, (IRI)resVal, annosOnMainNodeAnnotations));
                    resVal = getResourceObject(mainNode, predicate, true);
                }
                ILiteral litVal=getLiteralObject(mainNode, predicate, true);
                while (litVal != null) {
                    mainNodeAnnotations.add(Annotation.create(prop, litVal, annosOnMainNodeAnnotations));
                    litVal=getLiteralObject(mainNode, predicate, true);
                }
            }
        }
        return mainNodeAnnotations;
    }

    public ClassExpression translateClassExpression(Identifier mainNode) {
    	if (isAnonymousNode(mainNode)) {
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
    	} else if (isVariableNode(mainNode))
            return ClassVariable.create(mainNode.toString());
        else 
            return Clazz.create((IRI)mainNode);
    }
    public DataRange translateDataRange(Identifier iri) {
        Identifier oneOfObject=getResourceObject(iri, Vocabulary.OWL_ONE_OF.getIRI(), true);
        if (oneOfObject!=null) {
            Set<ILiteral> literals=translateToConstantSet(oneOfObject);
            Set<ILiteral> typedConstants=new HashSet<ILiteral>(literals.size());
            for (ILiteral con : literals) {
                if (con instanceof LiteralVariable) {
                    typedConstants.add((LiteralVariable) con);
                } else {
                    typedConstants.add((Literal)con);
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
        if (isVariableNode(iri)) return DatatypeVariable.create(iri.toString());
        else if (isAnonymousNode(iri)) throw new RuntimeException("A blank node occurred as datatype and could not be resolved to any custom or built-in datatype or data range. ");
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
    public Set<ILiteral> translateToConstantSet(Identifier mainNode) {
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
        Map<Identifier, Set<ILiteral>> predObjMapLit=litTriplesBySubject.get(subject);
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
    public ILiteral getLiteralObject(Identifier subject, Identifier predicate, boolean consume) {
        Map<Identifier, ILiteral> subjPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            ILiteral obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
            return obj;
        }
        Map<Identifier, Set<ILiteral>> predObjMap=litTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<ILiteral> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (!objects.isEmpty()) {
                    ILiteral object=objects.iterator().next();
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
                    if ((isVariableNode(object)) && (!rdfType.contains(object))) {
                        ILiteral litvar=LiteralVariable.create(object.toString());
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
    public boolean isTriplePresent(Identifier subject, Identifier predicate, ILiteral object, boolean consume) {
        Map<Identifier, ILiteral> subjPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            ILiteral obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
            return obj!=null;
        }
        Map<Identifier, Set<ILiteral>> predObjMap=litTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<ILiteral> objects=predObjMap.get(predicate);
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
        Map<Identifier, ILiteral> litPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (litPredMap!=null) return litPredMap.containsKey(subject);
        Map<Identifier, Set<Identifier>> resPredObjMap=resTriplesBySubject.get(subject);
        if (resPredObjMap!=null) {
            boolean b=resPredObjMap.containsKey(predicate);
            if (b) return true;
        }
        Map<Identifier, Set<ILiteral>> litPredObjMap=litTriplesBySubject.get(subject);
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

    public void addList(Identifier iri) {
        listIRIs.add(iri);
    }
    public boolean isList(Identifier iri, boolean consume) {
        if (consume) return listIRIs.remove(iri);
        else return listIRIs.contains(iri);
    }
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
    public ILiteral getFirstLiteral(Identifier subject) {
        return listFirstLiteralTripleMap.get(subject);
    }
    public Identifier getRest(Identifier subject, boolean consume) {
        if (consume) return listRestTripleMap.remove(subject);
        else return listRestTripleMap.get(subject);
    }
    public void addFirst(Identifier subject, ILiteral object) {
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
    public void addTriple(Identifier subject, Identifier predicate, ILiteral con) {
        Map<Identifier, ILiteral> subjObjMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjObjMap!=null) subjObjMap.put(subject, con);
        else {
            Map<Identifier, Set<ILiteral>> map=litTriplesBySubject.get(subject);
            if (map==null) {
                map=new HashMap<Identifier, Set<ILiteral>>();
                litTriplesBySubject.put(subject, map);
            }
            Set<ILiteral> objects=map.get(predicate);
            if (objects==null) {
                objects=new FakeSet<ILiteral>();
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
}
