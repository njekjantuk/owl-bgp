package org.semanticweb.sparql.owlbgpparser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ILiteral;

public class OptimisedListTranslator<O extends ExtendedOWLObject> {

    protected final OWLRDFConsumer consumer;
    protected final ListItemTranslator<O> translator;

    protected OptimisedListTranslator(OWLRDFConsumer consumer,ListItemTranslator<O> translator) {
        this.consumer=consumer;
        this.translator=translator;
    }
    protected OWLRDFConsumer getConsumer() {
        return consumer;
    }
    protected void translateList(String mainNode,List<O> list) {
        if (!consumer.isList(mainNode, true)) throw new RuntimeException("List with main node "+mainNode+" is missing a type triple.");
        String firstResource=getConsumer().getFirstResource(mainNode, true);
        if (firstResource != null) {
            list.add(translator.translate(firstResource));
        } else {
            ILiteral literal=getConsumer().getFirstLiteral(mainNode);
            if (literal != null) list.add(translator.translate(literal));
        }
        String rest=getConsumer().getRest(mainNode, true);
        if (rest != null)  translateList(rest, list);
    }
    public List<O> translateList(String mainNode) {
        List<O> list=new ArrayList<O>();
        translateList(mainNode, list);
        return list;
    }
    public Set<O> translateToSet(String mainNode) {
        return new HashSet<O>(translateList(mainNode));
    }
}