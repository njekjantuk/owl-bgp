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
                
                CostEstimationVisitor costEstimator;
                if (reasoner instanceof Reasoner)
                    costEstimator=new HermiTCostEstimationVisitor(ontologyGraph,positionInTuple,bindings);
                else 
                    costEstimator=new CostEstimationVisitor(ontologyGraph,positionInTuple,bindings);
                //if StaticEstimation {
                //costEstimator=new StaticCostEstimatorVisitor(ontologyGraph,positionInTuple,bindings);
                //}
                
long f=System.currentTimeMillis();                
                //////////////////Intersection Optimization/////////////////////////////
                /////////////////////////////////////////////////////////////////////////
/*                HashMap<Variable,Set<OWLNamedIndividual>> varHash=new HashMap<Variable, Set<OWLNamedIndividual>>();
                Set<Variable> variables=parser.getParsedOntology().getVariablesInSignature();
                Set<OWLNamedIndividual> indSet=reasoner.getRootOntology().getIndividualsInSignature();
                for (Variable i:variables) {
                	varHash.put(i,indSet);
                }
                
                for (QueryObject<? extends Axiom> queryAtom:connectedComponent){
                	Set<Variable> indVar=queryAtom.getAxiomTemplate().getVariablesInSignature();
                	Axiom obj = (Axiom) queryAtom.getAxiomTemplate();
                	
                	if (obj instanceof ClassAssertion){
                		ClassAssertion assertion=(ClassAssertion) obj;
                		ClassExpression expr=assertion.getClassExpression();
                		if (!indVar.isEmpty() && expr instanceof Clazz){
                			if (reasoner instanceof Reasoner) {
                				OWLDataFactory factory=((Reasoner) reasoner).getDataFactory();
                				Set<OWLNamedIndividual> knownInd =((Reasoner) reasoner).getKnownInstances((OWLClass)expr.asOWLAPIObject(factory));
                				Set<OWLNamedIndividual> possibleInd=((Reasoner) reasoner).getPossibleInstances((OWLClass)expr.asOWLAPIObject(factory));
                                int[] instanceno=((Reasoner) reasoner).getNumberOfInstances((OWLClass)expr.asOWLAPIObject(factory)); 
                				Variable[] indVarArray= (Variable[])indVar.toArray();
                				Set<OWLNamedIndividual> kpSet = varHash.get(indVarArray[0]);
                				//Set<OWLNamedIndividual> knownInd1=kpList.get(0);
                				//Set<OWLNamedIndividual> possibleInd1=kpList.get(1);
                				knownInd.addAll(possibleInd);
                				knownInd.retainAll(kpSet);
                				
                			} 	
                		}
                	}
                	else if (obj instanceof ObjectPropertyAssertion) {
                		ObjectPropertyAssertion assertion=(ObjectPropertyAssertion)obj;
                		ObjectPropertyExpression expr=assertion.getObjectPropertyExpression();
                		Individual ind1=assertion.getIndividual1();
                        Individual ind2=assertion.getIndividual2();
                		if (!indVar.isEmpty() && expr instanceof ObjectProperty){
                			if (reasoner instanceof Reasoner) {
                				OWLDataFactory factory=((Reasoner) reasoner).getDataFactory();
                				Map<OWLNamedIndividual,Set<OWLNamedIndividual>> knownInd=new HashMap<OWLNamedIndividual,Set<OWLNamedIndividual>>(); 
                				knownInd =((Reasoner) reasoner).getKnownInstances((OWLObjectProperty)expr.asOWLAPIObject(factory));
                				Map<OWLNamedIndividual,Set<OWLNamedIndividual>> possibleInd= new HashMap<OWLNamedIndividual,Set<OWLNamedIndividual>>();
                				possibleInd=((Reasoner) reasoner).getPossibleInstances((OWLObjectProperty)expr.asOWLAPIObject(factory));
                				knownInd.putAll(possibleInd);
                				
                				Set<OWLNamedIndividual> keys=knownInd.keySet();
                				
                				if (ind1.isVariable() && ind2.isVariable()) {
                		            Set<OWLNamedIndividual> ind1Set=varHash.get(ind1);
                		            Set<OWLNamedIndividual> ind2Set=varHash.get(ind2);
                		            ind1Set.retainAll(keys);
                		            Set<OWLNamedIndividual> ind2Values=new HashSet<OWLNamedIndividual>();
                		            for (OWLNamedIndividual ind:keys) {
                		            	if (ind1Set.contains(ind)) {
                		            		ind2Values.addAll(knownInd.get(ind));
                		            	}
                		            }	
                		            	ind2Set.retainAll(ind2Values);
                		            	varHash.put((Variable)ind2,ind2Set);          		            
                				}
                				else if (ind1.isVariable() && !ind2.isVariable()){
                					Set<OWLNamedIndividual> ind1Set=varHash.get(ind1);
                 		            ind1Set.retainAll(keys);
                 		            Set<OWLNamedIndividual> ind1Values=new HashSet<OWLNamedIndividual>();
                 		            for (OWLNamedIndividual ind:ind1Set) {
                 		            	if ((knownInd.get(ind)).contains(ind2)) {
                 		            		ind1Values.add(ind);
                 		            	}
                 		            }
                 		            varHash.put((Variable)ind1,ind1Values);
                				}
                				else {
                 		            Set<OWLNamedIndividual> ind2Set=varHash.get(ind2);
                                    if (keys.contains((OWLNamedIndividual)ind1)){
                                    	Set<OWLNamedIndividual> ind2Values=knownInd.get(ind1);
                                    	ind2Values.retainAll(ind2Set);
                                    	varHash.put((Variable)ind2, ind2Values);
                                    }
                                    else{
                                      Set<OWLNamedIndividual> ind2Values=new HashSet<OWLNamedIndividual>();
                   		              varHash.put((Variable)ind1,ind2Values);	
                				    } 
                				}               				
                			} 	
                		}
                	}
                	 
                }*/
                //////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////////////////////////////////////
System.out.println("intersection optimization: "+(System.currentTimeMillis()-f));                
                
                while (!connectedComponent.isEmpty() && !bindings.isEmpty()) {
                    m_monitor.costEvaluationStarted();
                    //if dynamic
                    QueryObject<? extends Axiom> cheapest=QueryReordering.getCheapest(costEstimator, connectedComponent, m_monitor);
                    //else if static
                    
                    
                    m_monitor.costEvaluationFinished(cheapest);
                    connectedComponent.remove(cheapest);
                    m_monitor.queryObjectEvaluationStarted(cheapest);
                    
              
/*                    Set<Variable> indVar=cheapest.getAxiomTemplate().getVariablesInSignature();
                	Axiom obj = (Axiom) cheapest.getAxiomTemplate();
                	OWLDataFactory fact=null;
                	if (reasoner instanceof Reasoner)
                	fact =((Reasoner)reasoner).getDataFactory();
                	List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
*/                	
 /*               	if (obj instanceof ClassAssertion){
                		ClassAssertion assertion=((QO_ClassAssertion) obj).getAxiomTemplate();
                		Individual ind=assertion.getIndividual();
                		ClassExpression expr=assertion.getClassExpression();
                		
                	    if (ind.isVariable()){
                	      Integer int1=positionInTuple.get(ind);
                	      newBindings= new ArrayList<Atomic[]>();
                	      if (!bindings.isEmpty()) {
                		     if (bindings.get(1)[int1]==null) {
                			   Set<OWLNamedIndividual> individuals=varHash.get(ind);
                			   for (OWLNamedIndividual individ:individuals) {
                				 Atomic at=NamedIndividual.create(individ.getIRI().toString());
                       			 for (Atomic[] binding:bindings){
                       				Atomic[] binding1=binding.clone();
                       				binding1[int1]=at;
                       				newBindings.add(binding1);
                			     }
                		       }
                	         } 
                	         else 
                				newBindings.addAll(bindings);
         			      }
                 	      else { 
                		    Set<OWLNamedIndividual> individuals=varHash.get(ind);
         			        for (OWLNamedIndividual individ:individuals) {
         				       Atomic at=NamedIndividual.create(individ.getIRI().toString());
                			   Atomic[] binding1= new Atomic[positionInTuple.keySet().size()];
                			   binding1[int1]=at;
                			   newBindings.add(binding1);
         		            }
                	      }    
                        }
                    }
                	else if (obj instanceof ObjectPropertyAssertion){
                		ObjectPropertyAssertion assertion=((QO_ObjectPropertyAssertion) obj).getAxiomTemplate();
                		Individual ind1=assertion.getIndividual1();
                		Individual ind2=assertion.getIndividual2();
                		ObjectPropertyExpression prop=assertion.getObjectPropertyExpression();
                	    if (ind1.isVariable() && ind2.isVariable()){
                	      Integer int1=positionInTuple.get(ind1);
                	      Integer int2=positionInTuple.get(ind2);
                	      newBindings= new ArrayList<Atomic[]>();
                	      if (!bindings.isEmpty()) {
                	    	  Set<OWLNamedIndividual> individuals1=varHash.get(ind1);
               			      Set<OWLNamedIndividual> individuals2=varHash.get(ind2);
               			  
             				   Map<OWLNamedIndividual,Set<OWLNamedIndividual>> knownInd=new HashMap<OWLNamedIndividual,Set<OWLNamedIndividual>>(); 
              				   knownInd =((Reasoner) reasoner).getKnownInstances((OWLObjectProperty)prop.asOWLAPIObject(fact));
              				   Map<OWLNamedIndividual,Set<OWLNamedIndividual>> possibleInd= new HashMap<OWLNamedIndividual,Set<OWLNamedIndividual>>();
              				   possibleInd=((Reasoner) reasoner).getPossibleInstances((OWLObjectProperty)prop.asOWLAPIObject(fact));
              				   knownInd.putAll(possibleInd);
              				   
                		     if (bindings.get(1)[int1]==null && bindings.get(1)[int2]==null) {
                			   
                			   for (OWLNamedIndividual individ1:individuals1) {
                				 Set<OWLNamedIndividual> indSet2=knownInd.get(individ1);
                				 Set<OWLNamedIndividual> varIndSet=varHash.get(ind2);
                				 indSet2.retainAll(varIndSet);
                       			 for (OWLNamedIndividual individ2:indSet2) {
                       				for (Atomic[] binding:bindings){
                       				 Atomic at1=NamedIndividual.create(individ1.getIRI().toString());
                       				 Atomic at2=NamedIndividual.create(individ2.getIRI().toString());
                       				 Atomic[] binding1=binding.clone();
                       				 binding1[int1]=at1;
                       				 binding1[int2]=at2;
                       				 newBindings.add(binding1);
                       				}
                       		     }
                		       }
                	         }
                		     else if (bindings.get(1)[int1]==null && bindings.get(1)[int2]!=null){ 
                		    	 for (Atomic[] binding:bindings){
                		    		 Atomic at=binding[int2];
                		    		 Set<OWLNamedIndividual> varIndSet=varHash.get(ind1);
                		    		 
                		    		 Atomic[] binding1=binding.clone();
                		    		 
                		    	 }
                		     }
                				newBindings.addAll(bindings);
         			      }
                 	      else { 
//                		    Set<OWLNamedIndividual> individuals=varHash.get(ind1);
//         			        for (OWLNamedIndividual individ:individuals) {
//         				       Atomic at=NamedIndividual.create(individ.getIRI().toString());
//                			   Atomic[] binding1=binding[int1];
//                			   binding1[int1]=at;
//                			   newBindings.add(binding1);
//         		            }
                	      }    
                        }
                    }
*/                	Set<Variable> varSet=cheapest.getAxiomTemplate().getVariablesInSignature();
                    Object[] vars=varSet.toArray();
                    Set<Atomic> atomicSet=new HashSet<Atomic>();
                	for (Atomic[] bind:bindings){
	                    atomicSet.add(bind[positionInTuple.get((Variable)vars[0])]);
	                }
                	
                	ClassAssertion assertion=(ClassAssertion) cheapest.getAxiomTemplate();
            		ClassExpression expr=assertion.getClassExpression();
            		
            			if (reasoner instanceof Reasoner) {
            				OWLDataFactory factory=((Reasoner) reasoner).getDataFactory();
            				InstanceManager instanceman=((Reasoner) reasoner).m_instanceManager;
            				Set<Individual> knownInd =instanceman.getKnownInstances(AtomicConcept.create(((Clazz)expr).getIRIString()));
            				Set<Individual> possibleInd=instanceman.getPossibleInstances(AtomicConcept.create(((Clazz)expr).getIRIString()));
             
            				int[] indmatrix =instanceman.getNumberOfInstances(AtomicConcept.create(((Clazz)expr).getIRIString()));
            				System.out.println(indmatrix);
            				Set<Atomic> atomicAtomSet=new HashSet<Atomic>();
            				for (Individual individ:possibleInd) {
               				  Atomic at=NamedIndividual.create(individ.getIRI().toString());
               				  if (atomicSet.contains(at))
               				  {System.out.println("lala");}
               				  atomicAtomSet.add(at);
            				}
            		 atomicAtomSet.retainAll(atomicSet);
            		 System.out.println(atomicAtomSet);
            			}
               		 bindings=cheapest.computeBindings(bindings, positionInTuple);
                    
                    
                    System.out.println("bindings size= " + bindings.size());
                    m_monitor.queryObjectEvaluationFinished(bindings.size());
                    costEstimator.updateCandidateBindings(bindings);
                }
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
