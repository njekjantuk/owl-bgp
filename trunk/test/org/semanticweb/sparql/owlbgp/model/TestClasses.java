package org.semanticweb.sparql.owlbgp.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;

public class TestClasses extends TestCase {
    
    protected static char[] chars={'i', 'r', 'i', '1'};
    
    public TestClasses() {
        super();
    }
    public TestClasses(String name) {
        super(name);
    }
    public void testClassEquality() {
        String iri1="http://www.example.org/iri1";
        String iri2="http://www.example.org/"+new String(chars);
        assertFalse(iri1==iri2);
        Clazz class1=Clazz.create(iri1);
        Clazz class2=Clazz.create(iri2);
        assertTrue(class1==class2);
    }
    public void testIntersection() {
        String iri1="http://www.example.org/"+new String(chars);
        String iri2="http://www.example.org/iri2";
        Clazz class1a=Clazz.create(iri1);
        Clazz class1b=Clazz.create(iri2);
        Clazz class2a=Clazz.create(iri1);
        Clazz class2b=Clazz.create(iri2);
        Set<ClassExpression> inter1=new HashSet<ClassExpression>();
        inter1.add(class1a);
        inter1.add(class1b);
        Set<ClassExpression> inter2=new HashSet<ClassExpression>();
        inter2.add(class2a);
        inter2.add(class2b);
        ObjectIntersectionOf intersection1=ObjectIntersectionOf.create(inter1);
        ObjectIntersectionOf intersection2=ObjectIntersectionOf.create(inter2);
        assertTrue(intersection1==intersection2);
    }
    public void testDataIntersection() {
        String iri1="http://www.example.org/"+new String(chars);
        String iri2="http://www.example.org/iri2";
        DataRange class1a=Datatype.create(iri1);
        DataRange class1b=Datatype.create(iri2);
        DataRange class2a=Datatype.create(iri2);
        DataRange class2b=Datatype.create(iri1);
        DataRange intersection1=DataIntersectionOf.create(class1a, class1b);
        DataRange intersection2=DataIntersectionOf.create(class2a, class2b);
        assertTrue(intersection1==intersection2);
    }
    public void testIntersectionMultipleVars() {
        String classIRI="http://www.example.org/"+new String(chars);
        String opIRI="http://www.example.org/iri2";
        ClassExpression classVar=ClassVariable.create("?x");
        ObjectPropertyExpression opVar=ObjectPropertyVariable.create("?y");
        ClassExpression clazz=Clazz.create(classIRI);
        ClassExpression or=ObjectUnionOf.create(clazz, classVar);
        ClassExpression some=ObjectSomeValuesFrom.create(opVar, or);
        ClassExpression and=ObjectIntersectionOf.create(clazz, classVar);
        ObjectPropertyExpression op=ObjectProperty.create(opIRI);
        ClassExpression all=ObjectAllValuesFrom.create(op, and);
        ClassExpression ce=ObjectIntersectionOf.create(all, some);
        Set<Variable> vars=ce.getVariablesInSignature();
        Set<Variable> classVars=ce.getVariablesInSignature(VarType.CLASS);
        Set<Variable> opVars=ce.getVariablesInSignature(VarType.OBJECT_PROPERTY);
        assertTrue(vars.size()==2);
        assertTrue(classVars.size()==1);
        assertTrue(opVars.size()==1);
        assertTrue(vars.contains(classVar));
        assertTrue(vars.contains(opVar));
        assertTrue(classVars.contains(classVar));
        assertTrue(opVars.contains(opVar));
    }
    public void testApplyMultipleBindings() {
        ClassExpression classVar1=ClassVariable.create("?x");
        ClassExpression classVar2=ClassVariable.create("?y");
        ClassExpression classVar3=ClassVariable.create("?z");
        ClassExpression or=ObjectUnionOf.create(classVar1, classVar2, classVar3);
        Map<String,Set<String>> varToBindingSets=new HashMap<String, Set<String>>();
        Set<String> bindingsX=new HashSet<String>();
        bindingsX.add("http://example.org/a");
        bindingsX.add("http://example.org/b");
        bindingsX.add("http://example.org/c");
        varToBindingSets.put(classVar1.getIdentifier(), bindingsX);
        Set<String> bindingsY=new HashSet<String>();
        bindingsY.add("http://example.org/k");
        bindingsY.add("http://example.org/l");
        varToBindingSets.put(classVar2.getIdentifier(), bindingsY);
        Set<String> bindingsZ=new HashSet<String>();
        bindingsZ.add("http://example.org/r");
        bindingsZ.add("http://example.org/q");
        bindingsZ.add("http://example.org/s");
        varToBindingSets.put(classVar3.getIdentifier(), bindingsZ);
        int i=0;
        for (ExtendedOWLObject eoo : or.applyBindingSets(varToBindingSets)) {
            System.out.println(eoo);
            assertTrue(eoo.getUnboundVariablesInSignature().size()==0);
            i++;
        }
        assertTrue(i==(3*3*2));
        for (ExtendedOWLObject eoo : or.applyBindingSets(varToBindingSets)) {
            System.out.println(eoo.asOWLAPIObject(OWLManager.getOWLDataFactory()));
        }
    }
    public void testIntersectionApplyBindings() {
        String classIRI="http://www.example.org/"+new String(chars);
        String opIRI="http://www.example.org/iri2";
        ClassExpression classVar=ClassVariable.create("?x");
        ObjectPropertyExpression opVar=ObjectPropertyVariable.create("?y");
        ClassExpression clazz=Clazz.create(classIRI);
        ClassExpression or=ObjectUnionOf.create(clazz, classVar);
        ClassExpression some=ObjectSomeValuesFrom.create(opVar, or);
        ClassExpression and=ObjectIntersectionOf.create(clazz, classVar);
        ObjectPropertyExpression op=ObjectProperty.create(opIRI);
        ClassExpression all=ObjectAllValuesFrom.create(op, and);
        ClassExpression ce=ObjectIntersectionOf.create(all, some);
        Map<String,String> varsToBindings=new HashMap<String, String>();
        varsToBindings.put(classVar.getIdentifier(), "http://www.example.org/CBind");
        varsToBindings.put(opVar.getIdentifier(), "http://www.example.org/OPBind");
        ce.applyBindings(varsToBindings);
        assertTrue(ce.getUnboundVariablesInSignature(VarType.CLASS).size()==0);
        assertTrue(ce.getUnboundVariablesInSignature(VarType.OBJECT_PROPERTY).size()==0);
        assertTrue(ce.getUnboundVariablesInSignature().size()==0);
        OWLObject owlCe=ce.asOWLAPIObject(OWLManager.getOWLDataFactory());
        assertTrue(owlCe instanceof OWLObjectIntersectionOf);
        assertTrue(((OWLObjectIntersectionOf)owlCe).getOperands().size()==2);
    }
}