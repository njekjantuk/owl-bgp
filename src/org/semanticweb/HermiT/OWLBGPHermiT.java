package org.semanticweb.HermiT;

import java.util.Collection;

import org.semanticweb.HermiT.hierarchy.InstanceStatistics;
import org.semanticweb.HermiT.model.DescriptionGraph;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

public class OWLBGPHermiT extends Reasoner {

    /**
     * Creates a new reasoner object with standard parameters for blocking, expansion strategy etc. Then the given manager is used to find all required imports for the given ontology and the ontology with the imports is loaded into the reasoner and the data factory of the manager is used to create fresh concepts during the preprocessing phase if necessary.
     *
     * @param rootOntology
     *            - the ontology that should be loaded by the reasoner
     */
    public OWLBGPHermiT(OWLOntology rootOntology) {
        super(rootOntology);
    }

    /**
     * Creates a new reasoner object with the parameters for blocking, expansion strategy etc as specified in the given configuration object. A default configuration can be obtained by just passing new Configuration(). Then the given manager is used to find all required imports for the given ontology and the ontology with the imports is loaded into the reasoner and the data factory of the manager is used to create fresh concepts during the preprocessing phase if necessary.
     *
     * @param configuration
     *            - a configuration in which parameters can be defined such as the blocking strategy to be used etc
     * @param rootOntology
     *            - the ontology that should be loaded by the reasoner
     */
    public OWLBGPHermiT(Configuration configuration,OWLOntology rootOntology) {
        super(configuration,rootOntology);
    }

    /**
     * Creates a new reasoner object loaded with the given ontology and the given description graphs. When creating the reasoner, the given configuration determines the parameters for blocking, expansion strategy etc. A default configuration can be obtained by just passing new Configuration(). Then the given manager is used to find all required imports for the given ontology and the ontology with the imports and the description graphs are loaded into the reasoner. The data factory of the manager is used to create fresh concepts during the preprocessing phase if necessary.
     *
     * @param configuration
     *            - a configuration in which parameters can be defined such as the blocking strategy to be used etc
     * @param rootOntology
     *            - the ontology that should be loaded by the reasoner
     * @param descriptionGraphs
     *            - a set of description graphs
     */
    public OWLBGPHermiT(Configuration configuration,OWLOntology rootOntology,Collection<DescriptionGraph> descriptionGraphs) {
    	super(configuration, rootOntology, descriptionGraphs);
    }

    public boolean hasType(OWLNamedIndividual namedIndividual,OWLClassExpression type,boolean direct) {
        // start measuring
        boolean result=super.hasType(namedIndividual, type, direct);
        // stop measuring
        return result;
    }
    
    // statistics for cost-based query axiom ordering

    public InstanceStatistics getInstanceStatistics() {
    	if (m_instanceManager==null) {
    		initialiseClassInstanceManager(); 
    		initialisePropertiesInstanceManager();
    	} else {
    		
    	}
        assert m_instanceManager!=null;
        assert m_instanceManager.areClassesInitialised();
        assert m_instanceManager.arePropertiesInitialised();
        return new InstanceStatistics(m_instanceManager, this);
    }
    
    public boolean isInstanceManagerInitialised() {
    	return m_instanceManager==null;
    }
}
