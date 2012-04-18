/* Copyright 2008, 2009, 2010 by the Oxford University Computing Laboratory

   This file is part of HermiT.

   HermiT is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   HermiT is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with HermiT.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.semanticweb.HermiT.hierarchy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.Individual;
import org.semanticweb.HermiT.tableau.ExtensionTable;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.NodeType;
import org.semanticweb.HermiT.tableau.ReasoningTaskDescription;
import org.semanticweb.HermiT.tableau.Tableau;

public class Partitioning implements Serializable {
    private static final long serialVersionUID=-2959900333817197464L;

    protected ExtensionTable.Retrieval m_binaryTableSearch1Bound;
    protected final Map<Individual,Node> m_individualToNode;
    protected final Map<Node,Individual> m_nodeToIndividual;
    
    public Partitioning() {
        m_nodeToIndividual=new HashMap<Node,Individual>();
        m_individualToNode=new HashMap<Individual,Node>();
    }
    
    public Map<Integer,Set<Individual>> computePartitions(Reasoner reasoner) {
    	Tableau tableau=reasoner.getTableau();
        for (Individual individual : reasoner.getDLOntology().getAllIndividuals())
            m_individualToNode.put(individual,null);
        tableau.isSatisfiable(true, false, null, null, null, null, m_individualToNode, ReasoningTaskDescription.isABoxSatisfiable());
        for (Individual ind :  m_individualToNode.keySet()) 
        	m_nodeToIndividual.put(m_individualToNode.get(ind), ind);
        
    	Map<Integer,Set<Individual>> m_hashToIndividuals=new HashMap<Integer, Set<Individual>>();
        m_binaryTableSearch1Bound=tableau.getExtensionManager().getBinaryExtensionTable().createRetrieval(new boolean[] { false,true },ExtensionTable.View.TOTAL);
        Node node=tableau.getFirstTableauNode();
        while (node!=null) {
            if (node.isActive() && node.getNodeType()==NodeType.NAMED_NODE) {
                int hashCode=getLabelHashCode(node);
                Set<Individual> individualsForThatHash=m_hashToIndividuals.get(hashCode);
                if (individualsForThatHash==null) {
                	individualsForThatHash=new HashSet<Individual>();
                    m_hashToIndividuals.put(hashCode, individualsForThatHash);
                }
                individualsForThatHash.add(m_nodeToIndividual.get(node));
            }
            node=node.getNextTableauNode();
        }
        // TODO: do something with the partitions
        return m_hashToIndividuals;
    }
    protected int getLabelHashCode(Node node) {
        Set<AtomicConcept> label=new HashSet<AtomicConcept>();
        m_binaryTableSearch1Bound.getBindingsBuffer()[1]=node;
        m_binaryTableSearch1Bound.open();
        Object[] tupleBuffer=m_binaryTableSearch1Bound.getTupleBuffer();
        while (!m_binaryTableSearch1Bound.afterLast()) {
            Object concept=tupleBuffer[0];
            if (concept instanceof AtomicConcept)
                label.add((AtomicConcept)concept);
            m_binaryTableSearch1Bound.next();
        }
        return label.hashCode();
    }
}