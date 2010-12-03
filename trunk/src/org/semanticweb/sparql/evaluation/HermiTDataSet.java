package org.semanticweb.sparql.evaluation;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.sparql.core.DatasetGraph;

public class HermiTDataSet implements Dataset {
    protected static int ontologyCounter=0;
    
    protected final HermiTGraph defaultGraph;
    protected final Map<String,HermiTGraph> namedGraphURIsToGraphs;
    
    public HermiTDataSet(File defaultGraphFile) throws OWLOntologyCreationException {
        this(defaultGraphFile,null);
    } 
    public HermiTDataSet(File defaultGraphFile, List<File> namedGraphFiles) throws OWLOntologyCreationException {
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        OWLOntology defaultOntology=manager.loadOntologyFromOntologyDocument(defaultGraphFile);
        defaultGraph=new HermiTGraph(defaultOntology);
        namedGraphURIsToGraphs=new HashMap<String,HermiTGraph>();
        if (namedGraphFiles!=null) {
            for (File namedGraphFile : namedGraphFiles) {
                OWLOntology o=manager.loadOntologyFromOntologyDocument(namedGraphFile);
                namedGraphURIsToGraphs.put(o.getOntologyID().getOntologyIRI().toString(), new HermiTGraph(o));
            }
        }
    }
    public HermiTDataSet(String defaultGraphURI) throws OWLOntologyCreationException {
        this(defaultGraphURI,null);
    } 
    public HermiTDataSet(String defaultGraphURI, List<String> namedGraphURIs) throws OWLOntologyCreationException {
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        OWLOntology defaultOntology=manager.loadOntology(IRI.create(defaultGraphURI));
        defaultGraph=new HermiTGraph(defaultOntology);
        namedGraphURIsToGraphs=new HashMap<String,HermiTGraph>();
        if (namedGraphURIs!=null) {
            for (String namedGraphURI : namedGraphURIs) {
                OWLOntology o=manager.loadOntology(IRI.create(namedGraphURI));
                namedGraphURIsToGraphs.put(namedGraphURI, new HermiTGraph(o));
            }
        }
    }
	public HermiTDataSet(List<String> graphURIs, List<String> namedGraphURIs) throws OWLOntologyCreationException {
	    OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        ontologyCounter++;
        OWLOntology ontology=manager.createOntology(IRI.create("http://sparql.org/defaultOntology"+ontologyCounter+"/"));
        for (String uri : graphURIs) {
            OWLOntology o=manager.loadOntology(IRI.create(uri));
            manager.addAxioms(ontology, o.getLogicalAxioms());
            manager.removeOntology(o);
        }
        defaultGraph=new HermiTGraph(ontology);
        namedGraphURIsToGraphs=new HashMap<String,HermiTGraph>();
        for (String namedGraphURI : namedGraphURIs) {
            OWLOntology o=manager.loadOntology(IRI.create(namedGraphURI));
            namedGraphURIsToGraphs.put(namedGraphURI, new HermiTGraph(o));
        }
	} 
    public HermiTDataSet(List<String> graphURIs, Map<String,HermiTGraph> namedGraphURIsToGraphs) throws OWLOntologyCreationException {
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        ontologyCounter++;
        OWLOntology ontology=manager.createOntology(IRI.create("http://sparql.org/defaultOntology"+ontologyCounter+"/"));
        for (String uri : graphURIs) {
            OWLOntology o=manager.loadOntology(IRI.create(uri));
            manager.addAxioms(ontology, o.getLogicalAxioms());
            manager.removeOntology(o);
        }
        defaultGraph=new HermiTGraph(ontology);
        this.namedGraphURIsToGraphs=namedGraphURIsToGraphs;
    } 
    public Map<String,HermiTGraph> getNamedGraphURIsToGraphs() {
        return namedGraphURIsToGraphs;
    } 
	public DatasetGraph asDatasetGraph() {
		return new HermiTDatasetGraph(defaultGraph, namedGraphURIsToGraphs);
	} 
    public HermiTGraph getDefaultGraph() {
        return defaultGraph;
    }
    public HermiTGraph getNamedGraph(String uri) {
        return namedGraphURIsToGraphs.get(uri);
    }
	public void close() {
	}
	public boolean containsNamedModel(String uri) {
		return namedGraphURIsToGraphs.containsKey(uri);
	}
	public Model getDefaultModel() {
		return null;
	}
	public Lock getLock() {
		return null;
	}
	public Model getNamedModel(String uri) {
		return null;
	}
	public Iterator<String> listNames() {
		return namedGraphURIsToGraphs.keySet().iterator();
	}
}
