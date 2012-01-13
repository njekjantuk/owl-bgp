/* Copyright 2011 by the Oxford University Computing Laboratory

   This file is part of OWL-BGP.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP. If not, see <http://www.gnu.org/licenses/>.
 */

package  org.semanticweb.sparql.bgpevaluation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.HermiTCostEstimationVisitor;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.StaticHermiTCostEstimationVisitor;
import org.semanticweb.HermiT.hierarchy.InstanceManager;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.Individual;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLBGPQueryIterator;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.monitor.Monitor;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_ClassAssertion;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_ObjectPropertyAssertion;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.axioms.ClassAssertion;
import org.semanticweb.sparql.owlbgp.model.axioms.ObjectPropertyAssertion;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
//import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.model.individuals.NamedIndividual;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.OWLBGPParser;
import org.semanticweb.sparql.owlbgp.parser.ParseException;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.core.BasicPattern;
import com.hp.hpl.jena.sparql.engine.ExecutionContext;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.main.StageGenerator;

public class OWLReasonerStageGenerator implements StageGenerator {
    public static final String LB = System.getProperty("line.separator"); 
    
    protected final StageGenerator m_above;
    protected final Monitor m_monitor;
    protected final Set<Variable> m_bnodes;
    
    public OWLReasonerStageGenerator(StageGenerator original, Monitor monitor){
        m_above=original;
        m_monitor=monitor;
        m_bnodes=new HashSet<Variable>();
    }
    @Override
    public QueryIterator execute(BasicPattern pattern, QueryIterator input, ExecutionContext execCxt) {
        m_monitor.bgpEvaluationStarted();
        
        int orderingMode=1;
        
        Graph activeGraph=execCxt.getActiveGraph();
        // Test to see if this is a graph we support.  
        if (!(activeGraph instanceof OWLOntologyGraph)) {
            // Not us - bounce up the StageGenerator chain
            return m_above.execute(pattern, input, execCxt);
        }
        OWLOntologyGraph ontologyGraph=(OWLOntologyGraph)activeGraph;
        OWLReasoner reasoner=ontologyGraph.getReasoner();
        List<List<Atomic[]>> bindingsPerComponent=new ArrayList<List<Atomic[]>>();
        List<Map<Variable,Integer>> bindingPositionsPerComponent=new ArrayList<Map<Variable,Integer>>();
        try {
            m_monitor.bgpParsingStarted();
            String bgp=arqPatternToBGP(pattern);
            OWLBGPParser parser=new OWLBGPParser(new StringReader(bgp));
            parser.loadDeclarations(ontologyGraph.getClassesInSignature(),ontologyGraph.getDatatypesInSignature(), ontologyGraph.getObjectPropertiesInSignature(), ontologyGraph.getDataPropertiesInSignature(), ontologyGraph.getAnnotationPropertiesInSignature(), ontologyGraph.getIndividualsInSignature());
            parser.parse();
            m_monitor.bgpParsingFinished();
            m_monitor.connectedComponentsComputationStarted();
            Set<Axiom> queryAxiomTemplates=parser.getParsedAxioms();
            RewriterAndSplitter rewriteAndSplitter=new RewriterAndSplitter(ontologyGraph, queryAxiomTemplates);
            Set<List<QueryObject<? extends Axiom>>> connectedComponents=rewriteAndSplitter.rewriteAndSplit();
            m_monitor.connectedComponentsComputationFinished(connectedComponents.size());
            Integer resultSize=null;
            for (List<QueryObject<? extends Axiom>> connectedComponent : connectedComponents) {
                m_monitor.componentsEvaluationStarted(connectedComponent);
                Map<Variable,Integer> positionInTuple=new HashMap<Variable,Integer>();
                int position=0;
                for (Variable var : parser.getParsedOntology().getVariablesInSignature()) {
                    positionInTuple.put(var, position);
                    position++;
                } 
                bindingPositionsPerComponent.add(positionInTuple);
                Atomic[] initialBinding=new Atomic[positionInTuple.keySet().size()];
                List<Atomic[]> bindings=new ArrayList<Atomic[]>();
                bindings.add(initialBinding);
                
                
                if (orderingMode==1){
                 System.out.println("Dynamic");
                 CostEstimationVisitor costEstimator;
                 if (reasoner instanceof Reasoner)
                    costEstimator=new HermiTCostEstimationVisitor(ontologyGraph,positionInTuple,bindings);
                 else 
                    costEstimator=new CostEstimationVisitor(ontologyGraph,positionInTuple,bindings);
                 while (!connectedComponent.isEmpty() && !bindings.isEmpty()) {
                	//if dynamic
                	m_monitor.costEvaluationStarted();
                	long t=System.currentTimeMillis();
                	QueryObject<? extends Axiom> cheapest=QueryReordering.getCheapest(costEstimator, connectedComponent, m_monitor);
                    System.out.println("the reordering time is "+(System.currentTimeMillis()-t)+" ms");
                	m_monitor.costEvaluationFinished(cheapest);
                    connectedComponent.remove(cheapest);
                    m_monitor.queryObjectEvaluationStarted(cheapest);
                    bindings=cheapest.computeBindings(bindings, positionInTuple);
//                    System.out.println(cheapest.getAxiomTemplate());
                    System.out.println("bindings size= "+bindings.size());
                    m_monitor.queryObjectEvaluationFinished(bindings.size());
//                    int sampleSize=bindings.size()/10;
//                    List<Atomic[]> sampleBindings= new ArrayList<Atomic[]>();
//                    sampleBindings=bindings.subList(1, sampleSize);
//                    costEstimator.updateCandidateBindings(sampleBindings);                    
                      costEstimator.updateCandidateBindings(bindings);
                      
                 }
                }
                else /*if (orderingMode==0)*/{
                 System.out.println("Static");	
                 StaticHermiTCostEstimationVisitor costEstimator=new StaticHermiTCostEstimationVisitor(ontologyGraph,positionInTuple);
                 long t=System.currentTimeMillis();
                 List<QueryObject<? extends Axiom>> staticAxiomOrder=StaticQueryReordering.getCheapestOrdering(costEstimator, connectedComponent, m_monitor);
                 System.out.println("The reordering lasted "+(System.currentTimeMillis()-t)+" ms");
                 for (QueryObject<? extends Axiom> cheapest:staticAxiomOrder){
                	if (!bindings.isEmpty()){
                	  bindings=cheapest.computeBindings(bindings, positionInTuple);
                	  System.out.println(cheapest.getAxiomTemplate());
                	  System.out.println("bindings size= "+bindings.size());
                	}
                 }
                }
/*                else {
                  DynamicCostEstimationVisitor costEstimator;
                  if (reasoner instanceof Reasoner)
                     costEstimator=new DynamicHermiTCostEstimationVisitor(ontologyGraph,positionInTuple,bindings);
                  else 
                     costEstimator=new CostEstimationVisitor(ontologyGraph,positionInTuple,bindings);
                  while (!connectedComponent.isEmpty() && !bindings.isEmpty()) {
                    //if dynamic
                    m_monitor.costEvaluationStarted();
                    long t=System.currentTimeMillis();
                    QueryObject<? extends Axiom> cheapest=QueryReordering.getCheapest(costEstimator, connectedComponent, m_monitor);
                    System.out.println("the reordering time is "+(System.currentTimeMillis()-t)+" ms");
                    m_monitor.costEvaluationFinished(cheapest);
                    
                    connectedComponent.remove(cheapest);
                    m_monitor.queryObjectEvaluationStarted(cheapest);
                    bindings=cheapest.computeBindings(bindings, positionInTuple);
                    System.out.println(cheapest.getAxiomTemplate());
                    System.out.println("bindings size= "+bindings.size());
                    m_monitor.queryObjectEvaluationFinished(bindings.size());
//                        int sampleSize=bindings.size()/10;
//                        List<Atomic[]> sampleBindings= new ArrayList<Atomic[]>();
//                        sampleBindings=bindings.subList(1, sampleSize);
//                        costEstimator.updateCandidateBindings(sampleBindings);
                    
                    costEstimator.updateCandidateBindings(bindings,1);
                  }
                }*/
                
                bindingsPerComponent.add(bindings);
                if (resultSize==null)
                    resultSize=bindings.size();
                else resultSize*=bindings.size();
                m_monitor.componentsEvaluationFinished(bindings.size());
            }
            m_monitor.bgpEvaluationFinished(resultSize);
            return new OWLBGPQueryIterator(pattern,input,execCxt,bindingsPerComponent,bindingPositionsPerComponent,m_bnodes);
        } catch (ParseException e) {
            System.err.println("ParseException: Probably types could not be disambuguated with this active graph. ");
            m_monitor.bgpEvaluationFinished(0);
            return new OWLBGPQueryIterator(pattern,input,execCxt,bindingsPerComponent,bindingPositionsPerComponent,m_bnodes);
        }
    }
    private String arqPatternToBGP(BasicPattern pattern) {
        StringBuffer buffer=new StringBuffer();
        for (Triple triple : pattern) {
            buffer.append(printNode(triple.getSubject()));
            buffer.append(" ");
            buffer.append(printNode(triple.getPredicate()));
            buffer.append(" ");
            buffer.append(printNode(triple.getObject()));
            buffer.append(" . ");
            buffer.append(LB);
        }
        return buffer.toString();
    }
    protected String printNode(Node node) {
        if (node.isURI())
            return "<"+node+">";
        else if (node.isVariable()) {
            String name=node.getName();
            if (name.startsWith("?")) {
                name=name.substring(1);
                m_bnodes.add(IndividualVariable.create(name));
            }
            return "?"+name;
        } else
            return node.toString();
    }
}
