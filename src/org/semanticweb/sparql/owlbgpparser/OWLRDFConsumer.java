package org.semanticweb.sparql.owlbgpparser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.coode.owlapi.rdfxml.parser.TPAllValuesFromHandler;
import org.coode.owlapi.rdfxml.parser.TPAnnotatedSourceHandler;
import org.coode.owlapi.rdfxml.parser.TPComplementOfHandler;
import org.coode.owlapi.rdfxml.parser.TPDataPropertDomainHandler;
import org.coode.owlapi.rdfxml.parser.TPDataPropertyRangeHandler;
import org.coode.owlapi.rdfxml.parser.TPDeclaredAsHandler;
import org.coode.owlapi.rdfxml.parser.TPDifferentFromHandler;
import org.coode.owlapi.rdfxml.parser.TPDisjointDataPropertiesHandler;
import org.coode.owlapi.rdfxml.parser.TPDisjointObjectPropertiesHandler;
import org.coode.owlapi.rdfxml.parser.TPDisjointUnionHandler;
import org.coode.owlapi.rdfxml.parser.TPDisjointWithHandler;
import org.coode.owlapi.rdfxml.parser.TPDistinctMembersHandler;
import org.coode.owlapi.rdfxml.parser.TPEquivalentClassHandler;
import org.coode.owlapi.rdfxml.parser.TPEquivalentDataPropertyHandler;
import org.coode.owlapi.rdfxml.parser.TPEquivalentObjectPropertyHandler;
import org.coode.owlapi.rdfxml.parser.TPEquivalentPropertyHandler;
import org.coode.owlapi.rdfxml.parser.TPFirstResourceHandler;
import org.coode.owlapi.rdfxml.parser.TPHasKeyHandler;
import org.coode.owlapi.rdfxml.parser.TPImportsHandler;
import org.coode.owlapi.rdfxml.parser.TPIntersectionOfHandler;
import org.coode.owlapi.rdfxml.parser.TPInverseOfHandler;
import org.coode.owlapi.rdfxml.parser.TPObjectPropertyDomainHandler;
import org.coode.owlapi.rdfxml.parser.TPObjectPropertyRangeHandler;
import org.coode.owlapi.rdfxml.parser.TPOnPropertyHandler;
import org.coode.owlapi.rdfxml.parser.TPOneOfHandler;
import org.coode.owlapi.rdfxml.parser.TPPropertyChainAxiomHandler;
import org.coode.owlapi.rdfxml.parser.TPPropertyDisjointWithHandler;
import org.coode.owlapi.rdfxml.parser.TPPropertyDomainHandler;
import org.coode.owlapi.rdfxml.parser.TPPropertyRangeHandler;
import org.coode.owlapi.rdfxml.parser.TPRestHandler;
import org.coode.owlapi.rdfxml.parser.TPSameAsHandler;
import org.coode.owlapi.rdfxml.parser.TPSomeValuesFromHandler;
import org.coode.owlapi.rdfxml.parser.TPSubClassOfHandler;
import org.coode.owlapi.rdfxml.parser.TPSubDataPropertyOfHandler;
import org.coode.owlapi.rdfxml.parser.TPSubObjectPropertyOfHandler;
import org.coode.owlapi.rdfxml.parser.TPSubPropertyOfHandler;
import org.coode.owlapi.rdfxml.parser.TPTypeHandler;
import org.coode.owlapi.rdfxml.parser.TPUnionOfHandler;
import org.coode.owlapi.rdfxml.parser.TPVersionIRIHandler;
import org.coode.owlapi.rdfxml.parser.TranslatedUnloadedImportException;
import org.semanticweb.owlapi.io.RDFOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.sparql.owlbgp.model.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.Axiom;
import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ClassVariable;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.DataComplementOf;
import org.semanticweb.sparql.owlbgp.model.DataIntersectionOf;
import org.semanticweb.sparql.owlbgp.model.DataOneOf;
import org.semanticweb.sparql.owlbgp.model.DataProperty;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataPropertyVariable;
import org.semanticweb.sparql.owlbgp.model.DataRange;
import org.semanticweb.sparql.owlbgp.model.DataUnionOf;
import org.semanticweb.sparql.owlbgp.model.Datatype;
import org.semanticweb.sparql.owlbgp.model.DatatypeRestriction;
import org.semanticweb.sparql.owlbgp.model.FacetRestriction;
import org.semanticweb.sparql.owlbgp.model.ILiteral;
import org.semanticweb.sparql.owlbgp.model.Individual;
import org.semanticweb.sparql.owlbgp.model.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.Literal;
import org.semanticweb.sparql.owlbgp.model.LiteralVariable;
import org.semanticweb.sparql.owlbgp.model.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.ObjectInverseOf;
import org.semanticweb.sparql.owlbgp.model.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyVariable;
import org.xml.sax.SAXException;

public class OWLRDFConsumer {
    protected String ontologyIRI;
    protected String xmlBase;
    protected Set<String> owlClassIRIs;
    protected Set<String> owlClassVars;
    protected Set<String> objectPropertyIRIs;
    protected Set<String> objectPropertyVars;
    protected Set<String> dataPropertyIRIs;
    protected Set<String> dataPropertyVars;
    protected Set<String> propertyIRIs;
    protected Set<String> individualIRIs;
    protected Set<String> individualVars;
    protected Set<String> literalVars;
    protected Set<String> datatypeVars;
    protected Set<String> annotationPropertyIRIs;
    protected Set<String> annotationIRIs;
    protected Set<String> ontologyPropertyIRIs;
    // IRIs that had a type triple to rdfs:DataRange
    protected Set<String> dataRangeIRIs;
    // The IRI of the first resource that is typed as an ontology
    protected String firstOntologyIRI;
    // IRIs that had a type triple to owl:Ontology
    protected Set<String> ontologyIRIs;
    // IRIs that had a type triple to owl:Restriction
    protected Set<String> restrictionIRIs;
    // IRIs that had a type triple to rdf:List
    protected Set<String> listIRIs;
    // Maps rdf:next triple subjects to objects
    protected Map<String,String> listRestTripleMap;
    protected Map<String,String> listFirstResourceTripleMap;
    protected Map<String,ILiteral> listFirstLiteralTripleMap;
    protected Map<String,Axiom> reifiedAxiomsMap;
    // A translator for lists of class expressions (such lists are used
    // in intersections, unions etc.)
    protected OptimisedListTranslator<ClassExpression> classExpressionListTranslator;
    // A translator for individual lists (such lists are used in
    // object oneOf constructs)
    protected OptimisedListTranslator<Individual> individualListTranslator;
    protected OptimisedListTranslator<ObjectPropertyExpression> objectPropertyListTranslator;
    protected OptimisedListTranslator<ILiteral> constantListTranslator;
    protected OptimisedListTranslator<DataPropertyExpression> dataPropertyListTranslator;
    protected OptimisedListTranslator<DataRange> dataRangeListTranslator;
    protected OptimisedListTranslator<FacetRestriction> faceRestrictionListTranslator;
    // Handlers for built in types
    protected Map<String,BuiltInTypeHandler> builtInTypeTripleHandlers;
    // Handlers for build in predicates
    protected Map<String, TriplePredicateHandler> predicateHandlers;
    protected Map<String, AbstractLiteralTripleHandler> skosTripleHandlers;
    // Handlers for general literal triples (i.e. triples which
    // have predicates that are not part of the built in OWL/RDFS/RDF
    // vocabulary.  Such triples either constitute annotationIRIs of
    // relationships between an individual and a data literal (typed or
    // untyped)
    protected List<AbstractLiteralTripleHandler> literalTripleHandlers;
    // Handlers for general resource triples (i.e. triples which
    // have predicates that are not part of the built in OWL/RDFS/RDF
    // vocabulary.  Such triples either constitute annotationIRIs or
    // relationships between an individual and another individual.
    protected List<AbstractResourceTripleHandler> resourceTripleHandlers;
    protected Set<OWLAnnotation> pendingAnnotations=new HashSet<OWLAnnotation>();
    protected Map<String, Set<String>> annotatedAnonSource2AnnotationMap=new HashMap<String, Set<String>>();
    protected RDFXMLOntologyFormat rdfxmlOntologyFormat;
    protected ClassExpressionTranslatorSelector classExpressionTranslatorSelector;
    protected Axiom lastAddedAxiom;
    protected Map<String, String> synonymMap;
    protected Set<String> rdfType; 
    
    protected boolean hasAnnotations=false;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public OWLRDFConsumer(OWLOntology ontology) {
        classExpressionTranslatorSelector=new ClassExpressionTranslatorSelector(this);
        owlClassIRIs=new HashSet<String>();
        owlClassVars=new HashSet<String>();
        objectPropertyIRIs=new HashSet<String>();
        objectPropertyVars=new HashSet<String>();
        dataPropertyIRIs=new HashSet<String>();
        dataPropertyVars=new HashSet<String>();
        individualIRIs=new HashSet<String>();
        individualVars=new HashSet<String>();
        literalVars=new HashSet<String>();
        datatypeVars=new HashSet<String>();
        annotationPropertyIRIs=CollectionFactory.createSet();
        for (String iri : OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS) {
            annotationPropertyIRIs.add(iri);
        }
        annotationIRIs=new HashSet<String>();
        ontologyPropertyIRIs=CollectionFactory.createSet();
        ontologyPropertyIRIs.add(OWLRDFVocabulary.OWL_PRIOR_VERSION.getIRI());
        ontologyPropertyIRIs.add(OWLRDFVocabulary.OWL_BACKWARD_COMPATIBLE_WITH.getIRI());
        ontologyPropertyIRIs.add(OWLRDFVocabulary.OWL_INCOMPATIBLE_WITH.getIRI());

        dataRangeIRIs=new HashSet<String>();
        propertyIRIs=new HashSet<String>();
        restrictionIRIs=new HashSet<String>();
        ontologyIRIs=new HashSet<String>();
        listIRIs=new HashSet<String>();
        listFirstLiteralTripleMap=new HashMap<String, ILiteral>();
        listFirstResourceTripleMap=new HashMap<String, String>();
        listRestTripleMap=new HashMap<String, String>();
        reifiedAxiomsMap=new HashMap<String, Axiom>();
        classExpressionListTranslator=new OptimisedListTranslator<ClassExpression>(this, new ClassExpressionListItemTranslator(this));
        individualListTranslator=new OptimisedListTranslator<Individual>(this, new IndividualListItemTranslator(this));
        constantListTranslator=new OptimisedListTranslator<ILiteral>(this, new TypedConstantListItemTranslator(this));
        objectPropertyListTranslator=new OptimisedListTranslator<ObjectPropertyExpression>(this, new ObjectPropertyListItemTranslator(this));
        dataPropertyListTranslator=new OptimisedListTranslator<DataPropertyExpression>(this, new DataPropertyListItemTranslator(this));
        dataRangeListTranslator=new OptimisedListTranslator<DataRange>(this, new DataRangeListItemTranslator(this));
        faceRestrictionListTranslator=new OptimisedListTranslator<FacetRestriction>(this, new OWLFacetRestrictionListItemTranslator(this));
        builtInTypeTripleHandlers=new HashMap<String, BuiltInTypeHandler>();
        setupTypeTripleHandlers();
        setupPredicateHandlers();

        literalTripleHandlers=new ArrayList<AbstractLiteralTripleHandler>();
        literalTripleHandlers.add(new GTPDataPropertyAssertionHandler(this));
        literalTripleHandlers.add(new TPFirstLiteralHandler(this));

        // General resource/object triples - i.e. triples which have a predicate
        // that is not a built in IRI. Object properties get precedence
        // over object properties
        resourceTripleHandlers=new ArrayList<AbstractResourceTripleHandler>();
        resourceTripleHandlers.add(new GTPObjectPropertyAssertionHandler(this));
        resourceTripleHandlers.add(new GTPAnnotationResourceTripleHandler(this));
        
        for (Datatype dt : Datatype.OWL2_DATATYPES) {
            dataRangeIRIs.add(dt.getIRIString());
        }
        dataRangeIRIs.add(OWLRDFVocabulary.RDFS_LITERAL.getIRI());        
        rdfType=new HashSet<String>();
        owlClassIRIs.add(OWLRDFVocabulary.OWL_THING.getIRI());
        owlClassIRIs.add(OWLRDFVocabulary.OWL_NOTHING.getIRI());
        objectPropertyIRIs.add(OWLRDFVocabulary.OWL_TOP_OBJECT_PROPERTY.getIRI());
        objectPropertyIRIs.add(OWLRDFVocabulary.OWL_BOTTOM_OBJECT_PROPERTY.getIRI());
        dataPropertyIRIs.add(OWLRDFVocabulary.OWL_TOP_DATA_PROPERTY.getIRI());
        dataPropertyIRIs.add(OWLRDFVocabulary.OWL_BOTTOM_DATA_PROPERTY.getIRI());
        setupSinglePredicateMaps();
    }
    protected void addSingleValuedResPredicate(OWLRDFVocabulary v) {
        Map<String, String> map=new HashMap<String, String>();
        singleValuedResTriplesByPredicate.put(v.getIRI(), map);
    }
    protected void addrdfType(String obj) {
        rdfType.add(obj);
    }
    protected void setupSinglePredicateMaps() {
        addSingleValuedResPredicate(OWLRDFVocabulary.OWL_ON_PROPERTY);
        addSingleValuedResPredicate(OWLRDFVocabulary.OWL_SOME_VALUES_FROM);
        addSingleValuedResPredicate(OWLRDFVocabulary.OWL_ALL_VALUES_FROM);
        addSingleValuedResPredicate(OWLRDFVocabulary.OWL_ON_CLASS);
        addSingleValuedResPredicate(OWLRDFVocabulary.OWL_ON_DATA_RANGE);
    }
    protected void addBuiltInTypeTripleHandler(BuiltInTypeHandler handler) {
        builtInTypeTripleHandlers.put(handler.getTypeIRI(), handler);
    }
    protected void setupTypeTripleHandlers() {
        addBuiltInTypeTripleHandler(new TypeAsymmetricPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeClassHandler(this));
        addBuiltInTypeTripleHandler(new TypeDataPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeDatatypeHandler(this));
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
        addBuiltInTypeTripleHandler(new TypeObjectRestrictionHandler(this));
        addBuiltInTypeTripleHandler(new TypeDataRestrictionHandler(this));
        addBuiltInTypeTripleHandler(new TypeListHandler(this));
        addBuiltInTypeTripleHandler(new TypeAnnotationPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeDataRangeHandler(this));
        addBuiltInTypeTripleHandler(new TypeAllDifferentHandler(this));
        addBuiltInTypeTripleHandler(new TypeNegativePropertyAssertionHandler(this));
        addBuiltInTypeTripleHandler(new TypeAxiomHandler(this));
        addBuiltInTypeTripleHandler(new TypeRDFPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeRDFSClassHandler(this));
        addBuiltInTypeTripleHandler(new TypePropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeAllDisjointClassesHandler(this));
        addBuiltInTypeTripleHandler(new TypeAllDisjointPropertiesHandler(this));
        addBuiltInTypeTripleHandler(new TypeNamedIndividualHandler(this));
        addBuiltInTypeTripleHandler(new TypeIndividualVariableHandler(this));
        addBuiltInTypeTripleHandler(new TypeLiteralVariableHandler(this));
        addBuiltInTypeTripleHandler(new TypeAnnotationHandler(this));
    }
    protected void addPredicateHandler(TriplePredicateHandler predicateHandler) {
        predicateHandlers.put(predicateHandler.getPredicateIRI(), predicateHandler);
    }
    protected void setupPredicateHandlers() {
        predicateHandlers=CollectionFactory.createMap();
        addPredicateHandler(new TPDataPropertDomainHandler(this));
        addPredicateHandler(new TPDataPropertyRangeHandler(this));
        addPredicateHandler(new TPDifferentFromHandler(this));
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
        addPredicateHandler(new TPDeclaredAsHandler(this));
        addPredicateHandler(new TPHasKeyHandler(this));
        addPredicateHandler(new TPVersionIRIHandler(this));
        addPredicateHandler(new TPPropertyChainAxiomHandler(this));
        addPredicateHandler(new TPAnnotatedSourceHandler(this));
        addPredicateHandler(new TPPropertyDisjointWithHandler(this));
    }
    // We cache IRIs to save memory!!
    protected Map<String, String> IRIMap=CollectionFactory.createMap();
    int currentBaseCount=0;

    /**
     * Gets any annotations that were translated since the last call of this method (calling
     * this method clears the current pending annotations)
     *
     * @return The set (possibly empty) of pending annotations.
     */
    public Set<OWLAnnotation> getPendingAnnotations() {
        if (!pendingAnnotations.isEmpty()) {
            Set<OWLAnnotation> annos=new HashSet<OWLAnnotation>(pendingAnnotations);
            pendingAnnotations.clear();
            return annos;
        }
        else {
            return Collections.emptySet();
        }
    }
    public void setPendingAnnotations(Set<OWLAnnotation> annotations) {
        pendingAnnotations.clear();
        pendingAnnotations.addAll(annotations);
    }
    protected boolean isAnonymousNode(String iri) {
        return iri.startsWith("_:");
    }
    protected boolean isVariableNode(String iri) {
        return iri.startsWith("?");
    }
    protected void addAxiom(Axiom axiom) {
        ontology.add(axiom);
    }
    public Axiom getLastAddedAxiom() {
        return lastAddedAxiom;
    }
    protected void addOWLClass(String iri) {
        owlClassIRIs.add(iri);
    }
    protected void addOWLObjectProperty(String iri) {
        objectPropertyIRIs.add(iri);
    }
    protected void addIndividual(String iri) {
        individualIRIs.add(iri);
    }
    protected boolean isIndividual(String iri) {
        return individualIRIs.contains(iri);
    }
    protected void addRDFProperty(String iri) {
        propertyIRIs.add(iri);
    }
    protected boolean isRDFProperty(String iri) {
        return propertyIRIs.contains(iri);
    }
    protected void addOWLDataProperty(String iri) {
        dataPropertyIRIs.add(iri);
    }
    protected void addOWLDatatype(String iri) {
        dataRangeIRIs.add(iri);
    }
    public void addOWLDataRange(String iri) {
        dataRangeIRIs.add(iri);
    }
    protected void addRestriction(String iri) {
        restrictionIRIs.add(iri);
    }
    protected void addAnnotationProperty(String iri) {
        annotationPropertyIRIs.add(iri);
    }
    protected void addAnnotationIRI(String iri) {
        annotationIRIs.add(iri);
    }
    public boolean isRestriction(String iri) {
        return restrictionIRIs.contains(iri);
    }
    protected boolean isClass(String iri) {
        if (owlClassIRIs.contains(iri)) return true;
        return false;
    }
    protected boolean isObjectPropertyOnly(String iri) {
        if (iri==null) return false;
        if (dataPropertyIRIs.contains(iri)) return false;
        if (objectPropertyIRIs.contains(iri)) return true;
        else return false;
    }
    protected boolean isDataPropertyOnly(String iri) {
        if (objectPropertyIRIs.contains(iri)) return false;
        if (dataPropertyIRIs.contains(iri)) return true;
        else return false;
    }
    protected boolean isOntologyProperty(String iri) {
        return ontologyPropertyIRIs.contains(iri);
    }
    protected boolean isAnnotationProperty(String iri) {
        if (annotationPropertyIRIs.contains(iri)) return true;
        else return false;
    }
    protected boolean isOntology(String iri) {
        return ontologyIRIs.contains(iri);
    }
    public void addAnnotatedSource(String annotatedAnonSource, String annotationMainNode) {
        Set<String> annotationMainNodes=annotatedAnonSource2AnnotationMap.get(annotatedAnonSource);
        if (annotationMainNodes==null) {
            annotationMainNodes=new HashSet<String>();
            annotatedAnonSource2AnnotationMap.put(annotatedAnonSource, annotationMainNodes);
        }
        annotationMainNodes.add(annotationMainNode);
    }
    public Set<String> getAnnotatedSourceAnnotationMainNodes(String source) {
        Set<String> mainNodes=annotatedAnonSource2AnnotationMap.get(source);
        if (mainNodes!=null) {
            return mainNodes;
        }
        else {
            return Collections.emptySet();
        }
    }
    protected void consumeTriple(String subject, String predicate, String object) {
        isTriplePresent(subject, predicate, object, true);
    }
    protected void consumeTriple(String subject, String predicate, ILiteral con) {
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


    public void handle(String subject, String predicate, String object) {
        if (predicate.equals(OWLRDFVocabulary.RDF_TYPE.getIRI())) {
            BuiltInTypeHandler typeHandler=builtInTypeTripleHandlers.get(object);
            if (typeHandler!=null) {
                typeHandler.handleTriple(subject, predicate, object);
                // Consumed the triple - no further processing
                return;
            } else {
                addIndividual(subject);
            }
        }
        AbstractResourceTripleHandler handler=predicateHandlers.get(predicate);
        if (handler!=null) {
            if (handler.canHandle(subject, predicate, object)) handler.handleTriple(subject, predicate, object);
            return;
        }
    }
    public void handle(String subject, String predicate, ILiteral object) {
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


    protected void dumpRemainingTriples() {
        if (!logger.isLoggable(Level.FINE)) {
            return;
        }
        StringWriter sw=new StringWriter();
        PrintWriter w=new PrintWriter(sw);

        for (String predicate : singleValuedResTriplesByPredicate.keySet()) {
            Map<String, IRI> map=singleValuedResTriplesByPredicate.get(predicate);
            for (String subject : map.keySet()) {
                String object=map.get(subject);
                printTriple(subject, predicate, object, w);
            }
        }

        for (String predicate : singleValuedLitTriplesByPredicate.keySet()) {
            Map<String, OWLLiteral> map=singleValuedLitTriplesByPredicate.get(predicate);
            for (String subject : map.keySet()) {
                OWLLiteralObject object=map.get(subject);
                printTriple(subject, predicate, object, w);
            }
        }

        for (String subject : new ArrayList<String>(resTriplesBySubject.keySet())) {
            Map<String, Set<String>> map=resTriplesBySubject.get(subject);
            for (String predicate : new ArrayList<String>(map.keySet())) {
                Set<String> objects=map.get(predicate);
                for (String object : objects) {
                    printTriple(subject, predicate, object, w);
                }
            }
        }
        for (String subject : new ArrayList<String>(litTriplesBySubject.keySet())) {
            Map<String, Set<OWLLiteral>> map=litTriplesBySubject.get(subject);
            for (String predicate : new ArrayList<String>(map.keySet())) {
                Set<OWLLiteral> objects=map.get(predicate);
                for (OWLLiteral object : objects) {
                    printTriple(subject, predicate, object, w);
                }
            }
        }
        w.flush();
        logger.fine(sw.getBuffer().toString());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Debug stuff

    protected int count=0;


    protected void incrementTripleCount() {
        count++;
        if (tripleProcessor.isLoggable(Level.FINE) && count % 10000==0) {
            tripleProcessor.fine("Parsed: " + count + " triples");
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void startModel(String string) throws SAXException {
        count=0;
        t0=System.currentTimeMillis();
    }


    /**
     * This is where we do all remaining parsing
     */
    public void endModel() throws SAXException {
        try {
//        try {
            IRIMap.clear();

            tripleProcessor.fine("Total number of triples: " + count);
            RDFXMLOntologyFormat format=getOntologyFormat();
           
            // We need to mop up all remaining triples.  These triples will be in the
            // triples by subject map.  Other triples which reside in the triples by
            // predicate (single valued) triple aren't "root" triples for axioms.  First
            // we translate all system triples and then go for triples whose predicates
            // are not system/reserved vocabulary IRIs to translate these into ABox assertions
            // or annotationIRIs
            for (String subject : new ArrayList<String>(resTriplesBySubject.keySet())) {
                Map<String, Set<String>> map=resTriplesBySubject.get(subject);
                if (map==null) {
                    continue;
                }
                for (String predicate : new ArrayList<String>(map.keySet())) {
                    Set<String> objects=map.get(predicate);
                    if (objects==null) {
                        continue;
                    }
                    for (String object : new ArrayList<String>(objects)) {
                        // We don't handle x rdf:type owl:Axiom because these must be handled after everything else
                        // so that the "base triples" that represent the axiom with out the annotations get mopped up first
                       /* if (isVariableNode(object) && (!rdfType.contains(object))) {
                        	//System.out.println(object);
                        	OWLLiteralVariable con=getDataFactory().getOWLLiteralVariable(object);
                            AbstractLiteralTripleHandler skosHandler=skosTripleHandlers.get(predicate);
                            if (skosHandler!=null) {
                               //System.out.println("skoshandler is not null");
                               skosHandler.handleTriple(subject, predicate, con);
                               continue;
                            }
                            for (AbstractLiteralTripleHandler handler : literalTripleHandlers) {
                              if (handler.canHandleStreaming(subject, predicate, con)) {
                                 handler.handleTriple(subject, predicate, con);
                                 continue;
                              }
                            }
                        }*/
                        /*else*/ if (!(predicate.equals(OWLRDFVocabulary.RDF_TYPE.getIRI()) && (object.equals(OWLRDFVocabulary.OWL_AXIOM.getIRI()) || object.equals(OWLRDFVocabulary.OWL_ALL_DISJOINT_CLASSES.getIRI())))) {
                            handle(subject, predicate, object);
                        }
                     }                      	
                 }
            }
       
            // Now handle axiom annotations
            // TODO: TIDY UP THIS COPY AND PASTE HACK!
            for (String subject : new ArrayList<String>(resTriplesBySubject.keySet())) {
                Map<String, Set<String>> map=resTriplesBySubject.get(subject);
                if (map==null) {
                    continue;
                }
                for (String predicate : new ArrayList<String>(map.keySet())) {
                    Set<String> objects=map.get(predicate);
                    if (objects==null) {
                        continue;
                    }
                    for (String object : new ArrayList<String>(objects)) {
                        if ((predicate.equals(OWLRDFVocabulary.RDF_TYPE.getIRI()) && (object.equals(OWLRDFVocabulary.OWL_AXIOM.getIRI()) || object.equals(OWLRDFVocabulary.OWL_ALL_DISJOINT_CLASSES.getIRI())))) {
                            handle(subject, predicate, object);
                        }
                    }
                }
            }

            // TODO: TIDY UP!  This is a copy and paste hack!!
            // Now for the ABox assertions and annotationIRIs
            for (String subject : new ArrayList<String>(resTriplesBySubject.keySet())) {
                Map<String, Set<String>> map=resTriplesBySubject.get(subject);
                if (map==null) {
                    continue;
                }
                for (String predicate : new ArrayList<String>(map.keySet())) {
                    Set<String> objects=map.get(predicate);
                    if (objects==null) {
                        continue;
                    }
                    for (String object : new ArrayList<String>(objects)) {
                        if (isVariableNode(object) && isDataPropertyOnly(predicate))  {
                        	OWLLiteralVariable obj=getDataFactory().getOWLLiteralVariable(object);
                        	for (AbstractLiteralTripleHandler handler : literalTripleHandlers) {
                                if (handler.canHandle(subject, predicate, obj)) {
                                    handler.handleTriple(subject, predicate, obj);
                                    continue;
                                }
                            }
                        }
                        else {		
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


            for (String subject : new ArrayList<String>(litTriplesBySubject.keySet())) {
                Map<String, Set<OWLLiteral>> map=litTriplesBySubject.get(subject);
                if (map==null) {
                    continue;
                }
                for (String predicate : new ArrayList<String>(map.keySet())) {
                    Set<OWLLiteral> objects=map.get(predicate);
                    for (OWLLiteral object : new ArrayList<OWLLiteral>(objects)) {
                        handle(subject, predicate, object);
                    }
                }
            }

//        translateDanglingEntities();

            if (format!=null) {
                RDFOntologyFormat.ParserMetaData metaData=new RDFOntologyFormat.ParserMetaData(count, RDFOntologyFormat.OntologyHeaderStatus.PARSED_ONE_HEADER);
                format.setParserMetaData(metaData);
            }


            // Do we need to change the ontology IRI?
            String ontologyIRIToSet=chooseOntologyIRI();
            if (ontologyIRIToSet!=null) {
                String versionIRI=ontology.getOntologyID().getVersionIRI();
                applyChange(new SetOntologyID(ontology, new OWLOntologyID(ontologyIRIToSet, versionIRI)));
            }

            if (tripleProcessor.isLoggable(Level.FINE)) {
                tripleProcessor.fine("Loaded " + ontology.getOntologyID());
            }


            dumpRemainingTriples();
            cleanup();
        }
        catch (UnloadableImportException e) {
            throw new TranslatedUnloadedImportException(e);
        }
    }

    /**
     * Selects an IRI to be the ontology IRI
     *
     * @return An IRI that should be used as the IRI of the parsed ontology, or <code>null</code>
     *         if the parsed ontology does not have an IRI
     */
    protected String chooseOntologyIRI() {
        String ontologyIRIToSet=null;
        if (ontologyIRIs.isEmpty()) {
            // No ontology IRIs
            // We used to use the xml:base here.  But this is probably incorrect for OWL 2 now.
        }
        else if (ontologyIRIs.size()==1) {
            // Exactly one ontologyIRI
            String ontologyIRI=ontologyIRIs.iterator().next();
            if (!isAnonymousNode(ontologyIRI)) {
                ontologyIRIToSet=ontologyIRI;
            }
        }
        else {
            // We have multiple to choose from
            // Choose one that isn't the object of an annotation assertion
            Set<String> candidateIRIs=new HashSet<String>(ontologyIRIs);
            for (OWLAnnotation anno : ontology.getAnnotations()) {
                if (anno.getValue() instanceof IRI) {
                    String iri=(IRI) anno.getValue();
                    if (ontologyIRIs.contains(iri)) {
                        candidateIRIs.remove(iri);
                    }
                }
            }
            // Choose the first one parsed
            if (candidateIRIs.contains(firstOntologyIRI)) {
                ontologyIRIToSet=firstOntologyIRI;
            }
            else if (!candidateIRIs.isEmpty()) {
                // Just pick any
                ontologyIRIToSet=candidateIRIs.iterator().next();
            }

        }
        return ontologyIRIToSet;
    }


    protected void cleanup() {
        owlClassIRIs.clear();
        objectPropertyIRIs.clear();
        dataPropertyIRIs.clear();
        dataRangeIRIs.clear();
        restrictionIRIs.clear();
        listFirstLiteralTripleMap.clear();
        listFirstResourceTripleMap.clear();
        listRestTripleMap.clear();
        translatedClassExpression.clear();
        listIRIs.clear();
        resTriplesBySubject.clear();
        litTriplesBySubject.clear();
        singleValuedLitTriplesByPredicate.clear();
        singleValuedResTriplesByPredicate.clear();
    }


    public void addModelAttribte(String string, String string1) throws SAXException {
    }


    public void includeModel(String string, String string1) throws SAXException {

    }


    public void logicalURI(String string) throws SAXException {

    }


    public String checkForSynonym(String original) {
        if (!strict) {
            String synonymIRI=synonymMap.get(original);
            if (synonymIRI!=null) {
                return synonymIRI;
            }
        }
        return original;
    }

    public void statementWithLiteralValue(String subject, String predicate, String object, String lang, String datatype) throws SAXException {
        incrementTripleCount();
        String subjectIRI=getIRI(subject);
        String predicateIRI=getIRI(predicate);
        predicateString=checkForSynonym(predicateIRI);
        handleStreaming(subjectIRI, predicateIRI, object, datatype, lang);
    }


    public void statementWithResourceValue(String subject, String predicate, String object) throws SAXException {
        try {
            incrementTripleCount();
            String subjectIRI=getIRI(subject);
            String predicateIRI=getIRI(predicate);
            predicateIRI=checkForSynonym(predicateIRI);
            String objectIRI=checkForSynonym(getIRI(object));
            handleStreaming(subjectIRI, predicateIRI, objectIRI);
        }
        catch (UnloadableImportException e) {
            throw new TranslatedUnloadedImportException(e);
        }
    }


    protected int addCount=0;


    /**
     * Called when a resource triple has been parsed.
     *
     * @param subject The subject of the triple that has been parsed
     * @param predicate The predicate of the triple that has been parsed
     * @param object The object of the triple that has been parsed
     */
    protected void handleStreaming(String subject, String predicate, String object) throws UnloadableImportException {
        if (predicate.equals(RDF_TYPE.getIRI())) {
        	if (isVariableNode(subject)){	
        	  rdfType.add(object);
        	}
        	BuiltInTypeHandler handler=null;
//        	if (object.equals(OWL_FUNCTIONAL_PROPERTY.getIRI()))
//        		System.out.println(subject+"  "+object);
        	if (!isVariableNode(subject) || object.equals(OWL_FUNCTIONAL_PROPERTY.getIRI()) || object.equals(OWL_INVERSE_FUNCTIONAL_PROPERTY.getIRI()) || object.equals(OWL_REFLEXIVE_PROPERTY.getIRI()) || object.equals(OWL_IRREFLEXIVE_PROPERTY.getIRI()) || object.equals(OWL_SYMMETRIC_PROPERTY.getIRI()) || object.equals(OWL_ASYMMETRIC_PROPERTY.getIRI()) || object.equals(OWL_TRANSITIVE_PROPERTY.getIRI())) {
               handler=builtInTypeTripleHandlers.get(object);
//               System.out.println(handler);
        	}
            else  { 
            if (object.equals(OWL_CLASS.getIRI())){ 
          		handler=builtInTypeTripleHandlers.get(CLASS_VARIABLE.getIRI());
          	}
          	else if (object.equals(OWL_OBJECT_PROPERTY.getIRI())) 
           	   handler=builtInTypeTripleHandlers.get(OBJECT_PROPERTY_VARIABLE.getIRI());  
          	else if (object.equals(OWL_DATA_PROPERTY.getIRI())) 
           	   handler=builtInTypeTripleHandlers.get(DATA_PROPERTY_VARIABLE.getIRI());	
          	else if (object.equals("OWL_NAMED_INDIVIDUAL")) 
           	   handler=builtInTypeTripleHandlers.get(INDIVIDUAL_VARIABLE.getIRI());
            }// 	else if (object.equals("OWL_ClASS")) 
         //  	   handler=builtInTypeTripleHandlers.get("CLASS_VARIABLE");
                    	
            if (handler!=null) {
                if (handler.canHandleStreaming(subject, predicate, object)) {
                    handler.handleTriple(subject, predicate, object);
                    //System.out.println(handler);
                    // Consumed the triple - no further processing
                    return;
                }
            }
            else {
                // Individual?
                addIndividual(subject);
 //               System.out.println(subject);
            }
        }
        
       
        AbstractResourceTripleHandler handler=predicateHandlers.get(predicate);
        if (handler!=null) {
            if (handler.canHandleStreaming(subject, predicate, object)) {
                handler.handleTriple(subject, predicate, object);
                return;
            }
        }
//        if (addCount < 10000) {
//            if (!predicate.equals(OWLRDFVocabulary.OWL_ON_PROPERTY.getIRI())) {
//                addCount++;
//            }

//        }
        // Not consumed, so add the triple
        //if (predicate.equals(OWLRDFVocabulary.OWL_HAS_VALUE.getIRI()))
        //	System.out.println(subject+"   "+predicate+"  "+object);
        addTriple(subject, predicate, object);
    }

    protected void handleStreaming(IRI subject, IRI predicate, String literal, String datatype, String lang) {
        // Convert all literals to OWLConstants
    	OWLLiteral con=getOWLConstant(literal, datatype, lang);
        AbstractLiteralTripleHandler skosHandler=skosTripleHandlers.get(predicate);
        if (skosHandler!=null) {
            //System.out.println(skosHandler);
        	skosHandler.handleTriple(subject, predicate, con);
            return;
        }
        for (AbstractLiteralTripleHandler handler : literalTripleHandlers) {
            if (handler.canHandleStreaming(subject, predicate, con)) {
               // System.out.println(handler);
            	handler.handleTriple(subject, predicate, con);
                return;
            }
        }
        addTriple(subject, predicate, con);
 //       System.out.println("from literal "+subject+"  "+predicate+"  ");
    }


    /**
     * A convenience method to obtain an <code>OWLConstant</code>
     *
     * @param literal The literal - must NOT be <code>null</code>
     * @param datatype The data type - may be <code>null</code>
     * @param lang The lang - may be <code>null</code>
     * @return The <code>OWLConstant</code> (either typed or untyped depending on the params)
     */
    protected OWLLiteral getOWLConstant(String literal, String datatype, String lang) {
        if (datatype!=null) {
            return dataFactory.getOWLTypedLiteral(literal, dataFactory.getOWLDatatype(getIRI(datatype)));
        }
        else {
            return dataFactory.getOWLStringLiteral(literal, lang);
        }
    }


    public DataRange translateDataRange(String iri) {
        String oneOfObject=getResourceObject(iri, OWLRDFVocabulary.OWL_ONE_OF.getIRI(), true);
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
        String intersectionOfObject=getResourceObject(iri, OWLRDFVocabulary.OWL_INTERSECTION_OF.getIRI(), true);
        if (intersectionOfObject!=null) {
            Set<DataRange> dataRanges=translateToDataRangeSet(intersectionOfObject);
            return DataIntersectionOf.create(dataRanges);
        }
        String unionOfObject=getResourceObject(iri, OWLRDFVocabulary.OWL_UNION_OF.getIRI(), true);
        if (unionOfObject!=null) {
            Set<DataRange> dataRanges=translateToDataRangeSet(unionOfObject);
            return DataUnionOf.create(dataRanges);
        }
        // The plain complement of triple predicate is in here for legacy reasons
        String complementOfObject=getResourceObject(iri, OWLRDFVocabulary.OWL_DATATYPE_COMPLEMENT_OF.getIRI(), true);
        if (complementOfObject!=null) {
            DataRange operand=translateDataRange(complementOfObject);
            return DataComplementOf.create(operand);
        }
        String onDatatypeObject=getResourceObject(iri, OWLRDFVocabulary.OWL_ON_DATA_TYPE.getIRI(), true);
        if (onDatatypeObject!=null) {
            Datatype restrictedDataRange=(Datatype)translateDataRange(onDatatypeObject);
            // Consume the datatype type triple
            getResourceObject(iri, OWLRDFVocabulary.RDF_TYPE.getIRI(), true);
            Set<FacetRestriction> restrictions=new HashSet<FacetRestriction>();
            String facetRestrictionList=getResourceObject(iri, OWLRDFVocabulary.OWL_WITH_RESTRICTIONS.getIRI(), true);
            if (facetRestrictionList!=null) {
                restrictions=translateToFacetRestrictionSet(facetRestrictionList);
            }
            return DatatypeRestriction.create(restrictedDataRange, restrictions);
        }
        return Datatype.create(iri);
    }


    public DataPropertyExpression translateDataPropertyExpression(String iri) {
        if (isVariableNode(iri))
    	   return DataPropertyVariable.create(iri);
        else
        	return DataProperty.create(iri);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Basic node translation - translation of entities
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected Map<String,ObjectPropertyExpression> translatedProperties=new HashMap<String,ObjectPropertyExpression>();

    public ObjectPropertyExpression translateObjectPropertyExpression(String mainNode) {
        ObjectPropertyExpression prop=translatedProperties.get(mainNode);
        if (prop!=null) return prop;
        if (isAnonymousNode(mainNode)) {
            // Inverse of a property expression
            String inverseOfObject=getResourceObject(mainNode, OWLRDFVocabulary.OWL_INVERSE_OF.getIRI(), true);
            if (inverseOfObject!=null) {
                if (isAnonymousNode(inverseOfObject)) {
                    // double inverse, cancel out
                    String inverseOfInverseObject=getResourceObject(mainNode, OWLRDFVocabulary.OWL_INVERSE_OF.getIRI(), true);
                    return translateObjectPropertyExpression(inverseOfInverseObject);
                } else {
                    ObjectProperty otherProperty=(ObjectProperty)translateObjectPropertyExpression(inverseOfObject);
                    prop=ObjectInverseOf.create(otherProperty);
                }
            } else prop=ObjectInverseOf.create(ObjectProperty.create(mainNode));
        } else if (isVariableNode(mainNode)) 
            prop=ObjectPropertyVariable.create(mainNode);
        else prop=ObjectProperty.create(mainNode);
        translatedProperties.put(mainNode, prop);
        return prop;
    }
    public Individual translateIndividual(String node) {
        if (isVariableNode(node))
            return IndividualVariable.create(node);
        else 
            if (node.startsWith("_:")) return AnonymousIndividual.create(node);
            else return NamedIndividual.create(node);
    }
    
    public void translateAnnotations(String mainNode) {
        Set<String> predicates=getPredicatesBySubject(mainNode);
        for (String predicate : predicates)
            if (isAnnotationProperty(predicate)) {
                hasAnnotations=true;
                break;
            }
    }

    protected Map<String,ClassExpression> translatedClassExpression=new HashMap<String,ClassExpression>();

    public ClassExpression translateClassExpression(String mainNode) {
    	if (!isAnonymousNode(mainNode)) {
        	if (!isVariableNode(mainNode))
              return Clazz.create(mainNode);
        	else 
        	  return ClassVariable.create(mainNode);	
        }
        ClassExpression desc=translatedClassExpression.get(mainNode);
        if (desc==null) {
            ClassExpressionTranslator translator=classExpressionTranslatorSelector.getClassExpressionTranslator(mainNode);
            if (translator!=null) {
            	desc=translator.translate(mainNode);
                translatedClassExpression.put(mainNode, desc);
                restrictionIRIs.remove(mainNode);
            }
            else {
            	if (isVariableNode(mainNode)) return ClassVariable.create(mainNode);
            	else return Clazz.create(mainNode);
            }
        }
        return desc;
    }
    public ClassExpression getClassExpressionIfTranslated(String mainNode) {
        return translatedClassExpression.get(mainNode);
    }
    public List<ObjectPropertyExpression> translateToObjectPropertyList(String mainNode) {
        return objectPropertyListTranslator.translateList(mainNode);
    }
    public List<DataPropertyExpression> translateToDataPropertyList(String mainNode) {
        return dataPropertyListTranslator.translateList(mainNode);
    }
    public Set<ClassExpression> translateToClassExpressionSet(String mainNode) {
        return classExpressionListTranslator.translateToSet(mainNode);
    }
    public Set<ILiteral> translateToConstantSet(String mainNode) {
        return constantListTranslator.translateToSet(mainNode);
    }
    public Set<Individual> translateToIndividualSet(String mainNode) {
        return individualListTranslator.translateToSet(mainNode);
    }

    public Set<DataRange> translateToDataRangeSet(String mainNode) {
        return dataRangeListTranslator.translateToSet(mainNode);
    }

    public Set<FacetRestriction> translateToFacetRestrictionSet(String mainNode) {
        return faceRestrictionListTranslator.translateToSet(mainNode);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Set<String> getPredicatesBySubject(String subject) {
        Set<String> IRIs=new HashSet<String>();
        Map<String, Set<String>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            IRIs.addAll(predObjMap.keySet());
        }
        Map<String, Set<OWLLiteral>> predObjMapLit=litTriplesBySubject.get(subject);
        if (predObjMapLit!=null) {
            IRIs.addAll(predObjMapLit.keySet());
        }
        return IRIs;
    }

    public String getResourceObject(String subject, String predicate, boolean consume) {
        Map<String, IRI> subjPredMap=singleValuedResTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            String obj=subjPredMap.get(subject);
            if (consume) {
                subjPredMap.remove(subject);
            }
            return obj;
        }
        Map<String, Set<String>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<String> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (!objects.isEmpty()) {
                    String object=objects.iterator().next();
                    if (consume) {
                        objects.remove(object);
                    }
                    if (objects.isEmpty()) {
                        predObjMap.remove(predicate);
                        if (predObjMap.isEmpty()) {
                            resTriplesBySubject.remove(subject);
                        }
                    }
                    return object;
                }
            }
        }
        return null;
    }


    public ILiteral getLiteralObject(String subject, String predicate, boolean consume) {
        Map<String, ILiteral> subjPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            ILiteral obj=subjPredMap.get(subject);
            if (consume) {
                subjPredMap.remove(subject);
            }
            return obj;
        }
        Map<String, Set<ILiteral>> predObjMap=litTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<ILiteral> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (!objects.isEmpty()) {
                    ILiteral object=objects.iterator().next();
                    if (consume) {
                        objects.remove(object);
                    }
                    if (objects.isEmpty()) {
                        predObjMap.remove(predicate);
                    }
                    return object;
                }
            }
        }
        
       
        Map<String, Set<String>> predObjMap1=resTriplesBySubject.get(subject);
        if (predObjMap1!=null) {
            Set<String> objects=predObjMap1.get(predicate);
            if (objects!=null) {
                if (!objects.isEmpty()) {
                    String object=objects.iterator().next();
                    if (consume) {
                        objects.remove(object);
                    }
                    if (objects.isEmpty()) {
                        predObjMap1.remove(predicate);
                    }
                    if ((isVariableNode(object)) && (!rdfType.contains(object))) {
                        ILiteral litvar=LiteralVariable.create(object);
                        return litvar;  
                   	}
                }
            }
        }	        
        return null;
    }


    public boolean isTriplePresent(String subject, String predicate, String object, boolean consume) {
        Map<String, IRI> subjPredMap=singleValuedResTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            String obj=subjPredMap.get(subject);
            if (consume) {
                subjPredMap.remove(subject);
            }
            return obj!=null;
        }
        Map<String, Set<String>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<String> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (objects.contains(object)) {
                    if (consume) {
                        objects.remove(object);
                        if (objects.isEmpty()) {
                            predObjMap.remove(predicate);
                            if (predObjMap.isEmpty()) {
                                resTriplesBySubject.remove(subject);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }


    public boolean isTriplePresent(String subject, String predicate, ILiteral object, boolean consume) {
        Map<String, OWLLiteral> subjPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            OWLLiteralObject obj=subjPredMap.get(subject);
            if (consume) {
                subjPredMap.remove(subject);
            }
            return obj!=null;
        }
        Map<String, Set<OWLLiteral>> predObjMap=litTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<OWLLiteral> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (objects.contains(object)) {
                    if (consume) {
                        objects.remove(object);
                        if (objects.isEmpty()) {
                            predObjMap.remove(predicate);
                            if (predObjMap.isEmpty()) {
                                litTriplesBySubject.remove(subject);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }


    public boolean hasPredicate(String subject, String predicate) {
        Map<String, IRI> resPredMap=singleValuedResTriplesByPredicate.get(predicate);
        if (resPredMap!=null) {
            return resPredMap.containsKey(subject);
        }
        Map<String, OWLLiteral> litPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (litPredMap!=null) {
            return litPredMap.containsKey(subject);
        }
        Map<String, Set<String>> resPredObjMap=resTriplesBySubject.get(subject);
        if (resPredObjMap!=null) {
            boolean b=resPredObjMap.containsKey(predicate);
            if (b) {
                return true;
            }
        }
        Map<String, Set<OWLLiteral>> litPredObjMap=litTriplesBySubject.get(subject);
        if (litPredObjMap!=null) {
            return litPredObjMap.containsKey(predicate);
        }
        return false;
    }


    public boolean hasPredicateObject(String subject, String predicate, String object) {
        Map<String, IRI> predMap=singleValuedResTriplesByPredicate.get(predicate);
        if (predMap!=null) {
            String objectIRI=predMap.get(subject);
            if (objectIRI==null) {
                return false;
            }
            return objectIRI.equals(object);
        }
        Map<String, Set<String>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<String> objects=predObjMap.get(predicate);
            if (objects!=null) {
                return objects.contains(object);
            }
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public void addList(String iri) {
        listIRIs.add(iri);
    }


    public boolean isList(String iri, boolean consume) {
        if (consume) {
            return listIRIs.remove(iri);
        } else {
            return listIRIs.contains(iri);
        }
    }


    public void addRest(String subject, String object) {
        listRestTripleMap.put(subject, object);
    }


    public void addFirst(String subject, String object) {
        listFirstResourceTripleMap.put(subject, object);
    }


    public String getFirstResource(String subject, boolean consume) {
        if (consume) {
            return listFirstResourceTripleMap.remove(subject);
        }
        else {
            return listFirstResourceTripleMap.get(subject);
        }
    }


    public ILiteral getFirstLiteral(String subject) {
        return listFirstLiteralTripleMap.get(subject);
    }


    public String getRest(String subject, boolean consume) {
        if (consume) {
            return listRestTripleMap.remove(subject);
        }
        else {
            return listRestTripleMap.get(subject);
        }
    }


    public void addFirst(String subject, ILiteral object) {
        listFirstLiteralTripleMap.put(subject, object);
    }


    public void addOntology(String iri) {
        if (ontologyIRIs.isEmpty()) {
            firstOntologyIRI=iri;
        }
        ontologyIRIs.add(iri);
    }

    public Set<String> getOntologies() {
        return ontologyIRIs;
    }

    public void addReifiedAxiom(String axiomIRI, OWLAxiom axiom) {
        reifiedAxiomsMap.put(axiomIRI, axiom);
    }


    public boolean isAxiom(String iri) {
        return reifiedAxiomsMap.containsKey(iri);
    }


    public OWLAxiom getAxiom(String iri) {
        return reifiedAxiomsMap.get(iri);
    }


    public boolean isDataRange(String iri) {
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
        object directly in varous maps.
    */

    // Resource triples

    // Subject, predicate, object
    protected Map<String, Map<String, Set<String>>> resTriplesBySubject=new HashMap<String, Map<String,Set<String>>>();
    // Predicate, subject, object
    protected Map<String, Map<String, String>> singleValuedResTriplesByPredicate=new HashMap<String, Map<String,String>>();
    // Literal triples
    protected Map<String, Map<String, Set<ILiteral>>> litTriplesBySubject=new HashMap<String, Map<String,Set<ILiteral>>>();
    // Predicate, subject, object
    protected Map<String, Map<String, ILiteral>> singleValuedLitTriplesByPredicate=new HashMap<String, Map<String,ILiteral>>();

    public void addTriple(String subject, String predicate, String object) {
        Map<String, String> subjObjMap=singleValuedResTriplesByPredicate.get(predicate);
        if (subjObjMap!=null) {
            subjObjMap.put(subject, object);
        }
        else {
            Map<String, Set<String>> map=resTriplesBySubject.get(subject);
            if (map==null) {
                map=CollectionFactory.createMap();
                resTriplesBySubject.put(subject, map);
            }
            Set<String> objects=map.get(predicate);
            if (objects==null) {
                objects=new FakeSet();
                map.put(predicate, objects);
            }
            objects.add(object);
        }
    }


    public void addTriple(String subject, String predicate, ILiteral con) {
        Map<String, ILiteral> subjObjMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjObjMap!=null) {
            subjObjMap.put(subject, con);
        }
        else {
            Map<String, Set<ILiteral>> map=litTriplesBySubject.get(subject);
            if (map==null) {
                map=CollectionFactory.createMap();
                litTriplesBySubject.put(subject, map);
            }
            Set<ILiteral> objects=map.get(predicate);
            if (objects==null) {
                objects=new FakeSet();
                map.put(predicate, objects);
            }
            objects.add(con);
        }
    }
    protected static class FakeSet<O> extends ArrayList<O> implements Set<O> {
        public FakeSet() {}
        public FakeSet(Collection<? extends O> c) {
            super(c);
        }
    }
    public boolean hasAnnotations() {
        return hasAnnotations;
    }
}
