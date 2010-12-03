package org.semanticweb.sparql.evaluation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.sparql.core.DatasetGraph;

public class HermiTDatasetGraph implements DatasetGraph {
    protected static int ontologyCounter=0;
    
    protected final HermiTGraph defaultgraph;
    protected final Map<String,HermiTGraph> namedGraphURIsToGraphs;
    
    public HermiTDatasetGraph(HermiTGraph defaultGraph, Map<String,HermiTGraph> namedGraphURIsToGraphs) {
        this.defaultgraph=defaultGraph;
        this.namedGraphURIsToGraphs=new HashMap<String,HermiTGraph>();
        for (String oName : namedGraphURIsToGraphs.keySet())
            this.namedGraphURIsToGraphs.put(oName, namedGraphURIsToGraphs.get(oName));
    } 
    public HermiTDatasetGraph(OWLOntology defaultOntology, Map<String,OWLOntology> namedGraphURIsToOntologies) throws OWLOntologyCreationException {
        this.defaultgraph=new HermiTGraph(defaultOntology);
        namedGraphURIsToGraphs=new HashMap<String,HermiTGraph>();
        for (String oName : namedGraphURIsToOntologies.keySet())
            namedGraphURIsToGraphs.put(oName, new HermiTGraph(namedGraphURIsToOntologies.get(oName)));
    } 
	@Override
	public void close() {
	}
	public boolean containsGraph(Node graphNode) {
	    return namedGraphURIsToGraphs.containsKey(graphNode.getURI());
	}
	public Graph getDefaultGraph() {
		return defaultgraph;
	}
	@Override
	public Graph getGraph(Node graphNode) {
	    return namedGraphURIsToGraphs.get(graphNode.getURI());
	}
	@Override
	public Lock getLock() {
		return null;
	}
	@Override
	public Iterator<Node> listGraphNodes() {
	    final Iterator<String> internalGraphNameIterator=namedGraphURIsToGraphs.keySet().iterator();
	    Iterator<Node> graphNameIterator=new Iterator<Node>() {
            public boolean hasNext() {
                return internalGraphNameIterator.hasNext();
            }
            public Node next() {
                return Node.createURI(internalGraphNameIterator.next());
            }
            public void remove() {
                throw new UnsupportedOperationException("Internal Error: HermiT dataset models do notallow removal from an iterator. ");
            }
        };
		return graphNameIterator;
	}
	@Override
	public int size() {
		return namedGraphURIsToGraphs.keySet().size()+1;
	}
}
