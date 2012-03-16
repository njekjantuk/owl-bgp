	package  org.semanticweb.HermiT;

	import java.util.List;
	import java.util.Map;
	import java.util.Set;
	import java.util.Random;
	
	import org.semanticweb.HermiT.hierarchy.InstanceManager;
	import org.semanticweb.HermiT.model.AtomicConcept;
	import org.semanticweb.HermiT.model.AtomicRole;
	import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.owlapi.model.OWLObjectProperty;
	import org.semanticweb.owlapi.reasoner.InferenceType;
	import org.semanticweb.sparql.arq.OWLOntologyGraph;
	import org.semanticweb.sparql.bgpevaluation.CostEstimationVisitor;
import org.semanticweb.sparql.bgpevaluation.StaticCostEstimationVisitor;
	import org.semanticweb.sparql.owlbgp.model.Atomic;
	import org.semanticweb.sparql.owlbgp.model.Variable;
	import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
	import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
	import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;

	public class StaticHermiTCostEstimationVisitor extends StaticCostEstimationVisitor {

	    protected double POSSIBLE_INSTANCE_SUCCESS=0.5;
	    
	    protected final Reasoner m_hermit;
	    protected final InstanceManager m_instanceManager;
	    protected final double m_numDisjunctions;
	    protected Integer m_classHierarchyDepth;
	    protected Integer m_opHierarchyDepth;
	    
	    public StaticHermiTCostEstimationVisitor(OWLOntologyGraph graph, Map<Variable,Integer> bindingPositions) {
	        super(graph, bindingPositions);
	        if (m_reasoner instanceof Reasoner) {
	            m_hermit=(Reasoner)m_reasoner;
	            m_instanceManager=m_hermit.m_instanceManager;
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
	        if (ce instanceof Atomic && (m_instanceManager==null || !m_instanceManager.areClassesInitialised()))
	            cost+=(m_classCount*m_indCount*COST_LOOKUP+COST_ENTAILMENT); // initialization required
	        if (indVar!=null && !bound.contains(indVar)){ //C(?x)
	            if (ce instanceof Atomic) {
	                	int[] estimate=m_instanceManager.getNumberOfInstances(AtomicConcept.create(((Clazz)ce).getIRIString()));
//	                    System.out.println("known: "+estimate[0]+"  possible: "+estimate[1]);
	                	return new double[] { cost+estimate[0]*COST_LOOKUP+estimate[1]*COST_ENTAILMENT*0.5*m_hermit.getClassHierarchyDepth(), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) };
	                    
	            }    
	            return new double[] { cost+m_indCount, m_indCount }; // needs refinement 
	        }    
	        else if (indVar!=null && bound.contains(indVar)) {//C(a)<-C(x)
	          if (ce instanceof Atomic) {
	        	  int[] estimate=m_instanceManager.getNumberOfInstances(AtomicConcept.create(((Clazz)ce).getIRIString()));
	        	  return new double[] { cost+(estimate[0]/(double)m_indCount)*COST_LOOKUP + (estimate[1]/(double)m_indCount)*COST_ENTAILMENT*0.5*m_hermit.getClassHierarchyDepth(), (estimate[0]+estimate[1]*POSSIBLE_INSTANCE_SUCCESS)/(double)m_indCount};
	          } 
	          else return new double[] { COST_ENTAILMENT, 1};
	        }
	        else if (ce instanceof Atomic) {//C(a)
	           boolean[] result=m_instanceManager.isKnownOrPossibleInstance(org.semanticweb.HermiT.model.Individual.create(((Atomic)ind).getIdentifierString()), 
               		  AtomicConcept.create(((Atomic)ce).getIdentifierString())); 
	           if (result[0])
	              return new double[] { cost+COST_LOOKUP, 1 };
	           else if (result[1])
	              return new double[] { cost+((double)0.5*COST_ENTAILMENT*getClassHierarchyDepth()), 1*POSSIBLE_INSTANCE_SUCCESS };
	           else return new double[] { cost+COST_LOOKUP, 0};   
	        }       
	        else return new double[] { COST_ENTAILMENT, 1};
	    }
	    
	    protected double[] getObjectPropertyAssertionCost(ObjectProperty op, Individual ind1, Individual ind2, Set<Variable> bound, Variable opVar) {
	        double cost=0;
	        
	        if (m_instanceManager==null || !m_instanceManager.arePropertiesInitialised())
	            cost+=(m_opCount*m_indCount*COST_LOOKUP+COST_ENTAILMENT); // initialization required
	        if (opVar==null && ind1 instanceof Variable && ind2 instanceof Variable) {// op(?x ?y)
	          if (!bound.contains(ind1) && !bound.contains(ind2)){//op(?x ?y)
//	        	if (m_instanceManager!=null && m_instanceManager.arePropertiesInitialised()) {
	                int[] estimate=m_instanceManager.getNumberOfPropertyInstances(AtomicRole.create(op.getIRIString()));
//	                System.out.println("known "+ op.toString()+ " instances: " +estimate[0]+"  possible: "+estimate[1]);
	                return new double[] { cost+estimate[0]*COST_LOOKUP+(estimate[1]*COST_ENTAILMENT*0.5*getOPHierarchyDepth()), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) };
//	            }
//	            return new double[] { cost+m_opCount*m_indCount*COST_LOOKUP*0.5*COST_ENTAILMENT*getOPHierarchyDepth(), m_indCount*m_indCount}; 
	          }
	          else if (bound.contains(ind1) && !bound.contains(ind2)) {//op(a ?y)
	        	int[] estimate=m_instanceManager.getNumberOfPropertyInstances(AtomicRole.create(op.getIRIString()));
	            if (estimate[2]!=0 && estimate[3]!=0) 
	               return new double[] { cost+(estimate[0]/estimate[2])*COST_LOOKUP+ (estimate[1]/estimate[3])*COST_ENTAILMENT*0.5*getOPHierarchyDepth(), (estimate[0]/estimate[2])+POSSIBLE_INSTANCE_SUCCESS*(estimate[1]/estimate[3]) };   
	            else if (estimate[3]!=0)  
	            	return new double[] {cost+(estimate[1]/estimate[3])*COST_ENTAILMENT*0.5*getOPHierarchyDepth(), POSSIBLE_INSTANCE_SUCCESS*(estimate[1]/estimate[3]) };
	            else if (estimate[2]!=0)
	            	return new double[] {cost+(estimate[0]/estimate[2])*COST_LOOKUP, (estimate[0]/estimate[2])};
	            else return new double[] {cost+0, 0};
	          }	
	          else if (!bound.contains(ind1) && bound.contains(ind2)){//op(?x a)
	        	int[] estimate=m_instanceManager.getNumberOfPropertyInstances(AtomicRole.create(op.getIRIString()));
	            if (estimate[4]!=0 && estimate[5]!=0) 
	               return new double[] { cost+(estimate[0]/estimate[4])*COST_LOOKUP+ (estimate[1]/estimate[5])*COST_ENTAILMENT*0.5*getOPHierarchyDepth(), (estimate[0]/estimate[4])+POSSIBLE_INSTANCE_SUCCESS*(estimate[1]/estimate[5]) };   
	            else if (estimate[4]!=0)
	            	return new double[] {cost+(estimate[0]/estimate[4])*COST_LOOKUP, (estimate[0]/estimate[4])};
	            else if (estimate[5]!=0)
	                return new double[] {cost+(estimate[1]/estimate[5])*COST_ENTAILMENT*0.5*getOPHierarchyDepth(), POSSIBLE_INSTANCE_SUCCESS*(estimate[1]/estimate[5]) };
	            else return new double[] {cost+0, 0};  
	          }
	          else {
	        	int[] estimate=m_instanceManager.getNumberOfPropertyInstances(AtomicRole.create((op.getIRIString())));
	            return new double[] { cost+(estimate[0]/(double)m_indCount)*COST_LOOKUP + (estimate[1]/(double)m_indCount)*COST_ENTAILMENT*0.5*getOPHierarchyDepth(), (estimate[0]+estimate[1]*POSSIBLE_INSTANCE_SUCCESS)/(double)m_indCount*(double)m_indCount};  
	          }
	        }  
	        else if (opVar==null && ind1 instanceof Variable) {// op(?x :a)
	            //if (m_instanceManager!=null && m_instanceManager.arePropertiesInitialised()) {
	                int[] estimate;
	                if (!bound.contains(ind1)) {
	                    estimate=m_instanceManager.getNumberOfPredecessors(AtomicRole.create(op.getIRIString()), org.semanticweb.HermiT.model.Individual.create(ind2.getIdentifierString()));
	                    return new double[] { cost+estimate[0]*COST_LOOKUP+(estimate[1]*COST_ENTAILMENT*0.5*getOPHierarchyDepth()), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) };
	                }
	                else {
	                	estimate=m_instanceManager.getNumberOfPropertyInstances(AtomicRole.create((op.getIRIString())));
	    	            return new double[] { cost+(estimate[0]/(double)m_indCount)*COST_LOOKUP + (estimate[1]/(double)m_indCount)*COST_ENTAILMENT*0.5*getOPHierarchyDepth(), (estimate[0]+estimate[1]*POSSIBLE_INSTANCE_SUCCESS)/(double)m_indCount*(double)m_indCount};  
	               }
	            //}
//	            return new double[] { cost+m_indCount*COST_LOOKUP, m_indCount }; // needs refinement 
	        } 
            else if (opVar==null && ind2 instanceof Variable) {// op(:a ?x)
	            if (m_instanceManager!=null && m_instanceManager.arePropertiesInitialised()) {
	                int[] estimate;
	                if (!bound.contains(ind2)) {
	                  estimate=m_instanceManager.getNumberOfSuccessors(AtomicRole.create(op.getIRIString()), org.semanticweb.HermiT.model.Individual.create(ind1.getIdentifierString()));
	                  return new double[] { cost+estimate[0]*COST_LOOKUP+(estimate[1]*COST_ENTAILMENT*0.5*getOPHierarchyDepth()), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) };
	                }
	                else {
	                	estimate=m_instanceManager.getNumberOfPropertyInstances(AtomicRole.create((op.getIRIString())));
	    	            return new double[] { cost+(estimate[0]/(double)m_indCount)*COST_LOOKUP + (estimate[1]/(double)m_indCount)*COST_ENTAILMENT*0.5*getOPHierarchyDepth(), (estimate[0]+estimate[1]*POSSIBLE_INSTANCE_SUCCESS)/(double)m_indCount*(double)m_indCount};  
	                }	                
//	            return new double[] { cost+m_indCount*COST_LOOKUP, m_indCount }; // needs refinement 
	            } 
            }
	        boolean[] result=m_instanceManager.hasSuccessor(AtomicRole.create(((Atomic)op).getIdentifierString()), org.semanticweb.HermiT.model.Individual.create(((Atomic)ind1).getIdentifierString()), org.semanticweb.HermiT.model.Individual.create(((Atomic)ind2).getIdentifierString()));
            if (result[0])
                return new double[] { cost+COST_LOOKUP, 1 };
            else if (result[1])
                return new double[] { cost+((double)0.5*COST_ENTAILMENT*getOPHierarchyDepth()), 1*POSSIBLE_INSTANCE_SUCCESS };
            else return new double[] {cost+COST_LOOKUP, 0 }; 
	    }
	    
	    protected double getClassHierarchyDepth() {
	        if (m_classHierarchyDepth==null && m_hermit.isPrecomputed(InferenceType.CLASS_HIERARCHY))
	            m_classHierarchyDepth=m_hermit.getClassHierarchyDepth();
	        if (m_classHierarchyDepth==null)
	            return m_classCount;
	        return m_classHierarchyDepth;
	    } 
	    protected double getOPHierarchyDepth() {
	        if (m_opHierarchyDepth==null && m_hermit.isPrecomputed(InferenceType.OBJECT_PROPERTY_HIERARCHY))
	            m_opHierarchyDepth=m_hermit.getObjectPropertyHierarchyDepth();
	        if (m_opHierarchyDepth==null)
	            return m_opCount;
	        return m_opHierarchyDepth;
	    } 
	
}
