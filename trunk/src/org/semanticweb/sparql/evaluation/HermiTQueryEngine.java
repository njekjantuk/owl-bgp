package org.semanticweb.sparql.evaluation;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.sparql.ARQInternalErrorException;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.engine.Plan;
import com.hp.hpl.jena.sparql.engine.QueryEngineFactory;
import com.hp.hpl.jena.sparql.engine.QueryEngineRegistry;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.main.QueryEngineMain;
import com.hp.hpl.jena.sparql.util.Context;

public class HermiTQueryEngine extends QueryEngineMain {
    public HermiTQueryEngine(Query query, DatasetGraph dataset, Binding input, Context context) { 
    	super(query, dataset, input, context); 
    }
    public HermiTQueryEngine(Query query, DatasetGraph dataset) { 
        this(query, dataset, null, null);
    }

    private static QueryEngineFactory factory = new HermiTQueryEngineFactory() ;

    public static QueryEngineFactory getFactory() { 
        return factory; 
    } 
    public static void register() {
        QueryEngineRegistry.addFactory(factory);
    }
    static public void unregister() {
        QueryEngineRegistry.removeFactory(factory);
    }
}
class HermiTQueryEngineFactory implements QueryEngineFactory {
    // Accept only HermiT dataset for query execution 
    public boolean accept(Query query, DatasetGraph dataset, Context context) {
        return dataset instanceof HermiTDatasetGraph;
    }
    public Plan create(Query query, DatasetGraph dataset, Binding initial, Context context) {
        // Create a query engine instance.
        HermiTQueryEngine engine=new HermiTQueryEngine(query, dataset, initial, context) ;
        return engine.getPlan() ;
    }
    public boolean accept(Op op, DatasetGraph dataset, Context context) {
        // Refuse to accept algebra expressions directly.
        return false ;
    }
    public Plan create(Op op, DatasetGraph dataset, Binding inputBinding, Context context) {
        // Should not be called because accept/Op is false
        throw new ARQInternalErrorException("HermiTQueryEngine: factory called directly with an algebra expression. That is not supported. ") ;
    }
} 
