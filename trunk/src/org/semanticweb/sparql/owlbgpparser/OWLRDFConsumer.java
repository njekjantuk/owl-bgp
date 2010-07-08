package org.semanticweb.sparql.owlbgpparser;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.AnonymousIndividual;
import org.semanticweb.sparql.owlbgp.model.Axiom;
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
import org.semanticweb.sparql.owlbgp.model.Datatype.OWL2_DATATYPES;
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
    protected Map<String, Set<String>> annotatedAnonSource2AnnotationMap=new HashMap<String, Set<String>>();
    protected ClassExpressionTranslatorSelector classExpressionTranslatorSelector;
    protected Axiom lastAddedAxiom;
    protected Set<Axiom> axioms=new HashSet<Axiom>();
    protected Map<String, String> synonymMap;
    protected Set<String> rdfType; 
    
    protected boolean hasAnnotations=false;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public OWLRDFConsumer() {
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
        annotationPropertyIRIs=new HashSet<String>();
        for (String iri : Vocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS) {
            annotationPropertyIRIs.add(iri);
        }
        annotationIRIs=new HashSet<String>();
        ontologyPropertyIRIs=new HashSet<String>();
        ontologyPropertyIRIs.add(Vocabulary.OWL_PRIOR_VERSION.getIRI());
        ontologyPropertyIRIs.add(Vocabulary.OWL_BACKWARD_COMPATIBLE_WITH.getIRI());
        ontologyPropertyIRIs.add(Vocabulary.OWL_INCOMPATIBLE_WITH.getIRI());

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
        faceRestrictionListTranslator=new OptimisedListTranslator<FacetRestriction>(this, new FacetRestrictionListItemTranslator(this));
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
        
        for (OWL2_DATATYPES dt : OWL2_DATATYPES.values()) {
            dataRangeIRIs.add(dt.getDatatype().getIRIString());
        }
        dataRangeIRIs.add(Vocabulary.RDFS_LITERAL.getIRI());        
        rdfType=new HashSet<String>();
        owlClassIRIs.add(Vocabulary.OWL_THING.getIRI());
        owlClassIRIs.add(Vocabulary.OWL_NOTHING.getIRI());
        objectPropertyIRIs.add(Vocabulary.OWL_TOP_OBJECT_PROPERTY.getIRI());
        objectPropertyIRIs.add(Vocabulary.OWL_BOTTOM_OBJECT_PROPERTY.getIRI());
        dataPropertyIRIs.add(Vocabulary.OWL_TOP_DATA_PROPERTY.getIRI());
        dataPropertyIRIs.add(Vocabulary.OWL_BOTTOM_DATA_PROPERTY.getIRI());
        setupSinglePredicateMaps();
    }
    protected void addSingleValuedResPredicate(Vocabulary v) {
        Map<String, String> map=new HashMap<String, String>();
        singleValuedResTriplesByPredicate.put(v.getIRI(), map);
    }
    protected void addrdfType(String obj) {
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
        predicateHandlers=new HashMap<String, TriplePredicateHandler>();
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
    // We cache IRIs to save memory!!
    protected Map<String, String> IRIMap=new HashMap<String, String>();
    int currentBaseCount=0;

    protected boolean isAnonymousNode(String iri) {
        return iri.startsWith("_:");
    }
    protected boolean isVariableNode(String iri) {
        return iri.startsWith("?");
    }
    protected void addAxiom(Axiom axiom) {
        axioms.add(axiom);
    }
    public Set<Axiom> getParsedAxioms() {
        return axioms;
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
        if (predicate.equals(Vocabulary.RDF_TYPE.getIRI())) {
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
    protected void dumpRemainingTriples(PrintWriter w) {
        for (String predicate : singleValuedResTriplesByPredicate.keySet()) {
            Map<String, String> map=singleValuedResTriplesByPredicate.get(predicate);
            for (String subject : map.keySet()) {
                String object=map.get(subject);
                printTriple(subject, predicate, object, w);
            }
        }
        for (String predicate : singleValuedLitTriplesByPredicate.keySet()) {
            Map<String, ILiteral> map=singleValuedLitTriplesByPredicate.get(predicate);
            for (String subject : map.keySet()) {
                ILiteral object=map.get(subject);
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
            Map<String, Set<ILiteral>> map=litTriplesBySubject.get(subject);
            for (String predicate : new ArrayList<String>(map.keySet())) {
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
        for (String subject : new ArrayList<String>(resTriplesBySubject.keySet())) {
            Map<String, Set<String>> map=resTriplesBySubject.get(subject);
            if (map==null) continue;
            for (String predicate : new ArrayList<String>(map.keySet())) {
                Set<String> objects=map.get(predicate);
                if (objects==null) continue;
                for (String object : new ArrayList<String>(objects))
                    if (!(predicate.equals(Vocabulary.RDF_TYPE.getIRI()) && (object.equals(Vocabulary.OWL_AXIOM.getIRI()) || object.equals(Vocabulary.OWL_ALL_DISJOINT_CLASSES.getIRI()))))
                        handle(subject, predicate, object);
            }
        }
        // Now handle axiom annotations
        // TODO: TIDY UP THIS COPY AND PASTE HACK!
        for (String subject : new ArrayList<String>(resTriplesBySubject.keySet())) {
            Map<String, Set<String>> map=resTriplesBySubject.get(subject);
            if (map==null) continue;
            for (String predicate : new ArrayList<String>(map.keySet())) {
                Set<String> objects=map.get(predicate);
                if (objects==null) continue;
                for (String object : new ArrayList<String>(objects))
                    if ((predicate.equals(Vocabulary.RDF_TYPE.getIRI()) && (object.equals(Vocabulary.OWL_AXIOM.getIRI()) || object.equals(Vocabulary.OWL_ALL_DISJOINT_CLASSES.getIRI())))) 
                        handle(subject, predicate, object);
            }
        }
        // TODO: TIDY UP!  This is a copy and paste hack!!
        // Now for the ABox assertions and annotationIRIs
        for (String subject : new ArrayList<String>(resTriplesBySubject.keySet())) {
            Map<String, Set<String>> map=resTriplesBySubject.get(subject);
            if (map==null) continue;
            for (String predicate : new ArrayList<String>(map.keySet())) {
                Set<String> objects=map.get(predicate);
                if (objects==null) continue;
                for (String object : new ArrayList<String>(objects)) {
                    if (isVariableNode(object) && isDataPropertyOnly(predicate)) {
                    	ILiteral obj=LiteralVariable.create(object);
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
        for (String subject : new ArrayList<String>(litTriplesBySubject.keySet())) {
            Map<String, Set<ILiteral>> map=litTriplesBySubject.get(subject);
            if (map==null) continue;
            for (String predicate : new ArrayList<String>(map.keySet())) {
                Set<ILiteral> objects=map.get(predicate);
                for (ILiteral object : new ArrayList<ILiteral>(objects))
                    handle(subject, predicate, object);
            }
        }
        cleanup();
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
    public void statementWithLiteralValue(String subject, String predicate, String object, String lang, Datatype datatype) throws SAXException {
        handleStreaming(subject, predicate, object, lang, datatype);
    }
    public void statementWithResourceValue(String subject, String predicate, String object) throws SAXException {
        handleStreaming(subject, predicate, object);
    }
    protected void handleStreaming(String subject, String predicate, String object) {
        if (predicate.equals(Vocabulary.RDF_TYPE.getIRI())) {
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
                // Individual?
                addIndividual(subject);
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
    protected void handleStreaming(String subject, String predicate, String literal, String lang, Datatype datatype) {
    	ILiteral con=getOWLConstant(literal, lang, datatype);
        for (AbstractLiteralTripleHandler handler : literalTripleHandlers) {
            if (handler.canHandleStreaming(subject, predicate, con)) {
            	handler.handleTriple(subject, predicate, con);
                return;
            }
        }
        addTriple(subject, predicate, con);
    }
    protected ILiteral getOWLConstant(String literal, String lang, Datatype datatype) {
        return Literal.create(literal, lang, datatype);
    }
    public DataRange translateDataRange(String iri) {
        String oneOfObject=getResourceObject(iri, Vocabulary.OWL_ONE_OF.getIRI(), true);
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
        String intersectionOfObject=getResourceObject(iri, Vocabulary.OWL_INTERSECTION_OF.getIRI(), true);
        if (intersectionOfObject!=null) {
            Set<DataRange> dataRanges=translateToDataRangeSet(intersectionOfObject);
            return DataIntersectionOf.create(dataRanges);
        }
        String unionOfObject=getResourceObject(iri, Vocabulary.OWL_UNION_OF.getIRI(), true);
        if (unionOfObject!=null) {
            Set<DataRange> dataRanges=translateToDataRangeSet(unionOfObject);
            return DataUnionOf.create(dataRanges);
        }
        String onDatatypeObject=getResourceObject(iri, Vocabulary.OWL_ON_DATA_TYPE.getIRI(), true);
        if (onDatatypeObject!=null) {
            Datatype restrictedDataRange=(Datatype)translateDataRange(onDatatypeObject);
            // Consume the datatype type triple
            getResourceObject(iri, Vocabulary.RDF_TYPE.getIRI(), true);
            Set<FacetRestriction> restrictions=new HashSet<FacetRestriction>();
            String facetRestrictionList=getResourceObject(iri, Vocabulary.OWL_WITH_RESTRICTIONS.getIRI(), true);
            if (facetRestrictionList!=null) {
                restrictions=translateToFacetRestrictionSet(facetRestrictionList);
            }
            return DatatypeRestriction.create(restrictedDataRange, restrictions);
        }
        return Datatype.create(iri);
    }
    public DataPropertyExpression translateDataPropertyExpression(String iri) {
        if (isVariableNode(iri)) return DataPropertyVariable.create(iri);
        else return DataProperty.create(iri);
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
            String inverseOfObject=getResourceObject(mainNode, Vocabulary.OWL_INVERSE_OF.getIRI(), true);
            if (inverseOfObject!=null) {
                if (isAnonymousNode(inverseOfObject)) {
                    // double inverse, cancel out
                    String inverseOfInverseObject=getResourceObject(mainNode, Vocabulary.OWL_INVERSE_OF.getIRI(), true);
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
        Map<String, Set<ILiteral>> predObjMapLit=litTriplesBySubject.get(subject);
        if (predObjMapLit!=null) {
            IRIs.addAll(predObjMapLit.keySet());
        }
        return IRIs;
    }
    public String getResourceObject(String subject, String predicate, boolean consume) {
        Map<String, String> subjPredMap=singleValuedResTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            String obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
            return obj;
        }
        Map<String, Set<String>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<String> objects=predObjMap.get(predicate);
            if (objects!=null) {
                if (!objects.isEmpty()) {
                    String object=objects.iterator().next();
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
    public ILiteral getLiteralObject(String subject, String predicate, boolean consume) {
        Map<String, ILiteral> subjPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            ILiteral obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
            return obj;
        }
        Map<String, Set<ILiteral>> predObjMap=litTriplesBySubject.get(subject);
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
        Map<String, Set<String>> predObjMap1=resTriplesBySubject.get(subject);
        if (predObjMap1!=null) {
            Set<String> objects=predObjMap1.get(predicate);
            if (objects!=null) {
                if (!objects.isEmpty()) {
                    String object=objects.iterator().next();
                    if (consume) objects.remove(object);
                    if (objects.isEmpty()) predObjMap1.remove(predicate);
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
        Map<String, String> subjPredMap=singleValuedResTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            String obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
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
    public boolean isTriplePresent(String subject, String predicate, ILiteral object, boolean consume) {
        Map<String, ILiteral> subjPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap!=null) {
            ILiteral obj=subjPredMap.get(subject);
            if (consume) subjPredMap.remove(subject);
            return obj!=null;
        }
        Map<String, Set<ILiteral>> predObjMap=litTriplesBySubject.get(subject);
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
    public boolean hasPredicate(String subject, String predicate) {
        Map<String, String> resPredMap=singleValuedResTriplesByPredicate.get(predicate);
        if (resPredMap!=null) return resPredMap.containsKey(subject);
        Map<String, ILiteral> litPredMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (litPredMap!=null) return litPredMap.containsKey(subject);
        Map<String, Set<String>> resPredObjMap=resTriplesBySubject.get(subject);
        if (resPredObjMap!=null) {
            boolean b=resPredObjMap.containsKey(predicate);
            if (b) return true;
        }
        Map<String, Set<ILiteral>> litPredObjMap=litTriplesBySubject.get(subject);
        if (litPredObjMap!=null) return litPredObjMap.containsKey(predicate);
        return false;
    }
    public boolean hasPredicateObject(String subject, String predicate, String object) {
        Map<String, String> predMap=singleValuedResTriplesByPredicate.get(predicate);
        if (predMap!=null) {
            String objectIRI=predMap.get(subject);
            if (objectIRI==null) return false;
            return objectIRI.equals(object);
        }
        Map<String, Set<String>> predObjMap=resTriplesBySubject.get(subject);
        if (predObjMap!=null) {
            Set<String> objects=predObjMap.get(predicate);
            if (objects!=null) return objects.contains(object);
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addList(String iri) {
        listIRIs.add(iri);
    }
    public boolean isList(String iri, boolean consume) {
        if (consume) return listIRIs.remove(iri);
        else return listIRIs.contains(iri);
    }
    public void addRest(String subject, String object) {
        listRestTripleMap.put(subject, object);
    }
    public void addFirst(String subject, String object) {
        listFirstResourceTripleMap.put(subject, object);
    }
    public String getFirstResource(String subject, boolean consume) {
        if (consume) return listFirstResourceTripleMap.remove(subject);
        else return listFirstResourceTripleMap.get(subject);
    }
    public ILiteral getFirstLiteral(String subject) {
        return listFirstLiteralTripleMap.get(subject);
    }
    public String getRest(String subject, boolean consume) {
        if (consume) return listRestTripleMap.remove(subject);
        else return listRestTripleMap.get(subject);
    }
    public void addFirst(String subject, ILiteral object) {
        listFirstLiteralTripleMap.put(subject, object);
    }
    public void addReifiedAxiom(String axiomIRI, Axiom axiom) {
        reifiedAxiomsMap.put(axiomIRI, axiom);
    }
    public boolean isAxiom(String iri) {
        return reifiedAxiomsMap.containsKey(iri);
    }
    public Axiom getAxiom(String iri) {
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
        object directly in various maps.
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
        if (subjObjMap!=null) subjObjMap.put(subject, object);
        else {
            Map<String, Set<String>> map=resTriplesBySubject.get(subject);
            if (map==null) {
                map=new HashMap<String, Set<String>>();
                resTriplesBySubject.put(subject, map);
            }
            Set<String> objects=map.get(predicate);
            if (objects==null) {
                objects=new FakeSet<String>();
                map.put(predicate, objects);
            }
            objects.add(object);
        }
    }
    public void addTriple(String subject, String predicate, ILiteral con) {
        Map<String, ILiteral> subjObjMap=singleValuedLitTriplesByPredicate.get(predicate);
        if (subjObjMap!=null) subjObjMap.put(subject, con);
        else {
            Map<String, Set<ILiteral>> map=litTriplesBySubject.get(subject);
            if (map==null) {
                map=new HashMap<String, Set<ILiteral>>();
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
    public boolean hasAnnotations() {
        return hasAnnotations;
    }
}
