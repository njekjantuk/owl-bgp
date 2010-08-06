package org.semanticweb.sparql.owlbgp.parser.translators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;

public class OptimisedListTranslator<O extends ExtendedOWLObject> {

    protected final TripleConsumer consumer;
    protected final ListItemTranslator<O> translator;

    public OptimisedListTranslator(TripleConsumer consumer,ListItemTranslator<O> translator) {
        this.consumer=consumer;
        this.translator=translator;
    }
    protected void translateList(Identifier mainNode,List<O> list) {
        Identifier first=consumer.getFirst(mainNode);
        if (first!=null) {
            O translated=translator.translate(first);
            if (translated!=null) 
                list.add(translated);
        }
        if (list.isEmpty()) return;
        Identifier rest=consumer.getRest(mainNode);
        if (rest!=null&&rest!=Vocabulary.RDF_NIL) translateList(rest, list);
    }
    public List<O> translateToList(Identifier mainNode) {
        List<O> list=new ArrayList<O>();
        translateList(mainNode, list);
        return list;
    }
    public Set<O> translateToSet(Identifier mainNode) {
        return new HashSet<O>(translateToList(mainNode));
    }
}