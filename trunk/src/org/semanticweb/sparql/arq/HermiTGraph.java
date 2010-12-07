package org.semanticweb.sparql.arq;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.monitor.CountingMonitor;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.sparql.bgpevaluation.Skolemizer;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.dataranges.Datatype;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.properties.AnnotationProperty;
import org.semanticweb.sparql.owlbgp.model.properties.DataProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;

import com.hp.hpl.jena.graph.BulkUpdateHandler;
import com.hp.hpl.jena.graph.Capabilities;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphEventManager;
import com.hp.hpl.jena.graph.GraphStatisticsHandler;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Reifier;
import com.hp.hpl.jena.graph.TransactionHandler;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.TripleMatch;
import com.hp.hpl.jena.graph.query.QueryHandler;
import com.hp.hpl.jena.shared.AddDeniedException;
import com.hp.hpl.jena.shared.DeleteDeniedException;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class HermiTGraph implements Graph {
	
	protected final OWLOntology m_ontology;
	protected final Set<String> m_skolemConstants;
	protected final Set<Clazz> m_classes;
    protected final Set<Datatype> m_datatypes;
    protected final Set<ObjectProperty> m_objectProperties;
    protected final Set<DataProperty> m_dataProperties;
    protected final Set<AnnotationProperty> m_annotationProperties;
    protected final Set<NamedIndividual> m_individuals;
    protected final Reasoner m_reasoner;
    protected final CountingMonitor m_countingMonitor;
    
	public HermiTGraph(final OWLOntology ontology) {
	    Skolemizer skolemizer=new Skolemizer();
		this.m_ontology=skolemizer.skolemize(ontology);
		m_skolemConstants=skolemizer.getSkolems();
		m_classes=new HashSet<Clazz>();
        for (OWLClass cls : m_ontology.getClassesInSignature(true))
            m_classes.add(Clazz.create(cls.toString()));
        m_datatypes=new HashSet<Datatype>();
        for (OWLDatatype dt : m_ontology.getDatatypesInSignature(true))
            if (!dt.isBuiltIn()) 
                m_datatypes.add(Datatype.create(dt.toString()));
        m_objectProperties=new HashSet<ObjectProperty>();
        for (OWLObjectProperty op : m_ontology.getObjectPropertiesInSignature(true))
            m_objectProperties.add(ObjectProperty.create(op.toString()));
        m_dataProperties=new HashSet<DataProperty>();
        for (OWLDataProperty dp : m_ontology.getDataPropertiesInSignature(true))
            m_dataProperties.add(DataProperty.create(dp.toString()));
        m_annotationProperties=new HashSet<AnnotationProperty>();
        for (OWLAnnotationProperty ap : m_ontology.getAnnotationPropertiesInSignature())
            m_annotationProperties.add(AnnotationProperty.create(ap.toString()));
        m_individuals=new HashSet<NamedIndividual>();
        for (OWLNamedIndividual ind : m_ontology.getIndividualsInSignature(true))
            m_individuals.add(NamedIndividual.create(ind.toString()));
        m_countingMonitor=new CountingMonitor();
        Configuration c=new Configuration();
        c.monitor=m_countingMonitor;
        m_reasoner=new Reasoner(c, m_ontology);
	}
	public Set<String>  getSkolemConstants() {
        return this.m_skolemConstants;
    }
	public Reasoner getReasoner() {
        return this.m_reasoner;
    }
	public OWLOntology getDefaultOntology() {
		return this.m_ontology;
	}
    public Set<Clazz> getClassesInSignature() {
        return m_classes;
    }
    public Set<Datatype> getDatatypesInSignature() {
        return m_datatypes;
    }
    public Set<ObjectProperty> getObjectPropertiesInSignature() {
        return m_objectProperties;
    }
    public Set<DataProperty> getDataPropertiesInSignature() {
        return m_dataProperties;
    }
    public Set<AnnotationProperty> getAnnotationPropertiesInSignature() {
        return m_annotationProperties;
    }
    public Set<NamedIndividual> getIndividualsInSignature() {
        return m_individuals;
    }
    @Override
	public void close() {
	}
	@Override
	public boolean contains(Triple t) {
		return false;
	}
	@Override
	public boolean contains(Node s, Node p, Node o) {
		return false;
	}
	@Override
	public void delete(Triple t) throws DeleteDeniedException {
	    throw new DeleteDeniedException("HermiT does not accept deletion of triples.");
	}
	@Override
	public boolean dependsOn(Graph other) {
		return false;
	}
	@Override
	public ExtendedIterator<Triple> find(TripleMatch m) {
		return null;
	}
	@Override
	public ExtendedIterator<Triple> find(Node s, Node p, Node o) {
		return null;
	}
	@Override
	public BulkUpdateHandler getBulkUpdateHandler() {
		return null;
	}
	@Override
	public Capabilities getCapabilities() {
		return null;
	}
	@Override
	public GraphEventManager getEventManager() {
		return null;
	}
	@Override
	public PrefixMapping getPrefixMapping() {
		return null;
	}
	@Override
	public Reifier getReifier() {
		return null;
	}
	@Override
	public GraphStatisticsHandler getStatisticsHandler() {
		return null;
	}
	@Override
	public TransactionHandler getTransactionHandler() {
		return null;
	}
	@Override
	public boolean isClosed() {
		return false;
	}
	@Override
	public boolean isEmpty() {
		return m_ontology.isEmpty();
	}
	@Override
	public boolean isIsomorphicWith(Graph g) {
		return false;
	}
	@Override
	public QueryHandler queryHandler() {
		return null;
	}
	@Override
	public int size() {
		return m_ontology.getAxiomCount();
	}
	@Override
	public void add(Triple t) throws AddDeniedException {
	    throw new AddDeniedException("HermiT does not accept additions to graphs.");
	}
}
