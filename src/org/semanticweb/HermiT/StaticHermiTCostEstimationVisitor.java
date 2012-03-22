	package  org.semanticweb.HermiT;

	import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.hierarchy.InstanceStatistics;
import org.semanticweb.HermiT.hierarchy.InstanceStatistics.RoleInstanceStatistics;
import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.StaticCostEstimationVisitor;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;

	public class StaticHermiTCostEstimationVisitor extends StaticCostEstimationVisitor {

	    protected double POSSIBLE_INSTANCE_SUCCESS=0.5;
	    
	    protected final Reasoner m_hermit;
	    protected final InstanceStatistics m_instanceStatistics;
	    protected final double m_numDisjunctions;
	    protected Integer m_classHierarchyDepth;
	    protected Integer m_opHierarchyDepth;
	    
	    public StaticHermiTCostEstimationVisitor(OWLOntologyGraph graph, Map<Variable,Integer> bindingPositions) {
	        super(graph, bindingPositions);
	        if (m_reasoner instanceof Reasoner) {
	            m_hermit=(Reasoner)m_reasoner;
	            m_instanceStatistics=m_hermit.getInstanceStatistics();
	            double numDisjunctions=0;
	            for (DLClause clause : m_hermit.getDLOntology().getDLClauses())
	                if (clause.getHeadLength()>1)
	                    numDisjunctions+=clause.getHeadLength();
	            m_numDisjunctions=numDisjunctions;
	        } else 
	            throw new IllegalArgumentException("Error: The HermiT cost estimator can only be instantiated with a graph that has a (HermiT) Reasoner instance attached to it.");
	    }

	    protected double[] getClassAssertionCost(ClassExpression ce, Individual ind, Set<Variable> bound, Variable indVar) {
	        double cost=0;
	        
//	        if (ce instanceof Atomic && (m_instanceStatistics==null || !m_instanceStatistics.areClassesInitialised()))
//	            cost+=(m_classCount*m_indCount*COST_LOOKUP+COST_ENTAILMENT); // initialization required
	        
	        if (indVar!=null && !bound.contains(indVar)){ //C(?x)
	            if (ce instanceof Atomic) {
	                	int[] estimate=m_instanceStatistics.getNumberOfInstances((OWLClass)ce.asOWLAPIObject(m_dataFactory));
//	                    System.out.println("known: "+estimate[0]+"  possible: "+estimate[1]);
	                	return new double[] { cost+estimate[0]*COST_LOOKUP+estimate[1]*COST_ENTAILMENT*0.5*m_instanceStatistics.getClassHierarchyDepth(), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) };
	                    
	            }    
	            return new double[] { cost+m_indCount, m_indCount }; // needs refinement 
	        }    
	        else if (indVar!=null && bound.contains(indVar)) {//C(a)<-C(x)
	          if (ce instanceof Atomic) {
	        	  int[] estimate=m_instanceStatistics.getNumberOfInstances((OWLClass)ce.asOWLAPIObject(m_dataFactory));
	        	  return new double[] { cost+(estimate[0]/(double)m_indCount)*COST_LOOKUP + (estimate[1]/(double)m_indCount)*COST_ENTAILMENT*0.5*m_instanceStatistics.getClassHierarchyDepth(), (estimate[0]+estimate[1]*POSSIBLE_INSTANCE_SUCCESS)/(double)m_indCount};
	          } 
	          else return new double[] { COST_ENTAILMENT, 1};
	        }
	        else if (ce instanceof Atomic) {//C(a)
	           boolean[] result=m_instanceStatistics.isKnownOrPossibleInstance((OWLClass)ce.asOWLAPIObject(m_dataFactory),(OWLNamedIndividual)ind.asOWLAPIObject(m_dataFactory)); 
	           if (result[0])
	              return new double[] { cost+COST_LOOKUP, 1 };
	           else if (result[1])
	              return new double[] { cost+((double)0.5*COST_ENTAILMENT*m_instanceStatistics.getClassHierarchyDepth()), 1*POSSIBLE_INSTANCE_SUCCESS };
	           else return new double[] { cost+COST_LOOKUP, 0};   
	        }       
	        else return new double[] { COST_ENTAILMENT, 1};
	    }
	    
	    protected double[] getObjectPropertyAssertionCost(ObjectProperty op, Individual ind1, Individual ind2, Set<Variable> bound, Variable opVar) {
	        double cost=0;
	        
//	        if (m_instanceStatistics==null || !m_instanceStatistics.arePropertiesInitialised())
//	            cost+=(m_opCount*m_indCount*COST_LOOKUP+COST_ENTAILMENT); // initialization required
//	        
	        if (opVar==null && ind1 instanceof Variable && ind2 instanceof Variable) {// op(?x ?y)
	          if (!bound.contains(ind1) && !bound.contains(ind2)){//op(?x ?y)
	        	  RoleInstanceStatistics roleStatistics=m_instanceStatistics.getRoleInstanceStatistics((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory));
//	                System.out.println("known "+ op.toString()+ " instances: " +estimate[0]+"  possible: "+estimate[1]);
	                return new double[] { cost+roleStatistics.getNumberOfKnownInstances()*COST_LOOKUP+(roleStatistics.getNumberOfPossibleInstances()*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth()), roleStatistics.getNumberOfKnownInstances()+(POSSIBLE_INSTANCE_SUCCESS*roleStatistics.getNumberOfPossibleInstances()) };
	          }
	          else if (bound.contains(ind1) && !bound.contains(ind2)) {//op(a ?y)
	        	//int[] estimate=m_instanceStatistics.getNumberOfPropertyInstances(AtomicRole.create(op.getIRIString()));
	        	  RoleInstanceStatistics roleStatistics=m_instanceStatistics.getRoleInstanceStatistics((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory));
	            if (roleStatistics.getNumberOfDistinctKnownPredecessors()!=0 && roleStatistics.getNumberOfDistinctPossiblePredecessors()!=0) 
	               return new double[] { cost+(roleStatistics.getNumberOfKnownInstances()/roleStatistics.getNumberOfDistinctKnownPredecessors())*COST_LOOKUP+ (roleStatistics.getNumberOfPossibleInstances()/roleStatistics.getNumberOfDistinctPossiblePredecessors())*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth(), (roleStatistics.getNumberOfKnownInstances()/roleStatistics.getNumberOfDistinctKnownPredecessors())+POSSIBLE_INSTANCE_SUCCESS*(roleStatistics.getNumberOfPossibleInstances()/roleStatistics.getNumberOfDistinctPossiblePredecessors()) };   
	            else if (roleStatistics.getNumberOfDistinctPossiblePredecessors()!=0)  
	            	return new double[] {cost+(roleStatistics.getNumberOfPossibleInstances()/roleStatistics.getNumberOfDistinctPossiblePredecessors())*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth(), POSSIBLE_INSTANCE_SUCCESS*(roleStatistics.getNumberOfPossibleInstances()/roleStatistics.getNumberOfDistinctPossiblePredecessors()) };
	            else if (roleStatistics.getNumberOfDistinctKnownPredecessors()!=0)
	            	return new double[] {cost+(roleStatistics.getNumberOfKnownInstances()/roleStatistics.getNumberOfDistinctKnownPredecessors())*COST_LOOKUP, (roleStatistics.getNumberOfKnownInstances()/roleStatistics.getNumberOfDistinctKnownPredecessors())};
	            else return new double[] {cost+0, 0};
	          }	
	          else if (!bound.contains(ind1) && bound.contains(ind2)){//op(?x a)
	        	  RoleInstanceStatistics roleStatistics=m_instanceStatistics.getRoleInstanceStatistics((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory));
	        	if (roleStatistics.getNumberOfDistinctKnownSuccessors()!=0 && roleStatistics.getNumberOfDistinctPossibleSuccessors()!=0) 
	               return new double[] { cost+(roleStatistics.getNumberOfKnownInstances()/roleStatistics.getNumberOfDistinctKnownSuccessors())*COST_LOOKUP+ (roleStatistics.getNumberOfPossibleInstances()/roleStatistics.getNumberOfDistinctPossibleSuccessors())*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth(), (roleStatistics.getNumberOfKnownInstances()/roleStatistics.getNumberOfDistinctKnownSuccessors())+POSSIBLE_INSTANCE_SUCCESS*(roleStatistics.getNumberOfPossibleInstances()/roleStatistics.getNumberOfDistinctPossibleSuccessors()) };   
	            else if (roleStatistics.getNumberOfDistinctKnownSuccessors()!=0)
	            	return new double[] {cost+(roleStatistics.getNumberOfKnownInstances()/roleStatistics.getNumberOfDistinctKnownSuccessors())*COST_LOOKUP, (roleStatistics.getNumberOfKnownInstances()/roleStatistics.getNumberOfDistinctKnownSuccessors())};
	            else if (roleStatistics.getNumberOfDistinctPossibleSuccessors()!=0)
	                return new double[] {cost+(roleStatistics.getNumberOfPossibleInstances()/roleStatistics.getNumberOfDistinctPossibleSuccessors())*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth(), POSSIBLE_INSTANCE_SUCCESS*(roleStatistics.getNumberOfPossibleInstances()/roleStatistics.getNumberOfDistinctPossibleSuccessors()) };
	            else return new double[] {cost+0, 0};  
	          }
	          else {
	        	  RoleInstanceStatistics roleStatistics=m_instanceStatistics.getRoleInstanceStatistics((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory));
	            return new double[] { cost+(roleStatistics.getNumberOfKnownInstances()/(double)m_indCount)*COST_LOOKUP + (roleStatistics.getNumberOfPossibleInstances()/(double)m_indCount)*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth(), (roleStatistics.getNumberOfKnownInstances()+roleStatistics.getNumberOfPossibleInstances()*POSSIBLE_INSTANCE_SUCCESS)/(double)m_indCount*(double)m_indCount};  
	          }  
	          }
	        else if (opVar==null && ind1 instanceof Variable) {// op(?x :a)    
	                if (!bound.contains(ind1)) {
	                	int[] estimate=m_instanceStatistics.getNumberOfPredecessors((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind2.asOWLAPIObject(m_dataFactory));
	                    return new double[] { cost+estimate[0]*COST_LOOKUP+(estimate[1]*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth()), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) };
	                }
	                else {
	                	RoleInstanceStatistics roleStatistics=m_instanceStatistics.getRoleInstanceStatistics((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory));
	    	            return new double[] { cost+(roleStatistics.getNumberOfKnownInstances()/(double)m_indCount)*COST_LOOKUP + (roleStatistics.getNumberOfPossibleInstances()/(double)m_indCount)*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth(), (roleStatistics.getNumberOfKnownInstances()+roleStatistics.getNumberOfPossibleInstances()*POSSIBLE_INSTANCE_SUCCESS)/(double)m_indCount*(double)m_indCount};  
	               } 
	        } 
            else if (opVar==null && ind2 instanceof Variable) {// op(:a ?x)

	                if (!bound.contains(ind2)) {
	                	int[] estimate=m_instanceStatistics.getNumberOfSuccessors((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind1.asOWLAPIObject(m_dataFactory));
	                  return new double[] { cost+estimate[0]*COST_LOOKUP+(estimate[1]*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth()), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) };
	                }
	                else {
	                	RoleInstanceStatistics roleStatistics=m_instanceStatistics.getRoleInstanceStatistics((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory));
	                	return new double[] { cost+(roleStatistics.getNumberOfKnownInstances()/(double)m_indCount)*COST_LOOKUP + (roleStatistics.getNumberOfPossibleInstances()/(double)m_indCount)*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth(), (roleStatistics.getNumberOfKnownInstances()+roleStatistics.getNumberOfPossibleInstances()*POSSIBLE_INSTANCE_SUCCESS)/(double)m_indCount*(double)m_indCount};
	                }	                
//	            return new double[] { cost+m_indCount*COST_LOOKUP, m_indCount }; // needs refinement 
            }
	        boolean[] result=m_instanceStatistics.hasSuccessor((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind1.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind2.asOWLAPIObject(m_dataFactory));
            if (result[0])
                return new double[] { cost+COST_LOOKUP, 1 };
            else if (result[1])
                return new double[] { cost+((double)0.5*COST_ENTAILMENT*m_instanceStatistics.getObjectPropertyHierarchyDepth()), 1*POSSIBLE_INSTANCE_SUCCESS };
            else return new double[] {cost+COST_LOOKUP, 0 }; 
	    }
	    
	    
}
