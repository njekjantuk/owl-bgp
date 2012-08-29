/* Copyright 2010-2012 by the developers of the OWL-BGP project. 

   This file is part of the OWL-BGP project.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP.  If not, see <http://www.gnu.org/licenses/>.
*/


package  org.semanticweb.sparql.bgpevaluation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.DynamicHermiTCostEstimationVisitor;
import org.semanticweb.HermiT.OWLBGPHermiT;
import org.semanticweb.HermiT.StaticHermiTCostEstimationVisitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.sparql.arq.OWLBGPQueryIterator;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.monitor.Monitor;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_ClassAssertion;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QO_ObjectPropertyAssertion;
import org.semanticweb.sparql.bgpevaluation.queryobjects.QueryObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.Axiom;
import org.semanticweb.sparql.owlbgp.model.individuals.IndividualVariable;
import org.semanticweb.sparql.owlbgp.parser.OWLBGPParser;
import org.semanticweb.sparql.owlbgp.parser.ParseException;
import org.uncommons.maths.combinatorics.PermutationGenerator;

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
    
    public OWLReasonerStageGenerator(StageGenerator original, Monitor monitor){
        m_above=original;
        m_monitor=monitor;
    }
    @Override
    public QueryIterator execute(BasicPattern pattern, QueryIterator input, ExecutionContext execCxt) {
        m_monitor.bgpEvaluationStarted();
        
        String orderingMode = System.getProperty("ordering");
        
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
             
            Set<IndividualVariable> bnodes=parser.getVariablesForAnonymousIndividual();

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
                
                if (orderingMode!=null && orderingMode.equals("PlanChecking")) {
                    try {
                    	// Create file 
                		FileWriter fstream = new FileWriter("output.txt");
                		BufferedWriter out = new BufferedWriter(fstream);
                		PermutationGenerator<QueryObject<? extends Axiom>> generator=new PermutationGenerator<QueryObject<? extends Axiom>>(connectedComponent);
                		List<QueryObject<? extends Axiom>> cheapestOrder=new ArrayList<QueryObject<? extends Axiom>>();
                        long minTime=Long.MAX_VALUE;
                		int permNum=0;
                        while (generator.hasMore()) {//new permutation of query atoms
                        	bindings=new ArrayList<Atomic[]>();
                            bindings.add(initialBinding);
                        	List<QueryObject<? extends Axiom>> atomList=generator.nextPermutationAsList();
                            Set<Variable> boundVar= new HashSet<Variable>();
                            int flag=1;
                            int valid=1;
                            Set<Variable> varscopy=new HashSet<Variable>();
                            for (int i=0; i<atomList.size();i++) {
                            	Set<Variable> vars=atomList.get(i).getAxiomTemplate().getVariablesInSignature();
                                varscopy.addAll(vars);
                            	if (flag!=1) 
                            		vars.retainAll(boundVar);
                            	if (vars.isEmpty()) {
                            	    valid=0;
                            		break;
                            	}
                            	boundVar.addAll(varscopy);
                            	flag=0;
                            } 	
                            if (valid!=0) {
                            	permNum++;
                            	long t=System.currentTimeMillis();
                            	for (QueryObject<? extends Axiom> cheapest:atomList)
                            		if (!bindings.isEmpty())
                            			bindings=cheapest.computeBindings(bindings, positionInTuple);
                            	out.write("Ordering:  ");
                            	out.newLine();
                            	for (QueryObject<? extends Axiom> cheapest:atomList) {
                            		out.write(cheapest.getAxiomTemplate().toString()+" ");
                            		out.newLine();
                            	}
                            	long y=System.currentTimeMillis()-t;
                            	if (y<minTime) {
                            		minTime=y;
                            		cheapestOrder=atomList;
                            	}	
                            	out.write("RunningTime: " + y +" ms.");
                            	out.newLine();
                            }
                         }
                		 out.write("The cheapestOrdering is: ");
                		 for (QueryObject<? extends Axiom> cheapest:cheapestOrder) {
                     		out.write(cheapest.getAxiomTemplate().toString()+" ");
                     		out.newLine();
                     	 }
                		 out.write("with running time "+minTime+" ms."); 
                		 out.newLine();
                		 out.newLine();
                		 out.write("The number of valid orderings is " + permNum+" .");
                		 out.close();
                    }catch (Exception e){//Catch exception if any
                		  System.err.println("Error: " + e.getMessage());
                	}
				}
                
                if ((orderingMode==null) || orderingMode.equals("Static")){
                    StaticCostEstimationVisitor costEstimator;
                    if (ontologyGraph.getReasoner() instanceof OWLBGPHermiT) 
                        costEstimator=new StaticHermiTCostEstimationVisitor(ontologyGraph,positionInTuple);
                    else 
                        costEstimator=new StaticCostEstimationVisitor(ontologyGraph,positionInTuple);
                    List<QueryObject<? extends Axiom>> staticAxiomOrder=StaticQueryReordering.getCheapestOrdering(costEstimator, connectedComponent, m_monitor);
                    for (QueryObject<? extends Axiom> cheapest:staticAxiomOrder){
                        if (!bindings.isEmpty()){
                            bindings=cheapest.computeBindings(bindings, positionInTuple);
                        }
                    }
                }
                else if (orderingMode.equals("Dynamic")){
                    DynamicCostEstimationVisitor costEstimator;
                    if (reasoner instanceof OWLBGPHermiT)
                    	costEstimator=new DynamicHermiTCostEstimationVisitor(ontologyGraph,positionInTuple,bindings);
                    else 
                        costEstimator=new DynamicCostEstimationVisitor(ontologyGraph,positionInTuple,bindings);
                    Set<Variable> boundVar=new HashSet<Variable>();
                    while (!connectedComponent.isEmpty() && !bindings.isEmpty()) {
                    	m_monitor.costEvaluationStarted();
                	    QueryObject<? extends Axiom> cheapest;
                	    if (connectedComponent.size()==1)
                	    	cheapest=connectedComponent.iterator().next();
                	    else 
                	        cheapest=DynamicQueryReordering.getCheapest(costEstimator, connectedComponent, boundVar, m_monitor);
                	    m_monitor.costEvaluationFinished(cheapest);
                        connectedComponent.remove(cheapest);   
                        Axiom ax=cheapest.getAxiomTemplate();
                        Set<Variable> varsss=ax.getVariablesInSignature();
                        boundVar.addAll(varsss);
                        m_monitor.queryObjectEvaluationStarted(cheapest);
                        bindings=cheapest.computeBindings(bindings, positionInTuple);
                        m_monitor.queryObjectEvaluationFinished(bindings.size());
                        costEstimator.updateCandidateBindings(bindings);
                    }
                }
                else if (orderingMode.equals("Intersection")){
                    StaticHermiTCostEstimationVisitor costEstimator=new StaticHermiTCostEstimationVisitor(ontologyGraph,positionInTuple);
                    List<QueryObject<? extends Axiom>> staticAxiomOrder=StaticQueryReordering.getCheapestOrdering(costEstimator, connectedComponent, m_monitor);
                    BindingsIntersection optimization=new BindingsIntersection(ontologyGraph, positionInTuple);
                    for (QueryObject<? extends Axiom> cheapest:staticAxiomOrder){
                   	    if (cheapest instanceof QO_ClassAssertion) 
                   	    	bindings=optimization.reduceClassBindings(bindings, (QO_ClassAssertion)cheapest);
                        else if (cheapest instanceof QO_ObjectPropertyAssertion)
                        	bindings=optimization.reduceObjectPropertyBindings(bindings, (QO_ObjectPropertyAssertion)cheapest);
                  }
                  for (QueryObject<? extends Axiom> cheapest:staticAxiomOrder){
                     if (!bindings.isEmpty()){
                         bindings=cheapest.computeBindings(bindings, positionInTuple);
                     }   	
                  }
                }
                bindingsPerComponent.add(bindings);
                if (resultSize==null)
                    resultSize=bindings.size();
                else resultSize*=bindings.size();
                m_monitor.componentsEvaluationFinished(bindings.size());
            }
            m_monitor.bgpEvaluationFinished(resultSize);
            return new OWLBGPQueryIterator(pattern,input,execCxt,bindingsPerComponent,bindingPositionsPerComponent,bnodes);
        } catch (ParseException e) {
            System.err.println("ParseException: Probably types could not be disambuguated with this active graph. ");
            m_monitor.bgpEvaluationFinished(0);
            return new OWLBGPQueryIterator(pattern,input,execCxt,bindingsPerComponent,bindingPositionsPerComponent,new HashSet<IndividualVariable>());
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
                return "_:"+name;
                //m_bnodes.add(IndividualVariable.create(name));
                //TODO: check how we get the bnodes (without also taking the auxiliary ones)
            }
            return "?"+name;
        } else
            return node.toString();
    }
}
