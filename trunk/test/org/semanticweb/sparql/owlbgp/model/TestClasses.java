package org.semanticweb.sparql.owlbgp.model;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClassExpression;

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
    public void testIntersectionToOWLAPI() {
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
        OWLAPIConverter converter=new OWLAPIConverter(OWLManager.getOWLDataFactory());
        OWLClassExpression ce1=(OWLClassExpression)converter.visit(intersection1);
        OWLClassExpression ce2=(OWLClassExpression)converter.visit(intersection2);
        assertTrue(ce1.containsConjunct((OWLClassExpression)converter.visit(class1a)));
        assertTrue(ce1.containsConjunct((OWLClassExpression)converter.visit(class1b)));
        assertTrue(ce2.containsConjunct((OWLClassExpression)converter.visit(class2a)));
        assertTrue(ce2.containsConjunct((OWLClassExpression)converter.visit(class2b)));
    }
}